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
            call = call(name, *args)
        })

    fun call(name: String, vararg args: Expression) =
        Call().apply {
            callable = Variable(name)
            arguments.addAll(args)
        }

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
        Block(null).apply(applyToBlock)

    val TRUE = BooleanValue(true)
    val FALSE = BooleanValue(false)

    fun Block.DECLARE(variable: String, initializer: Expression) =
        list.add(NameDeclaration(this).apply {
            this.variable = Variable(variable)
            this.rebindable = false
            this.initializer = initializer
        })

    fun Block.DECLARE_REBINDABLE(variable: String, initializer: Expression = Uninitalized) =
        list.add(NameDeclaration(this).apply {
            this.variable = Variable(variable)
            this.rebindable = true
            this.initializer = initializer
        })

    fun Block.DECLARE_REBINDABLE(variable: String, value: Int) =
        DECLARE_REBINDABLE(variable, DoubleValue(value.toDouble()))

    fun Block.FOR(variable: String, start: Int, end: Expression, applyToBlock: Block.() -> Unit) =
        list.add(For(this).apply {
            this.variable = Variable(variable)
            this.start = DoubleValue(start.toDouble())
            this.end = end
            block.apply(applyToBlock)
        })

    fun Block.ASSIGN(variable: String, expression: Expression) =
        list.add(Assignment(this).apply {
            this.variable = Variable(variable)
            this.expression = expression
        })

    fun Block.RETURN(expression: Expression) =
        list.add(Return(this).apply {
            this.expression = expression
        })

    fun Block.RETURN(variable: String) =
        RETURN(Variable(variable))

    fun ADD(a: Expression, b: Expression) =
        Addition(a, b)

    fun ADD(a: String, b: Int) =
        Addition(Variable(a), DoubleValue(b.toDouble()))

    fun Block.FUNCTION(vararg arguments: String, applyToBlock: Block.() -> Unit) =
        FunctionDefinition(this).apply {
            this.arguments.addAll(arguments.asList().map(::Variable))
            body.apply(applyToBlock)
        }
}