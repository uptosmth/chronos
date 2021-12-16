/*
 * This file is generated by jOOQ.
 */
package com.uptosmth.chronos.db;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import com.uptosmth.chronos.db.tables.Activity;
import com.uptosmth.chronos.db.tables.BrowserActivity;
import com.uptosmth.chronos.db.tables.EditorActivity;
import com.uptosmth.chronos.db.tables.Heartbeat;
import com.uptosmth.chronos.db.tables.MeetingActivity;
import com.uptosmth.chronos.db.tables.SqliteSequence;
import com.uptosmth.chronos.db.tables.WindowActivity;

/** This class is generated by jOOQ. */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /** The reference instance of <code>DEFAULT_SCHEMA</code> */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /** The table <code>activity</code>. */
    public final Activity ACTIVITY = Activity.ACTIVITY;

    /** The table <code>browser_activity</code>. */
    public final BrowserActivity BROWSER_ACTIVITY = BrowserActivity.BROWSER_ACTIVITY;

    /** The table <code>editor_activity</code>. */
    public final EditorActivity EDITOR_ACTIVITY = EditorActivity.EDITOR_ACTIVITY;

    /** The table <code>heartbeat</code>. */
    public final Heartbeat HEARTBEAT = Heartbeat.HEARTBEAT;

    /** The table <code>meeting_activity</code>. */
    public final MeetingActivity MEETING_ACTIVITY = MeetingActivity.MEETING_ACTIVITY;

    /** The table <code>sqlite_sequence</code>. */
    public final SqliteSequence SQLITE_SEQUENCE = SqliteSequence.SQLITE_SEQUENCE;

    /** The table <code>window_activity</code>. */
    public final WindowActivity WINDOW_ACTIVITY = WindowActivity.WINDOW_ACTIVITY;

    /** No further instances allowed */
    private DefaultSchema() {
        super("", null);
    }

    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
                Activity.ACTIVITY,
                BrowserActivity.BROWSER_ACTIVITY,
                EditorActivity.EDITOR_ACTIVITY,
                Heartbeat.HEARTBEAT,
                MeetingActivity.MEETING_ACTIVITY,
                SqliteSequence.SQLITE_SEQUENCE,
                WindowActivity.WINDOW_ACTIVITY);
    }
}
