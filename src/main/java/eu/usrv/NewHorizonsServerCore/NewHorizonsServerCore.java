
package eu.usrv.NewHorizonsServerCore;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import eu.usrv.NewHorizonsServerCore.modTrialKick.TrialKick;


public final class NewHorizonsServerCore extends JavaPlugin
{
  public NewHorizonsServerCore Instance;
  public FileConfiguration mConfig;
  public TrialKick mModule_TrialKick;

  public NewHorizonsServerCore()
  {
    Instance = this;
  }

  @Override
  public void onEnable()
  {
    getLogger().info( "Loading configuration..." );
    mConfig = getConfig();
    saveDefaultConfig();
    mConfig.options().copyDefaults( true );
    
    getLogger().info( "Enabling TrialKick submodule..." );
    mModule_TrialKick = new TrialKick( this );

    getServer().getPluginManager().registerEvents( mModule_TrialKick, this );
  }
  
  @Override
  public void onDisable()
  {
    getLogger().info( "Saving configuration..." );
    saveConfig();
    
    // Disabling modules
    mModule_TrialKick = null;
  }
}
