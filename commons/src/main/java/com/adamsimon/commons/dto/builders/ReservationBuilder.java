package com.adamsimon.commons.dto.builders;

import com.adamsimon.commons.dto.ReservationFailed;
import com.adamsimon.commons.dto.ReservationSuccess;
import com.adamsimon.commons.abstractions.AbstractPartnerResponse;

public class ReservationBuilder {
    public static class ReservationResponseBuilder {
        private Boolean success;
        private Long reservationId;
        private Integer errorCode;

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

        public AbstractPartnerResponse build() {
            if (this.success) {
                 return new ReservationSuccess(this.success, this.reservationId);
            } else {
                 return new ReservationFailed(this.success, this.errorCode);
             }
         }

    }
}
