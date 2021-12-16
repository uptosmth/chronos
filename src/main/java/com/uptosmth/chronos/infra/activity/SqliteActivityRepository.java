/*
 * Copyright (C) 2021 Viacheslav Tykhanovskyi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.uptosmth.chronos.infra.activity;

import static com.uptosmth.chronos.db.Tables.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.jooq.*;
import org.jooq.impl.DSL;

import com.uptosmth.chronos.domain.PayloadType;
import com.uptosmth.chronos.domain.activity.Activity;
import com.uptosmth.chronos.domain.activity.ActivityRepository;
import com.uptosmth.chronos.domain.activity.browser.BrowserPayload;
import com.uptosmth.chronos.domain.activity.editor.EditorPayload;
import com.uptosmth.chronos.domain.activity.meeting.MeetingPayload;
import com.uptosmth.chronos.domain.activity.window.WindowPayload;
import com.uptosmth.chronos.infra.DatabaseUtils;

public class SqliteActivityRepository<P> implements ActivityRepository<P> {
    private final DataSource dataSource;

    public SqliteActivityRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Activity<P>> load(UUID uuid) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.SQLITE);

            Record record =
                    create.select(
                                    ACTIVITY.UUID,
                                    ACTIVITY.TYPE,
                                    ACTIVITY.STARTED_AT,
                                    ACTIVITY.ELAPSED_MILLI,
                                    WINDOW_ACTIVITY.CATEGORY,
                                    WINDOW_ACTIVITY.APPLICATION,
                                    WINDOW_ACTIVITY.WINDOW_TITLE,
                                    BROWSER_ACTIVITY.TITLE,
                                    BROWSER_ACTIVITY.URL,
                                    BROWSER_ACTIVITY.URL_DOMAIN,
                                    EDITOR_ACTIVITY.PROJECT,
                                    EDITOR_ACTIVITY.FILE,
                                    EDITOR_ACTIVITY.BRANCH,
                                    MEETING_ACTIVITY.APPLICATION)
                            .from(ACTIVITY)
                            .leftJoin(WINDOW_ACTIVITY)
                            .on(WINDOW_ACTIVITY.ID.eq(ACTIVITY.ID))
                            .leftJoin(BROWSER_ACTIVITY)
                            .on(BROWSER_ACTIVITY.ID.eq(ACTIVITY.ID))
                            .leftJoin(EDITOR_ACTIVITY)
                            .on(EDITOR_ACTIVITY.ID.eq(ACTIVITY.ID))
                            .leftJoin(MEETING_ACTIVITY)
                            .on(MEETING_ACTIVITY.ID.eq(ACTIVITY.ID))
                            .where(ACTIVITY.UUID.eq(uuid.toString()))
                            .orderBy(ACTIVITY.STARTED_AT.desc())
                            .limit(1)
                            .fetchOne();

            if (record == null) {
                return Optional.empty();
            }

            PayloadType type = PayloadType.valueOf(record.getValue(ACTIVITY.TYPE));

            Activity.Builder<P> activityBuilder =
                    Activity.<P>builder()
                            .uuid(UUID.fromString(record.getValue(ACTIVITY.UUID)))
                            .type(type)
                            .startedAt(
                                    record.getValue(ACTIVITY.STARTED_AT).toInstant(ZoneOffset.UTC))
                            .elapsedMilli(record.getValue(ACTIVITY.ELAPSED_MILLI));

            activityBuilder.payload(mapPayload(record));

            return Optional.of(activityBuilder.build());
        } catch (SQLException e) {
            throw new RuntimeException("Error storing", e);
        }
    }

    @Override
    public Optional<Activity<P>> loadLast(PayloadType payloadType) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.SQLITE);
            Record1<String> record =
                    create.select(ACTIVITY.UUID)
                            .from(ACTIVITY)
                            .where(ACTIVITY.TYPE.eq(payloadType.toString()))
                            .orderBy(ACTIVITY.STARTED_AT.desc())
                            .limit(1)
                            .fetchOne();

            if (record == null) return Optional.empty();

            return load(UUID.fromString(record.getValue(ACTIVITY.UUID)));
        } catch (SQLException e) {
            throw new RuntimeException("Error storing", e);
        }
    }

    @Override
    public void append(Activity<P> activity) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            DSLContext create = DSL.using(connection, SQLDialect.SQLITE);

            Record record =
                    create.insertInto(
                                    ACTIVITY,
                                    ACTIVITY.UUID,
                                    ACTIVITY.TYPE,
                                    ACTIVITY.CREATED_AT,
                                    ACTIVITY.STARTED_AT,
                                    ACTIVITY.FINISHED_AT,
                                    ACTIVITY.LOCAL_STARTED_AT,
                                    ACTIVITY.LOCAL_FINISHED_AT,
                                    ACTIVITY.ELAPSED_MILLI)
                            .values(
                                    activity.getUuid().toString(),
                                    activity.getType().toString(),
                                    LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC),
                                    LocalDateTime.ofInstant(
                                            activity.getStartedAt(), ZoneOffset.UTC),
                                    LocalDateTime.ofInstant(
                                            activity.getFinishedAt(), ZoneOffset.UTC),
                                    LocalDateTime.ofInstant(
                                            activity.getStartedAt(), ZoneOffset.systemDefault()),
                                    LocalDateTime.ofInstant(
                                            activity.getFinishedAt(), ZoneOffset.systemDefault()),
                                    activity.getElapsedMilli())
                            .returningResult(ACTIVITY.ID)
                            .fetchOne();

            if (record == null) {
                throw new RuntimeException("Insert failed");
            }

            if (activity.getType() == PayloadType.WINDOW) {
                WindowPayload payload = (WindowPayload) activity.getPayload();

                create.insertInto(
                                WINDOW_ACTIVITY,
                                WINDOW_ACTIVITY.ID,
                                WINDOW_ACTIVITY.CATEGORY,
                                WINDOW_ACTIVITY.APPLICATION,
                                WINDOW_ACTIVITY.WINDOW_TITLE)
                        .values(
                                record.getValue(ACTIVITY.ID),
                                payload.getCategory(),
                                payload.getApplication(),
                                payload.getWindowTitle())
                        .execute();
            } else if (activity.getType() == PayloadType.BROWSER) {
                BrowserPayload payload = (BrowserPayload) activity.getPayload();

                create.insertInto(
                                BROWSER_ACTIVITY,
                                BROWSER_ACTIVITY.ID,
                                BROWSER_ACTIVITY.TITLE,
                                BROWSER_ACTIVITY.URL,
                                BROWSER_ACTIVITY.URL_DOMAIN)
                        .values(
                                record.getValue(ACTIVITY.ID),
                                payload.getTitle(),
                                payload.getUrl(),
                                payload.getUrlDomain())
                        .execute();
            } else if (activity.getType() == PayloadType.EDITOR) {
                EditorPayload payload = (EditorPayload) activity.getPayload();

                create.insertInto(
                                EDITOR_ACTIVITY,
                                EDITOR_ACTIVITY.ID,
                                EDITOR_ACTIVITY.FILE,
                                EDITOR_ACTIVITY.PROJECT,
                                EDITOR_ACTIVITY.BRANCH)
                        .values(
                                record.getValue(ACTIVITY.ID),
                                payload.getFile(),
                                payload.getProject(),
                                payload.getBranch())
                        .execute();
            } else if (activity.getType() == PayloadType.MEETING) {
                MeetingPayload payload = (MeetingPayload) activity.getPayload();

                create.insertInto(
                                MEETING_ACTIVITY, MEETING_ACTIVITY.ID, MEETING_ACTIVITY.APPLICATION)
                        .values(record.getValue(ACTIVITY.ID), payload.getApplication())
                        .execute();
            } else {
                throw new RuntimeException("Unknown payload type");
            }

            DatabaseUtils.processHeartbeats(connection, activity.getHeartbeats());

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Error storing", e);
        }
    }

    @Override
    public void store(Activity<P> activity) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            DSLContext context = DSL.using(connection, SQLDialect.SQLITE);

            int rowsUpdated =
                    context.update(ACTIVITY)
                            .set(ACTIVITY.ELAPSED_MILLI, activity.getElapsedMilli())
                            .where(ACTIVITY.UUID.eq(activity.getUuid().toString()))
                            .execute();

            if (rowsUpdated != 1) {
                throw new RuntimeException("Failed storing activity");
            }

            DatabaseUtils.processHeartbeats(connection, activity.getHeartbeats());

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Error storing", e);
        }
    }

    private P mapPayload(Record record) {
        if (PayloadType.valueOf(record.get(ACTIVITY.TYPE)) == PayloadType.WINDOW) {
            WindowPayload windowPayload =
                    new WindowPayload(
                            record.getValue(WINDOW_ACTIVITY.CATEGORY),
                            record.getValue(WINDOW_ACTIVITY.APPLICATION),
                            record.getValue(WINDOW_ACTIVITY.WINDOW_TITLE));

            @SuppressWarnings("unchecked")
            P payload = (P) windowPayload;
            return payload;
        } else if (PayloadType.valueOf(record.get(ACTIVITY.TYPE)) == PayloadType.BROWSER) {
            BrowserPayload browserPayload =
                    new BrowserPayload(
                            record.getValue(BROWSER_ACTIVITY.TITLE),
                            record.getValue(BROWSER_ACTIVITY.URL),
                            record.getValue(BROWSER_ACTIVITY.URL_DOMAIN));

            @SuppressWarnings("unchecked")
            P payload = (P) browserPayload;
            return payload;
        } else if (PayloadType.valueOf(record.get(ACTIVITY.TYPE)) == PayloadType.EDITOR) {
            EditorPayload editorPayload =
                    new EditorPayload(
                            record.getValue(EDITOR_ACTIVITY.FILE),
                            record.getValue(EDITOR_ACTIVITY.PROJECT),
                            record.getValue(EDITOR_ACTIVITY.BRANCH));

            @SuppressWarnings("unchecked")
            P payload = (P) editorPayload;
            return payload;
        } else if (PayloadType.valueOf(record.get(ACTIVITY.TYPE)) == PayloadType.MEETING) {
            MeetingPayload meetingPayload =
                    new MeetingPayload(record.getValue(MEETING_ACTIVITY.APPLICATION));

            @SuppressWarnings("unchecked")
            P payload = (P) meetingPayload;
            return payload;
        } else {
            throw new RuntimeException("Unknown activity type");
        }
    }
}
