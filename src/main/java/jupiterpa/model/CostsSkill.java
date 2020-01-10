package jupiterpa.model;

import lombok.Data;

@Data
public class CostsSkill {
    String name;
    String attribute;
    String groups;
    int le;
    String costRow;

    public CostsSkill(String[] v) {
        name = v[0];
        attribute = v[1];
        groups = v[2];
        le = Integer.getInteger(v[3]);
        costRow = v[4];
    }
}
