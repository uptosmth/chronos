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

package com.uptosmth.chronos.functional;

import static io.restassured.RestAssured.given;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.uptosmth.chronos.api.Api;
import com.uptosmth.chronos.infra.DefaultObjectMapper;

@Tag("functional")
public class HeartbeatResourceTest {
    private Api api;

    @TempDir Path tmpDir;

    @BeforeEach
    void setUp() {
        api = TestFunctional.buildApi(tmpDir);

        api.start();
    }

    @AfterEach
    void tearDown() {
        api.stop();
    }

    @Test
    void submitsBrowserHeartbeats() throws JsonProcessingException {
        List<Map<String, Object>> heartbeats = new ArrayList<>();
        heartbeats.add(
                new HashMap<String, Object>() {
                    {
                        put("timestamp", Instant.now().toEpochMilli());
                        put("title", "Chronos");
                        put("url", "https://github.com/vti/chronos");
                    }
                });

        given().header("Content-Type", "application/json")
                .body(DefaultObjectMapper.get().writeValueAsString(heartbeats))
                .when()
                .post("/heartbeats/browser")
                .then()
                .statusCode(200);
    }

    @Test
    void submitsEditorHeartbeats() throws JsonProcessingException {
        List<Map<String, Object>> heartbeats = new ArrayList<>();
        heartbeats.add(
                new HashMap<String, Object>() {
                    {
                        put("timestamp", Instant.now().toEpochMilli());
                        put("project", "Project");
                        put("file", "/home/vti/project/File.java");
                    }
                });

        given().header("Content-Type", "application/json")
                .body(DefaultObjectMapper.get().writeValueAsString(heartbeats))
                .when()
                .post("/heartbeats/editor")
                .then()
                .statusCode(200);
    }
}
