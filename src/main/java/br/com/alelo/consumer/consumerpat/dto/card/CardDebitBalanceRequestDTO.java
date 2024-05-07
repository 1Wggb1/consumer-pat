package br.com.alelo.consumer.consumerpat.dto.card;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record CardDebitBalanceRequestDTO(
    @NotEmpty( message = "EstablishmentType cannot be null or empty." ) String establishmentType,
    @NotEmpty( message = "EstablishmentName cannot be null or empty." ) String establishmentName,
    @Valid @NotEmpty( message = "DebitProducts cannot be null or empty." ) List<CardDebitProductDTO> debitProducts,
    @NotNull( message = "Debit value cannot be null." ) @Positive( message = "Debit value should be positive." ) Long debitValueCents )
{
}