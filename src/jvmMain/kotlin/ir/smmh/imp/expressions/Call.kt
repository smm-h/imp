package ir.smmh.imp.expressions

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.Stack.Companion.evaluateTo

class Call : Expression {

    var callable: Expression? = null
    val arguments: MutableList<Expression> = mutableListOf()

    override fun evaluate(stack: Stack): Value {
        val c = callable
            ?: stack.report("missing callable")

        val input = arguments.map { it.evaluate(stack) }
        val output = stack.evaluateTo<Callable>(c).call(stack, input)
        if (output == Uninitalized) stack.report("no value was returned")
        return output
    }

    override fun check(checker: Checker) {
        val c = callable

        if (c == null) {
            checker.report("missing callable")
        } else {
            c.check(checker)
            c.evaluate(Stack())
        }

        arguments.forEach { it.check(checker) }
    }
}