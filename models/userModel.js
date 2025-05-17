import bcrypt from 'bcryptjs';
import DB from '../config/db.js';

const UserModel = {
  // Função para buscar um usuário pelo email
  findByEmail: (email, callback) => {
    const query = 'SELECT * FROM usuarios WHERE email = ?';
    DB.query(query, [email], callback);
  },

  // Função para criar um novo usuário
  create: (nome, email, senha, callback) => {
    bcrypt.hash(senha, 10, (err, hashedPassword) => {
      if (err) return callback(err);
      const query = 'INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)';
      DB.query(query, [nome, email, hashedPassword], callback);
    });
  }
};

export default UserModel;
