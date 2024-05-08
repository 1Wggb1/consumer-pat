package br.com.alelo.consumer.consumerpat.exception.card;

import org.springframework.http.HttpStatus;
import br.com.alelo.consumer.consumerpat.exception.ValidationException;
import br.com.alelo.consumer.consumerpat.model.card.CardEstablishmentType;

public class ConsumerCardNotAcceptedEstablishmentException
    extends
        ValidationException
{
    private static final String MESSAGE = "Card %s not accepted in establishiment type %s.";

    public ConsumerCardNotAcceptedEstablishmentException(
        final CardEstablishmentType cardEstablishmentType,
        final CardEstablishmentType establishmentType )
    {
        super( String.format( MESSAGE, cardEstablishmentType.name(), establishmentType.name() ) );
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
