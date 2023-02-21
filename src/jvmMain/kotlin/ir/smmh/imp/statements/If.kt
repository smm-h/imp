package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.Stack.Companion.evaluateTo
import ir.smmh.imp.expressions.Expression
import or

class If(override val parent: Statement) : Statement() {

    var condition: Expression? = null
    val ifTrue = Block(parent)
    val ifFalse = Block(parent)

    override fun execute(stack: Stack) {
        val c = condition
            ?: stack.report("missing condition")

        if (stack.evaluateTo(c))
            ifTrue.execute(stack)
        else
            ifFalse.execute(stack)
    }

    override fun check(checker: Checker) {
        condition?.check(checker)
            ?: checker.report("missing condition")
        ifTrue.check(checker)
        ifFalse.check(checker)
    }

    override fun returns(): Boolean? =
        ifTrue.returns() or ifFalse.returns()
}