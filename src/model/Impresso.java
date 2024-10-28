package model;

public class Impresso extends Livro {
    private double frete;
    private int estoque;


    public Impresso(String titulo, String[] autores, String editora, double preco, double frete, int estoque) {
        super(titulo, autores, editora, preco);
        this.frete = frete;
        this.estoque = estoque;
    }



    public void atualizarEstoque() {
        if (estoque > 0) {
            estoque--;
        } else {
            System.out.println("Estoque zerado.");
        }
    }


    public double getFrete() {
        return frete;
    }

    public void setFrete(double frete) {
        this.frete = frete;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    @Override
    public String toString() {
        return super.toString() + ", Frete: R$" + frete + ", Estoque: " + estoque;
    }
}
