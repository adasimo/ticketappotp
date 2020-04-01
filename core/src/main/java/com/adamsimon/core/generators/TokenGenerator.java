package com.adamsimon.core.generators;

import com.adamsimon.core.domain.User;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Random;

public class TokenGenerator implements IdentifierGenerator {

    final Logger logger = LoggerFactory.getLogger(TokenGenerator.class);

    @Override
    public Serializable generate(final SharedSessionContractImplementor session, final Object object)
            throws HibernateException {

        final User user = (User) object;
        String token = generateUniqueToken(50);
        token += user.getUserId();

        return token;
    }

    public String generateUniqueToken(final int length){
        final int leftLimit = 48;
        final int rightLimit = 122;
        final int targetStringLength = length;
        final Random random = new Random();

        final String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}

