package ir.smmh.imp.expressions

import ir.smmh.imp.NameBinding
import ir.smmh.imp.Stack
import ir.smmh.imp.statements.FunctionBody

class Function(
    arguments: List<Variable>,
    body: FunctionBody,
) : Callable {

    private val argumentNames = arguments.map(Variable::name)

    override fun call(input: List<Value>): Value {
        val stack = Stack()
        stack.push().apply {
            argumentNames.forEachIndexed { i, name ->
                declare(NameBinding(name, input[i], false))
            }
        }
        body.execute(stack)
        return stack.pop().returnedValue
    }
}