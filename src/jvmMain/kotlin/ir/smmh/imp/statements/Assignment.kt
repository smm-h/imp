package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.RuntimeError
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Expression
import ir.smmh.imp.expressions.Variable

class Assignment : Statement() {

    var variable: Variable? = null
    var expression: Expression? = null

    override fun execute(stack: Stack) {
        val n = variable?.name
            ?: throw RuntimeError("missing variable")
        val e = expression
            ?: throw RuntimeError("missing expression")

        val binding = stack.retrieve(n)
        if (binding == null) {
            throw RuntimeError("name is not declared: $n")
        } else if (binding.rebindable) {
            binding.value = e.evaluate(stack)
        } else {
            throw RuntimeError("name is declared as non-rebindable: $n")
        }
    }

    override fun check(checker: Checker) {
        expression?.check(checker)
            ?: checker.report("missing expression")
    }

    override fun returns() = false
}