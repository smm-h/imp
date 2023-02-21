package ir.smmh.imp.statements

sealed class Loop(final override val parent: Statement) : Statement() {
    val block = Block(parent)
    override fun returns() = block.returns()
}