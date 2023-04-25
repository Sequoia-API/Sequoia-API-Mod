package api.sequoia.utils;

import api.sequoia.Commands.Command;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModRegistries {
    public static void registerModStuffs() {
        registerCommands();
        ChatAndLogs.log("Registered 4 commands...");
    }

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(Command::register);
    }
}
