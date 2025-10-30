package br.com.fiap.model;

public enum UnidadeDeMedida {
    UNIDADE("Unidade"),
    G("Grama"),
    KG("Quilograma"),
    ML("Mililitro"),
    L("Litro"),
    PACOTE("Pacote"),
    CAIXA("Caixa");

    private final String descricao;

    UnidadeDeMedida(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    // 🔹 Método auxiliar — converte texto do CSV para enum
    public static UnidadeDeMedida fromString(String texto) {
        texto = texto.trim().toUpperCase();
        return switch (texto) {
            case "UNIDADE" -> UNIDADE;
            case "GRAMA", "G" -> G;
            case "KG", "QUILO", "QUILOGRAMA" -> KG;
            case "MILILITRO", "ML" -> ML;
            case "L", "LITRO", "LITROS" -> L;
            case "PACOTE", "PCT" -> PACOTE;
            case "CAIXA", "CX" -> CAIXA;
            default -> throw new IllegalArgumentException("Unidade de medida inválida: " + texto);
        };
    }
}
