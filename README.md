# 📚 Livraria Virtual

Este projeto implementa um sistema de gerenciamento de uma livraria virtual na linguagem Java.

## Como Rodar 

- Na classe DatabaseConnection dentro do pacote DAO, altere o usuário e a senha do MySQL para os que você utiliza em sua máquina. Além disso, lembre-se de instalar o MySQL Connector no projeto.

## 🤝 Colaboradores

- Alice Lima Soares > Classe Livraria Virtual
- Danrley Silva de Jesus > Classe Impresso
- Landerson Evangelista Miranda > Classe Eletrônico
- Leandro de Almeida > Classe Venda, Banco de Dados
- Maria Eduarda Oliveira Sotério > Classe Livro
- Todos > Correções realizadas durante o desenvolvimento do projeto

## Metodologia

🗂 **Scrum** foi a metodologia utilizada durante o desenvolvimento deste projeto. Utilizamos Scrum para organizar e gerenciar as tarefas, permitindo uma abordagem iterativa e incremental no processo de desenvolvimento. A metodologia ajudou a garantir que o projeto evoluísse de forma estruturada e eficiente.


## 📝 Introdução

O sistema deve seguir o diagrama de classes UML fornecido, e implementar funcionalidades para cadastrar livros, realizar vendas e listar informações. 

## 🛠️ Descrição do Sistema

O sistema é baseado em um menu com as seguintes opções:

1. **Cadastrar livro**: Permite ao usuário cadastrar um livro.
2. **Realizar uma venda**: Permite ao usuário realizar a venda de um ou mais livros.
3. **Listar livros**: Lista todos os livros cadastrados, sejam eles eletrônicos ou impressos.
4. **Listar vendas**: Lista todas as vendas realizadas.
5. **Sair do programa**: Encerra a execução do programa.

## 🏛️ Descrição das Classes

### 1. Livro
A classe abstrata `Livro` possui os seguintes atributos:
- `titulo`: Título do livro.
- `autores`: Nome do autor ou autores do livro.
- `editora`: Nome da editora do livro.
- `preco`: Preço do livro.

Método obrigatório:
- `String toString()`: Representa textualmente os atributos de um livro.

### 2. Impresso
A classe `Impresso` representa um livro impresso e possui:
- `frete`: Frete cobrado para entrega do livro.
- `estoque`: Número de exemplares em estoque.

Métodos:
- `void atualizarEstoque()`: Subtrai 1 do valor do atributo `estoque`.
- `String toString()`: Representa textualmente os atributos de um livro impresso.

### 3. Eletronico
A classe `Eletronico` representa um livro eletrônico e possui:
- `tamanho`: Tamanho do arquivo eletrônico do livro em KB.

Método:
- `String toString()`: Representa textualmente os atributos de um livro eletrônico.

### 4. Venda
A classe `Venda` possui os seguintes atributos:
- `livros`: Vetor de referências a objetos do tipo `Livro`.
- `numVendas`: Atributo estático que representa a quantidade de vendas realizadas.
- `numero`: Número da venda.
- `cliente`: Nome do cliente.
- `valor`: Valor total da venda.

Métodos:
- `addLivro(Livro l, int index)`: Adiciona o livro `l` na posição `index` do vetor `livros`.
- `listarLivros()`: Lista todos os livros da venda.

### 5. LivrariaVirtual
A classe `LivrariaVirtual` possui os seguintes atributos:
- `MAX_IMPRESSOS`: Número máximo de livros impressos.
- `MAX_ELETRONICOS`: Número máximo de livros eletrônicos.
- `MAX_VENDAS`: Número máximo de vendas.
- `impressos`: Vetor de referências a objetos da classe `Impresso`.
- `eletronicos`: Vetor de referências a objetos da classe `Eletronico`.
- `vendas`: Vetor de referências a objetos da classe `Venda`.
- `numImpressos`: Número de livros impressos cadastrados.
- `numEletronicos`: Número de livros eletrônicos cadastrados.
- `numVendas`: Número de vendas realizadas.

Métodos:
- `cadastrarLivro()`: Permite cadastrar livros de diferentes tipos.
- `realizarVenda()`: Permite realizar uma venda e registrar o nome do cliente e os livros comprados.
- `listarLivrosImpressos()`: Exibe todos os livros impressos cadastrados.
- `listarLivrosEletronicos()`: Exibe todos os livros eletrônicos cadastrados.
- `listarLivros()`: Exibe todos os livros (impressos e eletrônicos).
- `listarVendas()`: Exibe todas as vendas realizadas.
- `main(String[] args)`: Instancia a classe `LivrariaVirtual` e exibe o menu de opções.



## 📄 Diagrama UML
Confira o diagrama UML incluído neste repositório para entender a estrutura e a relação entre as classes.

<p align="center">
<img src="https://github.com/user-attachments/assets/0d1e85a8-7340-40d1-9113-fb5730b19a60" alt="UMLLivrariaVirtual" width="600"/>
</p>


