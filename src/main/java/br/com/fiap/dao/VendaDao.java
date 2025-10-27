package br.com.fiap.dao;

import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.Venda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

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

}
