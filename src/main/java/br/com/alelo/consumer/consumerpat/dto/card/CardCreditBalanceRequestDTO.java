package br.com.alelo.consumer.consumerpat.dto.card;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public record CardCreditBalanceRequestDTO(
    @NotNull( message = "Credit cents cannot be null." ) @PositiveOrZero(
        message = "Credit cents should be positive or zero." ) Long creditCents )
{
}
