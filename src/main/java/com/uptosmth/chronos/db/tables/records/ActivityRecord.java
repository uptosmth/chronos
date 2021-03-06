/*
 * This file is generated by jOOQ.
 */
package com.uptosmth.chronos.db.tables.records;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;

import com.uptosmth.chronos.db.tables.Activity;

/** This class is generated by jOOQ. */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class ActivityRecord extends UpdatableRecordImpl<ActivityRecord>
        implements Record9<
                Integer,
                String,
                String,
                LocalDateTime,
                LocalDateTime,
                LocalDateTime,
                LocalDateTime,
                LocalDateTime,
                Long> {

    private static final long serialVersionUID = 1L;

    /** Setter for <code>activity.id</code>. */
    public void setId(Integer value) {
        set(0, value);
    }

    /** Getter for <code>activity.id</code>. */
    public Integer getId() {
        return (Integer) get(0);
    }

    /** Setter for <code>activity.uuid</code>. */
    public void setUuid(String value) {
        set(1, value);
    }

    /** Getter for <code>activity.uuid</code>. */
    public String getUuid() {
        return (String) get(1);
    }

    /** Setter for <code>activity.type</code>. */
    public void setType(String value) {
        set(2, value);
    }

    /** Getter for <code>activity.type</code>. */
    public String getType() {
        return (String) get(2);
    }

    /** Setter for <code>activity.created_at</code>. */
    public void setCreatedAt(LocalDateTime value) {
        set(3, value);
    }

    /** Getter for <code>activity.created_at</code>. */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(3);
    }

    /** Setter for <code>activity.started_at</code>. */
    public void setStartedAt(LocalDateTime value) {
        set(4, value);
    }

    /** Getter for <code>activity.started_at</code>. */
    public LocalDateTime getStartedAt() {
        return (LocalDateTime) get(4);
    }

    /** Setter for <code>activity.finished_at</code>. */
    public void setFinishedAt(LocalDateTime value) {
        set(5, value);
    }

    /** Getter for <code>activity.finished_at</code>. */
    public LocalDateTime getFinishedAt() {
        return (LocalDateTime) get(5);
    }

    /** Setter for <code>activity.local_started_at</code>. */
    public void setLocalStartedAt(LocalDateTime value) {
        set(6, value);
    }

    /** Getter for <code>activity.local_started_at</code>. */
    public LocalDateTime getLocalStartedAt() {
        return (LocalDateTime) get(6);
    }

    /** Setter for <code>activity.local_finished_at</code>. */
    public void setLocalFinishedAt(LocalDateTime value) {
        set(7, value);
    }

    /** Getter for <code>activity.local_finished_at</code>. */
    public LocalDateTime getLocalFinishedAt() {
        return (LocalDateTime) get(7);
    }

    /** Setter for <code>activity.elapsed_milli</code>. */
    public void setElapsedMilli(Long value) {
        set(8, value);
    }

    /** Getter for <code>activity.elapsed_milli</code>. */
    public Long getElapsedMilli() {
        return (Long) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row9<
                    Integer,
                    String,
                    String,
                    LocalDateTime,
                    LocalDateTime,
                    LocalDateTime,
                    LocalDateTime,
                    LocalDateTime,
                    Long>
            fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<
                    Integer,
                    String,
                    String,
                    LocalDateTime,
                    LocalDateTime,
                    LocalDateTime,
                    LocalDateTime,
                    LocalDateTime,
                    Long>
            valuesRow() {
        return (Row9) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Activity.ACTIVITY.ID;
    }

    @Override
    public Field<String> field2() {
        return Activity.ACTIVITY.UUID;
    }

    @Override
    public Field<String> field3() {
        return Activity.ACTIVITY.TYPE;
    }

    @Override
    public Field<LocalDateTime> field4() {
        return Activity.ACTIVITY.CREATED_AT;
    }

    @Override
    public Field<LocalDateTime> field5() {
        return Activity.ACTIVITY.STARTED_AT;
    }

    @Override
    public Field<LocalDateTime> field6() {
        return Activity.ACTIVITY.FINISHED_AT;
    }

    @Override
    public Field<LocalDateTime> field7() {
        return Activity.ACTIVITY.LOCAL_STARTED_AT;
    }

    @Override
    public Field<LocalDateTime> field8() {
        return Activity.ACTIVITY.LOCAL_FINISHED_AT;
    }

    @Override
    public Field<Long> field9() {
        return Activity.ACTIVITY.ELAPSED_MILLI;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getUuid();
    }

    @Override
    public String component3() {
        return getType();
    }

    @Override
    public LocalDateTime component4() {
        return getCreatedAt();
    }

    @Override
    public LocalDateTime component5() {
        return getStartedAt();
    }

    @Override
    public LocalDateTime component6() {
        return getFinishedAt();
    }

    @Override
    public LocalDateTime component7() {
        return getLocalStartedAt();
    }

    @Override
    public LocalDateTime component8() {
        return getLocalFinishedAt();
    }

    @Override
    public Long component9() {
        return getElapsedMilli();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getUuid();
    }

    @Override
    public String value3() {
        return getType();
    }

    @Override
    public LocalDateTime value4() {
        return getCreatedAt();
    }

    @Override
    public LocalDateTime value5() {
        return getStartedAt();
    }

    @Override
    public LocalDateTime value6() {
        return getFinishedAt();
    }

    @Override
    public LocalDateTime value7() {
        return getLocalStartedAt();
    }

    @Override
    public LocalDateTime value8() {
        return getLocalFinishedAt();
    }

    @Override
    public Long value9() {
        return getElapsedMilli();
    }

    @Override
    public ActivityRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public ActivityRecord value2(String value) {
        setUuid(value);
        return this;
    }

    @Override
    public ActivityRecord value3(String value) {
        setType(value);
        return this;
    }

    @Override
    public ActivityRecord value4(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public ActivityRecord value5(LocalDateTime value) {
        setStartedAt(value);
        return this;
    }

    @Override
    public ActivityRecord value6(LocalDateTime value) {
        setFinishedAt(value);
        return this;
    }

    @Override
    public ActivityRecord value7(LocalDateTime value) {
        setLocalStartedAt(value);
        return this;
    }

    @Override
    public ActivityRecord value8(LocalDateTime value) {
        setLocalFinishedAt(value);
        return this;
    }

    @Override
    public ActivityRecord value9(Long value) {
        setElapsedMilli(value);
        return this;
    }

    @Override
    public ActivityRecord values(
            Integer value1,
            String value2,
            String value3,
            LocalDateTime value4,
            LocalDateTime value5,
            LocalDateTime value6,
            LocalDateTime value7,
            LocalDateTime value8,
            Long value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** Create a detached ActivityRecord */
    public ActivityRecord() {
        super(Activity.ACTIVITY);
    }

    /** Create a detached, initialised ActivityRecord */
    public ActivityRecord(
            Integer id,
            String uuid,
            String type,
            LocalDateTime createdAt,
            LocalDateTime startedAt,
            LocalDateTime finishedAt,
            LocalDateTime localStartedAt,
            LocalDateTime localFinishedAt,
            Long elapsedMilli) {
        super(Activity.ACTIVITY);

        setId(id);
        setUuid(uuid);
        setType(type);
        setCreatedAt(createdAt);
        setStartedAt(startedAt);
        setFinishedAt(finishedAt);
        setLocalStartedAt(localStartedAt);
        setLocalFinishedAt(localFinishedAt);
        setElapsedMilli(elapsedMilli);
    }
}
