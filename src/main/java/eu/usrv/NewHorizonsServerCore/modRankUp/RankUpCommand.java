
package eu.usrv.NewHorizonsServerCore.modRankUp;

import java.util.ArrayList;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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
    NewHorizonsServerCore.logger.info( "[RankUp] Loading " + tTotalRanks + " Ranks from config" );

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

    String tUserGroup = NewHorizonsServerCore.perms.getPrimaryGroup( player );

    for (int i = 0; i < _mRanks.size(); i++)
    {
      RankUpDefinition rud = _mRanks.get( i );
      if (rud.getRankName().equalsIgnoreCase( tUserGroup ))
      {
        // Found current group. Get next group
        if (i +1 == _mRanks.size()) // Case 1: Player already has highest group
        {
          pSender.sendMessage( "You have the highest Rank avaliable. Rankup not possible" );
          return true;
        }
        else
        {
          RankUpDefinition tNextGroup = _mRanks.get(i+1);
          OfflinePlayer tOflPl = Bukkit.getServer().getOfflinePlayer(player.getUniqueId());
          double tBalance = _mMain.econ.getBalance(tOflPl);
          if ((double)tNextGroup.getRankCost() > tBalance)
          { // Case 2: Player doesn't have enough money
            pSender.sendMessage( "Insufficient funds for a RankUp." );
            pSender.sendMessage( String.format("For Rank [%s], you need %d currency", tNextGroup.getRankName(), tNextGroup.getRankCost()) );
            return true;            
          }
          else
          {
            EconomyResponse eResp = _mMain.econ.withdrawPlayer( tOflPl, (double)tNextGroup.getRankCost() );
            if (eResp.transactionSuccess())
            {
              NewHorizonsServerCore.perms.playerAddGroup( player, tNextGroup.getRankName() );
              pSender.sendMessage( String.format("You just Ranked up to %s by paying %d. Congratulations!", tNextGroup.getRankName(), tNextGroup.getRankCost()) );
            }
            else
            {
              pSender.sendMessage( "Sorry something went wrong. Please contact a mod/admin!" );
            }
            return true;
          }
        }
      }
    }
    
    return false;
  }
}
