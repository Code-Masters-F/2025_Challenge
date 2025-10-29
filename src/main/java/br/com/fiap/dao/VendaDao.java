package br.com.fiap.dao;

import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.Venda;
import br.com.fiap.model.UnidadeDeMedida;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class VendaDao {

    public void salvar(Venda venda) throws SQLException {
        String SQL = """
                INSERT INTO venda (id_varejo, nome_produto, tamanho_embalagem, preco_unitario, unidade_de_medida, quantidade, data_hora) 
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conexao.prepareStatement(SQL)) {
            preparedStatement.setInt(1, venda.getIdVarejo());
            preparedStatement.setString(2, venda.getNomeProduto());
            preparedStatement.setDouble(3, venda.getTamanhoEmbalagem());
            preparedStatement.setBigDecimal(4, venda.getPrecoUnitario());
            preparedStatement.setString(5, venda.getUnidadeDeMedida().name());
            preparedStatement.setDouble(6, venda.getQuantidade());
            preparedStatement.setTimestamp(7, Timestamp.from(venda.getDataHora()));
            preparedStatement.executeUpdate();
        }
    }

    public int totalVendasPorIdVarejo(int idComercio) throws SQLException {

        String sql = """
                SELECT COUNT(*) AS total_vendas
                FROM Venda
                WHERE id_varejo = ?
                """;

        try(Connection conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, idComercio);

            ResultSet result = stm.executeQuery();
            if(!result.next())
                return 0;

            return result.getInt(1);
        }
    }

    public List<Venda> vendasPorIdVarejo (int idComercio) throws SQLException{
        String sql = "SELECT * FROM Venda WHERE id_varejo = ?";
        List<Venda> listaVendas = new ArrayList<>();

        try(Connection conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql)) {

            ResultSet result = stm.executeQuery();
            //int id, int idVarejo, String nome_produto, Instant dataHora, BigDecimal preco,
            // UnidadeDeMedida unidadeDeMedida, double quantidade
            while (!result.next()) {
                int id = result.getInt("id");
                int idVarejo = result.getInt("id_varejo");
                String nomeProduto = result.getString("nome_produto");
                BigDecimal tamanhoEmbalagem = result.getBigDecimal("Tamanho_embalagem");
                BigDecimal preco = result.getBigDecimal("preco_unitario");
                String unidadeDeMedida = result.getString("unidade_de_medida");
                BigDecimal quantidade = result.getBigDecimal("quantidade");
                Instant dataSemFuso = result.getObject("data_hora", Instant.class);
            }

        }
    }
}
