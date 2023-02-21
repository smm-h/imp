package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.NameBinding
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.Expression
import ir.smmh.imp.expressions.Uninitalized
import ir.smmh.imp.expressions.Variable

class NameDeclaration(override val parent: Block) : Statement() {

    var variable: Variable? = null
    var rebindable: Boolean = false
    var initializer: Expression? = null

    override fun execute(stack: Stack) {
        val name = variable?.name
            ?: stack.report("missing variable")

        val value = initializer?.evaluate(stack) ?: Uninitalized
        stack.declare(NameBinding(name, value, rebindable))
    }

    override fun check(checker: Checker) {
        val name = variable?.name
            ?: checker.report("missing variable")
        if (initializer == null && !rebindable)
            checker.report("non-rebindable name must be initialized: $name")
    }

    override fun returns() = false
}