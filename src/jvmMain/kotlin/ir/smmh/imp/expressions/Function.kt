package ir.smmh.imp.expressions

import ir.smmh.imp.NameBinding
import ir.smmh.imp.Stack
import ir.smmh.imp.statements.Block
import ir.smmh.imp.statements.Statement

class Function(
    arguments: List<Variable>,
    block: Block,
) : Callable {

    private val argumentNames = arguments.map(Variable::name)
    private val body: Array<Statement>

    init {
        val list = block.list
        body = Array(list.size, list::get)
    }

    override fun call(input: List<Value>): Value {
        val stack = Stack()
        stack.push().apply {
            argumentNames.forEachIndexed { i, name ->
                declare(NameBinding(name, input[i], false))
            }
        }
        for (it in body) {
            it.execute(stack)
        }
        return stack.pop().returnedValue
    }
}