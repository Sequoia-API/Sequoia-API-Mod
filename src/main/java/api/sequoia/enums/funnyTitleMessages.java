package api.sequoia.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class funnyTitleMessages {
   static List<String> funnyTitleMessages = new ArrayList<>();

   public static void register() {
       funnyTitleMessages.add("Lama is very proud of you");
       funnyTitleMessages.add("You did it, yay!");
       funnyTitleMessages.add(":)");
       funnyTitleMessages.add("Only 99 wars to go for the 4LE prize!");
       funnyTitleMessages.add("Wow, another war worth a total of 1 emerald!");
       funnyTitleMessages.add("The tower had a wife and children");

   }

   public static String randomMessage() {
       Random random = new Random();
       int randomInt = random.nextInt(0, funnyTitleMessages.size());
       return funnyTitleMessages.get(randomInt);
   }

}
