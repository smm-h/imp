package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.RuntimeError
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Expression

data class Assignment(
    val name: String,
    val expression: Expression,
) : Statement() {
    override fun execute(stack: Stack) {
        val binding = stack.retrieve(name)
        if (binding == null) {
            throw RuntimeError("name is not declared: $name")
        } else if (binding.canBeRebound) {
            binding.value = expression.evaluate(stack)
        } else {
            throw RuntimeError("name is declared as non-rebindable: $name")
        }
    }

    override fun check(checker: Checker) {
        expression.check(checker)
    }

    override fun returns() = false
}