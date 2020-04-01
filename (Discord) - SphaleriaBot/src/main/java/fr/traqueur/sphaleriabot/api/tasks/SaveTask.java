package fr.traqueur.sphaleriabot.api.tasks;

import fr.traqueur.sphaleriabot.api.DiscordBot;

public class SaveTask implements Runnable {

    public static long save() {
        DiscordBot bot = DiscordBot.getInstance();
        long time = System.currentTimeMillis();
        bot.savePersists();
        time = System.currentTimeMillis() - time;
        return time;
    }

    @Override
    public void run() {
        System.out.println("All data saved successfully! (" + save() + "ms)");
    }
}
