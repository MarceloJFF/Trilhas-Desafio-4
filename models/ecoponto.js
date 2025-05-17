module.exports = (sequelize, DataTypes) => {
  const Ecoponto = sequelize.define('Ecoponto', {
    descricao: DataTypes.STRING,
    latitude: DataTypes.DECIMAL(8, 2),
    longitude: DataTypes.DECIMAL(8, 2),
    cep: DataTypes.STRING,
    aceita_lixo_eletronico: DataTypes.BOOLEAN,
  }, {
    tableName: 'tb_ecoponto',
    timestamps: false,
  });

  Ecoponto.associate = (models) => {
    Ecoponto.belongsTo(models.Usuario, { foreignKey: 'id_usuario' });
    Ecoponto.hasMany(models.TipoLixoAceitoEcoponto, { foreignKey: 'id_ecoponto' });
    Ecoponto.hasMany(models.EntregaMaterial, { foreignKey: 'id_ecoponto' });
  };

  return Ecoponto;
};