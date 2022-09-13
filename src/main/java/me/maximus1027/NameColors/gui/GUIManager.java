package me.maximus1027.NameColors.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.UUID;

public class GUIManager implements Listener {

    private final NameColorListGUI nameColorListGUI;
    private final HashSet<UUID> playersInGUI;

    public GUIManager() {
        nameColorListGUI = new NameColorListGUI();
        playersInGUI = new HashSet<>();
    }

    public void reloadAllGUIs() {
        nameColorListGUI.reload();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory() != e.getWhoClicked().getOpenInventory().getTopInventory()) return;

        if (playersInGUI.contains(e.getWhoClicked().getUniqueId())) {
            e.setCancelled(true);
            nameColorListGUI.handleClick((Player) e.getWhoClicked(), e.getSlot());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        playersInGUI.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        playersInGUI.remove(e.getPlayer().getUniqueId());
    }

    public void openNameColorListInventory(Player player) {
        nameColorListGUI.openInventory(player);
        playersInGUI.add(player.getUniqueId());
    }

}