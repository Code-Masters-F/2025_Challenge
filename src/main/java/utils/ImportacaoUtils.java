package utils;

import java.math.BigDecimal;

public class ImportacaoUtils {

    public static double parseDoubleSafe(String valor) {
        valor = valor.trim().replace(",", ".");
        if (valor.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(valor);
    }

    public static BigDecimal parseBigDecimalSafe(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        valor = valor.trim().replace(",", ".");
        return new BigDecimal(valor);
    }

}
