package fr.traqueur.sphaleriabot.api.commands;

import fr.traqueur.sphaleriabot.api.Bot;
import fr.traqueur.sphaleriabot.api.commands.annotations.Command;
import fr.traqueur.sphaleriabot.api.utils.EmbedHelper;
import fr.traqueur.sphaleriabot.api.setting.Settings;
import fr.traqueur.sphaleriabot.api.setting.SettingsManager;
import fr.traqueur.sphaleriabot.commands.PrefixCommand;
import lombok.Getter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandFramework extends ListenerAdapter {

    private Map<String, Map.Entry<Method, Object>> commandMap;
    @Getter private static String prefix;
    private static @Getter EmbedBuilder helper;

    public CommandFramework(Bot bot) {
        this.commandMap = new HashMap<>();
        CommandFramework.helper = new EmbedBuilder();
        CommandFramework.helper.setColor(Color.GREEN);
        CommandFramework.helper.setTitle("Liste des commandes:");
        CommandFramework.helper.setDescription(bot.getName());
        CommandFramework.prefix = SettingsManager.getInstance().getSettings().getPrefix();
        bot.getClient().addEventListener(this);
        this.registerCommands(new HelpCommand());
    }

    public void registerCommands(ICommand obj) {
        System.out.println("Enregistrement de la commande " + obj.getClass().getName() + "...");
        for (Method m : obj.getClass().getMethods()) {
            if (m.getAnnotation(Command.class) == null) { continue; }
            Command command = m.getAnnotation(Command.class);
            if (m.getParameterTypes().length > 1 || (m.getParameterTypes()[0] != CommandArgs.class)) {
                System.out.println("Unable to register command " + m.getName() + ". Arguments not matched.");
            } else {
                this.addHelp(command);
                for (String alias: command.name()) {
                    registerCommand(command, alias, m, obj);
                }
                System.out.println("Enregistrement effectuée avec succés.");
            }
        }
    }

    private void registerCommand(Command command, String alias, Method m, Object obj) {
        commandMap.put(alias.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));
    }

    public void addHelp(Command command) {
        String[] args = command.name()[0].split("\\.");
        StringBuilder builder = new StringBuilder();
        Settings settings = SettingsManager.getInstance().getSettings();
        for (String s: args) {
            builder.append(s + " ");
        }
        CommandFramework.helper.addField(builder.toString(), command.description(), false);
    }

    public void handleCommand(MessageReceivedEvent event) {
        if (event.getChannelType() != ChannelType.TEXT) {return;}
        Settings settings = SettingsManager.getInstance().getSettings();
        if (!event.getMessage().getContentRaw().startsWith(settings.getPrefix())) { return;}
        String[] msg = event.getMessage().getContentRaw().split(" ");
        for (int i = msg.length; i > 0 ; i--) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(msg[0].replace(settings.getPrefix(), ""));
            for (int x = 1; x < i; x++) {
                buffer.append(".").append(msg[x].toLowerCase());
            }
            String cmdLabel = buffer.toString();
            if (commandMap.containsKey(cmdLabel)) {
                Method m = commandMap.get(cmdLabel).getKey();
                Object object = commandMap.get(cmdLabel).getValue();
                Command command = m.getAnnotation(Command.class);
                boolean hasPerm = false;
                if (command.permittedRoles().length == 0) {
                    hasPerm = true;
                }
                if (command.permittedUsers().length == 0) {
                    hasPerm = true;
                }
                if (command.permittedUsers().length != 0) {
                    List<String> users = Arrays.asList(command.permittedUsers());
                    if (users.contains(event.getAuthor().getId())) {
                        hasPerm = true;
                    }
                }
                if (command.permittedRoles().length != 0) {
                    List<String> roles = Arrays.asList(command.permittedRoles());
                    for (Role role: event.getMember().getRoles()) {
                        if (roles.contains(role.getId())) {
                            hasPerm = true;
                        }
                    }
                }

                if (!hasPerm) {
                    EmbedBuilder builder = EmbedHelper.getErrorEmbed();
                    builder.addField("Erreur:","Vous n'avez pas la permission de faire cette commande", true);
                    event.getChannel().sendMessage(builder.build()).queueAfter(5, TimeUnit.SECONDS, message -> {
                        message.delete().queue();
                    });
                    return;
                }
                CommandArgs commandArgs = new CommandArgs(event, msg, cmdLabel.split("\\.").length, settings.getPrefix());
                try {
                    m.invoke(object, new Object[] {commandArgs});
                    System.out.println(event.getAuthor().getName() + " execute command " + cmdLabel + "...");
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        defaultCommand(new CommandArgs(event, msg, 0, settings.getPrefix()));
    }
    private void defaultCommand(CommandArgs args) {
        args.getChannel().sendMessage(args.getError().addField("Erreur:", "Cette commande n'existe pas.", true).build()).queueAfter(5, TimeUnit.SECONDS, message -> {
            message.delete().queue();
        });
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        handleCommand(event);
    }
}
