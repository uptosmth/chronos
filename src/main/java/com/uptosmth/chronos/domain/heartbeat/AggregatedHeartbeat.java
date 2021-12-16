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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AggregatedHeartbeat<P> {
    private final Instant createdAt;
    private final List<UUID> heartbeats = new ArrayList<>();
    private long elapsedMilli = 0;
    private final P payload;

    public AggregatedHeartbeat(Heartbeat<P> heartbeat, long elapsedMilli) {
        this.createdAt = heartbeat.getCreatedAt();
        this.heartbeats.add(heartbeat.getUuid());
        this.elapsedMilli += elapsedMilli;
        this.payload = heartbeat.getPayload();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<UUID> getHeartbeats() {
        return heartbeats;
    }

    public long getElapsedMilli() {
        return elapsedMilli;
    }

    public P getPayload() {
        return payload;
    }

    public void add(Heartbeat<P> heartbeat, long elapsedMilli) {
        this.heartbeats.add(heartbeat.getUuid());
        this.elapsedMilli += elapsedMilli;
    }
}
