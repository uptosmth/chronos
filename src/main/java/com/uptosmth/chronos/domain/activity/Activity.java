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

package com.uptosmth.chronos.domain.activity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.uptosmth.chronos.domain.PayloadType;

public class Activity<P> {
    private final UUID uuid;
    private final PayloadType type;
    private final Instant startedAt;
    private long elapsedMilli;
    private final P payload;
    private final List<UUID> heartbeats;

    private Activity(Builder<P> builder) {
        this.uuid = Objects.requireNonNull(builder.uuid, "uuid");
        this.type = Objects.requireNonNull(builder.type, "type");
        this.startedAt = Objects.requireNonNull(builder.startedAt, "startedAt");
        this.elapsedMilli = builder.elapsedMilli;
        this.payload = Objects.requireNonNull(builder.payload);
        this.heartbeats = builder.heartbeats;
    }

    public UUID getUuid() {
        return uuid;
    }

    public PayloadType getType() {
        return type;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getFinishedAt() {
        return startedAt.plusMillis(elapsedMilli);
    }

    public long getElapsedMilli() {
        return elapsedMilli;
    }

    public List<UUID> getHeartbeats() {
        return heartbeats;
    }

    public void addElapsedMilli(long elapsedMilli) {
        this.elapsedMilli += elapsedMilli;
    }

    public void addHeartbeats(List<UUID> heartbeats) {
        this.heartbeats.addAll(heartbeats);
    }

    public P getPayload() {
        return payload;
    }

    public boolean payloadEquals(P payload) {
        return this.payload.equals(payload);
    }

    public boolean isFinishedAfter(Instant when) {
        return getFinishedAt().isAfter(when);
    }

    @Override
    public String toString() {
        return String.format("%s={payload=%s}", getClass().getSimpleName(), payload);
    }

    public static <P> Builder<P> builder() {
        return new Builder<P>();
    }

    public static class Builder<P> {
        private UUID uuid;
        private PayloadType type;
        private Instant startedAt;
        private long elapsedMilli;
        private P payload;
        private final List<UUID> heartbeats = new ArrayList<>();

        public Builder<P> uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder<P> type(PayloadType type) {
            this.type = type;
            return this;
        }

        public Builder<P> startedAt(Instant startedAt) {
            this.startedAt = startedAt;
            return this;
        }

        public Builder<P> elapsedMilli(long elapsedMilli) {
            if (elapsedMilli < 0) throw new IllegalArgumentException("Elapsed time can't be < 0");

            this.elapsedMilli = elapsedMilli;
            return this;
        }

        public Builder<P> payload(P payload) {
            this.payload = payload;
            return this;
        }

        public Builder<P> heartbeats(List<UUID> heartbeats) {
            this.heartbeats.addAll(heartbeats);
            return this;
        }

        public Builder<P> addHeartbeat(UUID uuid) {
            this.heartbeats.add(uuid);
            return this;
        }

        public Activity<P> build() {
            return new Activity<>(this);
        }
    }
}
