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

package com.uptosmth.chronos.api.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.ResponseHeaders;
import com.linecorp.armeria.common.logging.LogLevel;
import com.linecorp.armeria.server.annotation.ExceptionHandler;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.RequestConverter;
import com.linecorp.armeria.server.annotation.decorator.CorsDecorator;
import com.linecorp.armeria.server.annotation.decorator.LoggingDecorator;

import com.uptosmth.chronos.api.JsonConverter;
import com.uptosmth.chronos.api.ServiceExceptionHandler;
import com.uptosmth.chronos.api.service.model.BrowserHeartbeatModel;
import com.uptosmth.chronos.api.service.model.EditorHeartbeatModel;
import com.uptosmth.chronos.domain.PayloadType;
import com.uptosmth.chronos.domain.RepositoryRegistry;
import com.uptosmth.chronos.domain.activity.browser.BrowserPayload;
import com.uptosmth.chronos.domain.activity.editor.EditorPayload;
import com.uptosmth.chronos.domain.heartbeat.Heartbeat;
import com.uptosmth.chronos.infra.activity.editor.FileResolver;

@LoggingDecorator(requestLogLevel = LogLevel.INFO)
@ExceptionHandler(ServiceExceptionHandler.class)
public class HeartbeatService {
    private final RepositoryRegistry repositoryRegistry;

    public HeartbeatService(RepositoryRegistry repositoryRegistry) {
        this.repositoryRegistry = repositoryRegistry;
    }

    @CorsDecorator(
            origins = "*",
            allowedRequestHeaders = "content-type",
            allowedRequestMethods = HttpMethod.POST)
    @RequestConverter(JsonConverter.class)
    @Post("/browser")
    public HttpResponse createBrowserHeartbeats(BrowserHeartbeatModel[] models) {
        List<Heartbeat<BrowserPayload>> heartbeats = new ArrayList<>();

        for (BrowserHeartbeatModel model : models) {
            if (model.getTitle().isEmpty() || model.getUrl().isEmpty()) {
                continue;
            }

            Heartbeat<BrowserPayload> heartbeat =
                    Heartbeat.<BrowserPayload>builder()
                            .uuid(UUID.randomUUID())
                            .deliveredAt(Instant.now())
                            .createdAt(
                                    model.getTimestamp() == null
                                            ? Instant.now()
                                            : Instant.ofEpochMilli(model.getTimestamp()))
                            .type(PayloadType.BROWSER)
                            .payload(new BrowserPayload(model.getTitle(), model.getUrl()))
                            .build();

            heartbeats.add(heartbeat);
        }

        repositoryRegistry.<BrowserPayload>getHeartbeatRepository().append(heartbeats);

        return HttpResponse.of(
                ResponseHeaders.of(
                        HttpStatus.OK,
                        "Access-Control-Allow-Origin",
                        "*",
                        "Access-Control-Allow-Headers",
                        "content-type"));
    }

    @RequestConverter(JsonConverter.class)
    @Post("/editor")
    public HttpResponse createEditorHeartbeats(EditorHeartbeatModel[] models) {
        List<Heartbeat<EditorPayload>> heartbeats = new ArrayList<>();

        for (EditorHeartbeatModel model : models) {
            if (model.getFile().isEmpty()) {
                continue;
            }

            EditorPayload payload =
                    new FileResolver()
                            .resolve(model.getFile(), model.getProject(), model.getBranch());

            Heartbeat<EditorPayload> heartbeat =
                    Heartbeat.<EditorPayload>builder()
                            .uuid(UUID.randomUUID())
                            .deliveredAt(Instant.now())
                            .createdAt(
                                    model.getTimestamp() == null
                                            ? null
                                            : Instant.ofEpochMilli(model.getTimestamp()))
                            .type(PayloadType.EDITOR)
                            .payload(payload)
                            .build();

            heartbeats.add(heartbeat);
        }

        repositoryRegistry.<EditorPayload>getHeartbeatRepository().append(heartbeats);

        return HttpResponse.of(HttpStatus.OK);
    }
}
