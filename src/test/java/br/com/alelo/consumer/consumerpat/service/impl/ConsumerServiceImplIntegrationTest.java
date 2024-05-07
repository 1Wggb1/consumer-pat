package br.com.alelo.consumer.consumerpat.service.impl;

import static br.com.alelo.consumer.consumerpat.TestData.CONSUMER_DTO;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.alelo.consumer.consumerpat.dto.ConsumerRequestDTO;
import br.com.alelo.consumer.consumerpat.repository.ConsumerRepository;

@ExtendWith( SpringExtension.class )
@SpringBootTest
@ActiveProfiles( "test" )
@AutoConfigureMockMvc
class ConsumerServiceImplIntegrationTest
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ConsumerRepository consumerRepository;

    @Test
    @DisplayName( "Deve retornar 400 e mensagem de erro quando campos do payload estão inválidos." )
    void shouldReturn400ErrorMessageWhenInvalidPayload()
        throws Exception
    {
        final ConsumerRequestDTO consumerDTO = new ConsumerRequestDTO( null,
            null,
            null,
            null,
            null );

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post( "/v1/consumers" )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( consumerDTO ) );
        final ResultActions result = mvc.perform( request );

        result
            .andExpect( MockMvcResultMatchers.status().isBadRequest() )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.errorMessages.length()", IsEqual.equalTo( 4 ) ) );
        assertTrue( consumerRepository.findAll().isEmpty() );
    }

    private static String writeAsJson(
        final Object object )
        throws JsonProcessingException
    {
        return OBJECT_MAPPER.writeValueAsString( object );
    }

    private static <T> T readAsObject(
        final String json,
        final Class<T> clazz )
        throws JsonProcessingException
    {
        return OBJECT_MAPPER.readValue( json, clazz );
    }

    @Test
    @DisplayName( "Deve retornar 404 quando consumidor não encontrado na atualização." )
    void shouldReturn404WhenConsumerNotFoundOUpdate()
        throws Exception
    {
        final int id = 11;
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put( "/v1/consumers/{id}", id )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( CONSUMER_DTO ) );

        final ResultActions result = mvc.perform( request );

        assertTrue( consumerRepository.findById( id ).isEmpty() );
        validateNotFoundResponse( result );
    }

    private static void validateNotFoundResponse(
        final ResultActions result )
        throws Exception
    {
        result.andExpect( MockMvcResultMatchers.status().isNotFound() )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.errorMessages", IsNull.notNullValue() ) );
    }
}