package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Expression
import ir.smmh.imp.expressions.Variable

class Assignment(override val parent: Statement) : Statement() {

    var variable: Variable? = null
    var expression: Expression? = null

    override fun execute(stack: Stack) {
        val n = variable?.name
            ?: stack.report("missing variable")
        val e = expression
            ?: stack.report("missing expression")

        val binding = stack.retrieve(n)
        if (binding == null) {
            stack.report("name is not declared: $n")
        } else if (binding.rebindable) {
            binding.value = e.evaluate(stack)
        } else {
            stack.report("name is declared as non-rebindable: $n")
        }
    }

    override fun check(checker: Checker) {
        expression?.check(checker)
            ?: checker.report("missing expression")
    }

    override fun returns() = false
}