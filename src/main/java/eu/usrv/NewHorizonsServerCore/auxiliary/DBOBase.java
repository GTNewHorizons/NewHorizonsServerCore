
package eu.usrv.NewHorizonsServerCore.auxiliary;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.UUID;

import eu.usrv.NewHorizonsServerCore.NewHorizonsServerCore;


public abstract class DBOBase
{
  private static final String SQLTEMPLATE_LOAD_SORTED = "SELECT * FROM %s WHERE %s = ? ORDER BY %s %s";
  private static final String SQLTEMPLATE_LOAD = "SELECT * FROM %s WHERE %s = ?";
  private String __sTableName;
  private String __sIDFieldName;

  private String __sPreparedSQLInsertStatement;
  private String __sPreparedSQLUpdateStatement;

  protected long __lID;
  private Hashtable<IDBO_SQLFields, String> __lstPropertyChanges = null;
  private IDBO_SQLFields __eFieldEnum;

  public DBOBase()
  {
    __lstPropertyChanges = new Hashtable<IDBO_SQLFields, String>();
  }

  public long getID()
  {
    return __lID;
  }

  /**
   * @return The load-command assembled for the current instance
   */
  protected final String getLoadCommand()
  {
    return getLoadCommand( "", false );
  }

  protected final String getLoadCommand( String pSortFieldName, boolean pAscending )
  {
    String tReturn = "";
    String tSortOrder = "ASC";
    if( !pAscending )
      tSortOrder = "DESC";

    if( pSortFieldName.length() == 0 )
      tReturn = String.format( SQLTEMPLATE_LOAD, __sTableName, __sIDFieldName, __lID );
    else
      tReturn = String.format( SQLTEMPLATE_LOAD_SORTED, __sTableName, __sIDFieldName, __lID, pSortFieldName, tSortOrder );

    return tReturn;
  }

  private void buildSQLStatements()
  {
    boolean tIsFirst = true;
    String tSQLNew_Body = "INSERT INTO %s (%s) VALUES (%s)";
    String tSQLNew_Fields = "";
    String tSQLNew_Values = "";

    for( IDBO_SQLFields tField : __eFieldEnum.getValues() )
    {
      if( !tIsFirst )
      {
        tSQLNew_Fields += ", ";
        tSQLNew_Values += ", ";
      }
      else
        tIsFirst = false;

      tSQLNew_Fields += tField.getSQLFieldName();
      tSQLNew_Values += "?";
    }

    __sPreparedSQLInsertStatement = String.format( tSQLNew_Body, tSQLNew_Fields, tSQLNew_Values );
  }

  protected <E extends IDBO_SQLFields> void _Init( String pTableName, String pIDFieldName, E pFieldEnum )
  {
    __sTableName = pTableName;
    __sIDFieldName = pIDFieldName;
    __eFieldEnum = pFieldEnum;
    buildSQLStatements();
  }

  protected void __ClearPendingChanges()
  {
    __lstPropertyChanges = new Hashtable<IDBO_SQLFields, String>();
  }

  public boolean hasPendingChanges()
  {
    return __lstPropertyChanges.isEmpty() ? false : true;
  }

  protected void __PropertyChanged( IDBO_SQLFields pSQLField, String pNewValue )
  {
    __PropertyChanged( pSQLField, pNewValue, false, true );
  }

  protected void __PropertyChanged( IDBO_SQLFields pSQLField, String pNewValue, boolean pForcePlainText )
  {
    __PropertyChanged( pSQLField, pNewValue, pForcePlainText, true );
  }

  protected void __PropertyChanged( IDBO_SQLFields pSQLField, String pNewValue, boolean pForcePlainText, boolean pForceString )
  {
    String lVal = "";
    if( pForcePlainText )
      lVal = pNewValue;
    else
      lVal = pNewValue;
    // lVal = escapeValueForSQL( pNewValue, pForceString );

    __lstPropertyChanges.put( pSQLField, lVal );
  }

  protected abstract boolean doPresaveActions();

  protected abstract void doPostSaveActions( boolean pSaveSuccessfull );

  private void setProperty( String fieldName, String value ) throws NoSuchFieldException, IllegalAccessException
  {
    Field field = this.getClass().getDeclaredField( fieldName );
    if( field.getType() == Character.TYPE )
    {
      field.set( getClass(), value.charAt( 0 ) );
      return;
    }
    if( field.getType() == Short.TYPE )
    {
      field.set( getClass(), Short.parseShort( value ) );
      return;
    }
    if( field.getType() == Integer.TYPE )
    {
      field.set( getClass(), Integer.parseInt( value ) );
      return;
    }
    if( field.getType() == Long.TYPE )
    {
      field.set( getClass(), Long.parseLong( value ) );
      return;
    }
    if( field.getType() == Float.TYPE )
    {
      field.set( getClass(), Float.parseFloat( value ) );
      return;
    }
    if( field.getType() == Double.TYPE )
    {
      field.set( getClass(), Double.parseDouble( value ) );
      return;
    }
    if( field.getType() == Byte.TYPE )
    {
      field.set( getClass(), Byte.parseByte( value ) );
      return;
    }
    if( field.getType() == Boolean.TYPE )
    {
      field.set( getClass(), Boolean.parseBoolean( value ) );
      return;
    }

    if( UUID.class.isInstance( field ) )
    {
      field.set( getClass(), UUID.fromString( value ) );
    }
    field.set( getClass(), value );
  }

  public boolean doLoad()
  {
    boolean tRet = false;
    try
    {
      ResultSet tRes = null;//NewHorizonsServerCore.OfflineUUIDCache.querySQL( getLoadCommand() );

      if( tRes != null )
      {
        for( IDBO_SQLFields tField : __eFieldEnum.getValues() )
        {
          if( tField.isReadOnlyProperty() )
            continue;

          setProperty( tField.getPropertyName(), tRes.getString( tField.getOrdinal() ) );
          tRet = true;
        }
      }
    }
    catch( Exception e )
    {
      NewHorizonsServerCore.logger.severe( "Database Query failed:" );
      e.printStackTrace();
    }

    return tRet;
  }

  public boolean doSave()
  {
    return false;

    // boolean tIsNewElement = false;
    // if( __lID == -1 )
    // tIsNewElement = true;
    //
    // if( doPresaveActions() == false )
    // return false;
    //
    // if( __lstPropertyChanges.isEmpty() )
    // return true;
    //
    // String tSQL = "";
    //
    // if( tIsNewElement )
    // tSQL += String.format( "INSERT INTO {0} SET ", __sTableName );
    // else
    // tSQL += String.format( "UPDATE {0} SET ", __sTableName );
    //
    // boolean tIsFirst = true;
    //
    // Set<IDBO_SQLFields> tKeys = __lstPropertyChanges.keySet();
    // for( IDBO_SQLFields tKey : tKeys )
    // {
    // if( !tIsFirst )
    // tSQL += ", ";
    // else
    // tIsFirst = false;
    //
    // tSQL += String.format( "%s = ?", tKey );
    //
    // if( tKey.getDataType() == String.class )
    // {}
    // else
    // if( tKey.getDataType() == Integer.class )
    // {}
    // else
    // if( tKey.getDataType() == Long.class )
    // {}
    // else
    // if( tKey.getDataType() == Date.class )
    // {}
    // }
  }
}
