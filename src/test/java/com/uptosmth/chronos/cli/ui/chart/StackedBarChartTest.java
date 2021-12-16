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

package com.uptosmth.chronos.cli.ui.chart;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StackedBarChartTest {

    StackedBarChart chart;

    @BeforeEach
    public void setUp() {
        chart = new StackedBarChart();
    }

    @Test
    void calculatesScale() {
        chart.addData(1, Arrays.asList(new Pair<>("foo", 1)));
        chart.addData(2, Arrays.asList(new Pair<>("foo", 3)));
        chart.addData(3, Arrays.asList(new Pair<>("foo", 10), new Pair<>("bar", 7)));

        assertEquals("4.71", String.format("%.02f", chart.calculateScale()));
    }

    @Test
    void rendersChart() {
        chart.addData(1, Arrays.asList(new Pair<>("foo", 1)));
        chart.addData(2, Arrays.asList(new Pair<>("foo", 3)));
        chart.addData(3, Arrays.asList(new Pair<>("foo", 10), new Pair<>("bar", 7)));

        String plot = chart.render();

        assertNotEquals("", plot);
    }
}
