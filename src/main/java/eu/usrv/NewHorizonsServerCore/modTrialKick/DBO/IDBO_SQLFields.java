
package eu.usrv.NewHorizonsServerCore.modTrialKick.DBO;

public interface IDBO_SQLFields
{
  String getSQLFieldName();

  String getPropertyName();

  // Class<?> getDataType();
  boolean isReadOnlyProperty();

  IDBO_SQLFields[] getValues();

  int getOrdinal();
}
