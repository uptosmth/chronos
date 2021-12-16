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

import java.util.LinkedList;
import java.util.List;

public class SymbolWheel {
    private final List<String> symbols = new LinkedList<>();
    private LinkedList<String> wheel = new LinkedList<>();

    public SymbolWheel(List<String> symbols) {
        this.symbols.addAll(symbols);

        reset();
    }

    public String next() {
        String symbol = wheel.poll();

        wheel.addLast(symbol);

        return symbol;
    }

    public void reset() {
        this.wheel = new LinkedList<>(symbols);
    }
}
