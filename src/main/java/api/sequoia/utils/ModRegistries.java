package api.sequoia.utils;

import api.sequoia.Commands.Command;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import static api.sequoia.enums.funnyTitleMessages.register;

public class ModRegistries {
    public static void registerModStuffs() {
        registerCommands();
        registerFunnyTitleMessages();
        ChatAndLogs.log("Registered 4 commands...");
    }

    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register(Command::register);
    }
    public static void registerFunnyTitleMessages() {register();}
}
