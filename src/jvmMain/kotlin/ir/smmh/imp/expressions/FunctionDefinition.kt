package ir.smmh.imp.expressions

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.statements.Block

class FunctionDefinition(parent: Block) : Expression {

    val body = Block(parent)
    val arguments: MutableList<Variable> = mutableListOf()

    override fun evaluate(stack: Stack): Function {
        return Function(arguments, body)
    }

    override fun check(checker: Checker) {
        body.check(checker)
    }
}