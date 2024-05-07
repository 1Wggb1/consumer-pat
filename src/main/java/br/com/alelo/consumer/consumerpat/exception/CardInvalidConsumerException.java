package br.com.alelo.consumer.consumerpat.exception;

import org.springframework.http.HttpStatus;

public class CardInvalidConsumerException
    extends
        ValidationException
{
    private static final String MESSAGE = "Card consumer not equals request consumer id. Invalid.";

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
