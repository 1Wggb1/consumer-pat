package br.com.alelo.consumer.consumerpat.exception.card;

import org.springframework.http.HttpStatus;
import br.com.alelo.consumer.consumerpat.exception.ValidationException;

public class CardNumberAlreadyExists
    extends
        ValidationException
{
    private static final String MESSAGE = "Card number already exists. It should be unique.";

    public CardNumberAlreadyExists()
    {
        super( MESSAGE );
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.CONFLICT;
    }
}
