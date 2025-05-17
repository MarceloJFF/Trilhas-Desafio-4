module.exports = (sequelize, DataTypes) => {
  const Beneficios = sequelize.define('Beneficios', {
    descricao: DataTypes.BIGINT,
    qtd_pontos_necessarios: DataTypes.BIGINT,
  }, {
    tableName: 'tb_beneficios',
    timestamps: false,
  });

  Beneficios.associate = (models) => {
    Beneficios.belongsTo(models.Empresa, { foreignKey: 'id_empresa' });
    Beneficios.hasMany(models.TrocaBeneficio, { foreignKey: 'id_beneficio' });
  };

  return Beneficios;
};