# LibraryDB

Baseado nos dois arquivos postados na página da disciplina (Livros-db.csv e Livros-db.zip), criar um banco de dados chamado livrosdb, carregar o csv para uma tabela chamada livros e inserir as páginas.

A criação do banco de dados e importação do CSV deverão ser feitas em comandos de linha.

A inserção das páginas deverá ser feita via um programa Java.

Após o carregamento e inserção, criar uma tabela auxiliar chamada autores, e colocar nela os autores de cada livro, relacionando com uma chave estrangeira. A criação de tabela deverá ser feita via comando de linha, e a colocação dos autores de cada livro via um programa Java.

## Relatório Introdução a Banco de Dados
## Guilherme Corrêa Koller
### Para criar o banco de dados e a tabela de livros foi criado o seguinte script sql:

```code
create database livrosdb;

use livrosdb;

CREATE TABLE books (
	book_id int PRIMARY KEY, 
	title varchar(150), 
	author varchar(100),
	edition int,
	year YEAR,
	publisher varchar(100)
);

LOAD DATA LOCAL INFILE 'livros-db.csv' INTO TABLE books

FIELDS TERMINATED BY ','

ENCLOSED BY '"'

LINES TERMINATED BY '\n'

IGNORE 1 ROWS;

alter table books add column cover longblob;
```

### Na qual irá criar o banco de dados, criar a tabela books, carregar os arquivos .csv utilizando o comando `LOAD LOCAL DATA` e após isso iria modificar a tabela livros e inserir a coluna cover.

### Para executar o script usar o seguinte comando:
```terminal
	$ sudo mysql --local-infile=1 -u root -p1 < books.sql
```

### Para a tabela de autores foi criado outro scirpt sql, na qual possui a função de criar a tabela authors e definir a relação do `book_id` como `foreign key`.
```code
use livrosdb;

CREATE TABLE authors (
	book_id int NOT NULL,
	name varchar(100),
	FOREIGN KEY (book_id) REFERENCES books(book_id)
);
```
### Executar este código utilizando o seguinte comando:
```terminal
	$ sudo mysql < authors.sql
```

### Para o desenvolvimento do programa foi criada uma coneção ao banco de dados MySQL Server utilizando as credenciais corretas. Para inserir as capas utilizou-se uma `preparedStatement` que será executada diversas vezes dentro do lação de repetição, que contém um buffer que irá ler o documento certo no direório correto e o inserir nos parâmentros do `update`.

```code
Connection conn;
conn = DriverManager.getConnection("jdbc:mysql://localhost/livrosdb", "koller", "password");

Statement stmt;
stmt = conn.createStatement();

PreparedStatement  insertCover;
insertCover = conn.prepareStatement("update books set cover = ? where book_id = ?");

ResultSet rs;
rs = stmt.executeQuery("Select book_id from books");

System.out.println("Inserting covers");
while(rs.next()){
    System.out.println(rs.getInt(1));
    
    //Loading BLOB
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    FileInputStream file;
    file = new FileInputStream("./livros-db/"+rs.getInt(1)+".jpg");
    int readByte = file.read();
    while(readByte != -1){
        buffer.write(readByte);
        readByte = file.read();
    }
    
    //updating bookcover
    insertCover.setInt(2, rs.getInt(1));
    insertCover.setBytes(1, buffer.toByteArray());
    insertCover.executeUpdate();
    
}
```
### Para a injeção dos autores foi utilizada a mesma ideia do código passado, porém como alguns livros possuem mais de um autor, foi necessário o uso da função `split()` que irá dividir uma string em um array de string que contém uma chave específica. Neste caso, foi utilizado a chave `','` e após isso usou-se a função `trim()` para remover eventuais espaços em branco no início e no fim da String.
```code
PreparedStatement  insertAuthor;
insertAuthor = conn.prepareStatement("insert into authors (book_id, name) values(?, ?)");

rs = stmt.executeQuery("Select book_id, author from books");

while(rs.next()){                
    String data = rs.getString(2);
    String[] split = data.split(",");
    for (int i=0; i < split.length; i++){
        split[i] = split[i].trim();
        System.out.println(split[i]);
        insertAuthor.setInt(1, rs.getInt(1));
        insertAuthor.setString(2, split[i]);
        insertAuthor.executeUpdate();
    }    
}
```
