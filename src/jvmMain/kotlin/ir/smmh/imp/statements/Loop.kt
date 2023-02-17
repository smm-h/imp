package ir.smmh.imp.statements

sealed class Loop : Statement() {
    abstract val block: Block
    override fun returns() = block.returns()
}