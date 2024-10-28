package model;

import java.util.ArrayList;
import java.util.List;

public class Venda {
    private static int numVendas = 0;
    private final int numero;
    private String cliente;
    private double valor;
    private Livro[] livros;

    public Venda(String cliente, int maxLivros) {
        this.numero = ++numVendas;
        this.cliente = cliente;
        this.valor = 0.0;
        this.livros = new Livro[maxLivros];
    }

    public void addLivro(Livro l, int index) {
        if (index >= 0 && index < livros.length) {
            if (livros[index] == null) {
                livros[index] = l;
                valor += l.getPreco();
            } else {
                System.out.println("Ja existe um livro nessa posicao.");
            }
        } else {
            System.out.println("indice fora dos limites do array.");
        }
    }

    public void listarLivros() {
        boolean livrosEncontrados = false;
        System.out.println("Livros comprados:");
        for (Livro livro : livros) {
            if (livro != null) {
                System.out.println(livro);
                livrosEncontrados = true;
            }
        }
        if (!livrosEncontrados) {
            System.out.println("Nenhum livro foi vendido.");
        }
    }

    public int getNumero() {
        return numero;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }

    public Livro[] getLivros() {
        return livros;
    }

    public static int getNumVendas() {
        return numVendas;
    }
}