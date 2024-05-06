package br.com.alelo.consumer.consumerpat.dto;

public record PageableDTO(
    int page,
    int size,
    int numberOfElements,
    int totalPages,
    int totalElements )
{
}
