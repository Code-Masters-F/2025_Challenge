package br.com.fiap.view;

import java.util.Scanner;

public class MainView {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("""
                1) Importar arquivo CSV
                2) Exibir estatísticas
                3) Consultar por ID de comércio
                4) Exportar relatório (.txt/.csv)
                0) Sair
                """);

            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    importarArquivoCSV();
                    break;

                case 2:
                    break;

                case 3:
                    break;

                case 4:
                    break;

                default:
            }
        } while (opcao != 0);

        scanner.close();
    }

    public static void importarArquivoCSV() {

    }










}
