package ir.smmh.imp.expressions

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack

/**
 * An [Expression] is a basic concept that can be [evaluate]d in a given
 * [Stack] to produce a [Value].
 */
interface Expression {
    fun evaluate(stack: Stack): Value
    fun check(checker: Checker)
}