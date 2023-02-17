package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.NameBinding
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Expression
import ir.smmh.imp.expressions.Uninitalized

data class NameDeclaration(
    val name: String,
    val canBeRebound: Boolean,
    val canBeMutated: Boolean,
    val initializer: Expression?,
) : Statement() {
    override fun execute(stack: Stack) {
        val value = initializer?.evaluate(stack) ?: Uninitalized
        stack.declare(NameBinding(name, value, canBeRebound, canBeMutated))
    }

    override fun check(checker: Checker) {
        if (initializer == null && !canBeMutated)
            checker.report("non-rebindable name must be initialized: $name")
    }

    override fun returns() = false
}