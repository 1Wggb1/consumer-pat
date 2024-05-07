package br.com.alelo.consumer.consumerpat.dto.consumer;

import java.time.LocalDate;

import br.com.alelo.consumer.consumerpat.model.consumer.ConsumerAddress;
import br.com.alelo.consumer.consumerpat.model.consumer.ConsumerContact;

public record ConsumerDTO(
    Integer id,
    String name,
    String documentNumber,
    LocalDate birthDate,
    ConsumerContact contact,
    ConsumerAddress address )
{
}