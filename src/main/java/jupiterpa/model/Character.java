package jupiterpa.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor
public class Character {
    UUID id;
    String name;

    String className;
    int level;
    int notSpentEp;
    int totalEp;

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
