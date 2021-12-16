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

package com.uptosmth.chronos.infra.activity.meeting.job;

import javax.sql.DataSource;

import com.uptosmth.chronos.domain.WindowManager;
import com.uptosmth.chronos.domain.activity.meeting.MeetingHeartbeatRecording;
import com.uptosmth.chronos.domain.heartbeat.HeartbeatRepository;
import com.uptosmth.chronos.infra.X11WindowManager;
import com.uptosmth.chronos.infra.heartbeat.SqliteHeartbeatRepository;

public class MeetingHeartbeatRecordingJob implements Runnable {
    private final DataSource dataSource;
    private final long samplingIntervalMilli;

    public MeetingHeartbeatRecordingJob(DataSource dataSource, long samplingIntervalMilli) {
        this.dataSource = dataSource;
        this.samplingIntervalMilli = samplingIntervalMilli;
    }

    @Override
    public void run() {
        HeartbeatRepository activityRepository = new SqliteHeartbeatRepository(dataSource);
        WindowManager windowManager = new X11WindowManager();

        MeetingHeartbeatRecording windowHeartbeatRecording =
                new MeetingHeartbeatRecording(activityRepository, windowManager);

        while (true) {
            windowHeartbeatRecording.record();

            try {
                Thread.sleep(samplingIntervalMilli);
            } catch (InterruptedException e) {
            }
        }
    }
}
