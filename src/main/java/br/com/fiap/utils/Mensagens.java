package br.com.fiap.utils;

import java.util.InputMismatchException;
import java.util.Scanner;


 // Classe que centraliza todas as mensagens do sistema
 public class Mensagens {

    public static final String ICONE_SUCESSO = "✓";
    public static final String ICONE_ERRO = "✗";
    public static final String ICONE_PROGRESSO = "⏳";
    public static final String ICONE_INFO = "ℹ️";

    public static final String VOLTAR_MENU = "\nℹ️  Voltando ao menu principal...\n";
    public static final String PRESSIONE_ENTER = "\nPressione ENTER para continuar...";

    public static final String DESPEDIDA = """
                    ENCERRANDO O SISTEMA...                       
                Obrigado por usar o Sistema !              
      
        """;

    public static final String ERRO_ENTRADA_NUMERICA = """
        ✗ Entrada inválida!
        Por favor, digite apenas números.
        """;

    public static final String ERRO_OPCAO_INVALIDA = """
        ✗ Opção inválida!
        Por favor, escolha uma opção válida.
        """;

    // ========== MENSAGENS DE ERRO - IMPORTAÇÃO ==========
    public static final String ERRO_CAMINHO_VAZIO = """
        ✗ Erro: Caminho não informado.
        Por favor, digite o caminho completo do arquivo.
        """;

    public static final String ERRO_FORMATO_INVALIDO = """
        ✗ Erro: Formato inválido.
        O arquivo deve ter extensão .csv
        """;

    public static final String ERRO_IMPORTACAO = """
        ✗ Erro durante a importação.
        Verifique se o arquivo está no formato correto.
        """;

    public static final String ERRO_ID_INVALIDO = """
        ✗ ID inválido!
        O ID deve ser um número positivo.
        """;

    public static final String ERRO_ID_NUMERO = """
        ✗ Entrada inválida!
        O ID deve ser um número inteiro.
        """;

    public static final String ERRO_ESTATISTICAS_GERAIS = """
        ✗ Erro ao exibir estatísticas gerais.
        """;

    public static final String ERRO_ESTATISTICAS_PRODUTO = """
        ✗ Erro ao exibir estatísticas por produto.
        """;

    public static final String ERRO_ESTATISTICAS_REGIAO = """
        ✗ Erro ao exibir estatísticas por região.
        """;

    public static final String ERRO_CONSULTA_COMERCIO = """
        ✗ Erro ao consultar comércio.
        Verifique se o ID está correto.
        """;

    public static final String ERRO_RELATORIO_GERAL = """
        ✗ Erro ao exportar relatório geral.
        """;

    public static final String ERRO_RELATORIO_COMERCIO = """
        ✗ Erro ao exportar relatório do comércio.
        """;

    // ========== MENSAGENS DE SUCESSO ==========
    public static final String SUCESSO_IMPORTACAO = """
        ✓ Importação concluída com sucesso!
        Os dados já estão disponíveis para consulta.
        """;

    public static String erroFormatoArquivo(String nomeArquivo) {
        return """
        ✗ Erro: Formato inválido.
        O arquivo deve ter extensão .csv
        Arquivo informado: %s
        """.formatted(nomeArquivo);
    }


    public static String erroComDetalhes(String mensagemBase, String detalhes) {
        return mensagemBase + "Detalhes: " + detalhes + "\n";
    }

    public static String erroOpcaoInvalidaFaixa(int min, int max) {
        return """
        ✗ Opção inválida!
        Escolha uma opção entre %d e %d.
        """.formatted(min, max);
    }

    public static int lerNumeroValidado(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int numero = scanner.nextInt();
                scanner.nextLine();

                if (numero < min || numero > max) {
                    System.out.println(ERRO_OPCAO_INVALIDA);
                    System.out.print("Digite novamente: ");
                    continue;
                }

                return numero;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println(ERRO_ENTRADA_NUMERICA);
                System.out.print("Digite novamente: ");
            }
        }
    }

    // Construtor privado para evitar instanciação
    private Mensagens() {}
}