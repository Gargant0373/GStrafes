package gargant.strafes.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import gargant.strafes.classes.Items;
import gargant.strafes.containers.CooldownsContainer;
import gargant.strafes.containers.VelocityContainer;
import masecla.mlib.annotations.RegisterableInfo;
import masecla.mlib.classes.Registerable;
import masecla.mlib.main.MLib;

@RegisterableInfo(command = "strafes")
public class StrafesCommand extends Registerable {

	private Items items;

	public StrafesCommand(MLib lib, Items items) {
		super(lib);
		this.items = items;
	}

	@Override
	public void fallbackCommand(CommandSender sender, String[] args) {
		if (!(sender instanceof Player))
			return;
		Player p = (Player) sender;

		if (sender.hasPermission("strafes.velocity") && args.length > 0 && args[0].equalsIgnoreCase("velocity")) {
			lib.getContainerAPI().openFor(p, VelocityContainer.class);
			return;
		}
		if (sender.hasPermission("strafes.cooldowns") && args.length > 0 && args[0].equalsIgnoreCase("cooldowns")) {
			lib.getContainerAPI().openFor(p, CooldownsContainer.class);
			return;
		}
		if (!sender.hasPermission("strafes.strafes")) {
			lib.getMessagesAPI().sendMessage("no-permission", sender);
			return;
		}
		if (!this.hasSlots(p, 3)) {
			lib.getMessagesAPI().sendMessage("no-space", sender);
			return;
		}
		if (!this.setItem(items.getBackStrafe(), 4, p)) {
			lib.getMessagesAPI().sendMessage("no-space", sender);
			return;
		}
		if (!this.setItem(items.getLeftStrafe(), 3, p)) {
			lib.getMessagesAPI().sendMessage("no-space", sender);
			return;
		}
		if (!this.setItem(items.getRightStrafe(), 5, p)) {
			lib.getMessagesAPI().sendMessage("no-space", sender);
			return;
		}
		lib.getMessagesAPI().sendMessage("strafes-activated", sender);
	}

	private boolean hasSlots(Player player, int slots) {
		for (int i = 0; i < player.getInventory().getSize(); i++) {
			if (player.getInventory().getItem(i) == null
					|| player.getInventory().getItem(i).getType().equals(Material.AIR))
				slots--;
			if (slots == 0)
				return true;
		}
		return false;
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

}
