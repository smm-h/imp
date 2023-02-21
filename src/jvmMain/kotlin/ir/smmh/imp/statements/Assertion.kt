package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.Stack.Companion.evaluateTo
import ir.smmh.imp.expressions.Expression

class Assertion(override val parent: Statement) : Statement() {

    var expression: Expression? = null

    override fun execute(stack: Stack) {
        val e = expression
            ?: stack.report("missing expression")

        if (!stack.evaluateTo<Boolean>(e.evaluate(stack))) {
            stack.report("assertion failed")
        }
    }

    override fun check(checker: Checker) {
        expression?.check(checker)
            ?: checker.report("missing expression")
    }

    override fun returns() = false
}