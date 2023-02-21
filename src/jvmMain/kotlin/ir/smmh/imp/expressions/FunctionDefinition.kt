package ir.smmh.imp.expressions

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.statements.FunctionBody

class FunctionDefinition : Expression {

    val arguments: MutableList<Variable> = mutableListOf()
    val body = FunctionBody()

    override fun evaluate(stack: Stack): Function {
        return Function(arguments, body)
    }

    override fun check(checker: Checker) {
        body.check(checker)
    }
}