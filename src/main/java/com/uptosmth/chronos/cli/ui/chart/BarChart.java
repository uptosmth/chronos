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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.javatuples.Pair;

public class BarChart {
    private static final char SYMBOL = '\u2588';

    private final List<Pair<Integer, Integer>> data;

    public BarChart() {
        data = new ArrayList<>();
    }

    public void addData(int x, int hours) {
        data.add(new Pair(x, hours));
    }

    public double calculateScale(int availableWidth) {

        // Prefix, e.g. 13:<space>
        availableWidth -= 4;

        int longestLegend =
                data.stream()
                        .map(p -> legendFormatted(p.getValue1()).length())
                        .max(Integer::compare)
                        .orElse(0);
        availableWidth -= 1 + longestLegend;

        return availableWidth / (double) TimeUnit.HOURS.toSeconds(1);
    }

    public String render(int width) {
        StringBuilder plot = new StringBuilder();

        double scale = calculateScale(width);

        for (Pair<Integer, Integer> bucket : data) {
            plot.append(String.format("%2s", bucket.getValue0()));
            plot.append(": ");

            int length = (int) Math.ceil(bucket.getValue1() * scale);
            plot.append(renderBar(SYMBOL, length));

            plot.append(" ");
            plot.append(legendFormatted(bucket.getValue1()));

            plot.append("\n");
        }

        return plot.toString();
    }

    private String legendFormatted(Integer legend) {
        return DurationFormatter.formatCompact(legend);
    }

    private String renderBar(char symbol, int length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(symbol);
        }

        return builder.toString();
    }
}
