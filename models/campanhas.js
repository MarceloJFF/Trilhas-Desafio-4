module.exports = (sequelize, DataTypes) => {
  const Campanhas = sequelize.define('Campanhas', {
    data_criacao: DataTypes.DATE,
    descricao: DataTypes.TEXT,
    data_expiracao: DataTypes.DATE,
  }, {
    tableName: 'campanhas',
    timestamps: false,
  });

  Campanhas.associate = (models) => {
    Campanhas.belongsTo(models.Empresa, { foreignKey: 'id_empresa' });
  };

  return Campanhas;
};