package ir.smmh.imp.statements

sealed class Loop(final override val parent: Block) : Statement() {
    val block = Block(parent)
    override fun returns() = block.returns()
}