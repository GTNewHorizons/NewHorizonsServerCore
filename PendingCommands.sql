CREATE TABLE `pendingcommands` (
                                   `ID` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'AutoIncrement Command ID',
                                   `serverID` varchar(20) NOT NULL COMMENT 'ServerID this command is supposed to run on',
                                   `commandText` varchar(255) NOT NULL COMMENT 'Command to be executed',
                                   `commandSetUUID` varchar(36) NOT NULL COMMENT 'UUID to identify commandsets that need to be executed together, on order',
                                   `commandSetIndex` int(8) unsigned NOT NULL COMMENT 'CommandIndex for this command within the commandSetUUID Group',
                                   `waitBefore` int(8) unsigned NOT NULL DEFAULT '0' COMMENT 'How many seconds to wait BEFORE this command is executed',
                                   `waitAfter` int(8) unsigned NOT NULL DEFAULT '0' COMMENT 'How many seconds to wait AFTER this command is executed',
                                   `commandExecuted` bit(1) NOT NULL DEFAULT b'0' COMMENT 'Has this command been executed or not',
                                   PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8
