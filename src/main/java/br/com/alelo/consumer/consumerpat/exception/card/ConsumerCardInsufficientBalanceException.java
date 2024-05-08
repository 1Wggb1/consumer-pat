package br.com.alelo.consumer.consumerpat.exception.card;

import org.springframework.http.HttpStatus;
import br.com.alelo.consumer.consumerpat.exception.ValidationException;

public class ConsumerCardInsufficientBalanceException
    extends
        ValidationException
{
    private static final String MESSAGE = "Debit cannot be realized because insufficient card balance. Card balance %d and Debit %d";

    public ConsumerCardInsufficientBalanceException(
        final Long cardBalance,
        final Long debitValue )
    {
        super( String.format( MESSAGE, cardBalance, debitValue ) );
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
