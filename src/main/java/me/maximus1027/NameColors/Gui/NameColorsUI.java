package me.maximus1027.NameColors.Gui;

import de.tr7zw.nbtapi.NBTItem;
import me.maximus1027.NameColors.Main;
import me.maximus1027.NameColors.Players.PlayerColors;
import me.maximus1027.NameColors.Utils.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class NameColorsUI implements Listener {
    private Player player;
    private int page;
    private static int maxPage;
    private final static HashMap<UUID, NameColorsUI> nameColorsUIS = new HashMap<>();
    public static ItemStack[] nameColors;
    public static ItemStack[] filler;

    public static void Load() {
        FileConfiguration config = Main.getInstance().getConfig();
        nameColors = new ItemStack[config.getConfigurationSection("namecolors").getKeys(false).size()];
        filler = new ItemStack[36];


        int i = 0;
        for (String id : config.getConfigurationSection("namecolors").getKeys(false)) {
            nameColors[i] = Misc.getNameColorStack(id);
            i++;
        }

        ItemStack empty = new ItemStack(Material.AIR).clone();

        ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE).clone();
        ItemMeta itemMeta = background.getItemMeta();
        itemMeta.setDisplayName("");
        background.setItemMeta(itemMeta);

        for (int slot = 0; slot < 36; slot++) {
            if (slot < 27) {
                filler[slot] = empty;
            } else {
                filler[slot] = background;
            }
        }

        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextPageMeta = nextPage.getItemMeta();
        nextPageMeta.setDisplayName(Misc.conv("&eNext Page"));
        nextPage.setItemMeta(nextPageMeta);

        ItemStack backPage = new ItemStack(Material.ARROW);
        ItemMeta backPageMeta = nextPage.getItemMeta();
        backPageMeta.setDisplayName(Misc.conv("&ePrevious Page"));
        backPage.setItemMeta(nextPageMeta);

        filler[30] = new ItemStack(backPage);
        filler[32] = new ItemStack(nextPage);

    }


    public NameColorsUI(Player player) {
        if (player == null) {
            return;
        }

        this.page = 0;
        this.maxPage = nameColors.length / 36;
        this.player = player;

        Inventory inventory = Bukkit.createInventory(player, 36, Misc.conv("&8Name Colors"));
        player.openInventory(inventory);
        updatePage();

        nameColorsUIS.put(player.getUniqueId(), this);
    }

    public void updatePage() {
        Inventory inventory = player.getOpenInventory().getTopInventory();

        inventory.setContents(filler);

        List<ItemStack> namecolors = Arrays.asList(nameColors);
        int e = 0;
        int slotNext = Math.min(namecolors.size(), (this.page + 1) * 36);

        for (ItemStack stack : namecolors.subList(page * 28, slotNext)) {
            NBTItem item = new NBTItem(stack);

            stack = stack.clone();
            if (player.hasPermission("namecolors.color." + item.getString("namecolor"))) {
                ItemStack unlocked;// = new ItemStack(Material.valueOf(item.getString("unlock")));

                String material = item.getString("unlock");
                if (material.contains(":")) {
                    unlocked = new ItemStack(Material.valueOf(material.toUpperCase().split(":")[0]), 1, Short.parseShort(material.split(":")[1]));
                } else {
                    unlocked = new ItemStack(Material.valueOf(material.toUpperCase()));
                }

                ItemMeta unlockedMeta = unlocked.getItemMeta();
                unlockedMeta.setDisplayName(stack.getItemMeta().getDisplayName());
                unlockedMeta.setLore(stack.getItemMeta().getLore());

                unlocked.setItemMeta(unlockedMeta);

                new NBTItem(stack).applyNBT(unlocked);

                stack = unlocked;
            }

            inventory.setItem(e, stack);
            e++;
        }
    }

    public void nextPage() {
        if (page < maxPage) {
            page++;
        }
    }

    public void previousPage() {
        if (page > 0) {
            page--;
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            if (nameColorsUIS.containsKey(e.getWhoClicked().getUniqueId()) && e.getWhoClicked().getOpenInventory().getTopInventory().getItem(e.getSlot()).getType() != Material.AIR) {
                e.setCancelled(true);
                assert e.getWhoClicked().getOpenInventory().getTopInventory().getItem(e.getSlot()).getType() != Material.GRAY_STAINED_GLASS_PANE;

                if (e.getClickedInventory() == e.getWhoClicked().getOpenInventory().getBottomInventory()) {
                    return;
                }

                if (e.getSlot() == 30) {
                    NameColorsUI nameColorsUI = nameColorsUIS.get(e.getWhoClicked().getUniqueId());
                    nameColorsUI.previousPage();
                    nameColorsUI.updatePage();
                    return;
                } else if (e.getSlot() == 32) {
                    NameColorsUI nameColorsUI = nameColorsUIS.get(e.getWhoClicked().getUniqueId());
                    nameColorsUI.nextPage();
                    nameColorsUI.updatePage();
                    return;
                }

                ItemStack itemStack = e.getWhoClicked().getOpenInventory().getTopInventory().getItem(e.getSlot());

                NBTItem nbtItem = new NBTItem(itemStack);
                FileConfiguration config = Main.getInstance().getConfig();
                if (nbtItem.hasKey("namecolor") && e.getWhoClicked().hasPermission("namecolors.color." + nbtItem.getString("namecolor"))) {
                    e.getWhoClicked().sendMessage(Misc.conv(config.getString("messages.on-enable").replace("%namecolor%", nbtItem.getString("namecolor"))));
                    PlayerColors.setPlayerPattern(e.getWhoClicked().getUniqueId(), config.getString("namecolors." + nbtItem.getString("namecolor") + ".pattern"));
                    PlayerColors.setPlayerUserame(e.getWhoClicked().getUniqueId(), config.getString("namecolors." + nbtItem.getString("namecolor") + ".pattern"));
                } else {
                    e.getWhoClicked().sendMessage(Misc.conv(config.getString("messages.no-access").replace("%namecolor%", nbtItem.getString("namecolor"))));
                }

            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (nameColorsUIS.containsKey(e.getPlayer().getUniqueId())) {
            nameColorsUIS.remove(e.getPlayer().getUniqueId());
        }
    }


}
