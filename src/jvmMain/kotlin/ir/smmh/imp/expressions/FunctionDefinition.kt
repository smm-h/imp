package ir.smmh.imp.expressions

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.statements.FunctionBody

data class FunctionDefinition(
    val arguments: List<String>,
    val body: FunctionBody,
) : Expression {
    private val function = Function(arguments, body)
    override fun evaluate(stack: Stack): Function {
        return function
    }

    override fun check(checker: Checker) {
        body.check(checker)
    }
}