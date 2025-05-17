import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import { Sequelize } from 'sequelize';
import dotenv from 'dotenv';

dotenv.config(); // Carrega as variáveis de ambiente

// Obter o caminho do arquivo atual com ESM
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const basename = path.basename(__filename);
const db = {};

// Configuração do banco de dados MariaDB - use suas próprias credenciais
// Altere essas credenciais conforme necessário para o seu ambiente
const dbConfig = {
  host: process.env.DB_HOST || 'localhost',
  user: process.env.DB_USER || 'root',
  password: process.env.DB_PASSWORD || '',
  database: process.env.DB_NAME || 'eco_ponto',
  port: process.env.DB_PORT || 3306
};

// Configuração para MariaDB
const sequelize = new Sequelize(dbConfig.database, dbConfig.user, dbConfig.password, {
  host: dbConfig.host,
  dialect: 'mariadb',
  port: dbConfig.port
});

// Teste simples de conexão - comentado para evitar erros durante o startup
// Descomente quando tiver configurado corretamente suas credenciais
/*
sequelize.authenticate()
  .then(() => console.log('Conexão com o banco de dados estabelecida com sucesso.'))
  .catch(err => console.error('Não foi possível conectar ao banco de dados:', err));
*/

// Não é possível usar require em ESM, então essa parte precisa ser adaptada
// para ESM quando houver necessidade de carregar os modelos dinamicamente

db.sequelize = sequelize;
db.Sequelize = Sequelize;

export { sequelize, Sequelize };
export default db;
