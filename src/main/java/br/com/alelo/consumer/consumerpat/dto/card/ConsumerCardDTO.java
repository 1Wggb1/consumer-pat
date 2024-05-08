package br.com.alelo.consumer.consumerpat.dto.card;

import java.math.BigDecimal;

public record ConsumerCardDTO(
    Integer id,
    Long number,
    BigDecimal balanceValue,
    String cardEstablishmentType )
{
}