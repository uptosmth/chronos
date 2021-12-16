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

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import com.uptosmth.chronos.api.exception.JsonSchemaValidationException;

public class JsonSchemaValidator {
    private static final Logger log = LogManager.getLogger(JsonSchemaValidator.class);
    private static final Map<String, JsonSchema> schemaCache = new ConcurrentHashMap<>();

    public static void validate(JsonNode jsonNode, String schemaPath) {
        JsonSchema schema = getJsonSchema(schemaPath);

        Set<ValidationMessage> validationResult = schema.validate(jsonNode);

        if (!validationResult.isEmpty()) {
            throw new JsonSchemaValidationException(validationResult);
        }
    }

    private static JsonSchema getJsonSchema(String schemaPath) {
        return schemaCache.computeIfAbsent(
                schemaPath,
                path -> {
                    final InputStream schemaStream =
                            JsonSchemaValidator.class.getResourceAsStream(path);

                    JsonSchemaFactory schemaFactory =
                            JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

                    try {
                        return schemaFactory.getSchema(schemaStream);
                    } catch (Exception e) {
                        throw new RuntimeException(
                                String.format(
                                        "An error occurred while loading JSON Schema: %s", path),
                                e);
                    }
                });
    }
}
