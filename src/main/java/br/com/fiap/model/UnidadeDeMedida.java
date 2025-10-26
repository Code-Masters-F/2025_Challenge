package br.com.fiap.model;

public enum UnidadeDeMedida {
    UNIDADE("Unidade"),
    KG("Quilograma"),
    LITRO("Litro"),
    CAIXA("Caixa"),
    PACOTE("Pacote");

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
            case "KG", "QUILO", "QUILOGRAMA" -> KG;
            case "L", "LITRO", "LITROS" -> LITRO;
            case "CAIXA" -> CAIXA;
            case "PACOTE" -> PACOTE;
            default -> throw new IllegalArgumentException("Unidade de medida inválida: " + texto);
        };
    }
}
