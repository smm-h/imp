package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Expression
import ir.smmh.imp.expressions.IntValue

data class Repeat(
    val times: Expression,
    override val block: Block,
) : Loop() {
    override fun execute(stack: Stack) {
        repeat((times.evaluate(stack) as IntValue).value) {
            block.execute(stack)
        }
    }

    override fun check(checker: Checker) {
        times.check(checker)
        block.check(checker)
    }
}