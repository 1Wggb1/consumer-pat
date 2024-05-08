package br.com.alelo.consumer.consumerpat.model.card;

import java.util.Arrays;

import br.com.alelo.consumer.consumerpat.exception.card.ConsumerCardEstablishmentTypeNotFoundException;
import br.com.alelo.consumer.consumerpat.model.card.calculator.DebitBalanceCalculator;
import br.com.alelo.consumer.consumerpat.model.card.calculator.DebitBalanceDrugStoreCardCalculator;
import br.com.alelo.consumer.consumerpat.model.card.calculator.DebitBalanceFoodCardCalculator;
import br.com.alelo.consumer.consumerpat.model.card.calculator.DebitBalanceFuelCardCalculator;
import lombok.Getter;

@Getter
public enum CardEstablishmentType
{
    DRUGSTORE( new DebitBalanceDrugStoreCardCalculator() ),
    FOOD( new DebitBalanceFoodCardCalculator() ),
    FUEL( new DebitBalanceFuelCardCalculator() );

    private final DebitBalanceCalculator debitBalanceCalculator;

    CardEstablishmentType(
        final DebitBalanceCalculator debitBalanceCalculator )
    {
        this.debitBalanceCalculator = debitBalanceCalculator;
    }

    public static CardEstablishmentType getOrThrownException(
        final String establishmentType )
    {
        final CardEstablishmentType[] values = CardEstablishmentType.values();
        return Arrays.stream( values )
            .filter( enumValue -> enumValue.name().equalsIgnoreCase( establishmentType ) )
            .findAny()
            .orElseThrow( () -> new ConsumerCardEstablishmentTypeNotFoundException( Arrays.toString( values ) ) );
    }
}
