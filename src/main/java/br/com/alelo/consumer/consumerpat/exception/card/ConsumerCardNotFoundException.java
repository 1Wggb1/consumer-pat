package br.com.alelo.consumer.consumerpat.exception.card;

import org.springframework.http.HttpStatus;
import br.com.alelo.consumer.consumerpat.exception.ValidationException;

public class ConsumerCardNotFoundException
    extends
        ValidationException
{
    private static final String MESSAGE = "Card with id = %d not found.";

    public ConsumerCardNotFoundException(
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
