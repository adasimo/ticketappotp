package com.adamsimon.commons.dto.builders;

import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.commons.dto.responseDto.ReservationSuccessResponse;
import com.adamsimon.commons.abstractions.AbstractPartnerResponse;

public class ReservationBuilder {
    public static class ReservationResponseBuilder {
        private Boolean success;
        private Long reservationId;
        private Integer errorCode;
        private String errorMessage;

        public ReservationResponseBuilder getSuccessBuilder() {
            this.success = true;
            return this;
        }

        public ReservationResponseBuilder getFailedBuilder() {
            this.success = false;
            return this;
        }

        public ReservationResponseBuilder withReservationIdToSuccess(Long reservationId) {
            this.reservationId = reservationId;
            return this;
        }

        public ReservationResponseBuilder withErrorCodeToFail(Integer errorCode) {
            this.errorCode = errorCode;
             return this;
        }

        public ReservationResponseBuilder withErrorMessageToFail(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public AbstractPartnerResponse build() {
            if (this.success) {
                 return new ReservationSuccessResponse(this.success, this.reservationId);
            } else {
                 return new ReservationFailedResponse(this.success, this.errorCode, this.errorMessage);
             }
         }

    }
}
