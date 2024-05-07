package br.com.alelo.consumer.consumerpat.util;

public final class UnmaskUtil
{
    public static String unmaskDate(
        final String date )
    {
        return doUnmask( date, "-" );
    }

    static String doUnmask(
        final String value,
        final String regex )
    {
        if( value == null || value.isBlank() ) {
            return null;
        }
        return value.replaceAll( regex, "" );
    }

    public static String unmaskDocumentNumber(
        final String document )
    {
        return doUnmask( document, "[/.-]" );
    }

    public static String unmaskPostalCode(
        final String postalCode )
    {
        return doUnmask( postalCode, "[.-]" );
    }
}
