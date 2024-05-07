package br.com.alelo.consumer.consumerpat.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class UnmaskUtilTest
{
    @Test
    @DisplayName( "Deve remover a máscara do documento." )
    void shouldRemoveDocumentMask()
    {
        assertEquals( "81767428057", UnmaskUtil.unmaskDocumentNumber( "817.674.280-57" ) );
    }

    @Test
    @DisplayName( "Deve remover a máscara da data." )
    void shouldRemoveDateMask()
    {
        assertEquals( "19990417", UnmaskUtil.unmaskDocumentNumber( "1999-04-17" ) );
    }

    @Test
    @DisplayName( "Deve remover a máscara do cep." )
    void shouldRemovePostalCodeMask()
    {
        assertEquals( "06766140", UnmaskUtil.unmaskDocumentNumber( "06766-140" ) );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName( "Deve retorna nulo quando nulo ou vazio." )
    void shouldReturnNullWhenNullOrEmpty(
        final String value )
    {
        assertNull( UnmaskUtil.doUnmask( value, "" ) );
    }
}
