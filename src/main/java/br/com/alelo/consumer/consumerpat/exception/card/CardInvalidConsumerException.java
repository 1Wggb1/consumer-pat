package br.com.alelo.consumer.consumerpat.exception.card;

import org.springframework.http.HttpStatus;
import br.com.alelo.consumer.consumerpat.exception.ValidationException;

public class CardInvalidConsumerException
    extends
        ValidationException
{
    private static final String MESSAGE = "Card consumer id not equals request consumer id.";

    public CardInvalidConsumerException()
    {
        super( MESSAGE );
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
