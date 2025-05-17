module.exports = (sequelize, DataTypes) => {
  const TipoLixo = sequelize.define('TipoLixo', {
    descricao: DataTypes.STRING,
  }, {
    tableName: 'tb_tipo_lixo',
    timestamps: false,
  });

  TipoLixo.associate = (models) => {
    TipoLixo.hasMany(models.TipoLixoAceitoEcoponto, { foreignKey: 'id_tipo_lixo' });
    TipoLixo.hasMany(models.EntregaTipoLixo, { foreignKey: 'id_tipo__lixo' });
  };

  return TipoLixo;
};