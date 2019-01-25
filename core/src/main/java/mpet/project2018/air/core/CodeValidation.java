package mpet.project2018.air.core;

public class CodeValidation {

    // Metoda koja provjerava da li je uneseni ili skenirani kod u odgovarajućem formatu
    public static boolean validateCodeFormat(String code )
    {
        return code.length()==10 && code.matches(("[A-Za-z0-9]+"));
    }

}
