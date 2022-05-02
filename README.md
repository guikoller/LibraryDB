# LibraryDB

Baseado nos dois arquivos postados na página da disciplina (Livros-db.csv e Livros-db.zip), criar um banco de dados chamado livrosdb, carregar o csv para uma tabela chamada livros e inserir as páginas.

A criação do banco de dados e importação do CSV deverão ser feitas em comandos de linha.

A inserção das páginas deverá ser feita via um programa Java.

Após o carregamento e inserção, criar uma tabela auxiliar chamada autores, e colocar nela os autores de cada livro, relacionando com uma chave estrangeira. A criação de tabela deverá ser feita via comando de linha, e a colocação dos autores de cada livro via um programa Java.

### executar o seguinte comando para criar o banco de dados, a tabela books inserir os dados carregados do arquivo .csv
```bash
	sudo mysql --local-infile=1 -u root -p1 < books.sql
```

### execute para criar a tabela authors
```bash
	sudo mysql < authors.sql
```

### execute o programa java para carregar os aquivos blobs e preencher a tabela de autores
