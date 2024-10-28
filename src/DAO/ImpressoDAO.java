package DAO;

import model.Impresso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImpressoDAO {

    private Connection connection;

    public ImpressoDAO(Connection connection) {
        this.connection = connection;
    }

    public void cadastrarImpresso(Impresso impresso) throws SQLException {
        String sqlLivro = "INSERT INTO livro (titulo, autores, editora, preco) VALUES (?, ?, ?, ?)";
        String sqlImpresso = "INSERT INTO impresso (id, frete, estoque) VALUES (?, ?, ?)";

        try (PreparedStatement stmtLivro = connection.prepareStatement(sqlLivro, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtImpresso = connection.prepareStatement(sqlImpresso)) {

            stmtLivro.setString(1, impresso.getTitulo());
            stmtLivro.setString(2, String.join(",", impresso.getAutores()));
            stmtLivro.setString(3, impresso.getEditora());
            stmtLivro.setDouble(4, impresso.getPreco());
            stmtLivro.executeUpdate();

            try (var generatedKeys = stmtLivro.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);

                    stmtImpresso.setInt(1, id);
                    stmtImpresso.setDouble(2, impresso.getFrete());
                    stmtImpresso.setInt(3, impresso.getEstoque());
                    stmtImpresso.executeUpdate();
                } else {
                    throw new SQLException("Falha ao obter o ID gerado para o livro.");
                }
            }
        }
    }


}
