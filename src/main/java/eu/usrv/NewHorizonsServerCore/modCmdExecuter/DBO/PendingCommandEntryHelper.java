package eu.usrv.NewHorizonsServerCore.modCmdExecuter.DBO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PendingCommandEntryHelper {
    public static DBO_PendingCommandEntry GetFromRS(ResultSet pSourceRow) throws SQLException {
        DBO_PendingCommandEntry tReturn = new DBO_PendingCommandEntry();
        if(pSourceRow != null)
        {
            tReturn.ID = pSourceRow.getInt(1);
            tReturn.serverID = pSourceRow.getString(2);
            tReturn.commandText = pSourceRow.getString(3);
            tReturn.commandSetUUID = pSourceRow.getString(4);
            tReturn.commandSetIndex = pSourceRow.getInt(5);
            tReturn.waitBefore = pSourceRow.getInt(6);
            tReturn.waitAfter = pSourceRow.getInt(7);
            tReturn.commandExecuted = pSourceRow.getInt(8);
        }

        return tReturn;
    }
}
