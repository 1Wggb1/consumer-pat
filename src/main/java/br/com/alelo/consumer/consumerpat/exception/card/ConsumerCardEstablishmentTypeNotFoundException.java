package br.com.alelo.consumer.consumerpat.exception.card;

import org.springframework.http.HttpStatus;
import br.com.alelo.consumer.consumerpat.exception.ValidationException;

public class ConsumerCardEstablishmentTypeNotFoundException
    extends
        ValidationException
{
    private static final String MESSAGE = "Card establishment type not found. Valid values %s.";

    public ConsumerCardEstablishmentTypeNotFoundException(
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
