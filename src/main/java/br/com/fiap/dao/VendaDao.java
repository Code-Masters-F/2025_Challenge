package br.com.fiap.dao;

import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.Venda;

import java.sql.*;

public class VendaDao {

    public void salvar(Venda venda) throws SQLException {
        String SQL = """
                INSERT INTO venda (id_varejo, produto, data_hora) VALUES (?, ?, ?)
                """;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conexao.prepareStatement(SQL)) {
            preparedStatement.setInt(1, venda.getIdVarejo());
            preparedStatement.setString(2, venda.getNomeProduto());
            preparedStatement.setTimestamp(3, Timestamp.from(venda.getDataHora()));
            preparedStatement.executeUpdate();
        }
    }

    public int totalVendasPorIdVarejo(int idComercio) throws SQLException {

        String sql = """
                SELECT COUNT(*) AS total_vendas
                FROM Venda
                WHERE id_varejo = ?;
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
}
