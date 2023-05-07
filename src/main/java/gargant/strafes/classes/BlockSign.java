package gargant.strafes.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import masecla.mlib.main.MLib;

@AllArgsConstructor
@Data
public class BlockSign {
    private String powerup;
    private int level = 0;
    private int duration = 20;

    public BlockSign(MLib lib, String[] lines) {
        this.powerup = lines[0];
        
        try {
            this.level = Integer.parseInt(lines[1]);
        } catch (NumberFormatException ex) {
            lib.getLoggerAPI().error("Invalid number formatted in powerup sign! [" + lines[1] + "]");
        }

        try {
            this.duration = Integer.parseInt(lines[2]) * 20;
        } catch (NumberFormatException ex) {
            lib.getLoggerAPI().error("Invalid number formatted in powerup sign! [" + lines[2] + "]");
        }
    }
}
