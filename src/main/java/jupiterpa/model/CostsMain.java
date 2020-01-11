package jupiterpa.model;

import lombok.Data;

@Data
public class CostsMain {
    String costRow;
    int bonus;
    int multiplier;

    public CostsMain(String[] v) {
        costRow = v[0];
        bonus = Integer.parseInt(v[1]);
        multiplier = Integer.parseInt(v[2]);
    }
}
