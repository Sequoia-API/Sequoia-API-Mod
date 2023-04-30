package api.sequoia.Messages;

import api.sequoia.misc.Options;
import api.sequoia.utils.ChatAndLogs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.text.Text;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Mixin(ChatHud.class)

public abstract class MessageHandler {
	@Shadow protected abstract void logChatMessage(Text message, @Nullable MessageIndicator indicator);

	@Inject(method = "addMessage(Lnet/minecraft/text/Text;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"))

	private void onMessage(Text message, CallbackInfo ci) {


		// Do nothing if disabled
		if(!Options.logWars) return;

		/*  This is the message that we try to process
		 * @MessageToProcess:
		 * - Captured "Dernel Jungle Mid"
		 *  This is the output we send over to the server
		 *         {
		 *           "timestamp": 1681295109887,
		 * 		     "Name": "Dernel Jungle Mid",
		 *           "uuid": "1b3cc989-48c1-4e7b-9717-c7e357dc3813"
		 *         }
		 */
		String content = message.getString().replaceAll("§.", "");
		if(!content.startsWith("- Captured \"") || !content.endsWith("\"") ) return;
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc.player == null) return;
		String uuid = mc.player.getUuidAsString();
		long time = System.currentTimeMillis();
		String terr =  content.replace("- Captured \"", "").replace("\"","");
		String json = "{\"timestamp\":"+time+",\"Name\":\""+terr+"\",\"uuid\":\""+uuid+"\"}";
		String encodedJson = Base64.getEncoder().encodeToString((json).getBytes());
		String urlToRead = "http://"+ Options.apiServer+":"+Options.apiPort+"/war/?uuid="+uuid+"&key="+encodedJson;

		if(Options.logWars) mc.player.sendMessage(Text.of("§7- [SEQ-API] War has been recorded"));
		CompletableFuture.runAsync(() -> {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlToRead)).build();
			String connResult = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).join();
			ChatAndLogs.debug("New connection result received: " + connResult);
		});
}
}
