package ir.smmh.imp.expressions

import ir.smmh.imp.Stack

interface Callable : Value {
    val argumentCount: Int
    fun call(stack: Stack, input: List<Value>): Value
}