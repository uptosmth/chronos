/*
 * This file is generated by jOOQ.
 */
package com.uptosmth.chronos.db.tables.records;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.TableRecordImpl;

import com.uptosmth.chronos.db.tables.SqliteSequence;

/** This class is generated by jOOQ. */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class SqliteSequenceRecord extends TableRecordImpl<SqliteSequenceRecord>
        implements Record2<Object, Object> {

    private static final long serialVersionUID = 1L;

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify
     *     how this type should be handled. Deprecation can be turned off using {@literal
     *     <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public void setName(Object value) {
        set(0, value);
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify
     *     how this type should be handled. Deprecation can be turned off using {@literal
     *     <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public Object getName() {
        return get(0);
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify
     *     how this type should be handled. Deprecation can be turned off using {@literal
     *     <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public void setSeq(Object value) {
        set(1, value);
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify
     *     how this type should be handled. Deprecation can be turned off using {@literal
     *     <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    public Object getSeq() {
        return get(1);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Object, Object> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Object, Object> valuesRow() {
        return (Row2) super.valuesRow();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify
     *     how this type should be handled. Deprecation can be turned off using {@literal
     *     <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    @Override
    public Field<Object> field1() {
        return SqliteSequence.SQLITE_SEQUENCE.NAME;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify
     *     how this type should be handled. Deprecation can be turned off using {@literal
     *     <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    @Override
    public Field<Object> field2() {
        return SqliteSequence.SQLITE_SEQUENCE.SEQ;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify
     *     how this type should be handled. Deprecation can be turned off using {@literal
     *     <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    @Override
    public Object component1() {
        return getName();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify
     *     how this type should be handled. Deprecation can be turned off using {@literal
     *     <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    @Override
    public Object component2() {
        return getSeq();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify
     *     how this type should be handled. Deprecation can be turned off using {@literal
     *     <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    @Override
    public Object value1() {
        return getName();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify
     *     how this type should be handled. Deprecation can be turned off using {@literal
     *     <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    @Override
    public Object value2() {
        return getSeq();
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify
     *     how this type should be handled. Deprecation can be turned off using {@literal
     *     <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    @Override
    public SqliteSequenceRecord value1(Object value) {
        setName(value);
        return this;
    }

    /**
     * @deprecated Unknown data type. Please define an explicit {@link org.jooq.Binding} to specify
     *     how this type should be handled. Deprecation can be turned off using {@literal
     *     <deprecationOnUnknownTypes/>} in your code generator configuration.
     */
    @Deprecated
    @Override
    public SqliteSequenceRecord value2(Object value) {
        setSeq(value);
        return this;
    }

    @Override
    public SqliteSequenceRecord values(Object value1, Object value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** Create a detached SqliteSequenceRecord */
    public SqliteSequenceRecord() {
        super(SqliteSequence.SQLITE_SEQUENCE);
    }

    /** Create a detached, initialised SqliteSequenceRecord */
    public SqliteSequenceRecord(Object name, Object seq) {
        super(SqliteSequence.SQLITE_SEQUENCE);

        setName(name);
        setSeq(seq);
    }
}
