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

package com.uptosmth.chronos.infra.activity.editor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.uptosmth.chronos.domain.activity.editor.EditorPayload;

class FileResolverTest {

    @Test
    void getsDataFromVcs() {
        EditorPayload payload =
                new FileResolver()
                        .resolve(
                                "./src/main/java/com/uptosmth/chronos/domain/activity/editor/EditorPayload.java",
                                "some project name",
                                null);

        assertEquals("chronos", payload.getProject());
        assertEquals("master", payload.getBranch());
    }

    @Test
    void noInfoWhenFileNotFound() {
        EditorPayload payload = new FileResolver().resolve("unknown-file.txt", "project", null);

        assertEquals("project", payload.getProject());
        assertNull(payload.getBranch());
    }
}
