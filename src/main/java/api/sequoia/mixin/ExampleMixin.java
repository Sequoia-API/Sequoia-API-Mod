package api.sequoia.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;

@Mixin(ChatHud.class)
public class ExampleMixin {
	@Inject(method = "addMessage(Lnet/minecraft/text/Text;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"),
			cancellable = true)
	private void onMessage(Text message, CallbackInfo ci) throws IOException {
				//- Captured "Dernel Jungle Mid"
		//        {
		//            "timestamp": 1681295109887,
		//                "Name": "Detlas",
		//                "uuid": "1b3cc989-48c1-4e7b-9717-c7e357dc3813"
		//        }

		        String content = message.getString().replaceAll("§.", "");

		        if(!content.startsWith("- Captured \"") || !content.endsWith("\"") ) return;
		        MinecraftClient mc = MinecraftClient.getInstance();
		        assert mc.player != null;
		        String uuid = mc.player.getUuidAsString();
		        long time = System.currentTimeMillis();
		        String terr =  content.replace("- Captured \"", "").replace("\"","");
//		        mc.player.sendMessage(Text.literal("added "+uuid+" "+ time+" :"+terr+":"), false);
		        String json = "{\"timestamp\":"+time+",\"Name\":\""+terr+"\",\"uuid\":\""+uuid+"\"}";
				String encoded = Base64.getEncoder().encodeToString((json).getBytes());
				String urlToRead = "http://64.226.79.170:8000/war/?uuid="+uuid+"&key="+encoded;
				StringBuilder result = new StringBuilder();
				URL url = new URL(urlToRead);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				try (BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()))) {
					for (String line; (line = reader.readLine()) != null; ) {
						result.append(line);
					}
				}
	}
}