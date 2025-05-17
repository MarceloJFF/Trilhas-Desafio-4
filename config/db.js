import mysql from 'mysql2';

// Criar a conexão com o banco de dados
const DB = mysql.createConnection({
  host: 'localhost',          // O servidor do MySQL está rodando no localhost 
  user: 'root',               // Usuário padrão do XAMPP (root)
  password: '12345',               // Senha padrão do XAMPP 
  database: 'eco_ponto'       // Nome do banco de dados criado no PhpMyAdmin
});

// Verificar a conexão
DB.connect((err) => {
  if (err) {
    console.error('Erro ao conectar ao banco de dados:', err);
  } else {
    console.log('Conectado ao banco de dados MySQL');
  }
});

export default DB;


// CREATE TABLE usuarios (
  //id INT AUTO_INCREMENT PRIMARY KEY,
  //nome VARCHAR(255) NOT NULL,
  //email VARCHAR(255) NOT NULL UNIQUE,
  //senha VARCHAR(255) NOT NULL
//);
