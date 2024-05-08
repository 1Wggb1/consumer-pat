package br.com.alelo.consumer.consumerpat.exception.card;

import org.springframework.http.HttpStatus;
import br.com.alelo.consumer.consumerpat.exception.ValidationException;

public class ConsumerCardNumberAlreadyExists
    extends
        ValidationException
{
    private static final String MESSAGE = "Card number already exists. It should be unique.";

    public ConsumerCardNumberAlreadyExists()
    {
        super( MESSAGE );
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.CONFLICT;
    }
}
