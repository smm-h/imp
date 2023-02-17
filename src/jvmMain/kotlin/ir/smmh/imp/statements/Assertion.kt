package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.RuntimeError
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.BooleanValue
import ir.smmh.imp.expressions.Expression

data class Assertion(
    val expression: Expression,
) : Statement() {
    override fun execute(stack: Stack) {
        if (!(expression.evaluate(stack) as BooleanValue).value) {
            throw RuntimeError("assertion failed")
        }
    }

    override fun check(checker: Checker) {
        expression.check(checker)
    }

    override fun returns() = false
}