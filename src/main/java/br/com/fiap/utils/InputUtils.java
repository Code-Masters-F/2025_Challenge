package br.com.fiap.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public final class InputUtils {
    private InputUtils() {}

    public static int lerOpcao(Scanner sc, int min, int max) {
        while (true) {
            try {
                int valor = sc.nextInt();
                sc.nextLine();
                if (valor < min || valor > max) {
                    System.out.println(Mensagens.erroOpcaoInvalidaFaixa(min, max));
                    System.out.print("Sua escolha: ");
                    continue;
                }
                return valor;
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println(Mensagens.ERRO_ENTRADA_NUMERICA);
                System.out.print("Sua escolha: ");
            }
        }
    }

    public static int lerIntPositivo(Scanner sc, String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                int valor = sc.nextInt();
                sc.nextLine();
                if (valor <= 0) {
                    System.out.println(Mensagens.ERRO_ID_INVALIDO);
                    System.out.print(prompt);
                    continue;
                }
                return valor;
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println(Mensagens.ERRO_ID_NUMERO);
                System.out.print(prompt);
            }
        }
    }

    public static String lerTextoNaoVazio(Scanner sc, String prompt) {
        System.out.print(prompt);
        while (true) {
            String v = sc.nextLine().trim();
            if (v.isEmpty()) {
                System.out.println(Mensagens.ERRO_CAMINHO_VAZIO);
                System.out.print(prompt);
            } else {
                return v;
            }
        }
    }

    public static void pausar(Scanner sc) {
        System.out.print(Mensagens.PRESSIONE_ENTER);
        sc.nextLine();
    }
}
