
package eu.usrv.NewHorizonsServerCore.auxiliary;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import eu.usrv.NewHorizonsServerCore.NewHorizonsServerCore;


public class FXHelper
{
  public void fxSpawnFirework( final int pNumFirework, final Player pPlayer, long pDelay )
  {
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask( NewHorizonsServerCore.Instance, 
        new Runnable() 
    {
      public void run() {
        for (int i = 0; i < pNumFirework; i++)
          spawnRandomFireworkAtPlayer( pPlayer );
      }
      
    }, pDelay );
  }

  private static void spawnRandomFireworkAtPlayer( Player pPlayer )
  {
    // Our random generator
    Random r = new Random();
    
    // Spawn the Firework, get the FireworkMeta.
    Location loc = pPlayer.getLocation();
    loc.add( r.nextInt(5)-2, 0, r.nextInt(5)-2 );
    
    Firework fw = (Firework) pPlayer.getWorld().spawnEntity( loc, EntityType.FIREWORK );
    FireworkMeta fwm = fw.getFireworkMeta();

    // Get the type
    int rt = r.nextInt( 5 ) + 1;
    Type type = Type.BALL;
    if( rt == 1 ) type = Type.BALL;
    if( rt == 2 ) type = Type.BALL_LARGE;
    if( rt == 3 ) type = Type.BURST;
    if( rt == 4 ) type = Type.CREEPER;
    if( rt == 5 ) type = Type.STAR;

    // Get our random colours
    int r1i = r.nextInt( 17 ) + 1;
    int r2i = r.nextInt( 17 ) + 1;
    Color c1 = getColor( r1i );
    Color c2 = getColor( r2i );

    // Create our effect with this
    FireworkEffect effect = FireworkEffect.builder().flicker( r.nextBoolean() ).withColor( c1 ).withFade( c2 ).with( type ).trail( r.nextBoolean() ).build();

    // Then apply the effect to the meta
    fwm.addEffect( effect );

    // Generate some random power and set it
    int rp = r.nextInt( 2 ) + 1;
    fwm.setPower( rp );

    // Then apply this to our rocket
    fw.setFireworkMeta( fwm );
  }

  private static Color getColor( int i )
  {
    Color c = null;
    if( i == 1 )
    {
      c = Color.AQUA;
    }
    if( i == 2 )
    {
      c = Color.BLACK;
    }
    if( i == 3 )
    {
      c = Color.BLUE;
    }
    if( i == 4 )
    {
      c = Color.FUCHSIA;
    }
    if( i == 5 )
    {
      c = Color.GRAY;
    }
    if( i == 6 )
    {
      c = Color.GREEN;
    }
    if( i == 7 )
    {
      c = Color.LIME;
    }
    if( i == 8 )
    {
      c = Color.MAROON;
    }
    if( i == 9 )
    {
      c = Color.NAVY;
    }
    if( i == 10 )
    {
      c = Color.OLIVE;
    }
    if( i == 11 )
    {
      c = Color.ORANGE;
    }
    if( i == 12 )
    {
      c = Color.PURPLE;
    }
    if( i == 13 )
    {
      c = Color.RED;
    }
    if( i == 14 )
    {
      c = Color.SILVER;
    }
    if( i == 15 )
    {
      c = Color.TEAL;
    }
    if( i == 16 )
    {
      c = Color.WHITE;
    }
    if( i == 17 )
    {
      c = Color.YELLOW;
    }

    return c;
  }
}
