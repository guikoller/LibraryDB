use livrosdb;

CREATE TABLE authors (
	book_id int NOT NULL,
	name varchar(100),
	FOREIGN KEY (book_id) REFERENCES books(book_id)
);
