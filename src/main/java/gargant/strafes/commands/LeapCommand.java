package gargant.strafes.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import gargant.strafes.classes.Items;
import masecla.mlib.annotations.RegisterableInfo;
import masecla.mlib.classes.Registerable;
import masecla.mlib.main.MLib;

@RegisterableInfo(command = "leap")
public class LeapCommand extends Registerable {

	private Items items;

	public LeapCommand(MLib lib, Items items) {
		super(lib);
		this.items = items;
	}

	@Override
	public void fallbackCommand(CommandSender sender, String[] args) {
		if (!(sender instanceof Player))
			return;
		if (!sender.hasPermission("strafes.leap")) {
			lib.getMessagesAPI().sendMessage("no-permission", sender);
			return;
		}
		Player p = (Player) sender;
		this.removeBoostItems(p);
		if (!this.setItem(items.getLeap(), 4, p)) {
			lib.getMessagesAPI().sendMessage("no-space", sender);
			return;
		}
		lib.getMessagesAPI().sendMessage("leap-activated", sender);
	}

	private boolean setItem(ItemStack is, int slot, Player p) {
		PlayerInventory inv = p.getInventory();
		if (inv.getItem(slot) == null || inv.getItem(slot).getType().equals(Material.AIR)) {
			inv.setItem(slot, is);
			return true;
		}
		if (p.getInventory().firstEmpty() == -1)
			return false;

		ItemStack previousItem = inv.getItem(slot).clone();
		boolean moved = false;
		for (int i = 0; i <= inv.getSize(); i++) {
			if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
				moved = true;
				p.getInventory().setItem(i, previousItem);
				p.getInventory().setItem(slot, is);
				break;
			}
		}
		if (!moved) {
			p.getInventory().addItem(is);
		}
		return true;
	}
	private void removeBoostItems(Player player) {
		PlayerInventory inv = player.getInventory();
		String itemTag;
		for (int i = 0; i <= inv.getSize(); i++) {
			if (inv.getItem(i) != null) {
				itemTag = lib.getNmsAPI().read(inv.getItem(i)).getString("StrafeDirection").getValue();
				if (items.containsBoostTag(itemTag))
					player.getInventory().setItem(i, null);
			}
		}
	}

}
