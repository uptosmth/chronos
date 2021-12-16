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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.uptosmth.chronos.domain.PayloadType;

public class MemoryHeartbeatRepository<P> implements HeartbeatRepository<P> {
    private final List<Heartbeat<P>> heartbeats;

    public MemoryHeartbeatRepository() {
        this.heartbeats = new ArrayList<>();
    }

    public MemoryHeartbeatRepository(List<Heartbeat<P>> heartbeats) {
        this.heartbeats = heartbeats;
    }

    @Override
    public Optional<Heartbeat<P>> load(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public List<Heartbeat<P>> findNotProcessed(PayloadType type, int limit, int offset) {
        int end = offset + limit;
        int size = heartbeats.size();

        if (end > size) {
            if (offset >= size) {
                return new ArrayList<>();
            }
            end = size;
        }

        return heartbeats.subList(offset, end);
    }

    @Override
    public void append(Heartbeat<P> heartbeat) {
        this.heartbeats.add(heartbeat);
    }

    @Override
    public void append(List<Heartbeat<P>> heartbeats) {
        this.heartbeats.addAll(heartbeats);
    }
}
