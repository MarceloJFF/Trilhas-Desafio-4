module.exports = (sequelize, DataTypes) => {
  const Endereco = sequelize.define('Endereco', {
    logradouro: DataTypes.STRING,
    bairro: DataTypes.STRING,
    complemento: DataTypes.STRING,
  }, {
    tableName: 'tb_endereco',
    timestamps: false,
  });

  Endereco.associate = (models) => {
    Endereco.hasMany(models.Usuario, { foreignKey: 'id_endereco' });
    Endereco.hasMany(models.Empresa, { foreignKey: 'id_endereco' });
  };

  return Endereco;
};