
package eu.usrv.NewHorizonsServerCore.modTrialKick;


import java.util.UUID;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import eu.usrv.NewHorizonsServerCore.NewHorizonsServerCore;


/**
 * Simple TrialKick function. Grants a certain amount of Deaths for any player
 * before they have to apply for a permanent slot on the Server
 * 
 * @author Namikon
 *
 */
public class TrialKick implements Listener
{
  public static final String GTNH_PERM_FULL_MEMBER = "GTNH.FullMember";
  private static final String TEMPLATE_PLAYERDEATH = "Players.%s.DeathCount";
  private NewHorizonsServerCore _mMain = null;
  private int _mMaxDeathCount = 10;

  public TrialKick( NewHorizonsServerCore pMain )
  {
    _mMain = pMain;
    _mMaxDeathCount = _mMain.mConfig.getInt( "MaxDeath", 10 );
  }

  /*
   * @EventHandler
   * public void onJoin( PlayerLoginEvent e )
   * {
   * int tDeathCount = getDeathCounter( e.getPlayer() );
   * if( tDeathCount >= _mMaxDeathCount && !e.getPlayer().hasPermission( GTNH_PERM_FULL_MEMBER ) )
   * {
   * e.setKickMessage( ChatColor.translateAlternateColorCodes( '&', _mMain.mConfig.getString( "Messages.BannedMessage" )
   * ) );
   * e.setResult( Result.KICK_BANNED );
   * }
   * }
   */
  private int getDeathCounter( UUID pUUID )
  {
    int tRetVal = _mMain.mConfig.getInt( String.format( TEMPLATE_PLAYERDEATH, pUUID ), 0 );
    return tRetVal;
  }

  private int getDeathCounter( Player pPlayer )
  {
    return getDeathCounter( pPlayer.getUniqueId() );
  }

  private void incDeathCounter( Player pPlayer )
  {
    _mMain.mConfig.set( String.format( TEMPLATE_PLAYERDEATH, pPlayer.getUniqueId() ), getDeathCounter( pPlayer ) + 1 );
    _mMain.saveConfig();
  }

  @EventHandler
  public void onPlayerDeath( PlayerDeathEvent pEvent )
  {
    Player tPlayer = pEvent.getEntity();
    if( tPlayer != null )
    {
      incDeathCounter( tPlayer );

      if( tPlayer.hasPermission( GTNH_PERM_FULL_MEMBER ) )
        return;

      int tDeathCount = getDeathCounter( tPlayer );
      if( tDeathCount >= _mMaxDeathCount )
      {
        Bukkit.getBanList( Type.NAME ).addBan( tPlayer.getName(), "Trial period expired. Contact an Admin", null, null );
        tPlayer.kickPlayer( ChatColor.translateAlternateColorCodes( '&', _mMain.mConfig.getString( "Messages.KickMessage" ) ) );
      }
      else
        tPlayer.sendMessage( ChatColor.translateAlternateColorCodes( '&', String.format( _mMain.mConfig.getString( "Messages.TrialWarning" ), tDeathCount, _mMaxDeathCount ) ) );
    }
  }
}
