package gargant.strafes.classes;

import org.bukkit.Material;
import org.bukkit.block.Block;

import lombok.AllArgsConstructor;
import lombok.Data;
import masecla.mlib.main.MLib;

@AllArgsConstructor
@Data
public class BlockSign {
    private String powerup;
    private int level = 0;
    private int duration = 20;

    public BlockSign(MLib lib, Block b, String[] lines) {
        if(ogPowerup(b, lines)) return;

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

    private boolean ogPowerup(Block b, String[] lines) {
        try {
            this.level = Integer.parseInt(lines[0]);
            this.duration = Integer.parseInt(lines[1]) * 20;
            if (b.getType().equals(Material.REDSTONE_BLOCK)) this.powerup = "speed";
            else if (b.getType().equals(Material.EMERALD_BLOCK)) this.powerup = "jump";
            else return false;
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
