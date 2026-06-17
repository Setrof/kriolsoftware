--script para criares suas tabelas limpinha cada maquina com a sua base dados 
CREATE TABLE IF NOT EXISTS Livro (
                                     isbn TEXT PRIMARY KEY,
                                     titulo TEXT NOT NULL,
                                     autor TEXT NOT NULL,
                                     Genero TEXT,
                                     editora TEXT NOT NULL,
                                     anoPublicacao INTEGER NOT NULL,
                                     emprestado INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS Utilizador (
                                          id INTEGER PRIMARY KEY,
                                          nome TEXT NOT NULL,
                                          morada TEXT NOT NULL,
                                          CNI TEXT NOT NULL,
                                          email TEXT,
                                          telefone TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Emprestimo (
                                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                                          livro_isbn TEXT NOT NULL,
                                          utilizador_id INTEGER NOT NULL,
                                          dataEmprestimo TEXT NOT NULL,
                                          dataDevolucaoPrevista TEXT NOT NULL,
                                          dataDevolucaoReal TEXT,
                                          FOREIGN KEY (livro_isbn) REFERENCES Livro(isbn),
                                          FOREIGN KEY (utilizador_id) REFERENCES Utilizador(id)
);