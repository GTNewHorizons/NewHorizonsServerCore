
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

  public void onEnable()
  {
    mConfig = getConfig();
    saveDefaultConfig();
    mConfig.options().copyDefaults( true );
    mModule_TrialKick = new TrialKick( this );

    getServer().getPluginManager().registerEvents( mModule_TrialKick, this );
    mConfig = getConfig();
    saveDefaultConfig();
  }
}
