package ir.smmh.imp.expressions

object Print : Callable {
    override fun call(input: List<Value>): Value {
        println((input.first() as StringValue).value)
        return Void
    }
}