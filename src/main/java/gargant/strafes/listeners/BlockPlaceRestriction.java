package gargant.strafes.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import masecla.mlib.classes.Registerable;
import masecla.mlib.main.MLib;

public class BlockPlaceRestriction extends Registerable {

	public BlockPlaceRestriction(MLib lib) {
		super(lib);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (event.getPlayer().getInventory().getItemInMainHand() == null)
			return;
		ItemStack held = event.getItemInHand();
		@SuppressWarnings("deprecation")
		String tag = lib.getNmsAPI().getNBTTagValueString(held, "StrafeDirection");
		if (tag == null)
			return;
		switch (tag) {
		case "LEFT":
		case "RIGHT":
		case "BACK":
		case "COOLDOWN":
		case "LEAP":
			event.setCancelled(true);
		default:
			return;
		}

	}

}
