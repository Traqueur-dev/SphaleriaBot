package fr.traqueur.sphaleriabot.managers.profiles;

import com.google.gson.reflect.TypeToken;
import fr.traqueur.sphaleriabot.api.DiscordBot;
import fr.traqueur.sphaleriabot.api.setting.Settings;
import fr.traqueur.sphaleriabot.api.utils.jsons.DiscUtil;
import fr.traqueur.sphaleriabot.api.utils.jsons.Saveable;
import fr.traqueur.sphaleriabot.managers.profiles.commands.InviteAddCommand;
import fr.traqueur.sphaleriabot.managers.profiles.commands.InviteCommand;
import fr.traqueur.sphaleriabot.managers.profiles.commands.InviteRemoveCommand;
import fr.traqueur.sphaleriabot.managers.profiles.commands.LeaderboardCommand;
import fr.traqueur.sphaleriabot.managers.profiles.listeners.ProfileListener;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.User;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ProfileManager extends Saveable {

    @Getter private static ProfileManager instance;
    @Getter @Setter private static List<Invite> invites;
    private List<Profile> profiles;
    private ProfileSettings settings;

    public ProfileManager(DiscordBot bot) {
        super(bot, "Profiles");

        instance = this;
        profiles = new ArrayList<>();
        invites = new ArrayList<>();
        this.settings = new ProfileSettings();
        this.registerCommand(new InviteCommand());
        this.registerCommand(new InviteAddCommand());
        this.registerCommand(new InviteRemoveCommand());
        this.registerCommand(new LeaderboardCommand());
        this.registerListener(new ProfileListener());
    }

    @Override
    public File getFile() {
        return new File(this.getBot().getDataFolder(), "/profiles/profiles.json");
    }

    public File getSettingsFile() {
        return new File(this.getBot().getDataFolder(), "/profiles/settings.json");
    }

    @Override
    public void loadData() {
        String content = DiscUtil.readCatch(this.getFile());
        if (content != null) {
            Type type = new TypeToken<List<Profile>>() {}.getType();
            this.profiles = this.getGson().fromJson(content, type);
        }

        content = DiscUtil.readCatch(this.getSettingsFile());
        if (content != null) {
            Type type = new TypeToken<ProfileSettings>() {}.getType();
            this.settings = this.getGson().fromJson(content, type);
        }
    }

    @Override
    public void saveData() {
        DiscUtil.writeCatch(this.getFile(), this.getGson().toJson(this.profiles));
        DiscUtil.writeCatch(this.getSettingsFile(), this.getGson().toJson(this.settings));
    }

    public Profile getProfile(User user) {
        return this.getProfile(user.getIdLong());
    }

    public Profile getProfile(long id) {
        for (Profile profile: this.getProfiles()) {
            if (profile.getId() == id) {
                return profile;
            }
        }
        return null;
    }

    public boolean exist(long id) {
        return this.getProfile(id) != null;
    }

    public boolean createProfile(User user, User inviter) {
        if (exist(user.getIdLong())) { return false; }
        this.addInvite(inviter);
        Profile profile = new Profile(user, inviter);
        this.profiles.add(profile);
        return true;
    }

    public void addInvite(User user)  {
        Profile profile = this.getProfile(user);
        if (profile != null) {
            profile.setJoinInvite(profile.getJoinInvite() + 1);
        }
    }

    public boolean deleteProfile(User user) {
        Profile profile = this.getProfile(user);
        if (profile != null) {
            Profile inviter = this.getProfile(profile.getIdInviter());
            if (inviter != null) {
                inviter.setLeaveInvite(inviter.getLeaveInvite() + 1);
            }
            this.profiles.removeIf(p -> p.getId() == profile.getId());
            return true;
        }
        return false;
    }

    public int getInvites(User user) {
        Profile profile = this.getProfile(user);
        return profile.getInvites();
    }

    public int getInvites(Profile profile) {
        return profile.getInvites();
    }

    public boolean addInvite(Profile profile, int n) {
        if (profile != null) {
            profile.setBonusInvite(profile.getBonusInvite() + n);
            return true;
        }
        return false;
    }

    public boolean removeInvite(Profile profile, int n) {
        if (profile != null) {
            profile.setBonusInvite(Math.max(profile.getBonusInvite() - n, 0));
            return true;
        }
        return false;
    }

    public Invite getInvite(Guild guild) {
        List<Invite> newInvites = guild.getInvites().complete();
        for (Invite i : newInvites) {
            for (Invite i2 : ProfileManager.getInvites()) {
                if (i.getURL().equals(i2.getURL())) {
                    if (i.getUses() > i2.getUses()) {
                        ProfileManager.invites = newInvites;
                        return i;
                    }
                }
            }
        }
        ProfileManager.invites = newInvites;
        return null;
    }

    public List<Profile> getClassement() {
        List<Profile> top = new ArrayList<>();
        this.profiles.stream().sorted().forEach(top::add);
        return top;
    }
}
