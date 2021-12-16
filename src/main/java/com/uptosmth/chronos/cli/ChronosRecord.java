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

package com.uptosmth.chronos.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.sql.DataSource;

import picocli.CommandLine;

import com.uptosmth.chronos.App;
import com.uptosmth.chronos.api.Api;
import com.uptosmth.chronos.domain.PayloadType;
import com.uptosmth.chronos.domain.RepositoryRegistry;
import com.uptosmth.chronos.domain.activity.browser.BrowserPayload;
import com.uptosmth.chronos.domain.activity.editor.EditorPayload;
import com.uptosmth.chronos.domain.activity.meeting.MeetingPayload;
import com.uptosmth.chronos.domain.activity.window.WindowPayload;
import com.uptosmth.chronos.infra.DataSourceBuilder;
import com.uptosmth.chronos.infra.SqliteRepositoryRegistry;
import com.uptosmth.chronos.infra.activity.ActivityProducerJob;
import com.uptosmth.chronos.infra.activity.meeting.job.MeetingHeartbeatRecordingJob;
import com.uptosmth.chronos.infra.activity.window.job.WindowHeartbeatRecordingJob;

@CommandLine.Command(
        name = "record",
        mixinStandardHelpOptions = true,
        sortOptions = false,
        description = "Start recording")
public class ChronosRecord implements Callable<Integer> {

    @CommandLine.ParentCommand private App parent;

    @CommandLine.Option(
            names = "--sampling-interval",
            paramLabel = "5000",
            description = "How often do the sampling in milliseconds")
    long samplingIntervalMilli = 5000;

    @CommandLine.Option(
            names = "--listen",
            paramLabel = "10203",
            description = "Listen on this port for incoming activities")
    int listenPort = 10203;

    @Override
    public Integer call() {
        DataSource dataSource = new DataSourceBuilder(parent.dbArg.toPath()).getDataSource();
        RepositoryRegistry repositoryRegistry = new SqliteRepositoryRegistry(dataSource);

        Thread windowHeartbeatRecordingJob =
                new Thread(new WindowHeartbeatRecordingJob(dataSource, samplingIntervalMilli));
        windowHeartbeatRecordingJob.start();

        Thread meetingHeartbeatRecordingJob =
                new Thread(new MeetingHeartbeatRecordingJob(dataSource, samplingIntervalMilli));
        meetingHeartbeatRecordingJob.start();

        List<Thread> producers = buildProducers(dataSource);

        producers.stream().forEach(Thread::start);

        new Api(repositoryRegistry, listenPort).start();

        try {
            windowHeartbeatRecordingJob.join();
            meetingHeartbeatRecordingJob.join();

            for (Thread producer : producers) {
                producer.join();
            }
        } catch (InterruptedException e) {
        }

        return 1;
    }

    private List<Thread> buildProducers(DataSource dataSource) {
        List<Thread> producers = new ArrayList<>();

        producers.add(
                this.<WindowPayload>buildProducerJob(
                        "window", dataSource, PayloadType.WINDOW, samplingIntervalMilli));
        producers.add(
                this.<BrowserPayload>buildProducerJob(
                        "browser", dataSource, PayloadType.BROWSER, samplingIntervalMilli));
        producers.add(
                this.<EditorPayload>buildProducerJob(
                        "editor", dataSource, PayloadType.EDITOR, samplingIntervalMilli));
        producers.add(
                this.<MeetingPayload>buildProducerJob(
                        "meeting", dataSource, PayloadType.MEETING, samplingIntervalMilli));
        return producers;
    }

    private <P> Thread buildProducerJob(
            String name,
            DataSource dataSource,
            PayloadType payloadType,
            long samplingIntervalMilli) {
        Thread thread =
                new Thread(
                        new ActivityProducerJob<P>(dataSource, payloadType, samplingIntervalMilli));

        thread.setName(String.format("%s-producer-job", name));

        return thread;
    }
}
