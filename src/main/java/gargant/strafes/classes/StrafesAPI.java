package gargant.strafes.classes;

import gargant.strafes.services.DatabaseService;
import gargant.strafes.services.PowerupService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class StrafesAPI {
    @Getter
    private DatabaseService databaseService;
    @Getter
    private PowerupService powerupService;
}
