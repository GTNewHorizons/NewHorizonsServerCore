
package eu.usrv.NewHorizonsServerCore;

import java.sql.Connection;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.huskehhh.mysql.sqlite.SQLite;

import eu.usrv.NewHorizonsServerCore.auxiliary.FXHelper;
import eu.usrv.NewHorizonsServerCore.modRankUp.RankUpCommand;
import eu.usrv.NewHorizonsServerCore.modTrialKick.TrialKick;


/*
 * https://bukkit.org/threads/using-mysql-in-your-plugins.132309/
 * 
 */

public final class NewHorizonsServerCore extends JavaPlugin
{
  public static NewHorizonsServerCore Instance;
  public FileConfiguration mConfig;
  public TrialKick mModule_TrialKick;
  public static SQLite OfflineUUIDCache = null; // new SQLite( "OfflineUUIDCache.sqlite" );
  public Connection OUUIDCCon = null;
  public FXHelper fx = null;

  public static Economy econ = null;
  public static Permission perms = null;

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
    setupPermissions();
    
    fx = new FXHelper();

    // logger.info( "Enabling TrialKick submodule..." );
    // mModule_TrialKick = new TrialKick( this );

    logger.info( "Registering RankUp command..." );
    mModule_Rankup = new RankUpCommand( this );
    getCommand( "rankup" ).setExecutor( mModule_Rankup );
    // getServer().getPluginManager().registerEvents( mModule_TrialKick, this );

    // Not yet
    // logger.info( "Initializing Offline UUID Database..." );
    // setupDBCons();
  }

  private boolean setupPermissions()
  {
    RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration( Permission.class );
    perms = rsp.getProvider();
    return perms != null;
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
    if( mModule_TrialKick != null )
      mModule_TrialKick = null;
    if( mModule_Rankup != null )
      mModule_Rankup = null;
  }
}
