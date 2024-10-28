package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "Livraria";
    private static final String USER = "usuario"; // seu usuario no banco de dados
    private static final String PASSWORD = "senha"; // sua senha no banco de dados

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver não encontrado: " + e.getMessage(), e);
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            String sql = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            statement.executeUpdate(sql);
            System.out.println("Banco de dados '" + DATABASE_NAME + "' criado/verificado com sucesso.");
        }

        Connection connection = DriverManager.getConnection(URL + DATABASE_NAME, USER, PASSWORD);

        try (Statement statement = connection.createStatement()) {
            criarTabelas(statement);

            System.out.println("Tabelas criadas/verificadas com sucesso.");
        } catch (SQLException e) {
            throw new SQLException("Erro ao criar/verificar tabelas: " + e.getMessage(), e);
        }

        return connection;
    }

    private static void criarTabelas(Statement statement) throws SQLException {
        criarTabelaLivro(statement);
        criarTabelaVenda(statement);
        criarTabelaVendaLivro(statement);
        criarTabelaImpresso(statement);
        criarTabelaEletronico(statement);
        criarTriggerReduzirEstoque(statement);
    }

    private static void criarTabelaLivro(Statement statement) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS livro ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "titulo VARCHAR(255) NOT NULL, "
                + "autores VARCHAR(255) NOT NULL, "
                + "editora VARCHAR(255) NOT NULL, "
                + "preco DECIMAL(10, 2) NOT NULL"
                + ");";
        statement.execute(sql);
    }

    private static void criarTabelaVenda(Statement statement) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS venda ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "numVendas INT NOT NULL, "
                + "cliente VARCHAR(255) NOT NULL, "
                + "valor FLOAT NOT NULL"
                + ");";
        statement.execute(sql);
    }

    private static void criarTabelaVendaLivro(Statement statement) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS vendalivro ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "venda_id INT NOT NULL, "
                + "livro_id INT NOT NULL, "
                + "INDEX(venda_id), "
                + "INDEX(livro_id), "
                + "FOREIGN KEY (venda_id) REFERENCES venda(id), "
                + "FOREIGN KEY (livro_id) REFERENCES livro(id)"
                + ");";
        statement.execute(sql);
    }

    private static void criarTabelaImpresso(Statement statement) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS impresso ("
                + "id INT PRIMARY KEY, "
                + "frete FLOAT NOT NULL, "
                + "estoque INT NOT NULL, "
                + "FOREIGN KEY (id) REFERENCES livro(id)"
                + ");";
        statement.execute(sql);
    }

    private static void criarTabelaEletronico(Statement statement) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS eletronico ("
                + "id INT PRIMARY KEY, "
                + "tamanho INT NOT NULL, "
                + "FOREIGN KEY (id) REFERENCES livro(id)"
                + ");";
        statement.execute(sql);
    }

    private static void criarTriggerReduzirEstoque(Statement statement) throws SQLException {
        String triggerSql = "CREATE TRIGGER IF NOT EXISTS reduzir_estoque_impresso " +
                "AFTER INSERT ON vendalivro " +
                "FOR EACH ROW " +
                "BEGIN " +
                "    UPDATE impresso " +
                "    SET estoque = estoque - 1 " +
                "    WHERE id = NEW.livro_id; " +
                "END;";
        statement.execute(triggerSql);
    }
}
