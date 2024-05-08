package br.com.alelo.consumer.consumerpat.dto.card;

import java.math.BigDecimal;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public record ConsumerCardRequestDTO(
    @NotNull( message = "Number cannot be null." ) Long number,
    @PositiveOrZero( message = "Balance value should be positive or zero." ) BigDecimal balanceValue,
    @NotEmpty( message = "CardEstablishmentType cannot be null or empty." ) String cardEstablishmentType )
{
}