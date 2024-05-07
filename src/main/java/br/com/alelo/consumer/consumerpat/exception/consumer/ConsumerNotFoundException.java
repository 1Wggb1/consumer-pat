package br.com.alelo.consumer.consumerpat.exception.consumer;

import org.springframework.http.HttpStatus;
import br.com.alelo.consumer.consumerpat.exception.ValidationException;

public class ConsumerNotFoundException
    extends
        ValidationException
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
