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

package com.uptosmth.chronos.domain.heartbeat;

import java.time.Instant;
import java.util.List;

import com.uptosmth.chronos.domain.PayloadType;

public class HeartbeatAggregator<P> {
    private final HeartbeatRepository<P> heartbeatRepository;
    private final PayloadType payloadType;
    private final long samplingIntervalMilli;

    public HeartbeatAggregator(
            HeartbeatRepository<P> heartbeatRepository,
            PayloadType payloadType,
            long samplingIntervalMilli) {
        this.heartbeatRepository = heartbeatRepository;
        this.payloadType = payloadType;
        this.samplingIntervalMilli = samplingIntervalMilli;
    }

    public void aggregate(AggregatorVisitor<AggregatedHeartbeat<P>> visitor) {
        AggregatedHeartbeat<P> aggregated = null;
        Heartbeat<P> previousHeartbeat = null;

        int offset = 0;

        while (true) {
            List<Heartbeat<P>> heartbeats =
                    heartbeatRepository.findNotProcessed(payloadType, 500, offset);

            if (heartbeats.size() == 0) {
                break;
            }

            for (Heartbeat<P> heartbeat : heartbeats) {
                if (previousHeartbeat != null) {
                    long elapsedMilli = heartbeat.elapsedFrom(previousHeartbeat);

                    if (elapsedMilli > samplingIntervalMilli * 2) {
                        elapsedMilli = samplingIntervalMilli;
                    }

                    if (aggregated == null) {
                        aggregated = new AggregatedHeartbeat<>(previousHeartbeat, elapsedMilli);
                    } else {
                        aggregated.add(previousHeartbeat, elapsedMilli);
                    }

                    if (!previousHeartbeat.getPayload().equals(heartbeat.getPayload())) {
                        visitor.call(aggregated);

                        aggregated = null;
                    }
                }

                previousHeartbeat = heartbeat;

                offset++;
            }
        }

        if (aggregated != null) {
            visitor.call(aggregated);
        }

        if (previousHeartbeat != null) {
            long elapsedMilli =
                    Instant.now().toEpochMilli() - previousHeartbeat.getCreatedAt().toEpochMilli();

            if (elapsedMilli > samplingIntervalMilli * 2) {
                visitor.call(new AggregatedHeartbeat<>(previousHeartbeat, samplingIntervalMilli));
            }
        }
    }
}
