package br.com.alelo.consumer.consumerpat.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;
import br.com.alelo.consumer.consumerpat.model.ConsumerAddress;
import br.com.alelo.consumer.consumerpat.model.ConsumerContact;

public record ConsumerRequestDTO(
    @NotEmpty String name,
    @NotNull @CPF String documentNumber,
    LocalDate birthday,
    @NotNull ConsumerContact contact,
    @NotNull ConsumerAddress address )
{
}