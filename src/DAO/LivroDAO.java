package DAO;

import model.Livro;
import model.Eletronico;
import model.Impresso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    private Connection connection;

    public LivroDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(Livro livro) {
        String sql = "INSERT INTO livro (titulo, autores, editora, preco) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, String.join(", ", livro.getAutores()));
            stmt.setString(3, livro.getEditora());
            stmt.setDouble(4, livro.getPreco());

            stmt.executeUpdate();
            System.out.println("Livro salvo com sucesso no banco de dados.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao salvar o livro no banco de dados.");
        }
    }

    public List<Livro> listarTodos() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT l.id, l.titulo, l.autores, l.editora, l.preco, e.tamanho, i.frete, i.estoque " +
                "FROM livro l " +
                "LEFT JOIN eletronico e ON l.id = e.id " +
                "LEFT JOIN impresso i ON l.id = i.id";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String[] autores = rs.getString("autores").split(", ");
                String editora = rs.getString("editora");
                double preco = rs.getDouble("preco");

                // Criação do objeto Livro apropriado
                Livro livro;

                if (rs.getDouble("tamanho") > 0) {
                    double tamanho = rs.getDouble("tamanho");
                    livro = new Eletronico(titulo, autores, editora, preco, tamanho);
                } else if (rs.getDouble("frete") > 0) {
                    double frete = rs.getDouble("frete");
                    int estoque = rs.getInt("estoque");
                    livro = new Impresso(titulo, autores, editora, preco, frete, estoque);
                } else {

                    continue;
                }

                livros.add(livro);
            }
        }


        return livros;
    }

    public List<Eletronico> listarEletronicos() throws SQLException {
        List<Eletronico> eletronicos = new ArrayList<>();
        String sql = "SELECT l.id, l.titulo, l.autores, l.editora, l.preco, e.tamanho " +
                "FROM livro l " +
                "JOIN eletronico e ON l.id = e.id";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String[] autores = rs.getString("autores").split(", ");
                String editora = rs.getString("editora");
                double preco = rs.getDouble("preco");
                double tamanho = rs.getDouble("tamanho");

                Eletronico eletronico = new Eletronico(titulo, autores, editora, preco, tamanho);
                eletronicos.add(eletronico);
            }
        }

        return eletronicos;
    }

    public List<Impresso> listarImpressos() throws SQLException {
        List<Impresso> impressos = new ArrayList<>();
        String sql = "SELECT l.id, l.titulo, l.autores, l.editora, l.preco, i.frete, i.estoque " +
                "FROM livro l " +
                "JOIN impresso i ON l.id = i.id";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String[] autores = rs.getString("autores").split(", ");
                String editora = rs.getString("editora");
                double preco = rs.getDouble("preco");
                double frete = rs.getDouble("frete");
                int estoque = rs.getInt("estoque");

                Impresso impresso = new Impresso(titulo, autores, editora, preco, frete, estoque);
                impressos.add(impresso);
            }
        }

        return impressos;
    }
}

