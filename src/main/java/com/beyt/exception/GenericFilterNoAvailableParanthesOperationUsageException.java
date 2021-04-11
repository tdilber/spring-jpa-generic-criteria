package com.beyt.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by tdilber at 24-Aug-19
 */
@Slf4j
public class GenericFilterNoAvailableParanthesOperationUsageException extends RuntimeException {

    public GenericFilterNoAvailableParanthesOperationUsageException(String errorMessage) {
        super(errorMessage);
        log.error(errorMessage, this);
    }

    public GenericFilterNoAvailableParanthesOperationUsageException(String errorMessage, Throwable err) {
        super(errorMessage, err);
        log.error(errorMessage, err);
    }
}
