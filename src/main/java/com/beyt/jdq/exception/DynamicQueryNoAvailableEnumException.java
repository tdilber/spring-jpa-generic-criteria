package com.beyt.jdq.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by tdilber at 24-Aug-19
 */
@Slf4j
public class DynamicQueryNoAvailableEnumException extends RuntimeException {

    public DynamicQueryNoAvailableEnumException(String errorMessage) {
        super(errorMessage);
        log.error(errorMessage, this);
    }

    public DynamicQueryNoAvailableEnumException(String errorMessage, Throwable err) {
        super(errorMessage, err);
        log.error(errorMessage, err);
    }
}
