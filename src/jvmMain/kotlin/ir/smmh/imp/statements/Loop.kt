package ir.smmh.imp.statements

sealed class Loop : Statement() {
    val block = Block()
    override fun returns() = block.returns()
}