package fr.traqueur.sphaleriabot.api.utils.jsons;

import java.io.File;

import com.google.gson.Gson;
import fr.traqueur.sphaleriabot.SphaleriaBot;
import fr.traqueur.sphaleriabot.api.DiscordBot;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import lombok.Getter;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

@Getter
public abstract class Saveable implements JsonPersist {
	
	public boolean needDir, needFirstSave;
	private DiscordBot bot;

	public Saveable(DiscordBot bot, String name) {
		this(bot, name, false, false);
	}
	
	public Saveable(DiscordBot bot, String name, boolean needDir, boolean needFirstSave) {
		this.bot = bot;
		this.needDir = needDir;
		this.needFirstSave = needFirstSave;
		if(this.needDir) {
			if(this.needFirstSave) {
				this.saveData();
			} else {
				File directory = this.getFile();
				if (!directory.exists()) {
					try {
						directory.mkdir();
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}
		}
	}

	public void registerCommand(ICommand command) { this.getBot().registerCommand(command); }
	public void registerListener(ListenerAdapter listener) {this.getBot().registerListener(listener);}

	public Gson getGson() {
		return this.getBot().getGson();
	}
}
