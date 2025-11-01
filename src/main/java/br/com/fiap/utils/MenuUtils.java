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

    public static final String MENU_EXPORTAR_RELATORIO = """
       === EXPORTAR RELATÓRIO ===
       1) Relatório Geral
       2) Relatório por Comércio
       0) Voltar
       """;

    public static void limparTela() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Erro ao limpar tela...");
        }

    }
}
