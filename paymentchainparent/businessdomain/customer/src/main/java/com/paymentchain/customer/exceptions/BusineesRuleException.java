package com.paymentchain.customer.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper=false)
public class BusineesRuleException extends Exception {

    private long id;

    private String code;

    private HttpStatus httpStatus;

    public BusineesRuleException(long id, String code, HttpStatus httpStatus,String message) {
        super(message);
        this.id = id;
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public BusineesRuleException(String code,String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public BusineesRuleException(String message, Throwable cause) {
        super(message, cause);
    }

}
