package fr.traqueur.sphaleriabot.api;

import fr.traqueur.sphaleriabot.SphaleriaBot;
import fr.traqueur.sphaleriabot.api.tasks.SaveTask;
import fr.traqueur.sphaleriabot.api.utils.MultiThreading;
import fr.traqueur.sphaleriabot.commands.StopCommand;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Getter
public class Bot {

	// base
	private JDA client = null;
	private boolean started = false;
	private String token;
	private String name;
	private List<ListenerAdapter> listeners;
	public Bot(String token, String name) {
		this.token = token;
		this.name = name;
		this.listeners = new ArrayList<>();
	}

	// get
	public JDA getClient() {
		return client;
	}

	public boolean isStarted() {
		return started;
	}

	// methods
	public void start() {
		if (started) {
			return;
		}
		try {
			System.out.println("Connecting DiscordBot...");
			JDABuilder builder = new JDABuilder(AccountType.BOT);
			builder.setToken(token);
			client = builder.build();
			client.getPresence().setStatus(OnlineStatus.ONLINE);
			client.getPresence().setGame(Game.playing("Sphaleria!"));
			this.loadListeners();
			client.awaitReady();
			MultiThreading.schedule(new SaveTask(), 10, 3600, TimeUnit.SECONDS);
			started = true;
			System.out.println("Discord bot is connected (" + client.getSelfUser().getAsTag() + ")");
		} catch (Throwable exception) {
			exception.printStackTrace();
			System.out.println("Couldn't connect Discord bot");
		}
	}

	public void registerListener(ListenerAdapter adapter) { this.listeners.add(adapter); }

	private void loadListeners() {
		for (ListenerAdapter l: this.listeners) {
			System.out.println("Enregistrement du listener " + l.getClass().getName() + "...");
			this.client.addEventListener(l);
			System.out.println("Enregistrement effectuée avec succés.");
		}
	}

	public void stop() {
		if (!started) {
			return;
		}
		if (client != null) {
			DiscordBot.getInstance().savePersists();
			MultiThreading.POOL.shutdown();
			MultiThreading.RUNNABLE_POOL.shutdown();
			client.shutdown();
			System.out.println("Discord bot is disconnected...");
			client = null;
		}
	}

}
