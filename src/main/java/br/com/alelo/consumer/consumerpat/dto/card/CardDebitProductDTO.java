package br.com.alelo.consumer.consumerpat.dto.card;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public record CardDebitProductDTO(
    @NotEmpty( message = "ProductName cannot be null or empty." ) String productName,
    @Positive( message = "Quantity should be positive." ) Long quantity,
    @Positive( message = "Unitary price should be positive." ) Long unitaryPriceCents )
{
}