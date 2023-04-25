package api.sequoia.utils;

import api.sequoia.misc.Options;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Objects;

public class ChatAndLogs {

    public static ChatAndLogs instance = new ChatAndLogs();

    public void locLog(String str) {
        System.out.println("[SEQ-API] [LOG] " + str);
    }

    public void locWarn(String str) {
        System.out.println("[SEQ-API] [WARNING] " + str);
    }

    public void locError(String str) {
        System.out.println("[SEQ-API] [ERROR] " + str);
    }

    public void locDebug(String str) { if(Options.debugMode) System.out.println("[SEQ-API] [DEBUG] " + str); }

    public void locChat(CommandContext<ServerCommandSource> context, String str, boolean prefix) {
        if(prefix) Objects.requireNonNull(context.getSource().getPlayer()).sendMessage(Text.of("§7[SEQ-API] "+str));
        else Objects.requireNonNull(context.getSource().getPlayer()).sendMessage(Text.of(str));
    }


    public static void log(String str) {
        instance.locLog(str);
    }

    public static void warn(String str) {
        instance.locWarn(str);
    }

    public static void error(String str) {
        instance.locError(str);
    }

    public static void debug(String str) { instance.locDebug(str); }

    public static void chat(CommandContext<ServerCommandSource> context, String str, boolean prefix) {instance.locChat(context, str, prefix);}
    public static void chat(CommandContext<ServerCommandSource> context, String str) {instance.locChat(context, str, true);}

}