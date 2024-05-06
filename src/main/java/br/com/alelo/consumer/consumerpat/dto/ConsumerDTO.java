package br.com.alelo.consumer.consumerpat.dto;

import java.time.LocalDate;

import br.com.alelo.consumer.consumerpat.model.ConsumerAddress;
import br.com.alelo.consumer.consumerpat.model.ConsumerContact;

public record ConsumerDTO(
    Integer id,
    String name,
    String documentNumber,
    LocalDate birthDate,
    ConsumerContact contact,
    ConsumerAddress address )
{
}