package br.com.alelo.consumer.consumerpat.exception;

import org.springframework.http.HttpStatus;

public class CardEstablishmentTypeNotFoundException
    extends
        ValidationException
{
    private static final String MESSAGE = "Card establishment type not found. Valid values %s.";

    public CardEstablishmentTypeNotFoundException(
        final String validValues )
    {
        super( String.format( MESSAGE, validValues ) );
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.NOT_FOUND;
    }
}
