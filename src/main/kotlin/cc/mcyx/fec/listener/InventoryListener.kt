package cc.mcyx.fec.listener

import cc.mcyx.fec.FastEnchantmentCovert
import cc.mcyx.fec.message.MessageLanguage
import com.google.gson.Gson
import com.google.gson.JsonObject
import de.tr7zw.nbtapi.NBTItem
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerChatEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import javax.xml.soap.Text


class InventoryListener : Listener {

    companion object {

        /**
         *  打开GUI界面
         *  @author Zcc
         *  @param player 打开玩家对象
         *
         * */
        fun openGui(player: Player) {
            val itemInMainHand = player.inventory.itemInMainHand

            val inventory = Bukkit.createInventory(null, 54, MessageLanguage.title)

            for (enchantment in itemInMainHand.enchantments) {
                inventory.addItem(ItemStack(Material.ENCHANTED_BOOK).apply {
                    val itemMeta = itemMeta as EnchantmentStorageMeta
                    itemMeta.addStoredEnchant(enchantment.key, enchantment.value, true)
                    setItemMeta(itemMeta)
                })
            }
            player.openInventory(inventory)
        }
    }

    @EventHandler
    fun onChat(chatEvent: PlayerChatEvent) {

//        测试模块
        chatEvent.player.inventory.addItem(ItemStack(Material.DIAMOND_SWORD).apply {
            val im = itemMeta!!
            im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true)
            im.addEnchant(Enchantment.ARROW_INFINITE, 1, true)
            im.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true)
            im.addEnchant(Enchantment.DURABILITY, 1, true)
            im.addEnchant(Enchantment.SWIFT_SNEAK, 1, true)
            itemMeta = im
        })
    }


    //玩家点击GUI界面
    @EventHandler
    fun onPlayerInventoryClick(e: InventoryClickEvent) {
        if (e.view.title == MessageLanguage.title && e.click === ClickType.LEFT && e.whoClicked is Player) {
            e.isCancelled = true
            val player = e.whoClicked as Player

            //获取点击的附魔书
            val currentItem = e.currentItem

            //附魔书不能为空
            if (currentItem != null) {
                //获得玩家主手上的物品
                val itemInMainHand = player.inventory.itemInMainHand

                val enchantmentStorageMeta = currentItem.itemMeta as EnchantmentStorageMeta
                for (enchantment in enchantmentStorageMeta.storedEnchants) {
                    val money = FastEnchantmentCovert.fastEnchantmentCovert.config.getDouble(
                        "cost." + enchantment.key.name.uppercase(),
                        FastEnchantmentCovert.fastEnchantmentCovert.config.getDouble("default_cost", -1.0)
                    )
                    if (money < 0) {
                        player.sendMessage("§c该附魔不支持卸载! §e类型ID: " + enchantment.key.name)
                        return
                    }

                    if (FastEnchantmentCovert.economy.provider.getBalance(player) < money) {
                        player.sendMessage("§c钱钱不够哦~ 此附魔你需要花费 $money")
                        return
                    }

                    //扣除玩家货币
                    FastEnchantmentCovert.economy.provider.withdrawPlayer(player, money)

                    val itemMeta = itemInMainHand.itemMeta!!
                    //删除主手上指定的附魔
                    itemMeta.removeEnchant(enchantment.key)
                    //更新物品NBT
                    itemInMainHand.setItemMeta(itemMeta)
                    //重新打开GUI界面
                    openGui(player)

                    val textComponent = TextComponent("§e[附魔书]")
                    textComponent.hoverEvent =
                        HoverEvent(
                            HoverEvent.Action.SHOW_ITEM,
                            ComponentBuilder(NBTItem.convertItemtoNBT(currentItem).toString()).create()
                        )
                    textComponent.addExtra(TextComponent(" §c已卸载"))
                    //将物品给予玩家
                    player.inventory.addItem(currentItem)
                    player.spigot().sendMessage(textComponent)
                }
            }
        }
    }
}