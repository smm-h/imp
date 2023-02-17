package ir.smmh.imp.expressions

import ir.smmh.imp.NameBinding
import ir.smmh.imp.Stack
import ir.smmh.imp.statements.FunctionBody

class Function(
    val arguments: List<String>,
    val body: FunctionBody,
) : Callable {
    override fun call(input: List<Value>): Value {
        val stack = Stack()
        stack.push().apply {
            arguments.forEachIndexed { i, name ->
                declare(NameBinding(name, input[i], false, false))
            }
        }
        body.execute(stack)
        return stack.pop().returnedValue
    }
}