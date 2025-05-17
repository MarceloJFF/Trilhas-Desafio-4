import express from 'express';
import UserController from '../controllers/userController.js';

const router = express.Router();

// Rota principal
router.get('/', (req, res) => {
    res.render('index', { 
        title: 'Página Inicial',
        message: 'Bem-vindo ao Eco Ponto!',
        pageStyle: 'index'
    });
});

// Rota para a página home
router.get('/home', (req, res) => {
    res.render('home', { 
        title: 'Home',
        message: 'Bem-vindo ao Eco Ponto!',
        pageStyle: 'home'
    });
});

// Rota de cadastro com Handlebars
router.get('/cadastro', (req, res) => {
    res.render('cadastro', { 
        title: 'Cadastro de Usuário',
        pageStyle: 'cadastro'
    });
});

// Rota para Ecopontos Próximos
router.get('/ecopontos', (req, res) => {
    res.render('ecopontoProximo', { 
        title: 'Ecopontos Próximos',
        pageStyle: 'ecopontoProximo'
    });
});

// Rota para resultado de busca de Ecopontos
router.get('/ecopontos/resultado', (req, res) => {
    res.render('resultadoEcopontoProximo', { 
        title: 'Resultado da Busca',
        pageStyle: 'resultadoEcopontoProximo'
    });
});

// Rota para Como Reciclar
router.get('/como-reciclar', (req, res) => {
    res.render('comoReciclar', { 
        title: 'Como Reciclar',
        pageStyle: 'comoReciclar'
    });
});

// Rota para Login de Cliente
router.get('/login', (req, res) => {
    res.render('clienteLogin', { 
        title: 'Login',
        pageStyle: 'clienteLogin'
    });
});

// Rota para Login de Empresa
router.get('/login-empresa', (req, res) => {
    res.render('loginEmpresa', { 
        title: 'Login de Empresa',
        pageStyle: 'loginEmpresa'
    });
});

// Rota para Cadastro de Empresa
router.get('/cadastrar-empresa', (req, res) => {
    res.render('cadastrarEmpresa', { 
        title: 'Cadastro de Empresa',
        pageStyle: 'cadastrarEmpresa'
    });
});

// Rota para Recompensa Verde
router.get('/recompensa-verde', (req, res) => {
    res.render('recompensaVerde', { 
        title: 'Recompensa Verde',
        pageStyle: 'recompensaVerde'
    });
});

// Rota para Inscrever em Ação
router.get('/inscrever-acao', (req, res) => {
    res.render('inscreverEmAcao', { 
        title: 'Inscrever em Ação',
        pageStyle: 'inscreverEmAcao'
    });
});

// Rota para resumo da inscrição em ação
router.get('/resumo-inscricao', (req, res) => {
    res.render('resumoDaInscricaoEmAcao', { 
        title: 'Resumo da Inscrição',
        pageStyle: 'resumoDaInscricaoEmAcao'
    });
});

// Rota para empresa cadastrar ação
router.get('/empresa/cadastrar-acao', (req, res) => {
    res.render('empresaCadastrarAcao', { 
        title: 'Cadastrar Ação',
        pageStyle: 'empresaCadastrarAcao'
    });
});

// Rota de processamento do cadastro
router.post('/cadastro', UserController.register);

export default router;
