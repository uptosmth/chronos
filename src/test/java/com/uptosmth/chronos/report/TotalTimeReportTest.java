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

package com.uptosmth.chronos.report;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.uptosmth.chronos.TestDatabase;

class TotalTimeReportTest {

    private TotalTimeReport report;

    @TempDir private Path tempDir;

    @BeforeEach
    void setUp() {
        report = new TotalTimeReport(TestDatabase.setupDatabase(tempDir));
    }

    @Test
    void handlesEmptyData() {
        LocalTime totalTime = report.run(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 2));

        assertEquals("00:00", totalTime.toString());
    }
}
