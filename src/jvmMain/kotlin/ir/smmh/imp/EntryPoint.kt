package ir.smmh.imp

import ir.smmh.imp.expressions.*
import ir.smmh.imp.expressions.Function
import ir.smmh.imp.statements.FunctionBody
import ir.smmh.imp.statements.OneCall
import ir.smmh.imp.statements.Return
import ir.smmh.imp.statements.Statement

val stack = Stack().apply {
    push().apply {
        declare(NameBinding("print", Print, false, false))
    }
}
val helloWorld: Statement = OneCall(Call(Variable("print"), listOf(StringValue("Hello, world!"))))
val program: Statement = Return(FunctionDefinition(emptyList(), FunctionBody(listOf(helloWorld))))

fun main() {
    program.execute(stack)
    val main = stack.pop().returnedValue as Function
    main.call(emptyList())
}