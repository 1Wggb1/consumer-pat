package br.com.alelo.consumer.consumerpat.exception;

import org.springframework.http.HttpStatus;

public abstract class ConsumerValidationException
    extends
        RuntimeException
{
    public ConsumerValidationException(
        final String message )
    {
        super( message );
    }

    public HttpStatus getStatus()
    {
        return HttpStatus.BAD_REQUEST;
    }
}
