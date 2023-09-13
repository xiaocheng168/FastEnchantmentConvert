package cc.mcyx.fec.fast.engine

class FastCommandBuilder(rootCommand: String) {

    companion object {
        fun create(rc: String): FastCommandBuilder {
            return FastCommandBuilder(rc)
        }
    }


    fun setSubCmd() {

    }

    fun build(): FastCommandBuilder {
        return this
    }
}