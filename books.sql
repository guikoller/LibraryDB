create database library;

use library;

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
