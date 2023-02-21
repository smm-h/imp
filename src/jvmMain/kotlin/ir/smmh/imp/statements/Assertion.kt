package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.BooleanValue
import ir.smmh.imp.expressions.Expression

class Assertion : Statement() {

    var expression: Expression? = null

    override fun execute(stack: Stack) {
        val e = expression
            ?: stack.report("missing expression")

        if (!(e.evaluate(stack) as BooleanValue).value) {
            stack.report("assertion failed")
        }
    }

    override fun check(checker: Checker) {
        expression?.check(checker)
            ?: checker.report("missing expression")
    }

    override fun returns() = false
}