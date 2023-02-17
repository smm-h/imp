package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Frame
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Uninitalized

/**
 * A [Block] is a [Statement] whose execution includes pushing its own
 * [Frame] on the [Stack], executing a [list] of sub-statements in
 * order, and then popping it.
 */
data class Block(
    val list: List<Statement>,
) : Statement() {

    override fun execute(stack: Stack) {
        val frame = stack.push()
        for (statement in list) {
            statement.execute(stack)
            if (frame.returnedValue != Uninitalized) break
        }
        stack.pop()
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