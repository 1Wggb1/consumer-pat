package br.com.alelo.consumer.consumerpat.dto.card;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record ConsumerCardUpdateRequestDTO(
    @NotNull( message = "Number cannot be null." ) Long number,
    @NotEmpty( message = "CardEstablishmentType cannot be null or empty." ) String cardEstablishmentType )
{
}