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

import java.util.Optional;
import java.util.UUID;

import com.uptosmth.chronos.domain.PayloadType;

public class MemoryActivityRepository<P> implements ActivityRepository<P> {
    @Override
    public Optional<Activity<P>> load(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<Activity<P>> loadLast(PayloadType type) {
        return Optional.empty();
    }

    @Override
    public void append(Activity<P> activity) {}

    @Override
    public void store(Activity<P> activity) {}
}
