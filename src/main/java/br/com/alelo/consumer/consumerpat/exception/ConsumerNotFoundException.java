package br.com.alelo.consumer.consumerpat.exception;

import org.springframework.http.HttpStatus;

public class ConsumerNotFoundException
    extends
        ConsumerValidationException
{
    private static final String MESSAGE = "Consumer with id = %s not found";

    public ConsumerNotFoundException(
        final Integer consumerId )
    {
        super( String.format( MESSAGE, consumerId ) );
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.NOT_FOUND;
    }
}
