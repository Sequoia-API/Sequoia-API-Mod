package api.sequoia;

import api.sequoia.utils.ChatAndLogs;
import api.sequoia.utils.ModRegistries;
import net.fabricmc.api.ModInitializer;

public class SequoiaApi implements ModInitializer {


    @Override
    public void onInitialize() {
        ChatAndLogs.log("Initializing SequoiaAPI...");
        ModRegistries.registerModStuffs();
    }
}