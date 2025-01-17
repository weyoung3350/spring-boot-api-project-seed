package com.company.project.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.LogbackException;
import com.dianping.cat.Cat;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CatLogbackAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent event) {
        try {
            Level level = event.getLevel();
            if (level.isGreaterOrEqual(Level.ERROR)) {
                logError(event);
            }
        } catch (Exception ex) {
            throw new LogbackException(event.getFormattedMessage(), ex);
        }
    }

    private void logError(ILoggingEvent event) {
        Throwable exception;
        ThrowableProxy info = (ThrowableProxy) event.getThrowableProxy();
        if (info != null) {
            exception = info.getThrowable();
        } else {
            exception = new Exception();
        }
        Object message = event.getFormattedMessage();
        if (message != null) {
            Cat.logError(String.valueOf(message), exception);
        } else {
            Cat.logError(exception);
        }

    }

    private void logErrorNew(ILoggingEvent event) {
        Object message = event.getFormattedMessage();
        String data;
        if (message instanceof Throwable) {
            data = buildExceptionStack((Throwable) message);
        } else {
            data = event.getFormattedMessage().toString();
        }

        ThrowableProxy info = (ThrowableProxy) event.getThrowableProxy();
        if (info != null) {
            data = data + '\n' + buildExceptionStack(info.getThrowable());
        }
        Cat.logErrorWithCategory("Exception", data, new RuntimeException());
        System.out.println("logError finish");
    }

    private String buildExceptionStack(Throwable exception) {
        if (exception != null) {
            StringWriter writer = new StringWriter(2048);
            exception.printStackTrace(new PrintWriter(writer));
            return writer.toString();
        } else {
            return "";
        }
    }

}