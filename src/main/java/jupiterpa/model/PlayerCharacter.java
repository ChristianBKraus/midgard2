package jupiterpa.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

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

    @Transient
    int stBonus;
    @Transient
    int koBonus;
    @Transient
    int gwBonus;
    @Transient
    int gsBonus;

    List<Skill> skills = new ArrayList<>();
}
