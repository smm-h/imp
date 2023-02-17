package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Uninitalized

data class FunctionBody(
    val list: List<Statement>,
) : Statement() {

    override fun execute(stack: Stack) {
        for (statement in list) {
            statement.execute(stack)
            if (stack.top().returnedValue != Uninitalized) break
        }
    }

    override fun check(checker: Checker) {
        for (statement in list) {
            statement.check(checker)
            //TODO unreachability check
            //val returns = statement.returns()
        }
    }

    override fun returns() = list.fold(false) { a, e -> a || e.returns() }
}