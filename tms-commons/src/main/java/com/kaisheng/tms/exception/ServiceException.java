package com.kaisheng.tms.exception;

/**
 *  业务的异常
 * @author mh
 * @date 2018/4/12
 */
public class ServiceException extends RuntimeException {

    public ServiceException() {
    }

    public ServiceException(Throwable throwable) {
        super(throwable);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
