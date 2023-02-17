package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.BooleanValue
import ir.smmh.imp.expressions.Expression

data class While(
    val condition: Expression,
    override val block: Block,
) : Loop() {
    override fun execute(stack: Stack) {
        while ((condition.evaluate(stack) as BooleanValue).value) {
            block.execute(stack)
        }
    }

    override fun check(checker: Checker) {
        condition.check(checker)
        block.check(checker)
    }
}