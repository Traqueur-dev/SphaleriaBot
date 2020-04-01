package fr.traqueur.sphaleriabot.managers.profiles;

import lombok.Data;
import net.dv8tion.jda.core.entities.User;
import org.jetbrains.annotations.NotNull;

@Data
public class Profile implements Comparable<Profile> {

    private long idInviter;
    private long id;
    private String name;
    private boolean isMuted;

    private int joinInvite;
    private int leaveInvite;
    private int bonusInvite;

    public Profile(User user, User inviter) {
        this.idInviter = inviter.getIdLong();
        this.id = user.getIdLong();
        this.name = user.getName();
        this.joinInvite = 0;
        this.leaveInvite = 0;
        this.bonusInvite = 0;
    }

    public int getInvites() {
        int joinInvites = this.getJoinInvite();
        int leaveInvites = this.getLeaveInvite();
        int bonusInvites = this.getBonusInvite();
        return Math.max(joinInvites - leaveInvites + bonusInvites, 0);
    }

    @Override
    public int compareTo(@NotNull Profile o) {
        return this.getInvites() - o.getInvites();
    }
}
