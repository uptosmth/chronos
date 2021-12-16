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

import java.util.Random;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.uptosmth.chronos.domain.PayloadType;
import com.uptosmth.chronos.domain.activity.ActivityProducer;
import com.uptosmth.chronos.domain.activity.ActivityRepository;
import com.uptosmth.chronos.domain.heartbeat.HeartbeatRepository;
import com.uptosmth.chronos.infra.heartbeat.SqliteHeartbeatRepository;

public class ActivityProducerJob<P> implements Runnable {
    private static final Logger log = LogManager.getLogger(ActivityProducerJob.class);

    private final DataSource dataSource;
    private final PayloadType payloadType;
    private final long samplingIntervalMilli;

    public ActivityProducerJob(
            DataSource dataSource, PayloadType payloadType, long samplingIntervalMilli) {
        this.dataSource = dataSource;
        this.payloadType = payloadType;
        this.samplingIntervalMilli = samplingIntervalMilli;
    }

    @Override
    public void run() {
        ActivityRepository<P> activityRepository = new SqliteActivityRepository<>(dataSource);
        HeartbeatRepository<P> heartbeatRepository = new SqliteHeartbeatRepository<>(dataSource);

        ActivityProducer<P> producer =
                new ActivityProducer<>(
                        activityRepository,
                        heartbeatRepository,
                        payloadType,
                        samplingIntervalMilli);

        try {
            Thread.sleep(new Random().nextInt(10) * 1_000);
        } catch (InterruptedException e) {
        }

        while (true) {
            try {
                int produced = producer.produce();

                log.debug("Produced new activities: {}", produced);
            } catch (Exception e) {
                log.error("Error producing activities", e);
            }

            try {
                Thread.sleep(30_000);
            } catch (InterruptedException e) {
            }
        }
    }
}
