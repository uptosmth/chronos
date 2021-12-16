/*
 * This file is generated by jOOQ.
 */
package com.uptosmth.chronos.db;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;

import com.uptosmth.chronos.db.tables.Activity;
import com.uptosmth.chronos.db.tables.BrowserActivity;
import com.uptosmth.chronos.db.tables.EditorActivity;
import com.uptosmth.chronos.db.tables.Heartbeat;
import com.uptosmth.chronos.db.tables.MeetingActivity;
import com.uptosmth.chronos.db.tables.WindowActivity;
import com.uptosmth.chronos.db.tables.records.ActivityRecord;
import com.uptosmth.chronos.db.tables.records.BrowserActivityRecord;
import com.uptosmth.chronos.db.tables.records.EditorActivityRecord;
import com.uptosmth.chronos.db.tables.records.HeartbeatRecord;
import com.uptosmth.chronos.db.tables.records.MeetingActivityRecord;
import com.uptosmth.chronos.db.tables.records.WindowActivityRecord;

/** A class modelling foreign key relationships and constraints of tables in the default schema. */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ActivityRecord> PK_ACTIVITY =
            Internal.createUniqueKey(
                    Activity.ACTIVITY,
                    DSL.name("pk_activity"),
                    new TableField[] {Activity.ACTIVITY.ID},
                    true);
    public static final UniqueKey<ActivityRecord> SQLITE_AUTOINDEX_ACTIVITY_1 =
            Internal.createUniqueKey(
                    Activity.ACTIVITY,
                    DSL.name("sqlite_autoindex_activity_1"),
                    new TableField[] {Activity.ACTIVITY.UUID},
                    true);
    public static final UniqueKey<BrowserActivityRecord> PK_BROWSER_ACTIVITY =
            Internal.createUniqueKey(
                    BrowserActivity.BROWSER_ACTIVITY,
                    DSL.name("pk_browser_activity"),
                    new TableField[] {BrowserActivity.BROWSER_ACTIVITY.ID},
                    true);
    public static final UniqueKey<EditorActivityRecord> PK_EDITOR_ACTIVITY =
            Internal.createUniqueKey(
                    EditorActivity.EDITOR_ACTIVITY,
                    DSL.name("pk_editor_activity"),
                    new TableField[] {EditorActivity.EDITOR_ACTIVITY.ID},
                    true);
    public static final UniqueKey<HeartbeatRecord> PK_HEARTBEAT =
            Internal.createUniqueKey(
                    Heartbeat.HEARTBEAT,
                    DSL.name("pk_heartbeat"),
                    new TableField[] {Heartbeat.HEARTBEAT.ID},
                    true);
    public static final UniqueKey<MeetingActivityRecord> PK_MEETING_ACTIVITY =
            Internal.createUniqueKey(
                    MeetingActivity.MEETING_ACTIVITY,
                    DSL.name("pk_meeting_activity"),
                    new TableField[] {MeetingActivity.MEETING_ACTIVITY.ID},
                    true);
    public static final UniqueKey<WindowActivityRecord> PK_WINDOW_ACTIVITY =
            Internal.createUniqueKey(
                    WindowActivity.WINDOW_ACTIVITY,
                    DSL.name("pk_window_activity"),
                    new TableField[] {WindowActivity.WINDOW_ACTIVITY.ID},
                    true);
}