
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
import eu.usrv.NewHorizonsServerCore.auxiliary.Utils;


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
      String tRankName = _mMain.mConfig.getString( String.format( "Rank_%d_Name", i ), "NOEXIST" );
      Integer tRankCost = _mMain.mConfig.getInt( String.format( "Rank_%d_Cost", i ), -1 );
      
      // Make sure the ranks are existing and well configured
      if ( tRankName.equals( "NOEXIST" ) || tRankCost == -1)
        break;
      
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
      pSender.sendMessage( ChatColor.RED + "[RankUp] Command only available to players!" );
      return true;
    }
    Player player = (Player) pSender;

    String tUserGroup = NewHorizonsServerCore.perms.getPrimaryGroup( player );

    boolean tFoundUserGroupEntry = false;
    for( int i = 0; i < _mRanks.size(); i++ )
    {
      RankUpDefinition rud = _mRanks.get( i );
      if( rud.getRankName().equalsIgnoreCase( tUserGroup ) )
      {
        tFoundUserGroupEntry = true;
        // Found current group. Get next group
        if( i + 1 == _mRanks.size() ) // Case 1: Player already has highest group
        {
          pSender.sendMessage( "[RankUp] " + ChatColor.GOLD + "You already have the highest Rank avaliable" );
          return true;
        }
        else
        {
          RankUpDefinition tNextGroup = _mRanks.get( i + 1 );
          
          // Make sure GroupManager is aware of this group
          boolean tFoundGroup = false;
          for ( String tGroup : NewHorizonsServerCore.perms.getGroups() )
          {
            if ( tGroup.equalsIgnoreCase( tNextGroup.getRankName() ) )
              tFoundGroup = true;
          }
          
          if ( !tFoundGroup )
          {
            pSender.sendMessage( "[RankUp] " + ChatColor.RED + "E:TARGETRANK_NOT_FOUND" );
            pSender.sendMessage( "[RankUp] " + ChatColor.RED + "Sorry something went wrong. Please contact a mod/admin!" );
            return true;
          }
          
          OfflinePlayer tOflPl = Bukkit.getServer().getOfflinePlayer( player.getUniqueId() );
          if ( tOflPl == null )
          {
            pSender.sendMessage( "[RankUp] " + ChatColor.RED + "E:PLAYER_NOT_FOUND" );
            pSender.sendMessage( "[RankUp] " + ChatColor.RED + "Sorry something went wrong. Please contact a mod/admin!" );
            return true;
          }
          
          double tBalance = NewHorizonsServerCore.econ.getBalance( tOflPl );
          String tGroupNameForChat = Utils.capitalizeFirst( tNextGroup.getRankName() );

          if( (double) tNextGroup.getRankCost() > tBalance )
          { // Case 2: Player doesn't have enough money
            pSender.sendMessage( "[RankUp] " + ChatColor.RED + "Insufficient funds" );
            pSender.sendMessage( String.format( "[RankUp] For Rank [%s], you need $%,d", tGroupNameForChat, tNextGroup.getRankCost() ) );
            return true;
          }
          else
          {
            EconomyResponse eResp = NewHorizonsServerCore.econ.withdrawPlayer( tOflPl, (double) tNextGroup.getRankCost() );
            if( eResp.transactionSuccess() )
            {
              NewHorizonsServerCore.perms.playerAddGroup( player, tNextGroup.getRankName() );
              if( i + 2 == _mRanks.size() )
              {
                Bukkit.getServer().broadcastMessage( String.format( "%s%s%s just reached the highest available Rank; %s%s!", ChatColor.GOLD, player.getName(), ChatColor.GREEN, ChatColor.GOLD, tGroupNameForChat ) );

                // Maybe do something for each player?
                // for (Player p : Bukkit.getServer().getOnlinePlayers())
                // {
                // Free diamonds :P
                // p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.DIAMOND));
                // }
              }
              else
                Bukkit.getServer().broadcastMessage( String.format( "%s%s%s just ranked up to be a %s%s", ChatColor.GOLD, player.getName(), ChatColor.GREEN, ChatColor.GOLD, tGroupNameForChat ) );

              // pSender.sendMessage( String.format( "%s%sYou just Ranked up to %s by paying %,d. Congratulations!",
              // ChatColor.GOLD, ChatColor.BOLD, tNextGroup.getRankName(), tNextGroup.getRankCost() ) );
              _mMain.fx.fxSpawnFirework( 1, player, 10 );
              _mMain.fx.fxSpawnFirework( 2, player, 30 );
              _mMain.fx.fxSpawnFirework( 3, player, 50 );
              _mMain.fx.fxSpawnFirework( 4, player, 70 );
            }
            else
            {
              pSender.sendMessage( "[RankUp] " + ChatColor.RED + "E:MONEYTRANSFER_FAILED" );
              pSender.sendMessage( "[RankUp] " + ChatColor.RED + "Sorry something went wrong. Please contact a mod/admin!" );
            }
            return true;
          }
        }
      }
    }
    if ( !tFoundUserGroupEntry )
    {
      pSender.sendMessage( "[RankUp] " + ChatColor.RED + "E:PLAYERRANK_NOT_FOUND" );
      pSender.sendMessage( "[RankUp] " + ChatColor.RED + "Sorry something went wrong. Please contact a mod/admin!" );
      return true;
    }

    return false;
  }
}
