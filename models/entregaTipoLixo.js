module.exports = (sequelize, DataTypes) => {
  const EntregaTipoLixo = sequelize.define('EntregaTipoLixo', {
    kg: DataTypes.DECIMAL(8, 2),
  }, {
    tableName: 'tb_entrega_tipo_lixo',
    timestamps: false,
  });

  EntregaTipoLixo.associate = (models) => {
    EntregaTipoLixo.belongsTo(models.TipoLixo, { foreignKey: 'id_tipo__lixo' });
    EntregaTipoLixo.belongsTo(models.EntregaMaterial, { foreignKey: 'id_entrega_material' });
  };

  return EntregaTipoLixo;
};