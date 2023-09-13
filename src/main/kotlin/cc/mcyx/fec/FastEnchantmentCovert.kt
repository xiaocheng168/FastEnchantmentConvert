package cc.mcyx.fec

import cc.mcyx.fec.command.FecCommand
import cc.mcyx.fec.listener.InventoryListener
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import javax.imageio.spi.RegisterableService


open class FastEnchantmentCovert : JavaPlugin() {


    companion object {
        lateinit var fastEnchantmentCovert: FastEnchantmentCovert
        lateinit var economy: RegisteredServiceProvider<Economy>
    }


    override fun onLoad() {
        fastEnchantmentCovert = this
        saveDefaultConfig()
    }

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(InventoryListener(), this)
        Bukkit.getPluginCommand("fec")?.setExecutor(FecCommand())
        //获取经济体
        economy = Bukkit.getServicesManager().getRegistration(Economy::class.java) as RegisteredServiceProvider<Economy>

    }
}
