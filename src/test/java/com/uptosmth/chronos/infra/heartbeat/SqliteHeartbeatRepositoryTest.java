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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.uptosmth.chronos.TestDatabase;
import com.uptosmth.chronos.TestFactory;
import com.uptosmth.chronos.domain.PayloadType;
import com.uptosmth.chronos.domain.activity.window.WindowPayload;
import com.uptosmth.chronos.domain.heartbeat.Heartbeat;
import com.uptosmth.chronos.domain.heartbeat.HeartbeatRepository;

class SqliteHeartbeatRepositoryTest {

    private HeartbeatRepository<WindowPayload> heartbeatRepository;

    @TempDir Path tempDir;

    @BeforeEach
    public void setUp() {
        heartbeatRepository = new SqliteHeartbeatRepository<>(TestDatabase.setupDatabase(tempDir));
    }

    @Test
    void appendsHeartbeats() {
        Heartbeat<WindowPayload> heartbeat = TestFactory.buildWindowHeartbeat();

        heartbeatRepository.append(heartbeat);

        Heartbeat<WindowPayload> loaded = heartbeatRepository.load(heartbeat.getUuid()).get();

        assertEquals(PayloadType.WINDOW, loaded.getType());

        assertEquals("Chronos", loaded.getPayload().getWindowTitle());
    }
}
