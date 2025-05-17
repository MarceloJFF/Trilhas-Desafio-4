import express from 'express';
import UserController from '../controllers/userController.js';

const router = express.Router();

// Rota de cadastro com Handlebars
router.get('/cadastro', (req, res) => {
    res.render('cadastro', { title: 'Cadastro de Usuário' });
});

router.post('/cadastro', UserController.register);

export default router;
