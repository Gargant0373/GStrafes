package gargant.strafes.main;

import org.bukkit.plugin.java.JavaPlugin;

import gargant.strafes.classes.Items;
import gargant.strafes.classes.StrafesAPI;
import gargant.strafes.commands.LeapCommand;
import gargant.strafes.commands.StrafesCommand;
import gargant.strafes.containers.CooldownsContainer;
import gargant.strafes.containers.VelocityContainer;
import gargant.strafes.listeners.BlockPlaceRestriction;
import gargant.strafes.listeners.PowerupListener;
import gargant.strafes.listeners.StrafeClickListener;
import gargant.strafes.services.DatabaseService;
import gargant.strafes.services.PowerupService;
import lombok.Getter;
import masecla.mlib.main.MLib;

public class Strafes extends JavaPlugin {

	private MLib lib;
	private Items items;
	private DatabaseService databaseService;
	private PowerupService powerupService;

	@Getter
	private static StrafesAPI api;

	@Override
	public void onEnable() {
		this.lib = new MLib(this);
		lib.getConfigurationAPI().requireAll();

		this.items = new Items(lib);
		this.databaseService = new DatabaseService(lib);
		this.powerupService = new PowerupService(lib, databaseService);
		this.powerupService.load();

		api = new StrafesAPI(databaseService, powerupService);

		new VelocityContainer(lib, databaseService).register();
		new CooldownsContainer(lib, databaseService).register();

		new StrafesCommand(lib, items, databaseService).register();
		new LeapCommand(lib, items).register();

		new BlockPlaceRestriction(lib).register();

		new StrafeClickListener(lib, items, databaseService).register();

		new PowerupListener(lib, powerupService).register();

		lib.getLoggerAPI().information("Strafes Plugin has loaded!");
	}
}
