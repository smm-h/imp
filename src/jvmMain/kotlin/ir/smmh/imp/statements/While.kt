package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.Stack.Companion.evaluateTo
import ir.smmh.imp.expressions.Expression

class While(parent: Statement) : Loop(parent) {

    var condition: Expression? = null

    override fun execute(stack: Stack) {
        val c = condition
            ?: stack.report("missing condition")
        val b = block

        while (stack.evaluateTo(c)) {
            b.execute(stack)
        }
    }

    override fun check(checker: Checker) {
        condition?.check(checker)
            ?: checker.report("missing condition")
        block.check(checker)
    }
}