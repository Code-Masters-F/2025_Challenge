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

}
