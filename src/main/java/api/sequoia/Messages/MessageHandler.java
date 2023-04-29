package api.sequoia.Messages;

import api.sequoia.misc.Options;
import api.sequoia.utils.ChatAndLogs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.text.Text;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Mixin(ChatHud.class)

public class MessageHandler {
	@Inject(method = "addMessage(Lnet/minecraft/text/Text;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V")
	)
	private void onMessage(Text message, CallbackInfo ci){

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
		System.out.println("phase 2");
		//if(Objects.requireNonNull(mc.player.getServer()).getOpPermissionLevel() > 0) return; //Prevents the mod from doing anything if the player is op - REMOVEd, the check will return everytime
		System.out.println("phase 3");
		String uuid = mc.player.getUuidAsString();
		long time = System.currentTimeMillis();
		String terr =  content.replace("- Captured \"", "").replace("\"","");
		String json = "{\"timestamp\":"+time+",\"Name\":\""+terr+"\",\"uuid\":\""+uuid+"\"}";
		String encodedJson = Base64.getEncoder().encodeToString((json).getBytes());
		String urlToRead = "http://"+ Options.apiServer+":"+Options.apiPort+"/war/?uuid="+uuid+"&key="+encodedJson;

//		StringBuilder connResult = new StringBuilder();
//		URL url = new URL(urlToRead);

//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setRequestMethod("GET");

		if(Options.logWars) mc.player.sendMessage(Text.of("§a [SEQ-API] War has been recorded"));
		CompletableFuture.runAsync(() -> {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlToRead)).build();
			String connResult = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).join();
			ChatAndLogs.debug("New connection result received: " + connResult);
		});
//		try (BufferedReader reader = new BufferedReader(
//				new InputStreamReader(conn.getInputStream()))) {
//			for (String line; (line = reader.readLine()) != null; ) {
//				connResult.append(line);
//				ChatAndLogs.debug("New connection result received: " + connResult);
//			}
//		} catch (Exception e) {
//			ChatAndLogs.error("An error occurred when receiving the response from the API.\n" + Arrays.toString(e.getStackTrace()));
//		}
	}
}