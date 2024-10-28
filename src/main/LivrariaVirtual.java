package main;

import model.Livro;
import model.Eletronico;
import model.Impresso;
import model.Venda;
import DAO.LivroDAO;
import DAO.EletronicoDAO;
import DAO.ImpressoDAO;
import DAO.VendaDAO;
import DAO.DatabaseConnection;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.SQLException;

public class LivrariaVirtual {

    private final int MAX_IMPRESSOS = 10;
    private final int MAX_ELETRONICOS = 20;
    private final int MAX_VENDAS = 50;
    private int numImpressos;
    private int numEletronicos;
    private int numVendas;
    private Impresso[] impressos;
    private Eletronico[] eletronicos;
    private Venda[] vendas;

    public LivrariaVirtual() {
        this.impressos = new Impresso[MAX_IMPRESSOS];
        this.eletronicos = new Eletronico[MAX_ELETRONICOS];
        this.vendas = new Venda[MAX_VENDAS];
        this.numImpressos = 0;
        this.numEletronicos = 0;
        this.numVendas = 0;
    }

    public void cadastrarLivro(Connection connection) {
        Scanner input = new Scanner(System.in);

        System.out.println("Qual o tipo de livro que você deseja cadastrar?");
        System.out.println("1. Impresso");
        System.out.println("2. Eletrônico");
        System.out.println("3. Voltar ao menu principal");
        int opcao = input.nextInt();
        input.nextLine();

        if (opcao == 1 && numImpressos >= MAX_IMPRESSOS) {
            System.out.println("Não há mais espaço para livros impressos.");
            return;
        }
        if (opcao == 2 && numEletronicos >= MAX_ELETRONICOS) {
            System.out.println("Não há mais espaço para livros eletrônicos.");
            return;
        }

        if (opcao == 3 ){
                System.out.println("Voltando ao menu principal...");
                return; // Volta ao menu principal
        }

        System.out.println("Digite o título do livro:");
        String titulo = input.nextLine();
        System.out.println("Digite o autor(a):");
        String autores = input.nextLine();
        System.out.println("Digite a editora:");
        String editora = input.nextLine();
        System.out.println("Digite o preço:");
        double preco = input.nextDouble();
        input.nextLine();

        switch (opcao) {
            case 1:
                Impresso livroImp = new Impresso(titulo, autores.split(","), editora, preco, 0, 0);

                System.out.println("Digite o valor do frete:");
                livroImp.setFrete(input.nextDouble());
                System.out.println("Digite o estoque:");
                livroImp.setEstoque(input.nextInt());
                input.nextLine();

                ImpressoDAO impressoDAO = new ImpressoDAO(connection);

                try {
                    impressoDAO.cadastrarImpresso(livroImp);
                } catch (SQLException e) {
                    System.out.println("Erro ao cadastrar livro impresso: " + e.getMessage());
                }

                impressos[numImpressos++] = livroImp;
                break;

            case 2:
                Eletronico livroEle = new Eletronico(titulo, autores.split(","), editora, preco, 0);
                System.out.println("Digite o tamanho do arquivo (em KB):");
                livroEle.setTamanho(input.nextDouble());
                input.nextLine();

                EletronicoDAO eletronicoDAO = new EletronicoDAO(connection);

                try {
                    eletronicoDAO.cadastrarEletronico(livroEle);
                } catch (SQLException e) {
                    System.out.println("Erro ao cadastrar livro eletronico: " + e.getMessage());
                }

                eletronicos[numEletronicos++] = livroEle;
                break;


            default:
                System.out.println("Opção inválida.");
        }
    }

    public void realizarVenda(Connection connection) {
        Scanner input = new Scanner(System.in);
        System.out.println("Digite o nome do cliente:");
        String nomeCliente = input.nextLine();
        System.out.println("Digite a quantidade de livros que o cliente deseja comprar:");
        int quantidade = input.nextInt();
        input.nextLine();

        if (quantidade <= 0) {
            System.out.println("Quantidade inválida.");
            return;
        }

        Venda venda = new Venda(nomeCliente, quantidade);
        LivroDAO livroDAO = new LivroDAO(connection);
        VendaDAO vendaDAO = new VendaDAO(connection);

        for (int i = 0; i < quantidade; i++) {
            System.out.println("Qual o tipo de livro que você deseja adquirir?");
            System.out.println("1. Impresso");
            System.out.println("2. Eletrônico");
            System.out.println("3. Voltar ao menu principal");
            int opcao = input.nextInt();
            input.nextLine();

            switch (opcao) {
                case 1:
                    try {
                        List<Impresso> listaImpressos = livroDAO.listarImpressos();

                        if (listaImpressos.isEmpty()) {
                            System.out.println("Nenhum livro impresso disponível.");
                            i--;
                            break;
                        }

                        boolean todosSemEstoque = true;
                        for (Impresso impresso : listaImpressos) {
                            if (impresso.getEstoque() > 0) {
                                todosSemEstoque = false;
                                break;
                            }
                        }

                        if (todosSemEstoque) {
                            System.out.println("Todos os livros impressos estão sem estoque. Voltando ao menu principal...");
                            return;
                        }

                        System.out.println("Livros impressos disponíveis:");
                        for (int j = 0; j < listaImpressos.size(); j++) {
                            System.out.println((j + 1) + ". " + listaImpressos.get(j).getTitulo() + " - R$ " + listaImpressos.get(j).getPreco() + " (Estoque: " + listaImpressos.get(j).getEstoque() + ")");
                        }

                        System.out.println("Digite o número do livro impresso que deseja comprar:");
                        int indice = input.nextInt() - 1;
                        input.nextLine();

                        if (indice >= 0 && indice < listaImpressos.size()) {
                            Impresso livroSelecionado = listaImpressos.get(indice);
                            if (livroSelecionado.getEstoque() > 0) {
                                venda.addLivro(livroSelecionado, i);
                                livroSelecionado.atualizarEstoque(); // Reduz o estoque do livro impresso
                            } else {
                                System.out.println("Estoque insuficiente para o livro selecionado.");
                                i--;
                            }
                        } else {
                            System.out.println("Índice inválido.");
                            i--;
                        }
                    } catch (SQLException e) {
                        System.out.println("Erro ao listar livros impressos: " + e.getMessage());
                        i--;
                    }
                    break;

                case 2:
                    try {
                        List<Eletronico> listaEletronicos = livroDAO.listarEletronicos();
                        if (listaEletronicos.isEmpty()) {
                            System.out.println("Nenhum livro eletrônico disponível.");
                            i--;
                            break;
                        }
                        System.out.println("Livros eletrônicos disponíveis:");
                        for (int j = 0; j < listaEletronicos.size(); j++) {
                            System.out.println((j + 1) + ". " + listaEletronicos.get(j).getTitulo() + " - R$ " + listaEletronicos.get(j).getPreco());
                        }

                        System.out.println("Digite o número do livro eletrônico que deseja comprar:");
                        int indice = input.nextInt() - 1;
                        input.nextLine();

                        if (indice >= 0 && indice < listaEletronicos.size()) {
                            Eletronico livroSelecionado = listaEletronicos.get(indice);
                            venda.addLivro(livroSelecionado, i);
                        } else {
                            System.out.println("Índice inválido.");
                            i--;
                        }
                    } catch (SQLException e) {
                        System.out.println("Erro ao listar livros eletrônicos: " + e.getMessage());
                        i--;
                    }
                    break;

                case 3:
                    System.out.println("Voltando ao menu principal...");
                    return; // Volta ao menu principal

                default:
                    System.out.println("Opção inválida.");
                    i--;
                    break;
            }
        }

        if (numVendas < vendas.length) {
            vendas[numVendas] = venda;
            numVendas++;

            try {
                vendaDAO.cadastrarVenda(venda);
                System.out.println("Venda cadastrada com sucesso!");
            } catch (SQLException e) {
                System.out.println("Erro ao cadastrar a venda: " + e.getMessage());
                System.out.println(venda);
            }
        } else {
            System.out.println("Não há mais espaço para novas vendas.");
        }
    }


    public void listarLivros(Connection connection) {
        LivroDAO livroDAO = new LivroDAO(connection);

        try {
            List<Livro> livros = livroDAO.listarTodos();
            if (livros.isEmpty()) {
                System.out.println("Nenhum livro encontrado no banco de dados.");
            } else {
                System.out.println("Lista de livros cadastrados:");
                for (Livro livro : livros) {
                    System.out.println("\nNome: " + livro.getTitulo() + "\n" +"Preco: "+  livro.getPreco() +"\n"+  "Editora: "
                            + livro.getEditora() + "\n"+ "Autor: " + Arrays.toString(livro.getAutores()));

                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar livros do banco de dados: " + e.getMessage());
        }
    }

    public void listarVendas(Connection connection) {
        VendaDAO vendaDAO = new VendaDAO(connection);

        vendaDAO.listarVendas();
    }

    public static void main(String[] args) {
        LivrariaVirtual livraria = new LivrariaVirtual();
        Scanner input = new Scanner(System.in);

        Connection connection = null;

        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            return;
        }

        int opcao;

        do {
            System.out.println("\nMenu:");
            System.out.println("1. Cadastrar livro");
            System.out.println("2. Realizar venda");
            System.out.println("3. Listar livros");
            System.out.println("4. Listar vendas");
            System.out.println("5. Sair");

            System.out.print("Escolha uma opção: ");
            opcao = input.nextInt();
            input.nextLine();

            switch (opcao) {
                case 1:
                    livraria.cadastrarLivro(connection);
                    break;
                case 2:
                    livraria.realizarVenda(connection);
                    break;
                case 3:
                    livraria.listarLivros(connection);
                    break;
                case 4:
                     livraria.listarVendas(connection);
                      break;
                case 5:
                    System.out.println("Saindo do programa");
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        } while (opcao != 5);

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
        }

        input.close();
    }
}
