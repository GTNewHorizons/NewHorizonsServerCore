
package eu.usrv.NewHorizonsServerCore.modTrialKick.DBO;

import java.sql.ResultSet;
import java.util.UUID;

import eu.usrv.NewHorizonsServerCore.NewHorizonsServerCore;
import eu.usrv.NewHorizonsServerCore.auxiliary.DBOBase;


/**
 * @author namikon
 * 
 */
public class DBO_UUIDEntry extends DBOBase
{
  public enum SQLFIELDS implements IDBO_SQLFields 
  {
    ID(Integer.class, true),
    playerName(Integer.class),
    playerUUID(String.class);
    
    private String _mFieldName;
    private String _mPropertyName;
    private Class<?> _mDataType;
    private boolean _mIsReadonly;
    
    @Override
    public String getSQLFieldName()
    {
      if (_mFieldName == "")
        return this.name();
      else
        return _mFieldName;
    }

    @Override
    public IDBO_SQLFields[] getValues()
    {
      return SQLFIELDS.values();
    }
    
    @Override
    public boolean isReadOnlyProperty()
    {
      return _mIsReadonly;
    }
    
    @Override
    public Class<?> getDataType()
    {
      return _mDataType;
    }

    private SQLFIELDS(Class<?> pDataType, String pPropertyName)
    {
      this("", pDataType, false);
    }
    
    private SQLFIELDS(Class<?> pDataType, boolean pIsReadOnly)
    {
      this("", pDataType, pIsReadOnly);
    }
    
    private SQLFIELDS(String pFieldName, Class<?> pDataType, boolean pIsReadOnly)
    {
      _mFieldName = pFieldName;
      _mDataType = pDataType;
    }

    @Override
    public String getPropertyName()
    {
      // TODO Auto-generated method stub
      return null;
    }
  }

  private String _mName;
  private UUID _mUUID;
  private int _mID;

  public int getID()
  {
    return _mID;
  }

  public UUID getUUID()
  {
    return _mUUID;
  }

  public String getName()
  {
    return _mName;
  }

  /**
   * Load ze data from ze database
   * 
   * @return
   */
  private boolean loadData()
  {
    boolean tRet = false;

    try
    {
      ResultSet tRes = NewHorizonsServerCore.OfflineUUIDCache.querySQL( String.format( getLoadCommand(), _mID ) );
      if( tRes != null )
      {
        _mUUID = UUID.fromString( tRes.getString( SQLFIELDS.playerUUID.ordinal() ) );
        _mName = tRes.getString( SQLFIELDS.playerName.ordinal() );
      }
    }
    catch( Exception e )
    {

    }

    return tRet;
  }

  /**
   * Instantiate our class with a DB ID. Triggers load
   * 
   * @param pDBID
   * @throws Exception
   */
  public DBO_UUIDEntry( int pDBID ) throws Exception
  {
    _mID = pDBID;
    if( !loadData() )
      throw new Exception( "Unknown ID found. Nothing to load" + String.format( "%d", pDBID ) );
  }

  /**
   * Create a new Instance of us with an UUID and Playername. Will trigger a new dataset. Will do nothing if it already
   * exists
   * 
   * @param pPlayerUUID
   * @param pPlayerName
   */
  public DBO_UUIDEntry( UUID pPlayerUUID, String pPlayerName )
  {
    _mUUID = pPlayerUUID;
    _mName = pPlayerName;

    saveDataSet();
  }

  private void saveDataSet()
  {

  }

  @Override
  protected boolean doPresaveActions()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  protected void doPostSaveActions( boolean pSaveSuccessfull )
  {
    // TODO Auto-generated method stub
    
  }
}
