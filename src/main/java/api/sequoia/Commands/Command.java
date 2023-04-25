package api.sequoia.Commands;

import api.sequoia.misc.Options;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import static api.sequoia.utils.ChatAndLogs.chat;

public class Command {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("seq")
                .then(CommandManager.literal("help").executes(Command::help))
                .then(CommandManager.literal("stats").executes(Command::stats))
                .then(CommandManager.literal("toggle")
                    .then(CommandManager.literal("notify").executes(Command::seqNotify))
                    .then(CommandManager.literal("debug").executes(Command::debug))
                    .then(CommandManager.literal("warlog").executes(Command::logWars))));
    }



    private static int help(CommandContext<ServerCommandSource> context) {
        chat(context,"""
                §3§m--------[§6§l Sequoia API Mod §3§m]--------§r
                §7/seq stats §3> Shows war stats
                §7/seq toggle §3> Toggle mod variables""", false);
        return 1;
    }


    private static int stats(CommandContext<ServerCommandSource> context) {
            chat(context,"Stat Command");
        return 1;
    }

    private static int logWars(CommandContext<ServerCommandSource> context) {
        if(Options.logWars) chat(context, "§7You will no longer report wars");
        else chat(context, "§7You will now report wars to the guild");

        Options.logWars = !Options.logWars;
        return 1;
    }

    private static int seqNotify(CommandContext<ServerCommandSource> context) {
        if(Options.notifywars) chat(context, "§7You will now be notified when submitting a war to the sequoia API");
        else chat(context, "You will no longer be notified when submitting a war to the sequoia API");

        Options.notifywars = !Options.notifywars;
        return 1;
    }

    private static int debug(CommandContext<ServerCommandSource> context) {
        Options.debugMode = !Options.debugMode;
        chat(context, "§cDebug mode set to: §6" + String.valueOf(Options.debugMode));
        return 1;
    }

}
