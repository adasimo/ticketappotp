package com.adamsimon.commons.dto.builders;

import com.adamsimon.commons.dto.ErrorResponse;

public class ErrorBuilder {
    public static class ErrorResponseBuilder {
        private String text;
        private Integer code;

        public ErrorResponseBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public ErrorResponseBuilder withCode(Integer code) {
            this.code = code;
             return this;
        }

        public ErrorResponse build() {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setText(text);
            errorResponse.setCode(code);
            return errorResponse;
        }

    }
}
