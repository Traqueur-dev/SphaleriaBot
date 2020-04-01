package fr.traqueur.sphaleriabot.managers.count.commands;

import fr.traqueur.sphaleriabot.api.commands.CommandArgs;
import fr.traqueur.sphaleriabot.api.commands.ICommand;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.managers.count.CountManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

public class CountIDCommand extends ICommand {

    @Command(name = {"count.setid", "count.id"},  permittedRoles = {"537682405893341185", "537682450407620637"},
            permittedUsers = "285375027480756224", description = "Permet de changer le channel compteur de membres.")
    public void onCommand(CommandArgs args) {
        TextChannel channel = args.getChannel();
        CountManager manager = CountManager.getInstance();
        if (args.size() != 1) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("Usage:", args.getPrefix() + "count setid <name>", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }

        Long id = args.asLong(0);
        if (id == null) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("Information:", "Veuillez saisir un id correcte.", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }

        if (args.getGuild().getVoiceChannelById(id) == null) {
            EmbedBuilder builder = args.getInfo();
            builder.addField("Information:", "Veuillez saisir un id qui correspond à un salon vocal.", true);
            channel.sendMessage(builder.build()).queue();
            return;
        }

        manager.updateIDChannel(id);
        EmbedBuilder builder = args.getSuccess();
        builder.addField("Succés:", args.getSender() + " vient de modifier le channel qui comptabilise membres.", true);
        channel.sendMessage(builder.build()).queue();
        manager.updateMembersCount(args.getGuild());
    }


}
