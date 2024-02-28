package com.beyt.jdq.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by tdilber at 24-Aug-19
 */
@Slf4j
public class DynamicQueryNoAvailableOrOperationUsageException extends RuntimeException {

    public DynamicQueryNoAvailableOrOperationUsageException(String errorMessage) {
        super(errorMessage);
        log.error(errorMessage, this);
    }

    public DynamicQueryNoAvailableOrOperationUsageException(String errorMessage, Throwable err) {
        super(errorMessage, err);
        log.error(errorMessage, err);
    }
}
