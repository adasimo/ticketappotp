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

public class CardIdGenerator implements IdentifierGenerator {

    Logger logger = LoggerFactory.getLogger(UserIdGenerator.class);

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {

        final String PREFIX = "C";
        final String FILLER = "0";

        Connection connection = session.connection();

        try {
            Statement statement=connection.createStatement();

            ResultSet rs=statement.executeQuery("select count(cardId) as Id from UserBankCard");

            if(rs.next()) {
                StringBuilder id = new StringBuilder((rs.getInt(1) + 1) + "");
                while(id.toString().length() < 3) {
                    id.insert(0, FILLER);
                }
                id.insert(0, PREFIX);
                return id.toString();
            }
        } catch (SQLException e) {
            logger.error("SQLException: ", e);
        }

        return null;
    }
}
