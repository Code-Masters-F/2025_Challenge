package br.com.fiap.dao;

import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.PequenoVarejo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PequenoVarejoDao {

    public void salvar(PequenoVarejo pequenoVarejo) throws SQLException {
        String SQL = """
                INSERT INTO pequenovarejo (nome, cnpj, endereco, cidade, estado, cep)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conexao.prepareStatement(SQL)) {
            preparedStatement.setString(1, pequenoVarejo.getNome());
            preparedStatement.setString(2, pequenoVarejo.getCnpj());
            preparedStatement.setString(3, pequenoVarejo.getEndereco());
            preparedStatement.setString(4, pequenoVarejo.getCidade());
            preparedStatement.setString(5, pequenoVarejo.getEstado());
            preparedStatement.setString(6, pequenoVarejo.getCep());
            preparedStatement.executeUpdate();
        }
    }

    public Integer buscarIdPorNome(String nome) throws SQLException {
        String SQL = "SELECT id FROM pequenovarejo WHERE nome = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = conexao.prepareStatement(SQL)) {
            preparedStatement.setString(1, nome);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
            return null;
        }
    }


}
