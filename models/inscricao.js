module.exports = (sequelize, DataTypes) => {
  const Inscricao = sequelize.define('Inscricao', {
    data: DataTypes.DATE,
  }, {
    tableName: 'tb_inscricao',
    timestamps: false,
  });

  Inscricao.associate = (models) => {
    Inscricao.belongsTo(models.Usuario, { foreignKey: 'id_usuario' });
    Inscricao.belongsTo(models.Eventos, { foreignKey: 'id_evento' });
  };

  return Inscricao;
};