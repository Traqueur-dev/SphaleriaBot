package fr.traqueur.sphaleriabot.managers.count;

import com.google.gson.reflect.TypeToken;
import fr.traqueur.sphaleriabot.api.DiscordBot;
import fr.traqueur.sphaleriabot.api.utils.Utils;
import fr.traqueur.sphaleriabot.api.utils.jsons.DiscUtil;
import fr.traqueur.sphaleriabot.api.utils.jsons.Saveable;
import fr.traqueur.sphaleriabot.managers.count.commands.CountIDCommand;
import fr.traqueur.sphaleriabot.managers.count.commands.CountNameCommand;
import fr.traqueur.sphaleriabot.managers.count.listeners.CountListener;
import lombok.Getter;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CountManager extends Saveable {

    @Getter private static CountManager instance;

    private CountSettings settings;

    public CountManager(DiscordBot bot) {
        super(bot, "Count");

        instance = this;

        settings = new CountSettings();

        this.registerCommand(new CountNameCommand());
        this.registerCommand(new CountIDCommand());
        this.registerListener(new CountListener());
    }

    @Override
    public File getFile() {
        return new File(this.getBot().getDataFolder(), "/count/settings.json");
    }

    public void updateNameChannel(String name) {
        this.settings.setNameCountChannel(name);
    }

    public void updateIDChannel(long id) {
        this.settings.setCountChannelId(id);
    }

    public void updateMembersCount(Guild guild) {
        List<Member> membersWithBots = guild.getMembers();
        List<Member> members = Utils.getMembersWithoutBots(membersWithBots);

        if (settings.getCountChannelId() == null) {
            VoiceChannel channel = (VoiceChannel) guild.getController().createVoiceChannel(settings.getNameCountChannel() + " " + members.size()).complete();
            channel.putPermissionOverride(guild.getPublicRole()).setDeny(Permission.VOICE_CONNECT).complete();
            settings.setCountChannelId(channel.getIdLong());
            return;
        }
        VoiceChannel channel = (VoiceChannel) guild.getVoiceChannelById(settings.getCountChannelId());
        channel.getManager().setName(settings.getNameCountChannel() + " " + members.size()).complete();
    }

    @Override
    public void loadData() {
        String content = DiscUtil.readCatch(this.getFile());
        if (content != null) {
            Type type = new TypeToken<CountSettings>() {}.getType();
            this.settings = this.getGson().fromJson(content, type);
        }
    }

    @Override
    public void saveData() { DiscUtil.writeCatch(this.getFile(), this.getGson().toJson(this.settings)); }
}
