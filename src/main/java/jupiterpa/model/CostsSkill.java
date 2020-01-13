package jupiterpa.model;

import lombok.Data;

@Data
public class CostsSkill {
    String name;
    int startBonus;
    String attribute;
    String groups;
    int le;
    String costRow;

    public CostsSkill(String[] v) {
        name = v[0];
        startBonus = Integer.parseInt(v[1]);
        attribute = v[2];
        groups = v[3];
        le = Integer.parseInt(v[4]);
        costRow = v[5];
    }
}
