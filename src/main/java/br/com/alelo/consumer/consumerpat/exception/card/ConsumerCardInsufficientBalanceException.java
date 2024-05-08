package br.com.alelo.consumer.consumerpat.exception.card;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import br.com.alelo.consumer.consumerpat.exception.ValidationException;

public class ConsumerCardInsufficientBalanceException
    extends
        ValidationException
{
    private static final String MESSAGE = "Debit cannot be realized because insufficient card balance. Card balance %s and Debit %s";

    public ConsumerCardInsufficientBalanceException(
        final BigDecimal cardBalance,
        final BigDecimal debitValue )
    {
        super( String.format( MESSAGE, cardBalance, debitValue ) );
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
