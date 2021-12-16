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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.uptosmth.chronos.domain.PayloadType;

public class Heartbeat<P> {

    private final UUID uuid;
    private final PayloadType type;
    private final Instant deliveredAt;
    private final Instant createdAt;
    private final Instant processedAt;
    private final P payload;

    private Heartbeat(Builder<P> builder) {
        this.uuid = Objects.requireNonNull(builder.uuid, "uuid");
        this.type = Objects.requireNonNull(builder.type, "type");
        this.deliveredAt = builder.deliveredAt == null ? Instant.now() : builder.deliveredAt;
        this.createdAt = builder.createdAt == null ? Instant.now() : builder.createdAt;
        this.processedAt = builder.processedAt;
        this.payload = Objects.requireNonNull(builder.payload, "payload");
    }

    public UUID getUuid() {
        return uuid;
    }

    public PayloadType getType() {
        return type;
    }

    public Instant getDeliveredAt() {
        return deliveredAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Optional<Instant> getProcessedAt() {
        return Optional.ofNullable(processedAt);
    }

    public P getPayload() {
        return payload;
    }

    public long elapsedFrom(Heartbeat heartbeat) {
        long elapsedMilli =
                getCreatedAt().minusMillis(heartbeat.getCreatedAt().toEpochMilli()).toEpochMilli();

        if (elapsedMilli < 0)
            throw new IllegalArgumentException("Other heartbeat has to be older, not newer");

        return elapsedMilli;
    }

    @Override
    public String toString() {
        return String.format(
                "%s={uuid=%s, createdAt=%s, type=%s}",
                getClass().getSimpleName(), uuid.toString(), createdAt.toString(), type);
    }

    public static <P> Builder<P> builder() {
        return new Builder<P>();
    }

    public static class Builder<P> {
        private UUID uuid;
        private Instant deliveredAt;
        private Instant createdAt;
        private Instant processedAt;
        private PayloadType type;
        private P payload;

        public Builder<P> uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder<P> deliveredAt(Instant deliveredAt) {
            this.deliveredAt = deliveredAt;
            return this;
        }

        public Builder<P> createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder<P> processedAt(Instant processedAt) {
            this.processedAt = processedAt;
            return this;
        }

        public Builder<P> type(PayloadType type) {
            this.type = type;
            return this;
        }

        public Builder<P> payload(P payload) {
            this.payload = payload;
            return this;
        }

        public Heartbeat<P> build() {
            return new Heartbeat<P>(this);
        }
    }
}
