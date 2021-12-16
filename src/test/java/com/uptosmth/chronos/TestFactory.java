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

package com.uptosmth.chronos;

import java.time.Instant;
import java.util.UUID;

import com.uptosmth.chronos.domain.PayloadType;
import com.uptosmth.chronos.domain.activity.Activity;
import com.uptosmth.chronos.domain.activity.meeting.MeetingPayload;
import com.uptosmth.chronos.domain.activity.window.ActiveWindow;
import com.uptosmth.chronos.domain.activity.window.WindowPayload;
import com.uptosmth.chronos.domain.heartbeat.Heartbeat;

public class TestFactory {

    public static WindowPayload windowPayload() {
        return new WindowPayload("UNKNOWN", "Firefox", "Chronos");
    }

    public static MeetingPayload meetingPayload() {
        return new MeetingPayload("Zoom");
    }

    public static Heartbeat<WindowPayload> buildWindowHeartbeat() {
        return windowHeartbeatBuilder().build();
    }

    public static Heartbeat.Builder<WindowPayload> windowHeartbeatBuilder() {
        return Heartbeat.<WindowPayload>builder()
                .uuid(UUID.randomUUID())
                .createdAt(Instant.now())
                .type(PayloadType.WINDOW)
                .payload(windowPayload());
    }

    public static Heartbeat<MeetingPayload> buildMeetingHeartbeat() {
        return meetingHeartbeatBuilder().build();
    }

    public static Heartbeat.Builder<MeetingPayload> meetingHeartbeatBuilder() {
        return Heartbeat.<MeetingPayload>builder()
                .uuid(UUID.randomUUID())
                .createdAt(Instant.now())
                .type(PayloadType.MEETING)
                .payload(meetingPayload());
    }

    public static ActiveWindow buildActiveWindow() {
        return ActiveWindow.builder()
                .category("UNKNOWN")
                .application("Firefox")
                .windowTitle("chronos — Spy on yourself")
                .build();
    }

    public static ActiveWindow.Builder activeWindowBuilder() {
        return ActiveWindow.builder()
                .category("UNKNOWN")
                .application("Firefox")
                .windowTitle("chronos — Spy on yourself");
    }

    public static Activity<WindowPayload> buildWindowActivity() {
        return windowActivityBuilder().build();
    }

    public static Activity.Builder<WindowPayload> windowActivityBuilder() {
        ActiveWindow activeWindow = buildActiveWindow();

        return Activity.<WindowPayload>builder()
                .uuid(UUID.randomUUID())
                .type(PayloadType.WINDOW)
                .startedAt(Instant.now())
                .elapsedMilli(1000)
                .payload(
                        new WindowPayload(
                                activeWindow.getCategory(),
                                activeWindow.getApplication(),
                                activeWindow.getWindowTitle()));
    }
}
