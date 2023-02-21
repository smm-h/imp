@file:Suppress("FunctionName")

import ir.smmh.imp.expressions.*
import ir.smmh.imp.statements.*

object ProgramHelpers {

    fun Block.PRINT(text: String) =
        PRINT(StringValue(text))

    fun Block.PRINT(expression: Expression) =
        CALL("print", expression)

    fun Block.CALL(name: String, vararg args: Expression) =
        list.add(OneCall(this).apply {
            call = Call().apply {
                callable = Variable(name)
                arguments.addAll(args)
            }
        })

    fun Block.IF(condition: Expression, applyIfTrue: Block.() -> Unit) =
        list.add(If(this).apply {
            this.condition = condition
            ifTrue.apply(applyIfTrue)
        })

    fun Block.IF(condition: Expression, applyIfTrue: Block.() -> Unit, applyIfFalse: Block.() -> Unit) =
        list.add(If(this).apply {
            this.condition = condition
            ifTrue.apply(applyIfTrue)
            ifFalse.apply(applyIfFalse)
        })

    fun PROGRAM(applyToBlock: Block.() -> Unit) =
        Program().apply { block.apply(applyToBlock) }

    val TRUE = BooleanValue(true)
    val FALSE = BooleanValue(false)
}