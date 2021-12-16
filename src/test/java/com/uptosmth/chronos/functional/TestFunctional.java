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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.file.Path;

import javax.sql.DataSource;

import com.uptosmth.chronos.TestDatabase;
import com.uptosmth.chronos.api.Api;
import com.uptosmth.chronos.domain.RepositoryRegistry;
import com.uptosmth.chronos.infra.SqliteRepositoryRegistry;
import io.restassured.RestAssured;

public class TestFunctional {
    public static int findOpenPort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            return serverSocket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException("No free port available");
        }
    }

    public static Api buildApi(Path tmpDir) {
        DataSource dataSource = TestDatabase.setupDatabase(tmpDir);
        RepositoryRegistry repositoryRegistry = new SqliteRepositoryRegistry(dataSource);

        int port = TestFunctional.findOpenPort();

        URL baseUrl;

        try {
            baseUrl = new URL("http", "localhost", port, "/");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error building base url");
        }

        RestAssured.baseURI = baseUrl.toString();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        return new Api(repositoryRegistry, port);
    }
}
