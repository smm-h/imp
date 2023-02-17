package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Call

data class OneCall(val call: Call) : Statement() {
    override fun execute(stack: Stack) {
        call.evaluate(stack)
    }

    override fun check(checker: Checker) {
        call.check(checker)
    }

    override fun returns() = false
}