// Importação do modelo de usuario existente (adaptando para ESM)
import { sequelize, Sequelize } from './index.js';

// Modelo de usuário simplificado
const UserModel = {
  findByEmail: (email, callback) => {
    // Implementação temporária
    callback(null, []);
  },
  create: (nome, email, senha, callback) => {
    // Implementação temporária
    callback(null, { id: 1 });
  }
};

export default UserModel; 