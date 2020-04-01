package fr.traqueur.sphaleriabot.managers.contest;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Contest {

    private String name;
    private String date;
    private List<Participant> participants;
    private boolean finish;

    public Contest(String name, String date) {
        this.name = name;
        this.date = date;
        this.participants = new ArrayList<>();
        this.finish = false;
    }
}
