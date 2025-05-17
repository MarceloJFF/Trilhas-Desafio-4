module.exports = (sequelize, DataTypes) => {
  const EntregaMaterial = sequelize.define('EntregaMaterial', {
    data_entrega: DataTypes.DATE,
    qtd_pontos_gerados: DataTypes.BIGINT,
    valor_total_gerado: DataTypes.DECIMAL(10, 2),
  }, {
    tableName: 'tb_entrega_material',
    timestamps: false,
  });

  EntregaMaterial.associate = (models) => {
    EntregaMaterial.belongsTo(models.Usuario, { foreignKey: 'id_user' });
    EntregaMaterial.belongsTo(models.Ecoponto, { foreignKey: 'id_ecoponto' });
    EntregaMaterial.belongsTo(models.Empresa, { foreignKey: 'id_empresa' });
    EntregaMaterial.hasMany(models.EntregaTipoLixo, { foreignKey: 'id_entrega_material' });
  };

  return EntregaMaterial;
};