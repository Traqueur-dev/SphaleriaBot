package fr.traqueur.sphaleriabot.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.traqueur.sphaleriabot.api.commands.CommandFramework;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.utils.EmbedHelper;
import fr.traqueur.sphaleriabot.api.utils.jsons.JsonPersist;
import fr.traqueur.sphaleriabot.api.setting.SettingsManager;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.File;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class DiscordBot {

    @Getter private static DiscordBot instance;
    private Bot client;
    private CommandFramework framework;
    private SettingsManager manager;
    private List<JsonPersist> persists;
    private List<ICommand> commands;
    private Gson gson;
    private String name;

    public DiscordBot(String name) {
        instance = this;
        this.name = name;
        EmbedHelper.setFooterText(name);
        this.getDataFolder().mkdir();
        this.gson = this.createGsonBuilder().create();
        this.persists = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.manager = new SettingsManager(this);
        this.registerPersist(manager);
        manager.loadData();
        client = new Bot(SettingsManager.getInstance().getSettings().getToken(), name);
        this.registerListeners();
        this.registerManagers();
        this.loadPersists();
        client.start();
        this.framework = new CommandFramework(this.client);
        this.registerCommands();
        this.loadCommands();
    }

    public void registerListener(ListenerAdapter adapter) { this.getClient().registerListener(adapter); }
    public void registerPersist(JsonPersist persist) {
        this.persists.add(persist);
    }
    public void registerCommand(ICommand command) { this.commands.add(command); }

    public void loadPersists() {
        for (JsonPersist persist: this.persists) {
            persist.loadData();
        }
        System.out.println("Data loaded with success...");
    }

    private void loadCommands() {
        for (ICommand c: this.commands) {
            this.framework.registerCommands(c);
        }
    }

    public void savePersists() {
        for (JsonPersist persist: this.persists) {
            persist.saveData();
        }
        System.out.println("Data saved with success...");
    }

    public File getDataFolder() {
        return new File(Paths.get("").toAbsolutePath().toFile(), this.name + "/");
    }

    private GsonBuilder createGsonBuilder() {
        GsonBuilder ret = new GsonBuilder();

        ret.setPrettyPrinting();
        ret.disableHtmlEscaping();
        ret.excludeFieldsWithModifiers(Modifier.TRANSIENT);
        return ret;
    }

    public abstract void registerManagers();
    public abstract void registerListeners();
    public abstract void registerCommands();
}
