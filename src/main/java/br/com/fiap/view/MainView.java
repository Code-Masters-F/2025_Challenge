package br.com.fiap.view;

import br.com.fiap.service.EstatisticaService;
import br.com.fiap.service.ImportacaoService;
import br.com.fiap.service.RelatorioService;

import java.util.Scanner;

import static utils.MenuUtils.*;

public class MainView {
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        Scanner scanner = new Scanner(System.in);
        RelatorioService relatorioService = new RelatorioService();
        int opcao;

        do {
            limparTela();
            System.out.println(MENU_PRINCIPAL);
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    limparTela();
                    System.out.print("Digite o caminho do arquivo CSV (Ex: vendas.csv):   ");
                    String caminho = scanner.nextLine().trim();
                    ImportacaoService.importarCSV(caminho);
                    break;

                case 2:
                    limparTela();
                    System.out.println(MENU_EXIBIR_ESTATISTICAS);

                    int escolha = scanner.nextInt();
                    scanner.nextLine();

                    switch (escolha) {
                        case 1 -> EstatisticaService.exibirGerais();
                        case 2 -> EstatisticaService.exibirPorProduto();
                        case 3 -> EstatisticaService.exibirPorRegiao();
                        case 0 -> System.out.println("Voltando ao menu principal...");
                        default -> System.out.println("Opção inválida!");
                    }
                    break;

                case 3:
                    limparTela();
                    System.out.print("Digite o id do pequeno varejo: ");
                    int idComercio = scanner.nextInt();
                    scanner.nextLine();

                    EstatisticaService.consultarComercioPorId(idComercio);
                    break;

                case 4:
                    System.out.println("""
                            === EXPORTAR RELATÓRIO ===
                            1) Relatório Geral
                            2) Relatório por Comércio
                            0) Voltar
                            """);

                    int tipo = scanner.nextInt();
                    scanner.nextLine();

                    switch (tipo) {
                        case 1 -> RelatorioService.exportarRelatorioGeral();
                        case 2 -> RelatorioService.exportarRelatorioPorComercio(scanner);
                        case 0 -> System.out.println("Voltando ao menu principal...");
                        default -> System.out.println("Opção inválida!");
                    }
                    break;

                case 0:
                    System.out.println("Saindo do sistema...");
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);

        scanner.close();
    }
}
