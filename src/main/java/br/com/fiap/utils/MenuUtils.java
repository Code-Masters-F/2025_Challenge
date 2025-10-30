package br.com.fiap.utils;

public class MenuUtils {
    public static final String MENU_PRINCIPAL = """
        ==============================
        📊  Sistema de Vendas FIAP
        ==============================
        1️⃣  Importar arquivo CSV
        2️⃣  Exibir estatísticas
        3️⃣  Consultar comércio
        4️⃣  Exportar relatório
        0️⃣  Sair
        ==============================
        """;

    public static final String MENU_EXIBIR_ESTATISTICAS = """
        === Estatísticas ===
        1) Gerais
        2) Por produto
        3) Por região
        0) Voltar
        """;

    public static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
