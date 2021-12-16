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

package com.uptosmth.chronos.cli;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.uptosmth.chronos.cli.util.DateReference;

class DateReferenceTest {

    @ParameterizedTest
    @MethodSource("referenceValues")
    public void parsesAgo(String reference, String expectedDate) {
        LocalDate now = LocalDate.of(2020, 4, 15);

        assertEquals(expectedDate, DateReference.parse(now, reference).toString());
    }

    private static Stream<Arguments> referenceValues() {
        return Stream.of(
                Arguments.of("today", "2020-04-15"),
                Arguments.of("yesterday", "2020-04-14"),
                Arguments.of("tomorrow", "2020-04-16"),
                Arguments.of("1 day ago", "2020-04-14"),
                Arguments.of("10 days ago", "2020-04-05"),
                Arguments.of("2 weeks ago", "2020-04-01"),
                Arguments.of("3 months ago", "2020-01-15"),
                Arguments.of("1 year ago", "2019-04-15"),
                Arguments.of("2021-01-01", "2021-01-01"));
    }
}
