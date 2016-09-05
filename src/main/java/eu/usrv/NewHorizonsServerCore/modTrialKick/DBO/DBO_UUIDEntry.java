package eu.usrv.NewHorizonsServerCore.modTrialKick.DBO;

import java.sql.ResultSet;
import java.util.UUID;

import eu.usrv.NewHorizonsServerCore.NewHorizonsServerCore;

/**
 * @author namikon
 *
 */
public class DBO_UUIDEntry
{
	public enum SQLFIELDS
	{
		ID,
		playerName,
		playerUUID
	}
	private static final String SQL_LOAD = "SELECT * FROM uuidcache WHERE ID = %d;";
	private static final String SQL_NEW = "INSERT INTO uuidcache (playerName, playerUUID) VALUES ('?', '?');";

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
			ResultSet tRes = NewHorizonsServerCore.OfflineUUIDCache.querySQL(String.format(SQL_LOAD, _mID));
			if (tRes != null)
			{
				_mUUID = UUID.fromString(tRes.getString(SQLFIELDS.playerUUID.ordinal()));
				_mName = tRes.getString(SQLFIELDS.playerName.ordinal());
			}
		}
		catch (Exception e)
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
	public DBO_UUIDEntry(int pDBID) throws Exception
	{
		_mID = pDBID;
		if (!loadData())
			throw new Exception("Unknown ID found. Nothing to load" + String.format("%d", pDBID));
	}
	
	/**
	 * Create a new Instance of us with an UUID and Playername. Will trigger a new dataset. Will do nothing if it already exists
	 * 
	 * @param pPlayerUUID
	 * @param pPlayerName
	 */
	public DBO_UUIDEntry(UUID pPlayerUUID, String pPlayerName)
	{
		_mUUID = pPlayerUUID;
		_mName = pPlayerName;
		
		saveDataSet();
	}
	
	private void saveDataSet()
	{
		
	}
	
}
