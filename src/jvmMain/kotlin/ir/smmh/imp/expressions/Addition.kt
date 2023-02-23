package ir.smmh.imp.expressions

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack
import ir.smmh.imp.Stack.Companion.evaluateTo

class Addition(val a: Expression, val b: Expression) : Expression {
    override fun evaluate(stack: Stack) =
        DoubleValue(stack.evaluateTo<Double>(a) + stack.evaluateTo<Double>(b))

    override fun check(checker: Checker) {
        a.check(checker)
        b.check(checker)
    }
}