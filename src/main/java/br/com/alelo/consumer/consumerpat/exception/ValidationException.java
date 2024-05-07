package br.com.alelo.consumer.consumerpat.exception;

import org.springframework.http.HttpStatus;

public abstract class ValidationException
    extends
        RuntimeException
{
    public ValidationException(
        final String message )
    {
        super( message );
    }

    public HttpStatus getStatus()
    {
        return HttpStatus.BAD_REQUEST;
    }
}
