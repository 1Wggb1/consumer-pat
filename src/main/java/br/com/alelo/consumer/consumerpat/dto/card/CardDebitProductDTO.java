package br.com.alelo.consumer.consumerpat.dto.card;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record CardDebitProductDTO(
    @NotEmpty( message = "ProductName cannot be null or empty." ) String productName,
    @NotNull( message = "Quantity cannot be null." ) @Positive( message = "Quantity should be positive." ) Long quantity,
    @NotNull( message = "Unitary price cannot be null." ) @Positive(
        message = "Unitary price should be positive." ) Long unitaryPriceCents )
{
}