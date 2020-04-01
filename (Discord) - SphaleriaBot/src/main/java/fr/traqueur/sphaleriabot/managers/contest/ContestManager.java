package fr.traqueur.sphaleriabot.managers.contest;

import com.google.gson.reflect.TypeToken;
import fr.traqueur.sphaleriabot.api.DiscordBot;
import fr.traqueur.sphaleriabot.api.setting.SettingsManager;
import fr.traqueur.sphaleriabot.api.utils.jsons.DiscUtil;
import fr.traqueur.sphaleriabot.api.utils.jsons.Saveable;
import fr.traqueur.sphaleriabot.managers.contest.commands.ContestCreateCommand;
import fr.traqueur.sphaleriabot.managers.contest.commands.ContestLeaderboardCommand;
import fr.traqueur.sphaleriabot.managers.contest.commands.ContestRestoreCommand;
import fr.traqueur.sphaleriabot.managers.contest.commands.ContestStopCommand;
import fr.traqueur.sphaleriabot.managers.contest.listeners.ContestListener;
import fr.traqueur.sphaleriabot.managers.profiles.Profile;
import fr.traqueur.sphaleriabot.managers.profiles.ProfileManager;
import lombok.Getter;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.User;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class ContestManager extends Saveable {

    @Getter private static ContestManager instance;
    private Contest contest;
    private DiscordBot bot;

    public ContestManager(DiscordBot bot) {
        super(bot, "Contest");

        instance = this;
        this.bot = bot;
        contest = null;
        this.registerCommand(new ContestCreateCommand());
        this.registerCommand(new ContestStopCommand());
        this.registerCommand(new ContestLeaderboardCommand());
        this.registerCommand(new ContestRestoreCommand());
        this.registerListener(new ContestListener());
    }

    public boolean haveContest() {
        return this.contest != null;
    }

    public boolean initContest(String name) {
        if (this.contest != null) {
            return false;
        }
        Date date = new Date();
        SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
        this.contest = new Contest(name, form.format(date));
        return true;
    }

    public boolean restoreContest(String date) {
        String newDate = this.formatDate(date);
        File[] files = new File(this.getBot().getDataFolder(), "/contest/").listFiles();
        if (files == null ||files.length == 0) { return false; }
        for (File file: files) {
            if (file.getName().contains(newDate)) {
                String content = DiscUtil.readCatch(file);
                if (content != null) {
                    Type type = new TypeToken<Contest>() {}.getType();
                    Contest contest = this.getGson().fromJson(content, type);
                    if (contest.isFinish()) { return false; }
                    this.contest = contest;
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private String formatDate(String date) {
        String[] args = date.split("/");
        StringBuilder builder = new StringBuilder();
        for (String str: args) {
            builder.append("-").append(str);
        }
        return builder.toString();
    }

    public boolean stopContest() {
        if (this.contest == null) {
            return false;
        }
        this.getContest().setFinish(true);
        List<Participant> top = this.getClassement();
        StringBuilder builder = new StringBuilder();
        this.saveContest();
        this.contest = null;
        return true;
    }

    public void saveContest() {
        if (this.contest != null) {
            File file = new File(this.getBot().getDataFolder(), "contest/contest" + this.formatDate(this.contest.getDate()) + ".json");
            DiscUtil.writeCatch(file, this.getGson().toJson(this.contest));
            System.out.println("Contest saved with success");
        }
    }

    public boolean exist(User user) {
        if (contest == null) {
            return true;
        }
        for (Participant participant : this.contest.getParticipants()) {
            if (participant.getId() == user.getIdLong()) {
                return true;
            }
        }
        return false;
    }

    public void addParticipant(User user) {
        if (exist(user)) { return; }
        Participant participant = new Participant(user);
        this.contest.getParticipants().add(participant);
    }

    public Participant getParticipant(User user) {
        for (Participant p: this.contest.getParticipants()) {
            if (p.getId() == user.getIdLong()) {
                return p;
            }
        }
        return null;
    }

    public void addInvite(User user) {
        if (!exist(user)) { return; }
        Participant participant = this.getParticipant(user);
        if (participant != null) {
            participant.setInvites(participant.getInvites() + 1);
        }
    }

    public void removeInvite(User user) {
        if (!exist(user)) { return; }
        Participant participant = this.getParticipant(user);
        if (participant != null) {
            participant.setInvites(Math.max(0,participant.getInvites() - 1));
        }
    }

    public Invite getInvite(Guild guild) {
        List<Invite> newInvites = guild.getInvites().complete();
        for (Invite i : newInvites) {
            for (Invite i2 : ProfileManager.getInvites()) {
                if (i.getURL().equals(i2.getURL())) {
                    if (i.getUses() > i2.getUses()) {
                        ProfileManager.setInvites(newInvites);
                        return i;
                    }
                }
            }
        }
        ProfileManager.setInvites(newInvites);
        return null;
    }

    public List<Participant> getClassement() {
        List<Participant> top = new ArrayList<>();
        this.contest.getParticipants().stream().sorted().forEach(top::add);
        return top;
    }

    @Override
    public File getFile() {return null;}

    @Override
    public void loadData() { }

    @Override
    public void saveData() { }
}
