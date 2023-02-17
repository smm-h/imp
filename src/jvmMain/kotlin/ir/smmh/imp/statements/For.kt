package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.NameBinding
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Expression
import ir.smmh.imp.expressions.Uninitalized
import ir.smmh.imp.expressions.Value
import ir.smmh.imp.expressions.Variable

data class For(
    val variable: Variable,
    val iterable: Expression,
    override val block: Block,
) : Loop() {
    override fun execute(stack: Stack) {
        val name = variable.name
        val nameBinding =
            NameBinding(name, Uninitalized, false, false)
        (iterable.evaluate(stack) as Iterable<*>).forEach {
            nameBinding.value = it as Value
            block.execute(stack)
        }
    }

    override fun check(checker: Checker) {
        iterable.check(checker)
        block.check(checker)
    }
}