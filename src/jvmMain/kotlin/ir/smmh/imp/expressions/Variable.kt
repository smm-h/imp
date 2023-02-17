package ir.smmh.imp.expressions

import ir.smmh.imp.Checker
import ir.smmh.imp.RuntimeError
import ir.smmh.imp.Stack

data class Variable(val name: String) : Expression {
    override fun evaluate(stack: Stack): Value {
        val nameBinding = stack.retrieve(name)
        if (nameBinding == null) {
            throw RuntimeError("name is undefined: $name")
        } else {
            val value = nameBinding.value
            if (value == Uninitalized) {
                throw RuntimeError("name is uninitalized: $name")
            } else {
                return value
            }
        }
    }

    override fun check(checker: Checker) = Unit
}