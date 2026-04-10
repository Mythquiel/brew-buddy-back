package com.brewbuddy.domain;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class BeverageTypeEnumType implements UserType<BeverageType> {

    @Override
    public int getSqlType() {
        return Types.OTHER;
    }

    @Override
    public Class<BeverageType> returnedClass() {
        return BeverageType.class;
    }

    @Override
    public BeverageType nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String value = rs.getString(position);
        return value == null ? null : BeverageType.valueOf(value);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, BeverageType value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            st.setObject(index, value.name(), Types.OTHER);
        }
    }

    @Override
    public boolean equals(BeverageType x, BeverageType y) {
        return x == y;
    }

    @Override
    public int hashCode(BeverageType x) {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public BeverageType deepCopy(BeverageType value) {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(BeverageType value) {
        return value;
    }

    @Override
    public BeverageType assemble(Serializable cached, Object owner) {
        return (BeverageType) cached;
    }

    @Override
    public BeverageType replace(BeverageType detached, BeverageType managed, Object owner) {
        return detached;
    }
}
