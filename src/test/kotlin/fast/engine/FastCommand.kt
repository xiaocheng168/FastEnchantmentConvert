package cc.mcyx.fec.fast.engine

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

open class FastCommand(command: String) : TabExecutor {
    init {
        Bukkit.getPluginCommand(command)?.setExecutor(this)
    }

    override fun onTabComplete(
        p0: CommandSender,
        p1: Command,
        p2: String,
        p3: Array<out String>
    ): MutableList<String>? {
        return mutableListOf()
    }

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        return true
    }
}