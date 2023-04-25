package gargant.strafes.main;

import org.bukkit.plugin.java.JavaPlugin;

import gargant.strafes.classes.Items;
import gargant.strafes.commands.LeapCommand;
import gargant.strafes.commands.StrafesCommand;
import gargant.strafes.containers.CooldownsContainer;
import gargant.strafes.containers.VelocityContainer;
import gargant.strafes.listeners.BlockPlaceRestriction;
import gargant.strafes.listeners.StrafeClickListener;
import gargant.strafes.services.DatabaseService;
import masecla.mlib.main.MLib;

public class Strafes extends JavaPlugin {

	private MLib lib;
	private Items items;
	private DatabaseService databaseService;

	@Override
	public void onEnable() {
		this.lib = new MLib(this);
		lib.getConfigurationAPI().requireAll();

		this.items = new Items(lib);
		this.databaseService = new DatabaseService(lib);

		new VelocityContainer(lib, databaseService).register();
		new CooldownsContainer(lib, databaseService).register();

		new StrafesCommand(lib, items, databaseService).register();
		new LeapCommand(lib, items).register();

		new BlockPlaceRestriction(lib).register();

		new StrafeClickListener(lib, items).register();

		lib.getLoggerAPI().information("Strafes Plugin has loaded!");
	}
}
