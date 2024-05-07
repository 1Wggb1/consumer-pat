package br.com.alelo.consumer.consumerpat.exception.card;

import org.springframework.http.HttpStatus;
import br.com.alelo.consumer.consumerpat.exception.ValidationException;

public class ConsumerCardInvalidCreditValueException
    extends
        ValidationException
{
    private static final String MESSAGE = "Consumer credit cannot be null or negative. Card id = %d, Consumer id = %d";

    public ConsumerCardInvalidCreditValueException(
        final Integer cardId,
        final Integer consumerId )
    {
        super( String.format( MESSAGE, cardId, consumerId ) );
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
