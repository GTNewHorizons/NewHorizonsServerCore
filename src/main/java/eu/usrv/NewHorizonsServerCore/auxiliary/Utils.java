
package eu.usrv.NewHorizonsServerCore.auxiliary;

public class Utils
{
  public static String capitalizeFirst( String pString )
  {
    String tRet = pString.toLowerCase();
    tRet = Character.toString( tRet.charAt( 0 ) ).toUpperCase() + tRet.substring( 1 );
    
    return tRet;
  }
}
