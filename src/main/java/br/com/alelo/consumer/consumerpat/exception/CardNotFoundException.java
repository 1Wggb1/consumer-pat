package br.com.alelo.consumer.consumerpat.exception;

import org.springframework.http.HttpStatus;

public class CardNotFoundException
    extends
        ValidationException
{
    private static final String MESSAGE = "Card with id = %d not found.";

    public CardNotFoundException(
        final Integer cardId )
    {
        super( String.format( MESSAGE, cardId ) );
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.NOT_FOUND;
    }
}
