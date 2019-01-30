package mpet.project2018.air.core;

/**
 * Klasa za validaciju formata dohvaćena koda
 */
public class CodeValidation {

    /**
     * Provjerava da li je dohvaćeni kod u predviđenom formatu
     * @param code kod koji se provjerava
     * @return status formata koda
     */
    public static boolean validateCodeFormat(String code )
    {
        return code.length()==10 && code.matches(("[A-Za-z0-9]+"));
    }

}
