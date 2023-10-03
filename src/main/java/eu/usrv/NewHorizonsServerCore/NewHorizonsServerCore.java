
package eu.usrv.NewHorizonsServerCore;

import java.util.logging.Logger;

import com.huskehhh.mysql.mysql.MySQL;
import eu.usrv.NewHorizonsServerCore.modCmdExecuter.GTNHCommandExecutor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import eu.usrv.NewHorizonsServerCore.auxiliary.FXHelper;
import eu.usrv.NewHorizonsServerCore.modRankUp.RankUpCommand;


/*
 * https://bukkit.org/threads/using-mysql-in-your-plugins.132309/
 * 
 */

public final class NewHorizonsServerCore extends JavaPlugin
{
  public static NewHorizonsServerCore Instance;
  public FileConfiguration mConfig;

  public MySQL DBConnection = null;
  public FXHelper fx = null;

  public static Economy econ = null;
  public static Permission perms = null;

  public GTNHCommandExecutor mModule_CommandExecutor = null;
  private RankUpCommand mModule_Rankup;
  public static Logger logger = null;

  public NewHorizonsServerCore()
  {
    Instance = this;
    logger = getLogger();
  }


    private void setupDBCons()
    {
      DBConnection = new MySQL(
              mConfig.getString( "MySQL_Host", "localhost" ),
              mConfig.getString( "MySQL_Port", "3306" ),
              mConfig.getString( "MySQL_Database", "database" ),
              mConfig.getString( "MySQL_User", "user" ),
              mConfig.getString( "MySQL_Password", "password" ));
    }

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
    fx = new FXHelper();
    if (!setupPermissions())
    {
      logger.severe("Unable to hook into Bukkit's Permission Provider. Commands cannot be registered");
    }
    else
    {
      logger.info( "Registering RankUp command..." );
      mModule_Rankup = new RankUpCommand( this );
      getCommand( "rankup" ).setExecutor( mModule_Rankup );
    }

    logger.info("Starting CommandExecutor...");
    mModule_CommandExecutor = new GTNHCommandExecutor(this);

    logger.info( "Initializing Database connection..." );
    setupDBCons();
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
    if( mModule_Rankup != null )
      mModule_Rankup = null;
    if (mModule_CommandExecutor != null) {
      mModule_CommandExecutor.Shutdown();
      mModule_CommandExecutor = null;
    }
  }
}
