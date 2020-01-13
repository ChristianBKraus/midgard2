package jupiterpa.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor
public class PlayerCharacterEntity {
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

    List<SkillEntity> skills = new ArrayList<>();
}
