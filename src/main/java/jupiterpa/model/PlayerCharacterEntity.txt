package jupiterpa.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor
public class PlayerCharacterEntity {
    @Id
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

    List<SkillEntity> skills = new ArrayList<>();
}
