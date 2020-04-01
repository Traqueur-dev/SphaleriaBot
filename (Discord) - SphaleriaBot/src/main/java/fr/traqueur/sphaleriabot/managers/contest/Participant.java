package fr.traqueur.sphaleriabot.managers.contest;

import lombok.Data;
import net.dv8tion.jda.core.entities.User;
import org.jetbrains.annotations.NotNull;

@Data
public class Participant implements Comparable<Participant> {

    private String name;
    private Long id;
    private int invites;

    public Participant(User user) {
        this.name = user.getName();
        this.id = user.getIdLong();
        this.invites = 0;
    }

    @Override
    public int compareTo(@NotNull Participant o) {
        return this.invites - o.getInvites();
    }
}
