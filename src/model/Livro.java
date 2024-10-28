package model;

import java.util.Arrays;

public abstract class Livro {

        protected String titulo;
        protected String[] autores;
        protected String editora;
        protected double preco;

        public Livro(String titulo, String[] autores, String editora, double preco) {
            this.titulo = titulo;
            this.autores = autores;
            this.editora = editora;
            this.preco = preco;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String[] getAutores() {
            return autores;
        }

        public void setAutores(String[] autores) {
            this.autores = autores;
        }

        public String getEditora() {
            return editora;
        }

        public void setEditora(String editora) {
            this.editora = editora;
        }

        public double getPreco() {
            return preco;
        }

        public void setPreco(float preco) {
            this.preco = preco;
        }

        @Override
        public String toString() {
            String autoresStr = Arrays.toString(autores);
            return "Livro: " +
                    "titulo='" + titulo + '\'' +
                    ", autores='" +  autoresStr +
                    ", editora='" + editora + '\'' +
                    ", preco=" + preco +
                    '.';
        }
    }

