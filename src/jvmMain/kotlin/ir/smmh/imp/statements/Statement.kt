package ir.smmh.imp.statements

import ir.smmh.imp.Checker
import ir.smmh.imp.Stack

/**
 * A [Statement] is a unit that expresses some action to be carried out.
 *
 * [Wikipedia](https://en.wikipedia.org/wiki/Statement_(computer_science))
 */
sealed class Statement {
    abstract val parent: Statement
    abstract fun execute(stack: Stack)
    abstract fun check(checker: Checker)
    abstract fun returns(): Boolean?
}