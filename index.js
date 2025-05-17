import express from 'express';
import dotenv from 'dotenv';
import publicRoutes from './routes/public.js';
import exphbs from 'express-handlebars';

dotenv.config(); // Carrega as variáveis de ambiente

const app = express();

// Configuração do Handlebars
app.engine('handlebars', exphbs());
app.set('view engine', 'handlebars');

// Configurações do servidor
app.use(express.json());
app.use(express.urlencoded({ extended: true })); // Para enviar dados em formulários
app.use('/', publicRoutes); // Roteamento das rotas públicas

// Rota inicial de exemplo, usando Handlebars
app.get('/', (req, res) => {
    res.render('home', { message: 'Bem-vindo ao Eco Ponto!' });
});

// Iniciar o servidor
app.listen(3000, () => {
  console.log('Servidor rodando na porta 3000');
});
