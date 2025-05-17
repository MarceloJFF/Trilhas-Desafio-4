module.exports = (sequelize, DataTypes) => {
  const Usuario = sequelize.define('Usuario', {
    primeirio_nome: DataTypes.STRING,
    ultimo_nome: DataTypes.STRING,
    is_admin: DataTypes.BOOLEAN,
    cpf: DataTypes.STRING,
    saldo_pontos: DataTypes.BIGINT,
  }, {
    tableName: 'tb_usuario',
    timestamps: false,
  });

  Usuario.associate = (models) => {
    Usuario.belongsTo(models.Acesso, { foreignKey: 'id_acesso' });
    Usuario.belongsTo(models.Endereco, { foreignKey: 'id_endereco' });
    Usuario.hasMany(models.Ecoponto, { foreignKey: 'id_usuario' });
    Usuario.hasMany(models.EntregaMaterial, { foreignKey: 'id_user' });
    Usuario.hasMany(models.TrocaBeneficio, { foreignKey: 'id_usuario' });
    Usuario.hasMany(models.Inscricao, { foreignKey: 'id_usuario' });
  };

  return Usuario;
};