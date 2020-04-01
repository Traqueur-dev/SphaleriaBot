package fr.traqueur.sphaleriabot.managers.bad;

import com.google.gson.reflect.TypeToken;
import fr.traqueur.sphaleriabot.api.DiscordBot;
import fr.traqueur.sphaleriabot.api.commands.CommandFramework;
import fr.traqueur.sphaleriabot.api.utils.jsons.DiscUtil;
import fr.traqueur.sphaleriabot.api.utils.jsons.Saveable;
import fr.traqueur.sphaleriabot.managers.bad.commands.BadChannelAddCommand;
import fr.traqueur.sphaleriabot.managers.bad.commands.BadChannelListCommand;
import fr.traqueur.sphaleriabot.managers.bad.commands.BadChannelRemoveCommand;
import fr.traqueur.sphaleriabot.managers.bad.commands.link.LinkAddCommand;
import fr.traqueur.sphaleriabot.managers.bad.commands.link.LinkListCommand;
import fr.traqueur.sphaleriabot.managers.bad.commands.link.LinkRemoveCommand;
import fr.traqueur.sphaleriabot.managers.bad.commands.words.WordAddCommand;
import fr.traqueur.sphaleriabot.managers.bad.commands.words.WordListCommand;
import fr.traqueur.sphaleriabot.managers.bad.commands.words.WordRemoveCommand;
import fr.traqueur.sphaleriabot.managers.bad.listeners.BadListener;
import lombok.Getter;

import java.io.File;
import java.lang.reflect.Type;

@Getter
public class BadManager extends Saveable {

    private static @Getter BadManager instance;

    private BadSettings settings;

    public BadManager(DiscordBot bot) {
        super(bot, "Bad");
        instance = this;
        this.settings = new BadSettings();
        this.registerCommand(new LinkRemoveCommand());
        this.registerCommand(new LinkAddCommand());
        this.registerCommand(new LinkListCommand());
        this.registerCommand(new WordAddCommand());
        this.registerCommand(new WordListCommand());
        this.registerCommand(new WordRemoveCommand());
        this.registerCommand(new BadChannelAddCommand());
        this.registerCommand(new BadChannelListCommand());
        this.registerCommand(new BadChannelRemoveCommand());
        this.registerListener(new BadListener());
    }

    @Override
    public File getFile() {
        return new File(this.getBot().getDataFolder(), "/bad/settings.json");
    }

    public boolean linkExit(String link) {
        return this.settings.getBadLinks().contains(link.toLowerCase());
    }

    public boolean wordExit(String word) {
        return this.settings.getBadWords().contains(word.toLowerCase());
    }

    public boolean channelExit(Long id) {
        return this.settings.getIgnoredChannelID().contains(id);
    }

    public boolean addChannel(Long id) {
        if (channelExit(id)) {
            return false;
        }
        this.settings.getIgnoredChannelID().add(id);
        return  true;
    }

    public boolean removeChannel(Long id) {
        return this.settings.getIgnoredChannelID().removeIf(i -> i.equals(id));
    }

    public boolean addWord(String word) {
        if (wordExit(word)) {
            return false;
        }
        this.settings.getBadWords().add(word);
        return  true;
    }

    public boolean removeWord(String word) {
        return this.settings.getBadWords().removeIf(w -> w.equalsIgnoreCase(word));
    }

    public boolean addLink(String link) {
        if (linkExit(link)) {
            return false;
        }
        this.settings.getBadLinks().add(link);
        return  true;
    }

    public boolean removeLink(String link) {
        return this.settings.getBadWords().removeIf(l -> l.equalsIgnoreCase(link));
    }

    @Override
    public void loadData() {
        String content = DiscUtil.readCatch(this.getFile());
        if (content != null) {
            Type type = new TypeToken<BadSettings>() {}.getType();
            this.settings = this.getGson().fromJson(content, type);
        }
    }

    @Override
    public void saveData() {
        DiscUtil.writeCatch(this.getFile(), this.getGson().toJson(this.settings));
    }
}
