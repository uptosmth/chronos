# Chronos

*Chronos* is an application that _spies_ on you. Yes, it spies on you for _your own good_. It reports how much you use
specific applications and builds a nice report so you can take action or bill your client!

## A picture is worth a thousand words

```
========================================================================
                        2021-12-20 -> 2021-12-21
========================================================================

------------------------------------------------------------------------
                               TOTAL TIME
------------------------------------------------------------------------

                                00:15:37

------------------------------------------------------------------------
                            ACTIVITY PER HOUR
------------------------------------------------------------------------
22: ████████████████ 15m 37s


------------------------------------------------------------------------
                         MOST USED APPLICATIONS
------------------------------------------------------------------------

Xfce4-terminal .................................................  7m  1s
Firefox ........................................................  4m 20s
jetbrains-idea-ce ..............................................  2m 15s
DBeaver ........................................................  1m 45s
Chromium .......................................................  0m 15s


------------------------------------------------------------------------
                        MOST COMMON WINDOW TITLES
------------------------------------------------------------------------

Terminal -  ....................................................  5m 40s
chronos – BarChart.java ........................................  1m 50s
DBeaver 21.3.1 - <chronos.db> Script-33  .......................  1m 45s
simplify · vti/chronos@152beb9 — Mozilla Firefox ...............  1m  0s
Terminal - maven.yml (~/dev/chronos/chronos/.github/workflows) .  0m 40s
GitHub · Where software is built — Mozilla Firefox .............  0m 30s
github action x11 at DuckDuckGo — Mozilla Firefox ..............  0m 25s
TimeUnit (Java Platform SE 7 ) — Mozilla Firefox ...............  0m 25s
chronos – Api.java .............................................  0m 25s
chronos/DEVELOPMENT.md at master · vti/chronos — Mozilla Firef .  0m 20s


------------------------------------------------------------------------
                          MOST VISITED WEBSITES
------------------------------------------------------------------------

docs.oracle.com ................................................  2m 21s
github.com .....................................................  1m 15s
linux-commands-examples.com ....................................  0m  6s
www.youtube.com ................................................  0m  1s
developer.mozilla.org ..........................................  0m  0s

```

## Features

At the moment chronos reports on:

- window activity (X11)
- website activity (Firefox, Chrome web extension)
- editor activity (JetBrains IDEs, VIM)
- meetings activity (Zoom, Microsoft Teams)

### Window activity

Right now only Linux/X11 is supported. Periodically the X11 is polled for the current active window.

Screen Saver or prolonged inactivity is also respected.

### Website activity

Web extension can be enabled in Firefox or Chrome in development mode just by directly pointing to the web-extension
directory. Get extension from [chronos-web-extension](https://github.com/uptosmth/chronos-web-extension). By default, it
posts the results to `http://localhost:10203`, this can be changed in the future or even made configurable.

### Editor activity

Editor plugins report the file & project that you're currently working on.

The following integrations are available:

- [JetBrains IDEs](https://github.com/uptosmth/jetbrains-chronos)
- [VIM](https://github.com/uptosmth/vim-chronos)

### Meetings activity

The following meetings are currently detected:

- Zoom calls
- Microsoft Teams calls

## Commands

### Record

A background job has to run in order to collect recordings and listen for incoming activity reports.

For example one can create a `systemd` service file like the following:

**TODO** provide a userspace systemd service configuration example.

```text
[Unit]
Description=Chronos
Requires=network.target
After=network.target
StartLimitIntervalSec=0

[Service]
Type=simple
Restart=always
RestartSec=10
User=<your-user>
Group=<your-group>
WorkingDirectory=<path-where-chronos-is-installed/cloned>
Environment="DISPLAY=:0"
ExecStart=<path-where-chronos-is-installed/cloned> record
ExecStop=/bin/kill ${MAINPID}
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

### Dashboard

```text
chronos dashboard
```

### Help

All commands & their arguments are documented (e.g. run just `chronos` without any command or option):

```text
Usage: chronos [-hV] [--debug] [--db=./chronos.db] [COMMAND]
Spies on your by recording what application are being used and for how long
      --db=./chronos.db   Path to the database
      --debug             Enable debug mode
  -h, --help              Show this help message and exit.
  -V, --version           Print version information and exit.
Commands:
  help             Displays help information about the specified command
  dashboard, dash  Display dashboard
  record           Start recording
```

Every command has its own help page (e.g. `chronos help record`):

```text
Usage: chronos dashboard [-hV] [--debug] [--db=./chronos.db] [--since=today]
                         [--until=tomorrow]
Display dashboard
      --db=./chronos.db   Path to the database
      --since=today       Start from date (inclusive)
      --until=tomorrow    Stop at date (not inclusive)
  -h, --help              Show this help message and exit.
  -V, --version           Print version information and exit.
      --debug             Enable debug mode
```

## Development

1. Make sure you have at least JDK 8 installed

1. Clone this repo

    ```
    $ git clone https://github.com/vti/chronos
    ```

1. Compile

    ```
    $ ./mvnw clean package
    ```

1. Run it

    ```
    $ bin/chronos
    ```

# Copyright & License

    Copyright (C) 2021 Viacheslav Tykhanovskyi

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
