package br.com.fiap.view;

import br.com.fiap.service.EstatisticaService;
import br.com.fiap.service.ImportacaoService;
import br.com.fiap.service.RelatorioService;

import br.com.fiap.utils.Mensagens;
import java.nio.file.Files;
import java.nio.file.Paths;


import java.util.Scanner;

import static br.com.fiap.utils.MenuUtils.*;

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

                    if (caminho.isEmpty()) {
                        System.out.println(Mensagens.ERRO_CAMINHO_VAZIO);
                        break;
                    }

                    if (!caminho.toLowerCase().endsWith(".csv")) {
                        System.out.println(Mensagens.erroFormatoArquivo(caminho));
                        break;
                    }

                    if (!Files.exists(Paths.get(caminho))) {
                        System.out.println("✗ Arquivo não encontrado em: " + Paths.get(caminho).toAbsolutePath());
                        break;
                    }

                    System.out.println(Mensagens.PROGRESSO_IMPORTACAO);
                    try {
                        ImportacaoService.importarCSV(caminho);
                        System.out.println(Mensagens.SUCESSO_IMPORTACAO);
                    } catch (Exception e) {
                        System.out.println(Mensagens.erroComDetalhes(Mensagens.ERRO_IMPORTACAO, e.getMessage()));
                    }
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
                        case 0 -> System.out.println(Mensagens.VOLTAR_MENU);
                        default -> System.out.println(Mensagens.ERRO_OPCAO_INVALIDA);
                    }
                    break;


                case 3:
                    limparTela();
                    System.out.print("Digite o id do pequeno varejo: ");
                    int idComercio = scanner.nextInt();
                    scanner.nextLine();

                    if (idComercio <= 0) {
                        System.out.println(Mensagens.ERRO_ID_INVALIDO);
                        break;
                    }

                    try {
                        EstatisticaService.consultarComercioPorId(idComercio);
                    } catch (Exception e) {
                        System.out.println(Mensagens.ERRO_CONSULTA_COMERCIO);
                    }
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
                        case 0 -> System.out.println(Mensagens.VOLTAR_MENU);
                        default -> System.out.println(Mensagens.ERRO_OPCAO_INVALIDA);
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
