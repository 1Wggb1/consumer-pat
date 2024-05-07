package br.com.alelo.consumer.consumerpat.dto.card;

public record ConsumerCardDTO(
    Integer id,
    Long number,
    Long balanceValueCents,
    String cardEstablishmentType )
{
}