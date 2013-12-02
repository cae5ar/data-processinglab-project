package com.pstu.dtl.shared.exception;

@SuppressWarnings("serial")
public class XMLParseException extends Exception {

    public XMLParseException() {
        super();
    }

    public XMLParseException(String message) {
        super(message, null);
    }

    public XMLParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public XMLParseException(Exception e) {
        super(e.getMessage(), e);
    }

}
