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

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.uptosmth.chronos.domain.PayloadType;
import com.uptosmth.chronos.domain.WindowManager;
import com.uptosmth.chronos.domain.heartbeat.Heartbeat;
import com.uptosmth.chronos.domain.heartbeat.HeartbeatRepository;

public class WindowHeartbeatRecording {
    private final HeartbeatRepository<WindowPayload> heartbeatRepository;
    private final WindowManager windowManager;

    public WindowHeartbeatRecording(
            HeartbeatRepository<WindowPayload> heartbeatRepository, WindowManager windowManager) {
        this.heartbeatRepository = heartbeatRepository;
        this.windowManager = windowManager;
    }

    public Optional<UUID> record() {
        Optional<ActiveWindow> optionalActiveWindow = windowManager.getActiveWindow();

        if (optionalActiveWindow.isPresent() && !windowManager.isAway()) {
            UUID uuid = UUID.randomUUID();

            ActiveWindow activeWindow = optionalActiveWindow.get();

            WindowPayload payload =
                    new WindowPayload(
                            activeWindow.getCategory(),
                            activeWindow.getApplication(),
                            activeWindow.getWindowTitle());

            Heartbeat<WindowPayload> heartbeat =
                    Heartbeat.<WindowPayload>builder()
                            .uuid(uuid)
                            .createdAt(Instant.now())
                            .type(PayloadType.WINDOW)
                            .payload(payload)
                            .build();

            heartbeatRepository.append(heartbeat);

            return Optional.of(uuid);
        }

        return Optional.empty();
    }
}
