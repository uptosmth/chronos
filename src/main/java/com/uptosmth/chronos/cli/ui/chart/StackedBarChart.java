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

import java.util.*;

import org.javatuples.Pair;

public class StackedBarChart {
    private static final int MAX_WIDTH = 80;

    private final SymbolWheel symbols = new SymbolWheel(Arrays.asList("\u2588", "*", "#", "."));

    private final List<Pair<Integer, List<Pair<String, Integer>>>> data;

    public StackedBarChart() {
        data = new ArrayList<>();
    }

    public void addData(int x, List<Pair<String, Integer>> buckets) {
        data.add(new Pair(x, buckets));
    }

    public double calculateScale() {
        double max = 0;

        for (Pair<Integer, List<Pair<String, Integer>>> entry : data) {
            int maxInBucket =
                    entry.getValue1().stream()
                            .reduce(
                                    0,
                                    (t, v) -> {
                                        return t + v.getValue1();
                                    },
                                    Integer::sum);

            if (maxInBucket > max) {
                max = maxInBucket;
            }
        }

        return MAX_WIDTH / max;
    }

    public String render() {
        StringBuilder plot = new StringBuilder();

        double scale = calculateScale();

        for (Pair<Integer, List<Pair<String, Integer>>> entry : data) {
            plot.append(String.format("%2s", entry.getValue0()));
            plot.append(": ");

            for (Pair<String, Integer> bucket : entry.getValue1()) {
                int length = (int) Math.ceil(bucket.getValue1() * scale);
                plot.append(renderBar(symbols.next(), length));
            }

            symbols.reset();

            plot.append("\n");
        }

        return plot.toString();
    }

    private String renderBar(String symbol, int length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(symbol);
        }

        return builder.toString();
    }
}
