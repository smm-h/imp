package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.RuntimeError
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.BooleanValue
import ir.smmh.imp.expressions.Expression

class While : Loop() {

    var condition: Expression? = null

    override fun execute(stack: Stack) {
        val c = condition
            ?: throw RuntimeError("missing condition")
        val b = block

        while ((c.evaluate(stack) as BooleanValue).value) {
            b.execute(stack)
        }
    }

    override fun check(checker: Checker) {
        condition?.check(checker)
            ?: checker.report("missing condition")
        block.check(checker)
    }
}