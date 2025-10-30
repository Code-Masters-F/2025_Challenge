package br.com.fiap.service;

import br.com.fiap.factory.ConnectionFactory;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class RelatorioService {

    public static void exportarRelatorioGeral() {
        final String SQL_COUNT = "SELECT COUNT(*) FROM venda";

        Scanner scanner = new Scanner(System.in);

        try (Connection conexao = ConnectionFactory.getConnection()) {

            int totalVendas = 0;
            try (PreparedStatement ps = conexao.prepareStatement(SQL_COUNT);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) totalVendas = rs.getInt(1);
            }

            if (totalVendas == 0) {
                System.out.println("""
            Nenhum dado encontrado.
            Importe os arquivos CSV de comércios e vendas antes de exportar relatórios.
            """);
                return;
            }

            System.out.println("\n1) TXT  2) CSV");
            System.out.print("Formato: ");
            int fmt = scanner.nextInt();
            scanner.nextLine();

            System.out.println("\nOnde deseja salvar o arquivo?");
            System.out.println("1) Diretório do projeto (padrão)");
            System.out.println("2) Escolher caminho personalizado");
            System.out.print("Opção: ");
            int localOpcao = scanner.nextInt();
            scanner.nextLine();

            String diretorio = "";
            if (localOpcao == 2) {
                System.out.print("Digite o caminho completo (Ex: C:\\Relatorios\\): ");
                diretorio = scanner.nextLine().trim();
                if (!diretorio.endsWith("\\") && !diretorio.endsWith("/")) {
                    diretorio += "\\";
                }
            }

            String formato = (fmt == 1) ? "txt" : "csv";
            String nomeArquivo = gerarNomeArquivo(diretorio + "relatorio_geral", formato);

            if (formato.equals("txt")) {
                gerarRelatorioGeralTXT(conexao, nomeArquivo);
            } else {
                gerarRelatorioGeralCSV(conexao, nomeArquivo);
            }

            System.out.println("\n✓ Relatório geral exportado com sucesso!");
            System.out.println("Arquivo: " + nomeArquivo + "\n");

        } catch (SQLException e) {
            System.out.println("Erro ao exportar relatório: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao gerar arquivo: " + e.getMessage());
        }
    }

    public static void exportarRelatorioPorComercio(Scanner scanner) {
        final String SQL_COUNT = "SELECT COUNT(*) FROM venda";

        try (Connection conexao = ConnectionFactory.getConnection()) {

            int totalVendas = 0;
            try (PreparedStatement ps = conexao.prepareStatement(SQL_COUNT);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) totalVendas = rs.getInt(1);
            }

            if (totalVendas == 0) {
                System.out.println("""
            Nenhum dado encontrado.
            Importe os arquivos CSV de comércios e vendas antes de exportar relatórios.
            """);
                return;
            }

            System.out.print("\nDigite o ID do comércio: ");
            int idComercio = scanner.nextInt();
            scanner.nextLine();

            final String SQL_COMERCIO = "SELECT nome, cnpj FROM pequenovarejo WHERE id = ?";
            String nomeComercio = null;
            String cnpj = null;

            try (PreparedStatement ps = conexao.prepareStatement(SQL_COMERCIO)) {
                ps.setInt(1, idComercio);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    nomeComercio = rs.getString("nome");
                    cnpj = rs.getString("cnpj");
                } else {
                    System.out.println("""
                Comércio não encontrado.
                Verifique o ID e tente novamente.
                """);
                    return;
                }
            }

            System.out.println("\n1) TXT  2) CSV");
            System.out.print("Formato: ");
            int fmt = scanner.nextInt();
            scanner.nextLine();

            System.out.println("\nOnde deseja salvar o arquivo?");
            System.out.println("1) Diretório do projeto (padrão)");
            System.out.println("2) Escolher caminho personalizado");
            System.out.print("Opção: ");
            int localOpcao = scanner.nextInt();
            scanner.nextLine();

            String diretorio = "";
            if (localOpcao == 2) {
                System.out.print("Digite o caminho completo (Ex: C:\\Relatorios\\): ");
                diretorio = scanner.nextLine().trim();
                if (!diretorio.endsWith("\\") && !diretorio.endsWith("/")) {
                    diretorio += "\\";
                }
            }

            String formato = (fmt == 1) ? "txt" : "csv";
            String nomeArquivo = gerarNomeArquivo(diretorio + "relatorio_comercio_" + idComercio, formato);

            if (formato.equals("txt")) {
                gerarRelatorioComercioTXT(conexao, nomeArquivo, idComercio, nomeComercio, cnpj);
            } else {
                gerarRelatorioComercioCSV(conexao, nomeArquivo, idComercio, nomeComercio);
            }

            System.out.println("\n✓ Relatório do comércio exportado com sucesso!");
            System.out.println("Arquivo: " + nomeArquivo + "\n");

        } catch (SQLException e) {
            System.out.println("Erro ao exportar relatório: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao gerar arquivo: " + e.getMessage());
        }
    }

    private static void gerarRelatorioGeralTXT(Connection conexao, String nomeArquivo) throws Exception {
        final String SQL_TOTAL_VENDAS = "SELECT COUNT(*) FROM venda";
        final String SQL_TOTAL_COMERCIOS = "SELECT COUNT(*) FROM pequenovarejo";
        final String SQL_FATURAMENTO = "SELECT SUM(preco_unitario * quantidade) FROM venda";
        final String SQL_TOP_PRODUTOS = """
            SELECT nome_produto, qtd, total FROM (
                SELECT nome_produto, COUNT(*) as qtd, SUM(preco_unitario * quantidade) as total
                FROM venda
                GROUP BY nome_produto
                ORDER BY qtd DESC
            ) WHERE ROWNUM <= 10
            """;

        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {

            writer.println("RELATÓRIO GERAL DE VENDAS - SISTEMA ASTERIA");
            writer.println("Data: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            writer.println();

            int totalVendas = 0;
            int totalComercios = 0;
            double faturamento = 0.0;

            try (PreparedStatement ps = conexao.prepareStatement(SQL_TOTAL_VENDAS);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) totalVendas = rs.getInt(1);
            }

            try (PreparedStatement ps = conexao.prepareStatement(SQL_TOTAL_COMERCIOS);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) totalComercios = rs.getInt(1);
            }

            try (PreparedStatement ps = conexao.prepareStatement(SQL_FATURAMENTO);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) faturamento = rs.getDouble(1);
            }

            writer.println("RESUMO GERAL");
            writer.println("Total de Vendas: " + totalVendas);
            writer.println("Total de Comércios: " + totalComercios);
            writer.printf("Faturamento Total: R$ %.2f\n", faturamento);
            writer.println();

            writer.println("TOP 10 PRODUTOS MAIS VENDIDOS");

            try (PreparedStatement ps = conexao.prepareStatement(SQL_TOP_PRODUTOS);
                 ResultSet rs = ps.executeQuery()) {

                int pos = 1;
                while (rs.next()) {
                    writer.printf("%2d. %-30s | Qtde: %-5d | R$ %.2f\n",
                            pos++,
                            rs.getString("nome_produto"),
                            rs.getInt("qtd"),
                            rs.getDouble("total"));
                }
            }

            writer.println();
            writer.println("Relatório gerado com sucesso!");
        }
    }

    private static void gerarRelatorioGeralCSV(Connection conexao, String nomeArquivo) throws Exception {
        final String SQL = """
            SELECT nome_produto, COUNT(*) as qtd, SUM(preco_unitario * quantidade) as total
            FROM venda
            GROUP BY nome_produto
            ORDER BY qtd DESC
            """;

        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {

            writer.println("Produto,Quantidade_Vendas,Faturamento");

            try (PreparedStatement ps = conexao.prepareStatement(SQL);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    writer.printf("%s,%d,%.2f\n",
                            escaparCSV(rs.getString("nome_produto")),
                            rs.getInt("qtd"),
                            rs.getDouble("total"));
                }
            }
        }
    }

    private static void gerarRelatorioComercioTXT(Connection conexao, String nomeArquivo,
                                                  int idComercio, String nomeComercio, String cnpj) throws Exception {
        final String SQL_TOTAL_VENDAS = "SELECT COUNT(*) FROM venda WHERE id_varejo = ?";
        final String SQL_FATURAMENTO = "SELECT SUM(preco_unitario * quantidade) FROM venda WHERE id_varejo = ?";
        final String SQL_TOP_PRODUTOS = """
            SELECT nome_produto, qtd, total FROM (
                SELECT nome_produto, COUNT(*) as qtd, SUM(preco_unitario * quantidade) as total
                FROM venda
                WHERE id_varejo = ?
                GROUP BY nome_produto
                ORDER BY qtd DESC
            ) WHERE ROWNUM <= 10
            """;
        final String SQL_ULTIMA_VENDA = """
            SELECT nome_produto, data_hora FROM (
                SELECT nome_produto, data_hora
                FROM venda
                WHERE id_varejo = ?
                ORDER BY data_hora DESC
            ) WHERE ROWNUM = 1
            """;

        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {

            writer.println("RELATÓRIO DE VENDAS - COMÉRCIO ESPECÍFICO");
            writer.println("Data: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            writer.println();

            writer.println("INFORMAÇÕES DO COMÉRCIO");
            writer.println("Nome: " + nomeComercio);
            writer.println("CNPJ: " + cnpj);
            writer.println("ID: " + idComercio);
            writer.println();

            int totalVendas = 0;
            double faturamento = 0.0;

            try (PreparedStatement ps = conexao.prepareStatement(SQL_TOTAL_VENDAS)) {
                ps.setInt(1, idComercio);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) totalVendas = rs.getInt(1);
            }

            try (PreparedStatement ps = conexao.prepareStatement(SQL_FATURAMENTO)) {
                ps.setInt(1, idComercio);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) faturamento = rs.getDouble(1);
            }

            writer.println("RESUMO DE VENDAS");
            writer.println("Total de Vendas: " + totalVendas);
            writer.printf("Faturamento: R$ %.2f\n", faturamento);
            writer.println();

            if (totalVendas > 0) {
                writer.println("TOP PRODUTOS MAIS VENDIDOS");

                try (PreparedStatement ps = conexao.prepareStatement(SQL_TOP_PRODUTOS)) {
                    ps.setInt(1, idComercio);
                    ResultSet rs = ps.executeQuery();

                    int pos = 1;
                    while (rs.next()) {
                        writer.printf("%2d. %-30s | Qtde: %-5d | R$ %.2f\n",
                                pos++,
                                rs.getString("nome_produto"),
                                rs.getInt("qtd"),
                                rs.getDouble("total"));
                    }
                }

                writer.println();
                writer.println("ÚLTIMA VENDA REGISTRADA");

                try (PreparedStatement ps = conexao.prepareStatement(SQL_ULTIMA_VENDA)) {
                    ps.setInt(1, idComercio);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        writer.println("Produto: " + rs.getString("nome_produto"));
                        Timestamp timestamp = rs.getTimestamp("data_hora");
                        if (timestamp != null) {
                            writer.println("Data/Hora: " + timestamp.toLocalDateTime()
                                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                        }
                    }
                }
            } else {
                writer.println("Nenhuma venda registrada para este comércio.");
            }

            writer.println();
            writer.println("Relatório gerado com sucesso!");
        }
    }

    private static void gerarRelatorioComercioCSV(Connection conexao, String nomeArquivo,
                                                  int idComercio, String nomeComercio) throws Exception {
        final String SQL = """
            SELECT nome_produto, COUNT(*) as qtd, SUM(preco_unitario * quantidade) as total
            FROM venda
            WHERE id_varejo = ?
            GROUP BY nome_produto
            ORDER BY qtd DESC
            """;

        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {

            writer.println("Comercio,Produto,Quantidade_Vendas,Faturamento");

            try (PreparedStatement ps = conexao.prepareStatement(SQL)) {
                ps.setInt(1, idComercio);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    writer.printf("%s,%s,%d,%.2f\n",
                            escaparCSV(nomeComercio),
                            escaparCSV(rs.getString("nome_produto")),
                            rs.getInt("qtd"),
                            rs.getDouble("total"));
                }
            }
        }
    }

    private static String gerarNomeArquivo(String prefixo, String formato) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return prefixo + "_" + timestamp + "." + formato;
    }

    private static String escaparCSV(String valor) {
        if (valor == null) return "";
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}