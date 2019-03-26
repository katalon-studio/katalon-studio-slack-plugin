package com.katalon.plugin.slack;

public class SlackException extends Exception {

    private static final long serialVersionUID = 1171601532588814667L;

    public SlackException(String message) {
        super(message, null);
    }

    public SlackException(Throwable throwable) {
        super("", throwable);
    }

    public SlackException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
