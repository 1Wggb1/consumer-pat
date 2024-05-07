package br.com.alelo.consumer.consumerpat.dto.card;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record CardDebitBalanceRequestDTO(
    @NotEmpty String establishmentType,
    @NotEmpty String establishmentName,
    @NotEmpty List<CardDebitProductDTO> debitProducts,
    @NotNull @Positive Double debitValue )
{
}