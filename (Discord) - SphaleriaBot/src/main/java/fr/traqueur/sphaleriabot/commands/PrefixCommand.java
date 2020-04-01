package fr.traqueur.sphaleriabot.commands;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.api.setting.Settings;
import fr.traqueur.sphaleriabot.api.setting.SettingsManager;
import net.dv8tion.jda.core.EmbedBuilder;

public class PrefixCommand extends ICommand {

    @Command(name = {"prefix"}, permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224", description = "Permet de changer le prefix des commandes")
    public void onCommand(CommandArgs args) {
        SettingsManager manager = SettingsManager.getInstance();
        if (args.size() != 1) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("Usage:", args.getPrefix() + "prefix <prefix>", true);
            args.getChannel().sendMessage(builder.build()).queue();
            return;
        }
        String prefix = args.getArgs(0);
        manager.changePrefix(prefix);
        EmbedBuilder builder = args.getSuccess();
        builder.addField("Succés:", args.getSender() + " vient de modifier le prefix des commandes en: " + prefix
                + ".", true);
        args.getChannel().sendMessage(builder.build()).queue();
    }
}
