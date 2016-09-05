package eu.usrv.NewHorizonsServerCore.modTrialKick.DBO;

import java.util.UUID;

import eu.usrv.NewHorizonsServerCore.NewHorizonsServerCore;

public class DBO_UUIDEntry_Helper
{
	private static final String SQL_QRY_UUIDENTRY_FROMUUID = "SELECT ID FROM uuidcache WHERE playerUUID = '%s';";
	private static final String SQL_QRY_UUIDENTRY_FROMNAME = "SELECT ID FROM uuidcache WHERE playerName = '%s';";
	private NewHorizonsServerCore _mMain = null;
	
	public DBO_UUIDEntry_Helper(NewHorizonsServerCore pCore)
	{
		_mMain = pCore;
	}
	
	/**
	 *  Try to find an User by UUID. Returns null if nothing is found
	 * 
	 * @param pPlayerUUID
	 * @return
	 */
	public DBO_UUIDEntry tryGetUUIDEntryFromUUID(UUID pPlayerUUID)
	{
		DBO_UUIDEntry tRet = null;
		int tDBResult = NewHorizonsServerCore.OfflineUUIDCache.getSimpleINT(String.format(SQL_QRY_UUIDENTRY_FROMUUID, pPlayerUUID.toString()), -1);
		if (tDBResult > -1)
			tRet = new DBO_UUIDEntry(tDBResult);

		return tRet;
	}
	
	/**
	 * Try to find an User by Name. Returns null if nothing is found
	 * 
	 * @param pPlayerName
	 * @return
	 */
	public DBO_UUIDEntry tryGetUUIDEntryFromName(String pPlayerName)
	{
		DBO_UUIDEntry tRet = null;
		int tDBResult = NewHorizonsServerCore.OfflineUUIDCache.getSimpleINT(String.format(SQL_QRY_UUIDENTRY_FROMNAME, pPlayerName), -1);
		if (tDBResult > -1)
			tRet = new DBO_UUIDEntry(tDBResult);

		return tRet;
	}
}
