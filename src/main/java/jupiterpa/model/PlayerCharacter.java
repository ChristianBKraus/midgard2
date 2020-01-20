package jupiterpa.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor
public class PlayerCharacter {
    String name;
    String user;

    String className;
    int level;
    int notSpentEp;
    int totalEp;
    int gold;

    int st;
    int ko;
    int gw;
    int gs;

    int stBonus;
    int koBonus;
    int gwBonus;
    int gsBonus;

    List<Skill> skills = new ArrayList<>();
}
