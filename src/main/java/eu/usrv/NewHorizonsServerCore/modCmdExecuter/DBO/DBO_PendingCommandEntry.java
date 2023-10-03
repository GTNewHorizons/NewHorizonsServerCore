package eu.usrv.NewHorizonsServerCore.modCmdExecuter.DBO;
public class DBO_PendingCommandEntry {
    public Integer ID;
    public String serverID;
    public String commandText;
    public String commandSetUUID;
    public Integer commandSetIndex;
    public Integer waitBefore;
    public Integer waitAfter;
    public Integer commandExecuted;


}
