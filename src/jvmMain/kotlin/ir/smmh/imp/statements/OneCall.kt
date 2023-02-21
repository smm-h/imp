package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Call

class OneCall(override val parent: Statement) : Statement() {

    var call: Call? = null

    override fun execute(stack: Stack) {
        call?.evaluate(stack)
            ?: stack.report("missing call")
    }

    override fun check(checker: Checker) {
        call?.check(checker)
            ?: checker.report("missing call")
    }

    override fun returns() = false
}