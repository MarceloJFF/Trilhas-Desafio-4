module.exports = (sequelize, DataTypes) => {
  const TipoEmpresaBeneficio = sequelize.define('TipoEmpresaBeneficio', {
    descricao: DataTypes.STRING,
  }, {
    tableName: 'tb_tipo_empresa_beneficio',
    timestamps: false,
  });

  TipoEmpresaBeneficio.associate = (models) => {
    TipoEmpresaBeneficio.belongsTo(models.Empresa, { foreignKey: 'id_empresa' });
  };

  return TipoEmpresaBeneficio;
};