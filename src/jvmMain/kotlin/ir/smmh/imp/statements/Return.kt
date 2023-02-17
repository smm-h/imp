package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Expression

data class Return(val expression: Expression) : Statement() {
    override fun execute(stack: Stack) {
        stack.top().returnedValue = expression.evaluate(stack)
    }

    override fun check(checker: Checker) {
        expression.check(checker)
    }

    override fun returns() = true
}