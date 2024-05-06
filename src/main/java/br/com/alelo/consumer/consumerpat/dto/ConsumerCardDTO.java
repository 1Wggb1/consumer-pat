package br.com.alelo.consumer.consumerpat.dto;

import javax.validation.constraints.NotNull;

public record ConsumerCardDTO(
    @NotNull Long number,
    Double balance,
    @NotNull String establishmentType )
{
}