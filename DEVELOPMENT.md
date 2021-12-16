# Development

## Format

```
$ ./mvnw spotless:apply
```

## Generate db schema

```
$ ./mvnw -P jooq package -DjooqDatabase=<databaseFile>
```

## Check dependencies

```
$ ./mvnw versions:display-dependency-updates

$ ./mvnw versions:display-plugin-updates
```

## JNI

Check signature

```
$ javap -s -classpath target/chronos.jar com/uptosmth/chronos/X11ScreenSaverInfo
```
