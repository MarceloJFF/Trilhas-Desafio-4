import UserModel from '../models/userModel.js';

const UserController = {
  // Função para cadastro de um novo usuário
  register: (req, res) => {
    const { nome, email, senha } = req.body;

    // Verificar se o nome, email e senha estão presentes
    if (!nome || !email || !senha) {
      return res.status(400).json({ error: 'Todos os campos são obrigatórios' });
    }

    // Verificar se o email já está cadastrado
    UserModel.findByEmail(email, (err, result) => {
      if (err) {
        return res.status(500).json({ error: 'Erro ao verificar o email' });
      }
      if (result.length > 0) {
        return res.status(400).json({ error: 'Email já registrado' });
      }

      // Criar o usuário no banco de dados
      UserModel.create(nome, email, senha, (err, result) => {
        if (err) {
          return res.status(500).json({ error: 'Erro ao cadastrar usuário' });
        }
        res.status(201).json({ message: 'Usuário cadastrado com sucesso!' });
      });
    });
  }
};

export default UserController;
