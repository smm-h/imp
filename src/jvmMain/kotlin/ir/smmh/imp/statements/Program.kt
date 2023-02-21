package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack

class Program : Statement() {
    override val parent: Statement get() = this
    val block = Block(this)
    override fun execute(stack: Stack) = block.execute(stack)
    override fun check(checker: Checker) = block.check(checker)
    override fun returns() = block.returns()
}