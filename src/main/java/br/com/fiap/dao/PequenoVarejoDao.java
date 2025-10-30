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

    public PequenoVarejo istanciarPorId(int idComercio) throws SQLException {
        String sql = "SELECT * FROM PequenoVarejo WHERE id = ?";

        try(Connection conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql)) {
            stm.setInt(1, idComercio);

            ResultSet result = stm.executeQuery();
            if (!result.next())
                throw new SQLException("Entidade não encontrada");

            int id = result.getInt("id");
            String nome = result.getString("nome");
            String cnpj = result.getString("CNPJ");
            String endereco = result.getString("endereco");
            String cidade = result.getString("cidade");
            String estado = result.getString("estado");
            String cep = result.getString("CEP");

            return new PequenoVarejo(id, nome, cnpj, cidade, estado, endereco, cep);
        }
    }

}
