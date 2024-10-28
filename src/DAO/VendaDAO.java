package DAO;

import model.Livro;
import model.Venda;


import java.sql.*;

public class VendaDAO {

    private Connection connection;

    public VendaDAO(Connection connection) {
        this.connection = connection;
    }

    public void cadastrarVenda(Venda venda) throws SQLException {
        String sql = "INSERT INTO venda (numVendas, cliente, valor) VALUES (?, ?, ?)";
        int vendaId;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, venda.getNumero());
            stmt.setString(2, venda.getCliente());
            stmt.setDouble(3, venda.getValor());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                vendaId = rs.getInt(1);
            } else {
                throw new SQLException("Erro ao obter o ID da venda.");
            }
        }

        for (Livro livro : venda.getLivros()) {
            if (livro != null) {
                cadastrarLivroVenda(vendaId, livro);
            }
        }
    }

    private void cadastrarLivroVenda(int vendaId, Livro livro) throws SQLException {
        String sqlVerificarEstoqueImpresso = "SELECT i.id, i.estoque FROM impresso i " +
                "JOIN livro l ON i.id = l.id " +
                "WHERE l.id = ?";

        String sqlVerificarEstoqueEletronico = "SELECT id FROM eletronico WHERE id = ?";

        String sqlInserirVendaLivro = "INSERT INTO vendalivro (venda_id, livro_id) VALUES (?, ?)";

        String sqlObterLivroId = "SELECT id FROM livro WHERE titulo = ? AND preco = ?";

        try (PreparedStatement stmtObterLivroId = connection.prepareStatement(sqlObterLivroId)) {
            stmtObterLivroId.setString(1, livro.getTitulo());
            stmtObterLivroId.setDouble(2, livro.getPreco());

            ResultSet rsLivro = stmtObterLivroId.executeQuery();

            if (rsLivro.next()) {
                int livroId = rsLivro.getInt("id");

                // Verifica se é um livro impresso
                try (PreparedStatement stmtVerificarEstoqueImpresso = connection.prepareStatement(sqlVerificarEstoqueImpresso)) {
                    stmtVerificarEstoqueImpresso.setInt(1, livroId);
                    ResultSet rsEstoqueImpresso = stmtVerificarEstoqueImpresso.executeQuery();

                    if (rsEstoqueImpresso.next()) {
                        int estoque = rsEstoqueImpresso.getInt("estoque");

                        if (estoque > 0) {
                            try (PreparedStatement stmtInserirVenda = connection.prepareStatement(sqlInserirVendaLivro)) {
                                stmtInserirVenda.setInt(1, vendaId);
                                stmtInserirVenda.setInt(2, livroId);
                                stmtInserirVenda.executeUpdate();
                            }
                        } else {
                            System.out.println("Estoque insuficiente para o livro impresso: " + livro.getTitulo());
                        }
                        return;
                    }
                }

                try (PreparedStatement stmtVerificarEstoqueEletronico = connection.prepareStatement(sqlVerificarEstoqueEletronico)) {
                    stmtVerificarEstoqueEletronico.setInt(1, livroId);
                    ResultSet rsEstoqueEletronico = stmtVerificarEstoqueEletronico.executeQuery();

                    if (rsEstoqueEletronico.next()) {
                        try (PreparedStatement stmtInserirVenda = connection.prepareStatement(sqlInserirVendaLivro)) {
                            stmtInserirVenda.setInt(1, vendaId);
                            stmtInserirVenda.setInt(2, livroId);
                            stmtInserirVenda.executeUpdate();
                        }
                    } else {
                        System.out.println("Livro não encontrado na tabela 'impresso' ou 'eletronico'.");
                    }
                }
            } else {
                System.out.println("Livro não encontrado na tabela 'livro'.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar livro na venda: " + e.getMessage());
        }
    }
    public void listarVendas() {
        String sql = "SELECT v.id AS venda_id, v.cliente, v.valor, l.id AS livro_id, l.titulo, l.preco " +
                "FROM venda v " +
                "JOIN vendalivro vl ON v.id = vl.venda_id " +
                "JOIN livro l ON vl.livro_id = l.id";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int currentVendaId = -1;

            while (rs.next()) {
                int vendaId = rs.getInt("venda_id");
                String cliente = rs.getString("cliente");
                double valor = rs.getDouble("valor");

                if (vendaId != currentVendaId) {
                    if (currentVendaId != -1) {
                        System.out.println(); // Linha em branco para separar vendas
                    }

                    System.out.println("Venda ID: " + vendaId);
                    System.out.println("Cliente: " + cliente);
                    System.out.println("Valor: R$ " + valor);
                    System.out.println("Livros:");

                    currentVendaId = vendaId;
                }

                int livroId = rs.getInt("livro_id");
                String titulo = rs.getString("titulo");
                double preco = rs.getDouble("preco");

                System.out.println(" - " + titulo + " - R$ " + preco);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar vendas: " + e.getMessage());
        }
    }
}
