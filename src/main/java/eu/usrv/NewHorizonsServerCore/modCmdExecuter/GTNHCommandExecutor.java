package eu.usrv.NewHorizonsServerCore.modCmdExecuter;

import eu.usrv.NewHorizonsServerCore.NewHorizonsServerCore;
import eu.usrv.NewHorizonsServerCore.modCmdExecuter.DBO.DBO_PendingCommandEntry;
import eu.usrv.NewHorizonsServerCore.modCmdExecuter.DBO.PendingCommandEntryHelper;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


public class GTNHCommandExecutor {
    private static boolean _mTimerjobBusy = false;
    private final NewHorizonsServerCore _mMain;
    private ArrayList<DBO_PendingCommandEntry> _mPendingCommands;
    private Timer _mTimer = null;
    private final String _mServerID;

    public GTNHCommandExecutor( NewHorizonsServerCore pMain )
    {
        _mMain = pMain;
        _mPendingCommands = new ArrayList<>();
        _mServerID = _mMain.mConfig.getString("ServerID", "set_server_id");
        if (_mServerID.equals("set_server_id"))
            NewHorizonsServerCore.logger.severe("[CommandExecutor] ServerID is not configured ConfigFile! No commands will be executed");
        else {
            NewHorizonsServerCore.logger.info(String.format("[CommandExecutor] This Server will execute commands for ServerID [%s]", _mServerID));
            SetupTimer();
        }
    }

    private void SetupTimer()
    {
        _mTimer = new Timer();
        // Start timer to check for new commands each x seconds
        _mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (_mTimerjobBusy) {
                    NewHorizonsServerCore.logger.warning("[CommandExecutor] Background progress is still running! Skipping this cycle...");
                    return;
                }

                try {
                    _mTimerjobBusy = true;
                    // Empty the CommandList. Processed Commands will be updated within the loop
                    // If a command fails,
                    _mPendingCommands = new ArrayList<>();
                    UpdateCommandsFromDatabase();
                    RunCommandsOnServer();
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                finally {
                    _mTimerjobBusy = false;
                }
            }
        }, 60*1000, 60*1000);
    }

    private void UpdateCommandsFromDatabase() throws SQLException, ClassNotFoundException {
        Connection con = _mMain.DBConnection.openConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM pendingcommands WHERE commandExecuted = 0 AND serverID = ?");
        stmt.setString(1, _mServerID);
        ResultSet res = stmt.executeQuery();
        while(res.next())
        {
            _mPendingCommands.add(PendingCommandEntryHelper.GetFromRS(res));
        }
        con.close();
        if (!_mPendingCommands.isEmpty())
            NewHorizonsServerCore.logger.info( String.format( "[CommandExecutor] Loaded %d new commands from DB", _mPendingCommands.size() ) );
    }

    private void RunCommandsOnServer()
    {
        NewHorizonsServerCore.logger.fine( String.format( "[CommandExecutor] About to process %d commands on this server", _mPendingCommands.size() ) );
        ArrayList<String> tCommandUUIDs = new ArrayList<>();

        // Find all CommandUUIDs
        for (DBO_PendingCommandEntry mPendingCommand : _mPendingCommands) {
            String tUUID = mPendingCommand.commandSetUUID;
            if (!tCommandUUIDs.contains(tUUID))
                tCommandUUIDs.add(tUUID);
        }
        NewHorizonsServerCore.logger.fine( String.format( "[CommandExecutor] Found %d command groups", tCommandUUIDs.size() ) );

        // Now process commandUUIDs
        for(int i = 0; i < tCommandUUIDs.size(); i++)
        {
            int finalI = i;
            List<DBO_PendingCommandEntry> uuidSet = _mPendingCommands
                    .stream().parallel()
                    .filter(dbo -> dbo.commandSetUUID.equals(tCommandUUIDs.get(finalI)))
                    .sorted(Comparator.comparing(o -> o.commandSetIndex))
                    .collect(Collectors.toList());
            NewHorizonsServerCore.logger.fine( String.format( "[CommandExecutor] Processing %d commands for command UUID %s", uuidSet.size(), tCommandUUIDs.get(finalI) ) );

            for (DBO_PendingCommandEntry dboPendingCommandEntry : uuidSet) {
                try {
                    NewHorizonsServerCore.logger.fine(String.format("[CommandExecutor] Processing command ID %d: %s", dboPendingCommandEntry.commandSetIndex, dboPendingCommandEntry.commandText));
                    if (dboPendingCommandEntry.waitBefore > 0) {
                        NewHorizonsServerCore.logger.fine(String.format("[CommandExecutor] waitBefore is set.. Going to sleep for %d seconds", dboPendingCommandEntry.waitBefore));
                        Thread.sleep(dboPendingCommandEntry.waitBefore * 1000);
                    }

                    _mMain.getServer().dispatchCommand(Bukkit.getConsoleSender(), dboPendingCommandEntry.commandText);

                    if (dboPendingCommandEntry.waitAfter > 0) {
                        NewHorizonsServerCore.logger.fine(String.format("[CommandExecutor] waitAfter is set.. Going to sleep for %d seconds", dboPendingCommandEntry.waitAfter));
                        Thread.sleep(dboPendingCommandEntry.waitAfter * 1000);
                    }
                    // update database so we don't re-execute this command again
                    _mMain.DBConnection.updateSQL(String.format("UPDATE pendingcommands SET commandExecuted = 1 WHERE ID = %d ", dboPendingCommandEntry.ID));
                } catch (Exception e) {
                    NewHorizonsServerCore.logger.severe("[CommandExecutor] Something went wrong while executing command %s");
                    NewHorizonsServerCore.logger.severe(e.getMessage());
                }
            }
        }
    }

    public void Shutdown() {
        NewHorizonsServerCore.logger.info("[CommandExecutor] Shutting down CommandExecutor");
        if (_mTimer != null) {
            _mTimer.cancel();
            _mTimer.purge();
        }
    }
}
