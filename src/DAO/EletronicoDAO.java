package DAO;


import model.Eletronico;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EletronicoDAO {

    private Connection connection;

    public EletronicoDAO(Connection connection) {
        this.connection = connection;
    }

    public void cadastrarEletronico(Eletronico eletronico) throws SQLException {
        String sqlLivro = "INSERT INTO livro (titulo, autores, editora, preco) VALUES (?, ?, ?, ?)";
        String sqlEletronico = "INSERT INTO eletronico (id, tamanho) VALUES (?, ?)";

        try (PreparedStatement stmtLivro = connection.prepareStatement(sqlLivro, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtEletronico = connection.prepareStatement(sqlEletronico)) {

            stmtLivro.setString(1, eletronico.getTitulo());
            stmtLivro.setString(2, String.join(",", eletronico.getAutores()));
            stmtLivro.setString(3, eletronico.getEditora());
            stmtLivro.setDouble(4, eletronico.getPreco());
            stmtLivro.executeUpdate();

            try (var generatedKeys = stmtLivro.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);

                    stmtEletronico.setInt(1, id);
                    stmtEletronico.setDouble(2, eletronico.getTamanho());
                    stmtEletronico.executeUpdate();
                } else {
                    throw new SQLException("Falha ao obter o ID gerado para o livro.");
                }
            }
        }
    }
}
