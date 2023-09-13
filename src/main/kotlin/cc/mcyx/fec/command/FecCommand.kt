package cc.mcyx.fec.command

import cc.mcyx.fec.FastEnchantmentCovert
import cc.mcyx.fec.listener.InventoryListener
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

class FecCommand : TabExecutor {
    override fun onTabComplete(
        p0: CommandSender, p1: Command, p2: String, p3: Array<out String>
    ): MutableList<String> {
        if (p3.size == 1) {
            return mutableListOf("open").apply {
                if (p0.hasPermission("cc.mcyx.fec.admin")) {
                    this.add("set")
                }
            }
        }
        if (p3.size == 2) {
            when (p3[0].lowercase()) {
                "set" -> {
                    val mutableListOf = mutableListOf<String>()
                    for (value in Enchantment.values()) {
                        if (value.name.contains(p3[1])) {
                            mutableListOf.add(value.name)
                        }
                    }
                    return mutableListOf
                }
            }
        }
        return mutableListOf()
    }

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if (p3.isNotEmpty()) {
            if (p3[0].lowercase() == "open") {
                if (p0 is Player) {
                    //获取用户手中物品
                    val itemInMainHand = p0.inventory.itemInMainHand
                    //判断手中物品是否为空
                    if (itemInMainHand.type != Material.AIR) {
                        //手中物品是否存在附魔
                        if (itemInMainHand.enchantments.isEmpty()) {
                            p0.sendMessage("§a这个物品没有附魔可操作")
                            return true
                        } else InventoryListener.openGui(p0)
                    } else p0.sendMessage("§c空手?你让我卸什么")
                }
                return true
            }
            if (p3[0].lowercase() == "set" && p0.hasPermission("cc.mcyx.fec.admin")) {
                if (p3.size == 3) {
                    FastEnchantmentCovert.fastEnchantmentCovert.config.set("cost." + p3[1], p3[2].toDouble())
                    FastEnchantmentCovert.fastEnchantmentCovert.saveConfig()
                    p0.sendMessage("§c已设置该附魔卸载价格 为 " + p3[2].toDouble())
                }
            }
            return true
        }

        //简单的命令提示
        p0.sendMessage("§f/fec open §a尝试打开手中物品附魔操作界面")
        if (p0.hasPermission("cc.mcyx.fec.admin")) {
            p0.sendMessage("§f/fec set [附魔ID] [价格/0.0] §a设置特定附魔的拆解价格")
        }
        return true
    }
}