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

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import com.uptosmth.chronos.domain.activity.window.WindowPayload;

public class DefaultObjectMapperTest {

    @Test
    public void doesNotIncludeNulls() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("foo", null);
        map.put("bar", "baz");
        map.put("baz", "");

        String json = DefaultObjectMapper.get().writeValueAsString(map);

        assertEquals("{\"bar\":\"baz\",\"baz\":\"\"}", json);
    }

    @Test
    public void doesNotIncludeEmptyOptionals() throws JsonProcessingException {
        Map<String, Optional<String>> map = new HashMap<>();
        map.put("foo", Optional.empty());
        map.put("bar", Optional.of("baz"));

        String json = DefaultObjectMapper.get().writeValueAsString(map);

        assertEquals("{\"bar\":\"baz\"}", json);
    }

    @Test
    public void throwsOnInvalidPayload() {
        assertThrows(
                MismatchedInputException.class,
                () -> DefaultObjectMapper.get().readValue("{}", WindowPayload.class));
    }
}
