
package eu.usrv.NewHorizonsServerCore.modRankUp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.usrv.NewHorizonsServerCore.NewHorizonsServerCore;


public class RankUpCommand implements CommandExecutor
{
  private NewHorizonsServerCore _mMain = null;

  public RankUpCommand( NewHorizonsServerCore pServerCoreInstance )
  {
    _mMain = pServerCoreInstance;
  }

  @Override
  public boolean onCommand(CommandSender pSender, Command pCmd, String pCommandLabel, String[] pArgs )
  {
    if( !( pSender instanceof Player ) )
    {
      pSender.sendMessage( ChatColor.RED + "Du musst ein Spieler sein!" );
      return true;
    }
    Player player = (Player) pSender;
    
    
    
    return false;
  }
}
