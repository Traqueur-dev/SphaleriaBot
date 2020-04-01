package fr.traqueur.sphaleriabot.managers.staff;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StaffSettings {

    private List<Long> staffRolesID;
    private long channelIDAnnoucementStaff;

    public StaffSettings() {
        this.staffRolesID = new ArrayList<>();
        this.channelIDAnnoucementStaff = 0L;
    }

}
