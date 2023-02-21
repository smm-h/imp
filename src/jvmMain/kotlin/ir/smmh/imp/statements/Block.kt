package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Frame
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Uninitalized
import ir.smmh.imp.or

/**
 * A [Block] is a [Statement] whose execution includes pushing its own
 * [Frame] on the [Stack], executing a [list] of sub-statements in
 * order, and then popping it.
 */
class Block : Statement() {

    val list: MutableList<Statement> = mutableListOf()

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

    override fun returns() = list.fold<Statement, Boolean?>(false) { a, e -> a or e.returns() }
}