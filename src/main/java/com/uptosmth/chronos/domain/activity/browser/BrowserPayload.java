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

package com.uptosmth.chronos.domain.activity.browser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BrowserPayload {
    private final String title;
    private final String url;
    private final String urlDomain;

    @JsonCreator
    public BrowserPayload(
            @JsonProperty(value = "title", required = true) String title,
            @JsonProperty(value = "url", required = true) String url) {
        this.title = Objects.requireNonNull(title, "title");
        this.url = Objects.requireNonNull(url, "url");
        this.urlDomain = parseDomain(url);
    }

    public BrowserPayload(String title, String url, String urlDomain) {
        this.title = Objects.requireNonNull(title, "title");
        this.url = Objects.requireNonNull(url, "url");
        this.urlDomain = Objects.requireNonNull(urlDomain, "urlDomain");
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlDomain() {
        return urlDomain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrowserPayload that = (BrowserPayload) o;
        return Objects.equals(title, that.title)
                && Objects.equals(url, that.url)
                && Objects.equals(urlDomain, that.urlDomain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, url, urlDomain);
    }

    private String parseDomain(String url) {
        if (url.startsWith("http")) {
            try {
                URL parsedUrl = new URL(url);

                return parsedUrl.getHost();
            } catch (MalformedURLException e) {
            }
        }

        return "localhost";
    }
}
