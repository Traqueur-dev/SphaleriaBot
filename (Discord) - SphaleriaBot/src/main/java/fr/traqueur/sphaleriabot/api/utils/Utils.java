package fr.traqueur.sphaleriabot.api.utils;

import net.dv8tion.jda.core.entities.Member;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Member> getMembersWithoutBots(List<Member> membersWithBots) {
        List<Member> members = new ArrayList<Member>();
        membersWithBots.forEach(m ->{
            if (!m.getUser().isBot()) {
                members.add(m);
            }
        });
        return members;
    }
}
