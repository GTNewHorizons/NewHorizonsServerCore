package eu.usrv.NewHorizonsServerCore.modRankUp;

public class RankUpDefinition
{
  private int _mRankID;
  private int _mRankCost;
  private String _mRankName;
  
  public int getRankID()
  {
    return _mRankID;
  }

  public int getRankCost()
  {
    return _mRankCost;
  }

  public String getRankName()
  {
    return _mRankName;
  }

  public RankUpDefinition(int pID, int pCost, String pName)
  {
    _mRankID = pID;
    _mRankCost = pCost;
    _mRankName = pName;
  }
}
