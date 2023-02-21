package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.NameBinding
import ir.smmh.imp.Stack
import ir.smmh.imp.Stack.Companion.evaluateTo
import ir.smmh.imp.expressions.DoubleValue
import ir.smmh.imp.expressions.Expression
import ir.smmh.imp.expressions.Uninitalized
import ir.smmh.imp.expressions.Variable
import kotlin.math.ceil

class For(parent: Statement) : Loop(parent) {

    var variable: Variable? = null
    var start: Expression? = null
    var end: Expression? = null

    override fun execute(stack: Stack) {
        val name = variable?.name
            ?: stack.report("missing variable")
        val s = start
            ?: stack.report("missing start limit")
        val e = end
            ?: stack.report("missing end limit")

        val counter = NameBinding(name, Uninitalized, false)

        (ceil(stack.evaluateTo(s)).toInt() until ceil(stack.evaluateTo(e)).toInt()).forEach {
            counter.value = DoubleValue(it.toDouble())
            block.execute(stack)
        }
    }

    override fun check(checker: Checker) {
        start?.check(checker)
            ?: checker.report("missing start limit")
        end?.check(checker)
            ?: checker.report("missing end limit")
        block.check(checker)
    }
}