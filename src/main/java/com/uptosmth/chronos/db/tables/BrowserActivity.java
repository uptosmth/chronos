/*
 * This file is generated by jOOQ.
 */
package com.uptosmth.chronos.db.tables;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import com.uptosmth.chronos.db.DefaultSchema;
import com.uptosmth.chronos.db.Keys;
import com.uptosmth.chronos.db.tables.records.BrowserActivityRecord;

/** This class is generated by jOOQ. */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class BrowserActivity extends TableImpl<BrowserActivityRecord> {

    private static final long serialVersionUID = 1L;

    /** The reference instance of <code>browser_activity</code> */
    public static final BrowserActivity BROWSER_ACTIVITY = new BrowserActivity();

    /** The class holding records for this type */
    @Override
    public Class<BrowserActivityRecord> getRecordType() {
        return BrowserActivityRecord.class;
    }

    /** The column <code>browser_activity.id</code>. */
    public final TableField<BrowserActivityRecord, Integer> ID =
            createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false), this, "");

    /** The column <code>browser_activity.title</code>. */
    public final TableField<BrowserActivityRecord, String> TITLE =
            createField(DSL.name("title"), SQLDataType.CLOB.nullable(false), this, "");

    /** The column <code>browser_activity.url</code>. */
    public final TableField<BrowserActivityRecord, String> URL =
            createField(DSL.name("url"), SQLDataType.CLOB.nullable(false), this, "");

    /** The column <code>browser_activity.url_domain</code>. */
    public final TableField<BrowserActivityRecord, String> URL_DOMAIN =
            createField(DSL.name("url_domain"), SQLDataType.CLOB.nullable(false), this, "");

    private BrowserActivity(Name alias, Table<BrowserActivityRecord> aliased) {
        this(alias, aliased, null);
    }

    private BrowserActivity(
            Name alias, Table<BrowserActivityRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /** Create an aliased <code>browser_activity</code> table reference */
    public BrowserActivity(String alias) {
        this(DSL.name(alias), BROWSER_ACTIVITY);
    }

    /** Create an aliased <code>browser_activity</code> table reference */
    public BrowserActivity(Name alias) {
        this(alias, BROWSER_ACTIVITY);
    }

    /** Create a <code>browser_activity</code> table reference */
    public BrowserActivity() {
        this(DSL.name("browser_activity"), null);
    }

    public <O extends Record> BrowserActivity(
            Table<O> child, ForeignKey<O, BrowserActivityRecord> key) {
        super(child, key, BROWSER_ACTIVITY);
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<BrowserActivityRecord> getPrimaryKey() {
        return Keys.PK_BROWSER_ACTIVITY;
    }

    @Override
    public List<UniqueKey<BrowserActivityRecord>> getKeys() {
        return Arrays.<UniqueKey<BrowserActivityRecord>>asList(Keys.PK_BROWSER_ACTIVITY);
    }

    @Override
    public BrowserActivity as(String alias) {
        return new BrowserActivity(DSL.name(alias), this);
    }

    @Override
    public BrowserActivity as(Name alias) {
        return new BrowserActivity(alias, this);
    }

    /** Rename this table */
    @Override
    public BrowserActivity rename(String name) {
        return new BrowserActivity(DSL.name(name), null);
    }

    /** Rename this table */
    @Override
    public BrowserActivity rename(Name name) {
        return new BrowserActivity(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Integer, String, String, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}