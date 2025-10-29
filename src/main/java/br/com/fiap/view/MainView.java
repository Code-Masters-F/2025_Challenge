package br.com.fiap.view;

import br.com.fiap.service.EstatisticaService;
import br.com.fiap.service.ImportacaoService;
import br.com.fiap.service.RelatorioService;

import java.util.Scanner;

public class MainView {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RelatorioService relatorioService = new RelatorioService();
        int opcao;

        do {
            System.out.println("""
                === Sistema de Coleta e Análise de Vendas ===
                1) Importar arquivo CSV
                2) Exibir estatísticas
                3) Consultar por ID de comércio
                4) Exportar relatório (.txt/.csv)
                0) Sair
                """);

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Digite o caminho do arquivo CSV (Ex: vendas.csv): ");
                    String caminho = scanner.nextLine().trim();
                    ImportacaoService.importarCSV(caminho);
                    break;

                case 2:
                    System.out.println("""
                            === Estatísticas ===
                            1) Estatísticas gerais
                            2) Estatísticas por produto
                            3) Estatísticas por região
                            0) Voltar
                            """);

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
                    System.out.println("Consultar comércio (em desenvolvimento)");
                    break;

                case 4:
                    // EXPORTAR RELATÓRIO
                    System.out.println("""
                        === EXPORTAR RELATÓRIO ===");
                        1) Relatório Geral
                        2) Relatório por Comércio
                        Escolha: """);
                    int tipo = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("\n1) TXT  2) CSV");
                    System.out.print("Formato: ");
                    int fmt = scanner.nextInt();
                    scanner.nextLine();
                    String formato = (fmt == 1) ? "txt" : "csv";

                    try {
                        String arquivo;
                        if (tipo == 1) {
                            arquivo = relatorioService.exportarRelatorioGeral(formato);
                            System.out.println("\n✓ Relatório geral exportado!");
                        } else {
                            System.out.print("ID do comércio: ");
                            int id = scanner.nextInt();
                            scanner.nextLine();
                            arquivo = relatorioService.exportarRelatorioPorComercio(id, formato);
                            System.out.println("\n✓ Relatório do comércio exportado!");
                        }
                        System.out.println("Arquivo: " + arquivo + "\n");
                    } catch (Exception e) {
                        System.out.println("✗ Erro: " + e.getMessage());
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