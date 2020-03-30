package com.adamsimon.core.generators;

import com.adamsimon.core.domain.Users;
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

    Logger logger = LoggerFactory.getLogger(UserIdGenerator.class);

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {

        Connection connection = session.connection();

        try {
            Statement statement=connection.createStatement();

            ResultSet rs=statement.executeQuery("select count(userId) as Id from Users");

            if(rs.next()) {
                int id=(rs.getInt(1) + 1) * 1000;
                return id;
            }
        } catch (SQLException e) {
            logger.error("SQLException: ", e);
        }

        return null;
    }
}
