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

package com.uptosmth.chronos.domain.activity.window;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.uptosmth.chronos.TestDatabase;
import com.uptosmth.chronos.domain.WindowManager;
import com.uptosmth.chronos.domain.heartbeat.Heartbeat;
import com.uptosmth.chronos.domain.heartbeat.HeartbeatRepository;
import com.uptosmth.chronos.infra.heartbeat.SqliteHeartbeatRepository;

class WindowHeartbeatRecordingTest {
    private HeartbeatRepository heartbeatRepository;
    private WindowHeartbeatRecording windowHeartbeatRecording;

    @TempDir Path tempDir;

    @BeforeEach
    public void setUp() {
        heartbeatRepository = new SqliteHeartbeatRepository(TestDatabase.setupDatabase(tempDir));

        windowHeartbeatRecording =
                new WindowHeartbeatRecording(
                        heartbeatRepository,
                        new WindowManager() {
                            @Override
                            public Optional<ActiveWindow> getActiveWindow() {
                                return Optional.of(
                                        ActiveWindow.builder()
                                                .category("UNKNOWN")
                                                .application("Firefox")
                                                .windowTitle("Mozilla")
                                                .build());
                            }

                            @Override
                            public boolean isWindowPresent(String title) {
                                return false;
                            }

                            @Override
                            public long getIdleMilli() {
                                return 0;
                            }

                            @Override
                            public boolean isAway() {
                                return false;
                            }
                        });
    }

    @Test
    public void recordsActivity() {
        UUID uuid = windowHeartbeatRecording.record().get();

        Optional<Heartbeat> heartbeat = heartbeatRepository.load(uuid);

        assertTrue(heartbeat.isPresent());
    }
}
