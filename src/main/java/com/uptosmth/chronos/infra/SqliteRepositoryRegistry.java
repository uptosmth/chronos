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

package com.uptosmth.chronos.infra;

import java.util.Objects;

import javax.sql.DataSource;

import com.uptosmth.chronos.domain.RepositoryRegistry;
import com.uptosmth.chronos.domain.heartbeat.HeartbeatRepository;
import com.uptosmth.chronos.infra.heartbeat.SqliteHeartbeatRepository;

public class SqliteRepositoryRegistry implements RepositoryRegistry {
    private final DataSource dataSource;

    public SqliteRepositoryRegistry(DataSource dataSource) {
        Objects.requireNonNull(dataSource);

        this.dataSource = dataSource;
    }

    @Override
    public <P> HeartbeatRepository<P> getHeartbeatRepository() {
        return new SqliteHeartbeatRepository<P>(dataSource);
    }
}
