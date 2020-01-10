package jupiterpa.model;

import lombok.Data;

@Data
public class CostsMain {
    String costRow;
    int bonus;
    int multiplier;

    public CostsMain(String[] values) {

    }
}
