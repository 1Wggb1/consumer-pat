package br.com.alelo.consumer.consumerpat.dto.card;

public record CardDebitProductDTO(
    String productName,
    String quantity,
    Double unitaryPrice )
{
}