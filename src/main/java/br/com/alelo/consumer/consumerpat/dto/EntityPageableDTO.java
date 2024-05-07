package br.com.alelo.consumer.consumerpat.dto;

import java.util.List;

public record EntityPageableDTO<T>(
    PageableDTO pageable,
    List<T> elements )
{
}