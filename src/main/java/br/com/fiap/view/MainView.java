package br.com.fiap.view;

import br.com.fiap.service.ImportacaoService;

import java.util.Scanner;

public class MainView {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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
                    System.out.print("Digite o caminho completo do arquivo CSV: ");
                    String caminho = scanner.nextLine().trim();
                    ImportacaoService.importarCSV(caminho);
                    break;

                case 2:
                    System.out.println("📊 Estatísticas (em desenvolvimento)");
                    break;

                case 3:
                    System.out.println("🔍 Consultar comércio (em desenvolvimento)");
                    break;

                case 4:
                    System.out.println("💾 Exportar relatório (em desenvolvimento)");
                    break;

                case 0:
                    System.out.println("Saindo do sistema...");
                    break;

                default:
                    System.out.println("❌ Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);

        scanner.close();
    }
}
