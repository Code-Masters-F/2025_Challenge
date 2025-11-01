package br.com.fiap.service;

import br.com.fiap.dao.PequenoVarejoDao;
import br.com.fiap.dao.VendaDao;
import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.PequenoVarejo;
import br.com.fiap.model.Venda;

import java.math.BigDecimal;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EstatisticaService {

    public static void exibirGerais() {
        final String SQL_TOTAL_VENDAS = "SELECT COUNT(*) FROM venda";
        final String SQL_TOTAL_FATURAMENTO = "SELECT SUM(preco_unitario * quantidade) FROM venda";
        final String SQL_TOTAL_COMERCIOS = "SELECT COUNT(DISTINCT id_varejo) FROM venda";

        try (Connection conexao = ConnectionFactory.getConnection()) {

            int totalVendas = 0;
            try (PreparedStatement ps = conexao.prepareStatement(SQL_TOTAL_VENDAS);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) totalVendas = rs.getInt(1);
            }

            if (totalVendas == 0) {
                System.out.println("""
            Nenhum dado encontrado.
            Importe os arquivos CSV de comércios e vendas antes de visualizar as estatísticas.
            """);
                return;
            }

            BigDecimal faturamento = BigDecimal.ZERO;
            try (PreparedStatement ps = conexao.prepareStatement(SQL_TOTAL_FATURAMENTO);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getBigDecimal(1) != null) {
                    faturamento = rs.getBigDecimal(1);
                }
            }

            int totalComercios = 0;
            try (PreparedStatement ps = conexao.prepareStatement(SQL_TOTAL_COMERCIOS);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) totalComercios = rs.getInt(1);
            }

            System.out.printf("""
                    === Estatísticas Gerais ===
                    Total de vendas: %d
                    Faturamento total: R$ %.2f
                    Total de comércios com vendas: %d
                    %n""", totalVendas, faturamento, totalComercios);

        } catch (SQLException e) {
            System.out.println("Erro ao gerar estatísticas gerais: " + e.getMessage());
        }
    }

    public static void exibirPorProduto() {
        final String SQL = """
            SELECT nome_produto, SUM(quantidade) AS total_vendido,
                   SUM(preco_unitario * quantidade) AS faturamento
            FROM venda
            GROUP BY nome_produto
            ORDER BY faturamento DESC
            """;

        final String SQL_COUNT = "SELECT COUNT(*) FROM venda";

        try (Connection conexao = ConnectionFactory.getConnection()) {

            int totalVendas = 0;
            try (PreparedStatement psCount = conexao.prepareStatement(SQL_COUNT);
                 ResultSet rsCount = psCount.executeQuery()) {
                if (rsCount.next()) totalVendas = rsCount.getInt(1);
            }

            if (totalVendas == 0) {
                System.out.println("""
            Nenhum dado encontrado.
            Importe os arquivos CSV de comércios e vendas antes de visualizar as estatísticas.
            """);
                return;
            }

            try (PreparedStatement ps = conexao.prepareStatement(SQL);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("=== Estatísticas por Produto ===");

                boolean temResultados = false;
                while (rs.next()) {
                    temResultados = true;
                    System.out.printf("🛒 %-25s | Qtde: %-5d | Faturamento: R$ %.2f%n",
                            rs.getString("nome_produto"),
                            rs.getInt("total_vendido"),
                            rs.getDouble("faturamento"));
                }
                System.out.println();

                if (!temResultados) {
                    System.out.println("""
                Nenhum dado encontrado.
                Certifique-se de que o arquivo de vendas foi importado corretamente.
                """);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao gerar estatísticas por produto: " + e.getMessage());
        }
    }

    public static void exibirPorRegiao() {
        final String SQL = """
            SELECT pv.cidade, SUM(v.preco_unitario * v.quantidade) AS total
            FROM venda v
            JOIN pequenovarejo pv ON v.id_varejo = pv.id
            GROUP BY pv.cidade
            ORDER BY total DESC
            """;

        final String SQL_COUNT = "SELECT COUNT(*) FROM venda";

        try (Connection conexao = ConnectionFactory.getConnection()) {

            int totalVendas = 0;
            try (PreparedStatement psCount = conexao.prepareStatement(SQL_COUNT);
                 ResultSet rsCount = psCount.executeQuery()) {
                if (rsCount.next()) totalVendas = rsCount.getInt(1);
            }

            if (totalVendas == 0) {
                System.out.println("""
            Nenhum dado encontrado.
            Importe os arquivos CSV de comércios e vendas antes de visualizar as estatísticas.
            """);
                return;
            }

            try (PreparedStatement ps = conexao.prepareStatement(SQL);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("=== Estatísticas por Região ===");

                boolean temResultados = false;
                while (rs.next()) {
                    temResultados = true;
                    System.out.printf("%-20s | Faturamento: R$ %.2f%n",
                            rs.getString("cidade"),
                            rs.getDouble("total"));
                }
                System.out.println();

                if (!temResultados) {
                    System.out.println("""
                Nenhum dado encontrado.
                Certifique-se de que os comércios e as vendas estão corretamente relacionados.
                """);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao gerar estatísticas por região: " + e.getMessage());
        }
    }

    public static void consultarComercioPorId(int id) {
        PequenoVarejoDao pequenoVarejoDao = new PequenoVarejoDao();
        VendaDao vendaDao = new VendaDao();

        try {
            PequenoVarejo pqnVarejo = pequenoVarejoDao.istanciarPorId(id);
            System.out.println(System.lineSeparator() + "---- " + pqnVarejo.getNome() + " ----");
            System.out.println("CNPJ: " + pqnVarejo.getCnpj() + ", CEP: " + pqnVarejo.getCep());
            System.out.println("Endereço: " + pqnVarejo.getCidade() + " - " + pqnVarejo.getEstado() + ", " + pqnVarejo.getEndereco());

            System.out.println("---- Estatísticas ----");
            System.out.println("Total de vendas: " + vendaDao.totalVendasPorIdVarejo(id));
            List<Venda> listaVendas = vendaDao.vendasPorIdVarejo(id);
            System.out.print("Produtos vendidos: ");
            for (int i = 0; i < listaVendas.size(); i++) {
                if (i != 0)
                    System.out.print(", ");
                System.out.print(listaVendas.get(i).getNomeProduto() + " " + listaVendas.get(i).getTamanhoEmbalagem() +
                        listaVendas.get(i).getUnidadeDeMedida().name());
            }

            Venda ultimaVenda = vendaDao.ultimaVendaPorVarejo(id);
            ZoneId fusoLocal = ZoneId.systemDefault();
            ZonedDateTime dataHoraComFuso = ultimaVenda.getDataHora().atZone(fusoLocal);
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String dataFormatada = dataHoraComFuso.format(formatador);
            System.out.print(System.lineSeparator());
            System.out.print("Última venda: " + ultimaVenda.getNomeProduto() + " " + ultimaVenda.getTamanhoEmbalagem() +
                    ultimaVenda.getUnidadeDeMedida().name() + " " + dataFormatada);

            System.out.println(System.lineSeparator());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }



}
