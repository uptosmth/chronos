/*
 * This file is generated by jOOQ.
 */
package com.uptosmth.chronos.db.tables.records;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

import com.uptosmth.chronos.db.tables.MeetingActivity;

/** This class is generated by jOOQ. */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class MeetingActivityRecord extends UpdatableRecordImpl<MeetingActivityRecord>
        implements Record2<Integer, String> {

    private static final long serialVersionUID = 1L;

    /** Setter for <code>meeting_activity.id</code>. */
    public void setId(Integer value) {
        set(0, value);
    }

    /** Getter for <code>meeting_activity.id</code>. */
    public Integer getId() {
        return (Integer) get(0);
    }

    /** Setter for <code>meeting_activity.application</code>. */
    public void setApplication(String value) {
        set(1, value);
    }

    /** Getter for <code>meeting_activity.application</code>. */
    public String getApplication() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Integer, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return MeetingActivity.MEETING_ACTIVITY.ID;
    }

    @Override
    public Field<String> field2() {
        return MeetingActivity.MEETING_ACTIVITY.APPLICATION;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getApplication();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getApplication();
    }

    @Override
    public MeetingActivityRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public MeetingActivityRecord value2(String value) {
        setApplication(value);
        return this;
    }

    @Override
    public MeetingActivityRecord values(Integer value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** Create a detached MeetingActivityRecord */
    public MeetingActivityRecord() {
        super(MeetingActivity.MEETING_ACTIVITY);
    }

    /** Create a detached, initialised MeetingActivityRecord */
    public MeetingActivityRecord(Integer id, String application) {
        super(MeetingActivity.MEETING_ACTIVITY);

        setId(id);
        setApplication(application);
    }
}
