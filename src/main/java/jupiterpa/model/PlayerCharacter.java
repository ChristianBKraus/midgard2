package jupiterpa.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor
public class PlayerCharacter {
    @Id
    String name;
    String user;

    String className;
    int level;
    String race;
    int spentLevel;
    int notSpentEp;
    int totalEp;
    int gold;

    int st;
    int gs;
    int gw;
    int ko;
    int in;
    int zt;
    int au;
    int pa;

    @Transient int stBonus;
    @Transient int gsBonus;
    @Transient int gwBonus;
    @Transient int koBonus;
    @Transient int inBonus;
    @Transient int ztBonus;
    @Transient int auBonus;
    @Transient int paBonus;

    int lp;
    int apWurf;
    @Transient int apBonus;
    @Transient int ap;

    List<Skill> skills = new ArrayList<>();
}
