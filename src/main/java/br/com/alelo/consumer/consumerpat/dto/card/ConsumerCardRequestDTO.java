package br.com.alelo.consumer.consumerpat.dto.card;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record ConsumerCardRequestDTO(
    @NotNull( message = "Number cannot be null." ) Long number,
    Long balanceValueCents,
    @NotEmpty( message = "CardEstablishmentType cannot be null or empty." ) String cardEstablishmentType )
{
}