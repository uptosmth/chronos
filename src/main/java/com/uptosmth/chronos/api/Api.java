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

package com.uptosmth.chronos.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import com.linecorp.armeria.server.logging.ContentPreviewingService;

import com.uptosmth.chronos.api.service.HeartbeatService;
import com.uptosmth.chronos.domain.RepositoryRegistry;

public final class Api {
    private static final Logger log = LogManager.getLogger(Api.class);

    private final RepositoryRegistry repositoryRegistry;
    private final int port;
    private Server server;

    public Api(RepositoryRegistry repositoryRegistry, int port) {
        this.repositoryRegistry = repositoryRegistry;
        this.port = port;
    }

    public void start() {
        server = newServer(port);

        server.start().join();

        log.info("Server started: port {}", server.activeLocalPort());
    }

    public void stop() {
        server.stop().join();
    }

    private Server newServer(int httpPort) {
        return Server.builder()
                .http(httpPort)
                .accessLogWriter(AccessLogWriter.combined(), true)
                .decoratorUnder("/", ContentPreviewingService.newDecorator(1024))
                .annotatedService("/heartbeats", new HeartbeatService(repositoryRegistry))
                .build();
    }
}
