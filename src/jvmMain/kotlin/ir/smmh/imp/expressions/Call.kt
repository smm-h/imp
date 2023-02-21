package ir.smmh.imp.expressions

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack

class Call : Expression {

    var callable: Expression? = null
    val arguments: MutableList<Expression> = mutableListOf()

    override fun evaluate(stack: Stack): Value {
        val c = callable
            ?: stack.report("missing callable")

        val input = arguments.map { it.evaluate(stack) }
        val output = (c.evaluate(stack) as Callable).call(input)
        if (output == Uninitalized) stack.report("no value was returned")
        return output
    }

    override fun check(checker: Checker) {
        callable?.check(checker)
            ?: checker.report("missing callable")
        arguments.forEach { it.check(checker) }
    }
}