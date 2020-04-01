package com.adamsimon.core.generators;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserIdGenerator implements IdentifierGenerator {

    final Logger logger = LoggerFactory.getLogger(UserIdGenerator.class);

    @Override
    public Serializable generate(final SharedSessionContractImplementor session, final Object object)
            throws HibernateException {

        final Connection connection = session.connection();

        try {
            final Statement statement = connection.createStatement();

            final ResultSet rs = statement.executeQuery("select count(userId) as Id from Users");

            if(rs.next()) {
                return (rs.getInt(1) + 1) * 1000;
            }
        } catch (final SQLException e) {
            logger.error("SQLException: ", e);
        }

        return null;
    }
}
