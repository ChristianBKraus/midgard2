package jupiterpa.model;

import lombok.Data;

@Data
public class CostsMain {
    String costRow;
    int bonus;
    int multiplier;

    public CostsMain(String[] v) {
        costRow = v[0];
        bonus = Integer.getInteger(v[1]);
        multiplier = Integer.getInteger(v[2]);
    }
}
