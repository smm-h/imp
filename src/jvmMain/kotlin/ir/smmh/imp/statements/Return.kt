package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Expression

class Return(override val parent: Block) : Statement() {

    var expression: Expression? = null

    override fun execute(stack: Stack) {
        stack.top().returnedValue = expression?.evaluate(stack)
            ?: stack.report("missing expression")
    }

    override fun check(checker: Checker) {
        expression?.check(checker)
            ?: checker.report("missing expression")
    }

    override fun returns() = true
}