package br.com.alelo.consumer.consumerpat.dto;

import java.util.List;

public record ConsumerPageableDTO(
    PageableDTO pageable,
    List<ConsumerDTO> elements )
{
}