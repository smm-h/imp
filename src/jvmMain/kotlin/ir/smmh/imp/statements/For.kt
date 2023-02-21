package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.NameBinding
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Expression
import ir.smmh.imp.expressions.Uninitalized
import ir.smmh.imp.expressions.Value
import ir.smmh.imp.expressions.Variable

class For(parent: Statement) : Loop(parent) {

    var variable: Variable? = null
    var iterable: Expression? = null

    override fun execute(stack: Stack) {
        val name = variable?.name
            ?: stack.report("missing variable")
        val i = iterable
            ?: stack.report("missing iterable")

        val nameBinding =
            NameBinding(name, Uninitalized, false)
        (i.evaluate(stack) as Iterable<*>).forEach {
            nameBinding.value = it as Value
            block.execute(stack)
        }
    }

    override fun check(checker: Checker) {
        iterable?.check(checker)
            ?: checker.report("missing iterable")
        block.check(checker)
    }
}