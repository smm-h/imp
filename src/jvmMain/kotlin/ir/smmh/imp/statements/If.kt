package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.BooleanValue
import ir.smmh.imp.expressions.Expression
import or

data class If(
    val condition: Expression,
    val ifTrue: Block,
    val ifFalse: Block?,
) : Statement() {
    override fun execute(stack: Stack) {
        if ((condition.evaluate(stack) as BooleanValue).value)
            ifTrue.execute(stack)
        else
            ifFalse?.execute(stack)
    }

    override fun check(checker: Checker) {
        condition.check(checker)
        ifTrue.check(checker)
        ifFalse?.check(checker)
    }

    override fun returns(): Boolean? {
        val f = ifFalse
        return ifTrue.returns() or (if (f == null) false else f.returns())
    }
}