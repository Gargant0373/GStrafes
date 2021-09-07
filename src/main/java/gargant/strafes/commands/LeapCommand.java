package gargant.strafes.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import masecla.mlib.annotations.RegisterableInfo;
import masecla.mlib.classes.Registerable;
import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;

@RegisterableInfo(command = "leap")
public class LeapCommand extends Registerable {

	public LeapCommand(MLib lib) {
		super(lib);
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
		if (!this.setItem(getLeap(), 4, p)) {
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

	private ItemStack getLeap() {
		ItemStack result = new ItemStack(Material.FEATHER);
		ItemMeta theMeta = result.getItemMeta();
		theMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&lLeap &7[Right Click]"));
		List<String> thelore = new ArrayList<String>();
		thelore.add("");
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&7Stop reading this"));
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&7and go play!"));
		thelore.add("");
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&b\u25BA Right click to use!"));
		theMeta.setLore(thelore);

		result.setItemMeta(theMeta);
		return lib.getNmsAPI().write().tagString("StrafeDirection", "LEAP").applyOn(result);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent ev) {
		Player p = ev.getPlayer();
		if (p.getInventory().getItemInMainHand() == null)
			return;
		if (p.getInventory().getItemInMainHand().equals(this.getLeap()))
			ev.setCancelled(true);
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if ((!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
			return;

		ItemStack held = event.getPlayer().getInventory().getItemInMainHand();
		if (held == null || held.getType().equals(Material.AIR))
			return;

		Player p = event.getPlayer();

		@SuppressWarnings("deprecation")
		String tag = lib.getNmsAPI().getNBTTagValueString(held, "StrafeDirection");
		if (tag == null)
			return;
		if (tag.equals("LEAP")) {
			this.applyLeap(p);
			this.applyLeapSound(p);
		}
	}

	private double getLeapVelocity() {
		return (double) lib.getConfigurationAPI().getConfig().get("leap.leap_velocity", 1.5);
	}

	private double getVerticalVelocity() {
		return (double) lib.getConfigurationAPI().getConfig().get("leap.vertical_velocity", 0.4);
	}

	private void applyLeap(Player p) {
		Vector v = p.getLocation().getDirection();
		v.multiply(this.getLeapVelocity());
		v.setY(this.getVerticalVelocity());
		p.setVelocity(v);
	}

	private void applyLeapSound(Player p) {
		p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 0.6f, 1);
	}

}
