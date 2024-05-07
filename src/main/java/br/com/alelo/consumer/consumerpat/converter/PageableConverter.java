package br.com.alelo.consumer.consumerpat.converter;

import java.util.List;

import org.springframework.data.domain.Page;
import br.com.alelo.consumer.consumerpat.dto.EntityPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.PageableDTO;

public interface PageableConverter<P, DTO>
{

    DTO toDTO(
        P persitent );

    default List<DTO> toDTO(
        final List<P> persistents )
    {
        return persistents.stream()
            .map( this::toDTO )
            .toList();
    }

    default EntityPageableDTO<DTO> toPageableDTO(
        final Page<P> page )
    {
        final PageableDTO pageableDTO = new PageableDTO(
            page.getNumber(),
            page.getSize(),
            page.getNumberOfElements(),
            page.getTotalPages(),
            (int) page.getTotalElements() );
        return new EntityPageableDTO<>( pageableDTO, toDTO( page.getContent() ) );
    }
}
