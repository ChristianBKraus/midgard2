package jupiterpa.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor
public class PlayerCharacterEntity {
    @Id
    long id;
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

    List<SkillEntity> skills = new ArrayList<>();
}
