package br.com.fiap.view;

import br.com.fiap.service.EstatisticaService;
import br.com.fiap.service.ImportacaoService;
import br.com.fiap.service.RelatorioService;
import br.com.fiap.utils.Mensagens;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

import static br.com.fiap.utils.MenuUtils.*;

public class MainView {
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            limparTela();
            System.out.println(MENU_PRINCIPAL);

            try {
                System.out.print("Escolha uma opção: ");  // garante que sempre aparece
                opcao = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println(Mensagens.ERRO_ENTRADA_NUMERICA);
                System.out.print("Pressione ENTER e tente novamente...");
                scanner.nextLine();
                opcao = -1;
                continue;
            }

            if (opcao < 0 || opcao > 4) {
                System.out.println(Mensagens.ERRO_OPCAO_INVALIDA);
                System.out.print("Pressione ENTER e tente novamente...");
                scanner.nextLine();
                continue;
            }

            switch (opcao) {
                case 1:
                    limparTela();

                    while (true) {
                        System.out.print("Digite o caminho do arquivo CSV (Ex: vendas.csv) ou 0 para voltar: ");
                        String caminho = scanner.nextLine().trim();

                        // opção de voltar
                        if (caminho.equals("0")) break;

                        if (caminho.isEmpty()) {
                            System.out.println(Mensagens.ERRO_CAMINHO_VAZIO);
                            continue;
                        }

                        if (!caminho.toLowerCase().endsWith(".csv")) {
                            System.out.println(Mensagens.erroFormatoArquivo(caminho));
                            continue;
                        }

                        if (!Files.exists(Paths.get(caminho))) {
                            System.out.println("✗ Arquivo não encontrado em: " + Paths.get(caminho).toAbsolutePath());
                            continue;
                        }

                        System.out.println(System.lineSeparator() + "Importando...");
                        try {
                            ImportacaoService.importarCSV(caminho);
                            System.out.println(Mensagens.SUCESSO_IMPORTACAO + "\n");
                        } catch (Exception e) {
                            System.out.println(Mensagens.erroComDetalhes(Mensagens.ERRO_IMPORTACAO, e.getMessage()));
                        }

                        System.out.println("Deseja importar outro arquivo?");
                        System.out.print("Digite S para sim ou qualquer tecla para voltar ao menu: ");
                        String resp = scanner.nextLine().trim().toUpperCase();

                        if (!resp.equals("S")) break;

                        System.out.println(); // espaçamento
                    }
                    break;

                case 2:
                    limparTela();
                    System.out.println(MENU_EXIBIR_ESTATISTICAS);

                    int escolha = Mensagens.lerNumeroValidado(scanner, 0, 3);

                    switch (escolha) {
                        case 1 -> EstatisticaService.exibirGerais();
                        case 2 -> EstatisticaService.exibirPorProduto();
                        case 3 -> EstatisticaService.exibirPorRegiao();
                        case 0 -> System.out.println(Mensagens.VOLTAR_MENU);
                    }
                    Mensagens.pausaOperacao(scanner);
                    break;

                case 3:
                    limparTela();
                    System.out.print("Digite o id do pequeno varejo: ");

                    int idComercio = Mensagens.lerNumeroValidado(scanner, 1, Integer.MAX_VALUE);

                    try {
                        EstatisticaService.consultarComercioPorId(idComercio);
                    } catch (Exception e) {
                        System.out.println(Mensagens.ERRO_CONSULTA_COMERCIO);
                    }
                    Mensagens.pausaOperacao(scanner);
                    break;

                case 4:
                    System.out.println("""
                            === EXPORTAR RELATÓRIO ===
                            1) Relatório Geral
                            2) Relatório por Comércio
                            0) Voltar
                            """);

                    int tipo = Mensagens.lerNumeroValidado(scanner, 0, 2);

                    switch (tipo) {
                        case 1 -> RelatorioService.exportarRelatorioGeral();
                        case 2 -> RelatorioService.exportarRelatorioPorComercio(scanner);
                        case 0 -> System.out.println(Mensagens.VOLTAR_MENU);
                    }
                    Mensagens.pausaOperacao(scanner);
                    break;

                case 0:
                    System.out.println(Mensagens.DESPEDIDA);
                    break;

                default:
                    System.out.println(Mensagens.ERRO_OPCAO_INVALIDA);
                    System.out.print("Pressione ENTER e tente novamente...");
                    scanner.nextLine();

            }
        } while (opcao != 0);

        scanner.close();
    }
}
