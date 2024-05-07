package br.com.alelo.consumer.consumerpat.model.card;

import java.util.Arrays;

import br.com.alelo.consumer.consumerpat.exception.card.CardEstablishmentTypeNotFoundException;

public enum CardEstablishmentType
{
    FOOD,
    DRUGSTORE,
    FUEL;

    public static CardEstablishmentType getOrThrownException(
        final String establishmentType )
    {
        final CardEstablishmentType[] values = CardEstablishmentType.values();
        return Arrays.stream( values )
            .filter( enumValue -> enumValue.name().equalsIgnoreCase( establishmentType ) )
            .findAny()
            .orElseThrow( () -> new CardEstablishmentTypeNotFoundException( Arrays.toString( values ) ) );
    }
}
