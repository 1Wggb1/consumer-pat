package br.com.alelo.consumer.consumerpat.service.impl;

import static br.com.alelo.consumer.consumerpat.TestData.SECOND_VALID_DOCUMENT_NUMBER_WITHOUT_MASK;
import static br.com.alelo.consumer.consumerpat.TestData.VALID_DOCUMENT_NUMBER_WITHOUT_MASK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Optional;
import java.util.stream.Stream;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import br.com.alelo.consumer.consumerpat.dto.PageableDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardCreditBalanceRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardUpdateRequestDTO;
import br.com.alelo.consumer.consumerpat.model.card.CardEstablishmentType;
import br.com.alelo.consumer.consumerpat.model.card.PersistentConsumerCard;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;
import br.com.alelo.consumer.consumerpat.repository.CardRepository;

class ConsumerCardServiceImplIntegrationTest
    extends
        AbstractIntegrationTestConfiguration
{
    private static final String BASE_PATH = "/v1/consumers/{consumer_id}/cards";

    @Autowired
    private CardRepository cardRepository;

    @AfterEach
    void tearDown()
    {
        cardRepository.deleteAll();
        consumerRepository.deleteAll();
    }

    @Test
    @DisplayName( "Deve retornar 201 quando cartão criado." )
    void shouldReturn201WhenCreatedCardFromConsumerSuccessfully()
        throws Exception
    {
        final PersistentConsumer persistentConsumer = createConsumer( "T", VALID_DOCUMENT_NUMBER_WITHOUT_MASK );
        final ConsumerCardRequestDTO cardRequestDTO = new ConsumerCardRequestDTO( 78899555L, null, CardEstablishmentType.DRUGSTORE.name() );
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post( BASE_PATH, persistentConsumer.getId() )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( cardRequestDTO ) );

        final ResultActions result = mvc.perform( request );

        result
            .andExpect( MockMvcResultMatchers.status().isCreated() )
            .andExpect( jsonPath( "$.id", IsNull.notNullValue() ) );
        final String response = result.andReturn().getResponse().getContentAsString();
        final ConsumerCardResponseDTO responseDTO = readAsObject( response, ConsumerCardResponseDTO.class );
        final PersistentConsumerCard createdConsumerCard = cardRepository.findById( responseDTO.id() )
            .orElseThrow();
        assertNotNull( createdConsumerCard.getId() );
        assertEquals( cardRequestDTO.number(), createdConsumerCard.getNumber() );
        assertEquals( Optional.ofNullable( cardRequestDTO.balanceValueCents() ).orElse( 0L ), createdConsumerCard.getBalanceCents() );
        assertEquals( cardRequestDTO.cardEstablishmentType(), createdConsumerCard.getEstablishmentType().name() );
    }

    @Test
    @DisplayName( "Deve retornar 404 quando não for possível achar consumidor na criação de cartão." )
    void shouldReturn404WhenConsumerNotFoundOnCardCreation()
        throws Exception
    {
        final ConsumerCardRequestDTO cardRequestDTO = new ConsumerCardRequestDTO( 78899555L, null, CardEstablishmentType.DRUGSTORE.name() );
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post( BASE_PATH, 788888 )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( cardRequestDTO ) );

        final ResultActions result = mvc.perform( request );

        final ResultMatcher notFound = MockMvcResultMatchers.status().isNotFound();
        validateErrorResponse( result, notFound );
    }

    @Test
    @DisplayName( "Deve retornar 400 quando payload inválido na criação de um cartão." )
    void shouldReturn400WhenConsumerCardPayloadInvalid()
        throws Exception
    {
        final ConsumerCardRequestDTO cardRequestDTO = new ConsumerCardRequestDTO(
            null, - 1L, null );
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post( BASE_PATH, 788888 )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( cardRequestDTO ) );

        final ResultActions result = mvc.perform( request );

        result.andExpect( jsonPath( "$.errorMessages.length()", Is.is( 3 ) ) );
        final ResultMatcher badRequest = MockMvcResultMatchers.status().isBadRequest();
        validateErrorResponse( result, badRequest );
    }

    @Test
    @DisplayName( "Deve retornar 404 quando Tipo do estabelecimento do cartão é inválido." )
    void shouldReturn404WhenCardEstablishmentTypeInvalid()
        throws Exception
    {
        final PersistentConsumer persistentConsumer = createConsumer( "T", VALID_DOCUMENT_NUMBER_WITHOUT_MASK );
        final ConsumerCardRequestDTO cardRequestDTO = new ConsumerCardRequestDTO(
            788545668L, 100L, "ANYY" );
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post( BASE_PATH, persistentConsumer.getId() )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( cardRequestDTO ) );

        final ResultActions result = mvc.perform( request );

        result.andExpect( jsonPath( "$.errorMessages.length()", Is.is( 1 ) ) );
        final ResultMatcher notFound = MockMvcResultMatchers.status().isNotFound();
        validateErrorResponse( result, notFound );
    }

    @Test
    @DisplayName( "Deve retornar 204 quando cartão do consumidor atualizado com sucesso." )
    void shouldReturn204WhenConsumerCardUpdatedSuccessfully()
        throws Exception
    {
        final PersistentConsumer persistentConsumer = createConsumer( "Will", VALID_DOCUMENT_NUMBER_WITHOUT_MASK );
        final PersistentConsumerCard consumerCard = createConsumerCard( 44445456L,
            CardEstablishmentType.DRUGSTORE,
            persistentConsumer );
        final ConsumerCardUpdateRequestDTO updateCardRequestDTO = new ConsumerCardUpdateRequestDTO(
            7854455L, CardEstablishmentType.FUEL.name() );

        final Integer consumerId = persistentConsumer.getId();
        final Integer cardId = consumerCard.getId();
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(
            BASE_PATH + "/{card_Id}", consumerId, cardId )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( updateCardRequestDTO ) );

        final ResultActions result = mvc.perform( request );
        result
            .andExpect( MockMvcResultMatchers.status().isNoContent() );
        final PersistentConsumerCard updatedConsumerCard = cardRepository.findById( cardId )
            .orElseThrow();
        assertNotNull( updatedConsumerCard.getId() );
        assertEquals( updateCardRequestDTO.cardEstablishmentType(), updatedConsumerCard.getEstablishmentType().name() );
        assertEquals( updateCardRequestDTO.number(), updatedConsumerCard.getNumber() );
        assertEquals( consumerCard.getBalanceCents(), updatedConsumerCard.getBalanceCents() );
        assertEquals( consumerCard.getConsumer(), updatedConsumerCard.getConsumer() );
    }

    private PersistentConsumerCard createConsumerCard(
        final Long number,
        final CardEstablishmentType cardEstablishmentType,
        final PersistentConsumer consumer )
    {
        final PersistentConsumerCard consumerCard = PersistentConsumerCard.builder()
            .number( number )
            .consumer( consumer )
            .establishmentType( cardEstablishmentType )
            .balanceCents( 10L )
            .build();
        return cardRepository.save( consumerCard );
    }

    @Test
    @DisplayName( "Deve retornar 404 quando cartão do consumidor não encontrado na atualização." )
    void shouldReturn404WhenConsumerCardNotFoundOnUpdate()
        throws Exception
    {
        final PersistentConsumer persistentConsumer = createConsumer( "Will", VALID_DOCUMENT_NUMBER_WITHOUT_MASK );
        final ConsumerCardUpdateRequestDTO updateCardRequestDTO = new ConsumerCardUpdateRequestDTO(
            7854455L, CardEstablishmentType.FUEL.name() );

        final Integer consumerId = persistentConsumer.getId();
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(
            BASE_PATH + "/{card_Id}", consumerId, 7888 )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( updateCardRequestDTO ) );

        final ResultActions result = mvc.perform( request );

        final ResultMatcher notFound = MockMvcResultMatchers.status().isNotFound();
        validateErrorResponse( result, notFound );
    }

    @Test
    @DisplayName( "Deve retornar 422 quando consumidor é diferente do que possui o cartão (Não é permitido mudar o cartão de dono)." )
    void shouldReturn422WhenConsumerIsntSameThanRequestOnCardUpdate()
        throws Exception
    {
        final PersistentConsumer secondPersistentConsumer = createConsumer( "Mari Palma", SECOND_VALID_DOCUMENT_NUMBER_WITHOUT_MASK );
        final ConsumerCardUpdateRequestDTO updateCardRequestDTO = new ConsumerCardUpdateRequestDTO(
            7854455L, CardEstablishmentType.FUEL.name() );
        final PersistentConsumerCard consumerCard = createConsumerCard( 44445456L,
            CardEstablishmentType.DRUGSTORE,
            createConsumer( "Will", VALID_DOCUMENT_NUMBER_WITHOUT_MASK ) );

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(
            BASE_PATH + "/{card_Id}", secondPersistentConsumer.getId(), consumerCard.getId() )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( updateCardRequestDTO ) );

        final ResultActions result = mvc.perform( request );

        final ResultMatcher unprocessableEntity = MockMvcResultMatchers.status().isUnprocessableEntity();
        validateErrorResponse( result, unprocessableEntity );
    }

    @Test
    @DisplayName( "Deve retornar 404 quando não for possível achar cartões do consumidor." )
    void shouldReturn404WhenNotFoundConsumerCards()
        throws Exception
    {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get( BASE_PATH, 558 );

        final ResultActions result = mvc.perform( request );

        final ResultMatcher notFound = MockMvcResultMatchers.status().isNotFound();
        validateErrorResponse( result, notFound );
    }

    @Test
    @DisplayName( "Deve retornar 200 e cartões do consumidor paginados." )
    void shouldReturn200WhenCardsFound()
        throws Exception
    {
        final PersistentConsumer persistentConsumer = createConsumer( "Will", VALID_DOCUMENT_NUMBER_WITHOUT_MASK );
        createConsumerCard( 44445456L,
            CardEstablishmentType.DRUGSTORE,
            persistentConsumer );
        createConsumerCard( 123586L,
            CardEstablishmentType.FOOD,
            persistentConsumer );
        createConsumerCard( 2224711L,
            CardEstablishmentType.FUEL,
            persistentConsumer );
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get( BASE_PATH, persistentConsumer.getId() );

        final ResultActions result = mvc.perform( request );

        final PageableDTO expectedPageable = new PageableDTO( 0, 10,
            3, 1, 3 );
        validatePageableResult( result, expectedPageable );
    }

    @Test
    @DisplayName( "Deve retornar 404 quando cartão do consumidor não encontrado na adição de credito." )
    void shouldReturn404WhenConsumerCardNotFoundOnCreditBalance()
        throws Exception
    {
        final CardCreditBalanceRequestDTO cardCreditBalanceRequestDTO = new CardCreditBalanceRequestDTO(
            10000L );
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(
            BASE_PATH + "/{card_Id}/credits-balances", 1, 1 )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( cardCreditBalanceRequestDTO ) );

        final ResultActions result = mvc.perform( request );
        final ResultMatcher notFound = MockMvcResultMatchers.status().isNotFound();
        validateErrorResponse( result, notFound );
    }

    @ParameterizedTest
    @MethodSource( "invalidCreditValues" )
    @DisplayName( "Deve retornar 400 quando valor para crédito é inválido (nulo ou negativo)." )
    void shouldReturn400WhenConsumerCardCreditNullOrNegative(
        final Long value )
        throws Exception
    {
        final CardCreditBalanceRequestDTO cardCreditBalanceRequestDTO = new CardCreditBalanceRequestDTO(
            value );
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(
            BASE_PATH + "/{card_Id}/credits-balances", 1, 1 )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( cardCreditBalanceRequestDTO ) );

        final ResultActions result = mvc.perform( request );
        final ResultMatcher badRequest = MockMvcResultMatchers.status().isBadRequest();
        validateErrorResponse( result, badRequest );
    }

    private static Stream<Long> invalidCreditValues()
    {
        return Stream.of( null, - 1L );
    }

    @Test
    @DisplayName( "Deve retornar 204 quando de crédito adicionado com sucesso." )
    void shouldReturn204WhenConsumerCardCreditAddSuccessfully()
        throws Exception
    {
        final PersistentConsumer persistentConsumer = createConsumer( "Will", VALID_DOCUMENT_NUMBER_WITHOUT_MASK );
        final PersistentConsumerCard consumerCard = createConsumerCard( 44445456L,
            CardEstablishmentType.DRUGSTORE,
            persistentConsumer );
        final Long hundred_99 = 100_99L;
        final CardCreditBalanceRequestDTO cardCreditBalanceRequestDTO = new CardCreditBalanceRequestDTO( hundred_99 );
        final Integer consumerId = persistentConsumer.getId();
        final Integer cardId = consumerCard.getId();
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(
            BASE_PATH + "/{card_Id}/credits-balances", consumerId, cardId )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( cardCreditBalanceRequestDTO ) );

        final ResultActions result = mvc.perform( request );

        result.andExpect( MockMvcResultMatchers.status().isNoContent() );
        final PersistentConsumerCard rechargedCard = cardRepository.findByIdAndConsumerId( cardId, consumerId )
            .orElseThrow();
        final Long expected = consumerCard.getBalanceCents() + cardCreditBalanceRequestDTO.creditCents();
        assertEquals( expected, rechargedCard.getBalanceCents() );
    }
}