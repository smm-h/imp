package ir.smmh.imp.expressions

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack

/**
 * A [Value] is an [Expression] whose evaluation produces itself and thus
 * is [Stack]-independent.
 */
interface Value : Expression {
    override fun evaluate(stack: Stack): Value = this
    override fun check(checker: Checker) = Unit
}