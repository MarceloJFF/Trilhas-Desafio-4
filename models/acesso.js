module.exports = (sequelize, DataTypes) => {
  const Acesso = sequelize.define('Acesso', {
    login: DataTypes.STRING,
    password: DataTypes.STRING,
  }, {
    tableName: 'tb_acesso',
    timestamps: false,
  });

  Acesso.associate = (models) => {
    Acesso.hasMany(models.Usuario, { foreignKey: 'id_acesso' });
    Acesso.hasMany(models.Empresa, { foreignKey: 'id_acesso' });
  };

  return Acesso;
};