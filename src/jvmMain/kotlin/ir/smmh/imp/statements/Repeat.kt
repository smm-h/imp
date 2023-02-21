package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Expression
import ir.smmh.imp.expressions.IntValue

class Repeat : Loop() {
    var times: Expression? = null

    override fun execute(stack: Stack) {
        val t = times
            ?: stack.report("missing expression")

        repeat((t.evaluate(stack) as IntValue).value) {
            block.execute(stack)
        }
    }

    override fun check(checker: Checker) {
        times?.check(checker)
            ?: checker.report("missing expression")
        block.check(checker)
    }
}