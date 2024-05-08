package br.com.alelo.consumer.consumerpat.exception.card;

import org.springframework.http.HttpStatus;
import br.com.alelo.consumer.consumerpat.exception.ValidationException;

public class ConsumerCardInvalidConsumerException
    extends
        ValidationException
{
    private static final String MESSAGE = "Card consumer id not equals request consumer id.";

    public ConsumerCardInvalidConsumerException()
    {
        super( MESSAGE );
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
