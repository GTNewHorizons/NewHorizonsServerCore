
package eu.usrv.NewHorizonsServerCore.modRankUp;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.usrv.NewHorizonsServerCore.NewHorizonsServerCore;


public class RankUpCommand implements CommandExecutor
{
  private NewHorizonsServerCore _mMain = null;
  private ArrayList<RankUpDefinition> _mRanks = null;

  public RankUpCommand( NewHorizonsServerCore pServerCoreInstance )
  {
    _mMain = pServerCoreInstance;
    _mRanks = new ArrayList<RankUpDefinition>();

    loadConfig();
  }

  private void loadConfig()
  {
    int tTotalRanks = _mMain.mConfig.getInt( "NumRanks" );
    NewHorizonsServerCore.logger.info( "Loading " + tTotalRanks + " Ranks from config" );

    for( int i = 0; i < tTotalRanks; i++ )
    {
      String tRankName = _mMain.mConfig.getString( String.format( "Rank_%d_Name", i ) );
      Integer tRankCost = _mMain.mConfig.getInt( String.format( "Rank_%d_Cost", i ) );
      RankUpDefinition tRank = new RankUpDefinition( i, tRankCost, tRankName );

      NewHorizonsServerCore.logger.info( String.format( "[RankUp] Loaded new Rank; ID %d - Group \"%s\" Cost %d", tRank.getRankID(), tRank.getRankName(), tRank.getRankCost() ) );
      _mRanks.add( tRank );
    }
  }

  @Override
  public boolean onCommand( CommandSender pSender, Command pCmd, String pCommandLabel, String[] pArgs )
  {
    if( !( pSender instanceof Player ) )
    {
      pSender.sendMessage( ChatColor.RED + "Command only available to players!" );
      return true;
    }
    Player player = (Player) pSender;

    String tUserGroup = _mMain.mGMHook.getGroup( player );

    for (int i = 0; i < _mRanks.size(); i++)
    {
      RankUpDefinition rud = _mRanks.get( i );
      if (rud.getRankName().equalsIgnoreCase( tUserGroup ))
      {
        // Found current group. Get next group
        if (i +1 == _mRanks.size())
        {
          pSender.sendMessage( "You have the highest Rank avaliable. Rankup not possible" );
          return true;
        }
        else
        {
          RankUpDefinition tNextGroup = _mRanks.get(i+1);
          
        }
      }
    }
    
    return false;
  }
}
