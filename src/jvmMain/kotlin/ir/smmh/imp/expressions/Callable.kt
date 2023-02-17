package ir.smmh.imp.expressions

interface Callable : Value {
    fun call(input: List<Value>): Value
}