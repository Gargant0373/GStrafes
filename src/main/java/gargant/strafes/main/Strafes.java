package gargant.strafes.main;

import org.bukkit.plugin.java.JavaPlugin;

import gargant.strafes.commands.LeapCommand;
import gargant.strafes.commands.StrafesCommand;
import masecla.mlib.main.MLib;

public class Strafes extends JavaPlugin {
	private MLib lib;

	@Override
	public void onEnable() {
		this.lib = new MLib(this);
		lib.getConfigurationAPI().requireAll();

		new StrafesCommand(lib).register();
		new LeapCommand(lib).register();

		lib.getLoggerAPI().information("Strafes Plugin has loaded!");
	}
}
