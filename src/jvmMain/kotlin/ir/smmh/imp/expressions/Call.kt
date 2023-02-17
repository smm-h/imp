package ir.smmh.imp.expressions

import ir.smmh.imp.Checker
import ir.smmh.imp.RuntimeError
import ir.smmh.imp.Stack

data class Call(
    val callable: Expression,
    val arguments: List<Expression>,
) : Expression {
    override fun evaluate(stack: Stack): Value {
        val input = arguments.map { it.evaluate(stack) }
        val output = (callable.evaluate(stack) as Callable).call(input)
        if (output == Uninitalized) throw RuntimeError("no value was returned")
        return output
    }

    override fun check(checker: Checker) {
        callable.check(checker)
        arguments.forEach { it.check(checker) }
    }
}