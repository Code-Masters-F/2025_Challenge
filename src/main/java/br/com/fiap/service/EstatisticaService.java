package br.com.fiap.service;

import br.com.fiap.factory.ConnectionFactory;

import javax.xml.transform.Result;
import java.sql.*;

public class EstatisticaService {

    public static void exibirGerais() {
        try (Connection conexao = ConnectionFactory.getConnection()) {
            Statement stmt = conexao.createStatement();

            ResultSet resultSet1 = stmt.executeQuery("SELECT COUNT(*) FROM venda");
            if (resultSet1.next()) {
                System.out.println("Total de vendas: " + resultSet1.getInt(1));
            }

            ResultSet resultSet2 = stmt.executeQuery("SELECT SUM(preco_unitario * quantidade) FROM venda");
            if (resultSet2.next()) {
                System.out.println("Faturamento total: R$ " + resultSet2.getBigDecimal(1));
            }

            ResultSet resultSet3 = stmt.executeQuery("SELECT COUNT(DISTINCT id_varejo) FROM venda");
            if (resultSet3.next()) {
                System.out.println("Total de comércios com vendas: " + resultSet3.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao gerar estatísticas gerais: " + e.getMessage());
        }
    }


    public static void exibirPorProduto() {
        final String SQL = """
                SELECT nome_produto, SUM(quantidade) AS total_vendida,
                    SUM(preco_unitario * quantidade) AS faturamento
                FROM venda
                GROUP BY nome_produto
                ORDER BY faturamento DESC
                """;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conexao.prepareStatement(SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("=== Estatísticas por Produto ===");
            while (resultSet.next()) {
                System.out.printf("🛒 %-25s | Qtde: %-5d | Faturamento: R$ %.2f%n",
                        resultSet.getString("nome_produto"),
                        resultSet.getInt("total_vendida"),
                        resultSet.getDouble("faturamento"));
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
        try (Connection conexao = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = conexao.prepareStatement(SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("=== Estatísticas por Região ===");
            while (resultSet.next()) {
                System.out.printf("🏙️ %-20s | Faturamento: R$ %.2f%n",
                        resultSet.getString("cidade"),
                        resultSet.getDouble("total"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao gerar estatísticas por região: " + e.getMessage());
        }
    }




}
