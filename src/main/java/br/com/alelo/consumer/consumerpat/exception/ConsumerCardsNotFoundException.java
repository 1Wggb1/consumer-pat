package br.com.alelo.consumer.consumerpat.exception;

import org.springframework.http.HttpStatus;

public class ConsumerCardsNotFoundException
    extends
        ValidationException
{
    private static final String MESSAGE = "Cards not found from consumer with id %d.";

    public ConsumerCardsNotFoundException(
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
