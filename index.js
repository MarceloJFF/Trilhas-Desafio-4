import express from 'express';
import dotenv from 'dotenv';
import publicRoutes from './routes/public.js';
import pkg from 'express-handlebars';
import path from 'path';
import { fileURLToPath } from 'url';

const { engine } = pkg;

// Configuração de __dirname para ESM
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

dotenv.config(); // Carrega as variáveis de ambiente

const app = express();
// Fixando a porta em 3001 para evitar conflitos com MariaDB
const PORT = 3001; 

// Configuração do Handlebars
app.engine('handlebars', engine({
  defaultLayout: 'main',
  layoutsDir: path.join(__dirname, 'views/layouts')
}));
app.set('view engine', 'handlebars');
app.set('views', path.join(__dirname, 'views'));

// Configurações do servidor
app.use(express.json());
app.use(express.urlencoded({ extended: true })); // Para enviar dados em formulários
app.use(express.static(path.join(__dirname, 'public'))); // Arquivos estáticos
app.use('/', publicRoutes); // Roteamento das rotas públicas

// Rota inicial de exemplo, usando Handlebars
app.get('/', (req, res) => {
    res.render('home', { 
      title: 'Página Inicial',
      message: 'Bem-vindo ao Eco Ponto!' 
    });
});

// Iniciar o servidor
app.listen(PORT, () => {
  console.log(`Servidor rodando na porta ${PORT}`);
});
