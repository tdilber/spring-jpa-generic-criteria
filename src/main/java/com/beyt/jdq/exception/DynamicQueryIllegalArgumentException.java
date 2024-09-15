package com.beyt.jdq.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by tdilber at 14-Seo-2024
 */
@Slf4j
public class DynamicQueryIllegalArgumentException extends IllegalArgumentException {

    public DynamicQueryIllegalArgumentException(String errorMessage) {
        super(errorMessage);
        log.error(errorMessage, this);
    }

    public DynamicQueryIllegalArgumentException(String errorMessage, Throwable err) {
        super(errorMessage, err);
        log.error(errorMessage, err);
    }
}
