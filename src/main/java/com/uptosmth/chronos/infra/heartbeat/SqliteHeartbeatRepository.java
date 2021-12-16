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

package com.uptosmth.chronos.infra.heartbeat;

import static com.uptosmth.chronos.db.Tables.HEARTBEAT;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.uptosmth.chronos.domain.PayloadType;
import com.uptosmth.chronos.domain.activity.browser.BrowserPayload;
import com.uptosmth.chronos.domain.activity.editor.EditorPayload;
import com.uptosmth.chronos.domain.activity.meeting.MeetingPayload;
import com.uptosmth.chronos.domain.activity.window.WindowPayload;
import com.uptosmth.chronos.domain.heartbeat.Heartbeat;
import com.uptosmth.chronos.domain.heartbeat.HeartbeatRepository;
import com.uptosmth.chronos.infra.DefaultObjectMapper;

class HeartbeatRecordMapper {
    static <P> Heartbeat<P> mapRecord(Record record) {
        try {
            PayloadType payloadType = PayloadType.valueOf(record.getValue(HEARTBEAT.TYPE));

            @SuppressWarnings("unchecked")
            P payload =
                    (P)
                            DefaultObjectMapper.get()
                                    .readValue(
                                            record.getValue(HEARTBEAT.DATA),
                                            mapPayload(payloadType));

            return Heartbeat.<P>builder()
                    .uuid(UUID.fromString(record.getValue(HEARTBEAT.UUID)))
                    .deliveredAt(record.getValue(HEARTBEAT.DELIVERED_AT).toInstant(ZoneOffset.UTC))
                    .createdAt(record.getValue(HEARTBEAT.CREATED_AT).toInstant(ZoneOffset.UTC))
                    .processedAt(
                            Optional.ofNullable(record.getValue(HEARTBEAT.PROCESSED_AT))
                                    .map(v -> v.toInstant(ZoneOffset.UTC))
                                    .orElse(null))
                    .type(payloadType)
                    .payload(payload)
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing data", e);
        }
    }

    static Class mapPayload(PayloadType payloadType) {
        switch (payloadType) {
            case WINDOW:
                return WindowPayload.class;
            case BROWSER:
                return BrowserPayload.class;
            case EDITOR:
                return EditorPayload.class;
            case MEETING:
                return MeetingPayload.class;
            default:
                throw new RuntimeException("Unknown payload type");
        }
    }
}

public class SqliteHeartbeatRepository<P> implements HeartbeatRepository<P> {
    private final DataSource dataSource;

    public SqliteHeartbeatRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Heartbeat<P>> load(UUID uuid) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.SQLITE);

            Record6<String, LocalDateTime, LocalDateTime, LocalDateTime, String, String> record =
                    create.select(
                                    HEARTBEAT.UUID,
                                    HEARTBEAT.DELIVERED_AT,
                                    HEARTBEAT.CREATED_AT,
                                    HEARTBEAT.PROCESSED_AT,
                                    HEARTBEAT.TYPE,
                                    HEARTBEAT.DATA)
                            .from(HEARTBEAT)
                            .where(HEARTBEAT.UUID.eq(uuid.toString()))
                            .fetchOne();

            if (record == null) {
                return Optional.empty();
            }

            Heartbeat<P> heartbeat = HeartbeatRecordMapper.mapRecord(record);

            return Optional.of(heartbeat);
        } catch (SQLException e) {
            throw new RuntimeException("Error storing", e);
        }
    }

    @Override
    public List<Heartbeat<P>> findNotProcessed(PayloadType payloadType, int limit, int offset) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.SQLITE);

            Result<Record6<String, LocalDateTime, LocalDateTime, LocalDateTime, String, String>>
                    result =
                            create.select(
                                            HEARTBEAT.UUID,
                                            HEARTBEAT.DELIVERED_AT,
                                            HEARTBEAT.CREATED_AT,
                                            HEARTBEAT.PROCESSED_AT,
                                            HEARTBEAT.TYPE,
                                            HEARTBEAT.DATA)
                                    .from(HEARTBEAT)
                                    .where(HEARTBEAT.TYPE.eq(payloadType.toString()))
                                    .and(HEARTBEAT.PROCESSED_AT.isNull())
                                    .orderBy(HEARTBEAT.CREATED_AT.asc())
                                    .limit(limit)
                                    .offset(offset)
                                    .fetch();

            List<Heartbeat<P>> heartbeats = new ArrayList<>();

            for (Record record : result) {
                Heartbeat<P> heartbeat = HeartbeatRecordMapper.mapRecord(record);

                heartbeats.add(heartbeat);
            }

            return heartbeats;
        } catch (SQLException e) {
            throw new RuntimeException("Error storing", e);
        }
    }

    @Override
    public void append(Heartbeat<P> heartbeat) {
        append(Collections.singletonList(heartbeat));
    }

    @Override
    public void append(List<Heartbeat<P>> heartbeats) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            DSLContext create = DSL.using(connection, SQLDialect.SQLITE);

            for (Heartbeat<P> heartbeat : heartbeats) {
                create.insertInto(
                                HEARTBEAT,
                                HEARTBEAT.UUID,
                                HEARTBEAT.DELIVERED_AT,
                                HEARTBEAT.CREATED_AT,
                                HEARTBEAT.LOCAL_CREATED_AT,
                                HEARTBEAT.TYPE,
                                HEARTBEAT.DATA)
                        .values(
                                heartbeat.getUuid().toString(),
                                LocalDateTime.ofInstant(heartbeat.getDeliveredAt(), ZoneOffset.UTC),
                                LocalDateTime.ofInstant(heartbeat.getCreatedAt(), ZoneOffset.UTC),
                                LocalDateTime.ofInstant(
                                        heartbeat.getCreatedAt(), ZoneOffset.systemDefault()),
                                heartbeat.getType().toString(),
                                DefaultObjectMapper.get()
                                        .writeValueAsString(heartbeat.getPayload()))
                        .execute();
            }

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Error storing", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing data", e);
        }
    }
}
