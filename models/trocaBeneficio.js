module.exports = (sequelize, DataTypes) => {
  const TrocaBeneficio = sequelize.define('TrocaBeneficio', {
    qtd_pontos_consumidos: DataTypes.BIGINT,
  }, {
    tableName: 'tb_troca_beneficio',
    timestamps: false,
  });

  TrocaBeneficio.associate = (models) => {
    TrocaBeneficio.belongsTo(models.Usuario, { foreignKey: 'id_usuario' });
    TrocaBeneficio.belongsTo(models.Beneficios, { foreignKey: 'id_beneficio' });
    TrocaBeneficio.belongsTo(models.Empresa, { foreignKey: 'id_empresa' });
  };

  return TrocaBeneficio;
};