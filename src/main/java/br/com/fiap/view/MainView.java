package br.com.fiap.view;

import br.com.fiap.service.EstatisticaService;
import br.com.fiap.service.ImportacaoService;
import br.com.fiap.service.RelatorioService;
import br.com.fiap.utils.InputUtils;
import br.com.fiap.utils.Mensagens;

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

            System.out.print("Sua escolha: ");
            opcao = InputUtils.lerOpcao(scanner, 0, 4);

            switch (opcao) {
                case 1 -> {
                    limparTela();
                    String caminho = InputUtils.lerTextoNaoVazio(scanner,
                            "Digite o caminho do arquivo CSV (Ex: vendas.csv): ");
                    System.out.println(Mensagens.PROGRESSO_IMPORTACAO);
                    try {
                        ImportacaoService.importarCSV(caminho);
                        System.out.println(Mensagens.SUCESSO_IMPORTACAO);
                    } catch (Exception e) {
                        System.out.println(Mensagens.erroComDetalhes(Mensagens.ERRO_IMPORTACAO, e.getMessage()));
                    }
                    InputUtils.pausar(scanner);
                }

                case 2 -> {
                    limparTela();
                    System.out.println(MENU_EXIBIR_ESTATISTICAS);

                    System.out.print("Sua escolha: ");
                    int escolha = InputUtils.lerOpcao(scanner, 0, 3);

                    switch (escolha) {
                        case 1 -> EstatisticaService.exibirGerais();
                        case 2 -> EstatisticaService.exibirPorProduto();
                        case 3 -> EstatisticaService.exibirPorRegiao();
                        case 0 -> System.out.println(Mensagens.VOLTAR_MENU);
                        default -> System.out.println(Mensagens.ERRO_OPCAO_INVALIDA);
                    }
                    InputUtils.pausar(scanner);
                }

                case 3 -> {
                    limparTela();
                    int idComercio = InputUtils.lerIntPositivo(scanner, "Digite o id do pequeno varejo: ");
                    try {
                        EstatisticaService.consultarComercioPorId(idComercio);
                    } catch (Exception e) {
                        System.out.println(Mensagens.erroComDetalhes(Mensagens.ERRO_CONSULTA_COMERCIO, e.getMessage()));
                    }
                    InputUtils.pausar(scanner);
                }

                case 4 -> {
                    System.out.println("""
                            === EXPORTAR RELATÓRIO ===
                            1) Relatório Geral
                            2) Relatório por Comércio
                            0) Voltar
                            """);

                    System.out.print("Sua escolha: ");
                    int tipo = InputUtils.lerOpcao(scanner, 0, 2);

                    switch (tipo) {
                        case 1 -> RelatorioService.exportarRelatorioGeral();
                        case 2 -> RelatorioService.exportarRelatorioPorComercio(scanner);
                        case 0 -> System.out.println(Mensagens.VOLTAR_MENU);
                        default -> System.out.println(Mensagens.ERRO_OPCAO_INVALIDA);
                    }
                    InputUtils.pausar(scanner);
                }

                case 0 -> {
                    System.out.println(Mensagens.DESPEDIDA);
                }

                default -> {
                    System.out.println(Mensagens.ERRO_OPCAO_INVALIDA);
                    InputUtils.pausar(scanner);
                }
            }
        } while (opcao != 0);

        scanner.close();
    }
}
