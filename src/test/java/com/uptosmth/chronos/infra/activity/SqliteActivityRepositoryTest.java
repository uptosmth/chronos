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

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.uptosmth.chronos.TestDatabase;
import com.uptosmth.chronos.TestFactory;
import com.uptosmth.chronos.domain.activity.Activity;
import com.uptosmth.chronos.domain.activity.ActivityRepository;
import com.uptosmth.chronos.domain.activity.window.WindowPayload;
import com.uptosmth.chronos.domain.heartbeat.Heartbeat;
import com.uptosmth.chronos.domain.heartbeat.HeartbeatRepository;
import com.uptosmth.chronos.infra.heartbeat.SqliteHeartbeatRepository;

class SqliteActivityRepositoryTest {

    private ActivityRepository<WindowPayload> activityRepository;
    private HeartbeatRepository<WindowPayload> heartbeatRepository;

    @TempDir Path tempDir;

    @BeforeEach
    public void setUp() {
        DataSource dataSource = TestDatabase.setupDatabase(tempDir);

        heartbeatRepository = new SqliteHeartbeatRepository<>(dataSource);
        activityRepository = new SqliteActivityRepository<>(dataSource);
    }

    @Test
    void appendsActivity() {
        Activity<WindowPayload> activity = TestFactory.buildWindowActivity();

        activityRepository.append(activity);

        Activity<WindowPayload> loaded = activityRepository.load(activity.getUuid()).get();

        assertEquals(activity.getPayload().getWindowTitle(), loaded.getPayload().getWindowTitle());
    }

    @Test
    void appendsActivities() {
        Activity<WindowPayload> activity1 = TestFactory.buildWindowActivity();
        Activity<WindowPayload> activity2 = TestFactory.buildWindowActivity();
        Activity<WindowPayload> activity3 = TestFactory.buildWindowActivity();

        activityRepository.append(activity1);
        activityRepository.append(activity2);
        activityRepository.append(activity3);

        assertTrue(activityRepository.load(activity1.getUuid()).isPresent());
        assertTrue(activityRepository.load(activity2.getUuid()).isPresent());
        assertTrue(activityRepository.load(activity3.getUuid()).isPresent());
    }

    @Test
    void processesLinkedHeartbeats() {
        Heartbeat<WindowPayload> heartbeat = TestFactory.buildWindowHeartbeat();
        heartbeatRepository.append(heartbeat);

        Activity<WindowPayload> activity =
                TestFactory.windowActivityBuilder().addHeartbeat(heartbeat.getUuid()).build();

        activityRepository.append(activity);

        assertNotNull(heartbeatRepository.load(heartbeat.getUuid()).get().getProcessedAt());
    }
}
