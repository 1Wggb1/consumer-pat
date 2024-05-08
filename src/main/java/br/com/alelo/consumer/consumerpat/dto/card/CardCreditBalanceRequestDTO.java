package br.com.alelo.consumer.consumerpat.dto.card;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public record CardCreditBalanceRequestDTO(
    @NotNull( message = "Credit cents cannot be null." ) @PositiveOrZero(
        message = "Credit should be positive or zero." ) BigDecimal creditValue )
{
}
