module.exports = (sequelize, DataTypes) => {
  const Eventos = sequelize.define('Eventos', {
    descricao: DataTypes.STRING,
    created_at: DataTypes.DATE,
    endereco: DataTypes.BIGINT,
    obs: DataTypes.TEXT,
    data_evento: DataTypes.DATE,
    horario_inicio_funcionamento: DataTypes.TIME,
    horario_termino_funcionamento: DataTypes.TIME,
    beneficios_disponiveis: DataTypes.BIGINT,
    foto: DataTypes.STRING,
  }, {
    tableName: 'tb_eventos',
    timestamps: false,
  });

  Eventos.associate = (models) => {
    Eventos.belongsTo(models.Empresa, { foreignKey: 'id_empresa' });
    Eventos.hasMany(models.Inscricao, { foreignKey: 'id_evento' });
  };

  return Eventos;
};