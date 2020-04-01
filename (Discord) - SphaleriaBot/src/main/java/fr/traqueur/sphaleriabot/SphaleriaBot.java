package fr.traqueur.sphaleriabot;

import fr.traqueur.sphaleriabot.api.DiscordBot;
import fr.traqueur.sphaleriabot.commands.ClearCommand;
import fr.traqueur.sphaleriabot.commands.PrefixCommand;
import fr.traqueur.sphaleriabot.commands.SaveCommand;
import fr.traqueur.sphaleriabot.commands.StopCommand;
import fr.traqueur.sphaleriabot.managers.bad.BadManager;
import fr.traqueur.sphaleriabot.managers.contest.ContestManager;
import fr.traqueur.sphaleriabot.managers.count.CountManager;
import fr.traqueur.sphaleriabot.managers.profiles.ProfileManager;
import fr.traqueur.sphaleriabot.managers.staff.StaffManager;
import lombok.Getter;

@Getter
public class SphaleriaBot extends DiscordBot {

    public static void main(String[] args) { new SphaleriaBot(); }

    public SphaleriaBot() {
        super("SphaleriaBot");
    }

    @Override
    public void registerManagers() {
        this.registerPersist(new CountManager(this));
        this.registerPersist(new ProfileManager(this));
        this.registerPersist(new StaffManager(this));
        this.registerPersist(new BadManager(this));
        this.registerPersist(new ContestManager(this));
    }

    @Override
    public void registerListeners() { }

    @Override
    public void registerCommands() {
        this.registerCommand(new StopCommand());
        this.registerCommand(new SaveCommand());
        this.registerCommand(new ClearCommand());
        this.registerCommand(new PrefixCommand());
    }
}
