
package eu.usrv.NewHorizonsServerCore;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import com.huskehhh.mysql.sqlite.SQLite;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import eu.usrv.NewHorizonsServerCore.auxiliary.GMHook;

import eu.usrv.NewHorizonsServerCore.modRankUp.RankUpCommand;
import eu.usrv.NewHorizonsServerCore.modTrialKick.TrialKick;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;


/*
 * https://bukkit.org/threads/using-mysql-in-your-plugins.132309/
 * 
 */

public final class NewHorizonsServerCore extends JavaPlugin
{
  public NewHorizonsServerCore Instance;
  public FileConfiguration mConfig;
  public TrialKick mModule_TrialKick;
  public static SQLite OfflineUUIDCache = null; // new SQLite( "OfflineUUIDCache.sqlite" );
  public Connection OUUIDCCon = null;
  public GMHook mGMHook;

  private RankUpCommand mModule_Rankup;
  public static Logger logger = null;

  public NewHorizonsServerCore()
  {
    Instance = this;
    logger = getLogger();
  }

  /*
    private void setupDBCons()
    {
      try
      {
        OUUIDCCon = OfflineUUIDCache.openConnection();
      }
      catch( ClassNotFoundException e )
      {
        e.printStackTrace();
      }
      catch( SQLException e )
      {
        e.printStackTrace();
      }
    }
  */
  @Override
  public void onEnable()
  {
    logger.info( "Loading configuration..." );
    mConfig = getConfig();
    saveDefaultConfig();
    mConfig.options().copyDefaults( true );

    if( !setupEconomy() )
    {
      logger.severe( String.format( "[%s] - Disabled due to no Vault dependency found!", getDescription().getName() ) );
      getServer().getPluginManager().disablePlugin( this );
      return;
    }

    mGMHook = new GMHook( this );
    logger.info( "Enabling TrialKick submodule..." );
    mModule_TrialKick = new TrialKick( this );

    mModule_Rankup = new RankUpCommand( this );
    getCommand( "rankup" ).setExecutor( mModule_Rankup );
    getServer().getPluginManager().registerEvents( mModule_TrialKick, this );

    // Not yet
    // logger.info( "Initializing Offline UUID Database..." );
    // setupDBCons();
  }

  private boolean setupEconomy()
  {
    if( getServer().getPluginManager().getPlugin( "Vault" ) == null )
    {
      return false;
    }
    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration( Economy.class );
    if( rsp == null )
    {
      return false;
    }
    econ = rsp.getProvider();
    return econ != null;
  }

  @Override
  public void onDisable()
  {
    logger.info( "Saving configuration..." );
    saveConfig();

    // Disabling modules
    mModule_TrialKick = null;
  }
}
