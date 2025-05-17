module.exports = (sequelize, DataTypes) => {
  const Empresa = sequelize.define('Empresa', {
    nome: DataTypes.STRING,
    endereco: DataTypes.BIGINT,
    contato: DataTypes.STRING,
    latitude: DataTypes.DECIMAL(8, 2),
    longitude: DataTypes.DECIMAL(8, 2),
    cpnj: DataTypes.STRING,
  }, {
    tableName: 'tb_empresa',
    timestamps: false,
  });

  Empresa.associate = (models) => {
    Empresa.belongsTo(models.Acesso, { foreignKey: 'id_acesso' });
    Empresa.belongsTo(models.Endereco, { foreignKey: 'id_endereco' });
    Empresa.hasMany(models.Eventos, { foreignKey: 'id_empresa' });
    Empresa.hasMany(models.Beneficios, { foreignKey: 'id_empresa' });
    Empresa.hasMany(models.Campanhas, { foreignKey: 'id_empresa' });
    Empresa.hasMany(models.EntregaMaterial, { foreignKey: 'id_empresa' });
    Empresa.hasMany(models.TipoEmpresaBeneficio, { foreignKey: 'id_empresa' });
    Empresa.hasMany(models.TrocaBeneficio, { foreignKey: 'id_empresa' });
  };

  return Empresa;
};