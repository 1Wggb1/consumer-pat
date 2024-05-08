package br.com.alelo.consumer.consumerpat.model.card;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.util.Pair;
import br.com.alelo.consumer.consumerpat.exception.card.ConsumerCardEstablishmentTypeNotFoundException;
import br.com.alelo.consumer.consumerpat.model.card.calculator.DebitBalanceCalculator;
import br.com.alelo.consumer.consumerpat.model.card.calculator.DebitBalanceDrugStoreCardCalculator;
import br.com.alelo.consumer.consumerpat.model.card.calculator.DebitBalanceFoodCardCalculator;
import br.com.alelo.consumer.consumerpat.model.card.calculator.DebitBalanceFuelCardCalculator;

class CardEstablishmentTypeTest
{
    @ParameterizedTest
    @ValueSource( strings = {
        "sominvalid", "foodd"
    } )
    @DisplayName( "Deve lançar exceção quando tipo do estabelecimento é inválido." )
    void shouldThrowExceptionWhenTypeNotFound(
        final String value )
    {
        assertThrows( ConsumerCardEstablishmentTypeNotFoundException.class,
            () -> CardEstablishmentType.getOrThrownException( value ) );
    }

    @ParameterizedTest
    @ValueSource( strings = {
        "food", "FooD", "FUEl", "drugstore"
    } )
    @DisplayName( "Não deve lançar exceção quando tipo do estabelecimento é válido." )
    void shouldNotThrownExceptionWhenTypeValid(
        final String value )
    {
        assertDoesNotThrow( () -> CardEstablishmentType.getOrThrownException( value ) );
    }

    @ParameterizedTest
    @MethodSource( "debitCalculatorByAlgorithm" )
    @DisplayName( "Deve retornar o algoritmo para cálculo de débito a partir do tipo de estabelecimento." )
    void shouldReturnCalculatorAlgorithmFromType(
        final Pair<CardEstablishmentType,DebitBalanceCalculator> calculatorByType )
    {
        final CardEstablishmentType cardEstablishmentType = calculatorByType.getFirst();
        final Class<? extends DebitBalanceCalculator> expectedClazz = calculatorByType.getSecond().getClass();
        assertInstanceOf( expectedClazz, cardEstablishmentType.getDebitBalanceCalculator() );
    }

    private static Stream<Pair<CardEstablishmentType,DebitBalanceCalculator>> debitCalculatorByAlgorithm()
    {
        final Pair<CardEstablishmentType,DebitBalanceCalculator> drugstore = Pair.of( CardEstablishmentType.DRUGSTORE,
            new DebitBalanceDrugStoreCardCalculator() );
        final Pair<CardEstablishmentType,DebitBalanceCalculator> food = Pair.of( CardEstablishmentType.FOOD,
            new DebitBalanceFoodCardCalculator() );
        final Pair<CardEstablishmentType,DebitBalanceCalculator> fuel = Pair.of( CardEstablishmentType.FUEL,
            new DebitBalanceFuelCardCalculator() );
        return Stream.of( drugstore, food, fuel );
    }
}
