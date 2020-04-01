package fr.traqueur.sphaleriabot.api.setting;

import com.google.gson.reflect.TypeToken;
import fr.traqueur.sphaleriabot.SphaleriaBot;
import fr.traqueur.sphaleriabot.api.DiscordBot;
import fr.traqueur.sphaleriabot.api.utils.jsons.DiscUtil;
import fr.traqueur.sphaleriabot.api.utils.jsons.Saveable;
import lombok.Getter;
import net.dv8tion.jda.core.entities.Invite;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

@Getter
public class SettingsManager extends Saveable {

    private static @Getter SettingsManager instance;

    private Settings settings;

    public SettingsManager(DiscordBot bot) {
        super(bot,  "Settings");

        instance = this;
        this.settings = new Settings();
    }

    public void changePrefix(String prefix) {
        this.settings.setPrefix(prefix);
    }

    @Override
    public File getFile() {
        return new File(this.getBot().getDataFolder(), "settings.json");
    }

    @Override
    public void loadData() {
        String content = DiscUtil.readCatch(this.getFile());
        if (content != null) {
            Type type = new TypeToken<Settings>() {}.getType();
            this.settings = this.getGson().fromJson(content, type);
        }
    }

    @Override
    public void saveData() {
        DiscUtil.writeCatch(this.getFile(), this.getGson().toJson(this.settings));
    }
}
