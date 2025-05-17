module.exports = (sequelize, DataTypes) => {
  const TipoLixoAceitoEcoponto = sequelize.define('TipoLixoAceitoEcoponto', {
    pontos_kg: DataTypes.DOUBLE,
    img: DataTypes.STRING,
  }, {
    tableName: 'tb_tipo_lixo_aceito_ecoponto',
    timestamps: false,
  });

  TipoLixoAceitoEcoponto.associate = (models) => {
    TipoLixoAceitoEcoponto.belongsTo(models.TipoLixo, { foreignKey: 'id_tipo_lixo' });
    TipoLixoAceitoEcoponto.belongsTo(models.Ecoponto, { foreignKey: 'id_ecoponto' });
  };

  return TipoLixoAceitoEcoponto;
};