package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.Stack.Companion.evaluateTo
import ir.smmh.imp.expressions.Expression

class Repeat(parent: Block) : Loop(parent) {

    var times: Expression? = null

    override fun execute(stack: Stack) {
        val t = times
            ?: stack.report("missing expression")

        repeat(stack.evaluateTo<Double>(t).toInt()) {
            block.execute(stack)
        }
    }

    override fun check(checker: Checker) {
        times?.check(checker)
            ?: checker.report("missing expression")
        block.check(checker)
    }
}