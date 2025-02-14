package org.example.notificationservice.infrastructure.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Slf4j
public class InvalidPayloadFatalExceptionStrategy implements FatalExceptionStrategy {

    @Override
    public boolean isFatal(Throwable t) {
        if (t instanceof ListenerExecutionFailedException e &&
                (e.getCause() instanceof MessageConversionException ||
                        e.getCause() instanceof MethodArgumentNotValidException)) {
            log.warn("Fatal message conversion error; message rejected; it will be dropped: {}",
                    ((ListenerExecutionFailedException) t).getFailedMessage());
            return true;
        }
        return false;
    }
}
