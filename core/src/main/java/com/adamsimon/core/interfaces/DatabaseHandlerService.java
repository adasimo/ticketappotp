package com.adamsimon.core.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.core.domain.User;

import java.math.BigDecimal;

public interface DatabaseHandlerService {

    User getUserFromAuthToken(final String token);
    Boolean getIfUserIdOwnsCardId(final Long userId, final String cardId);
    BigDecimal getAmountFromCardId(final String cardId);
}
