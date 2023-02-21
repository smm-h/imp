package ir.smmh.imp

import ir.smmh.imp.expressions.Value

/**
 * A [NameBinding] is the association of a [name] with a [value].
 *
 * [Wikipedia](https://en.wikipedia.org/wiki/Name_binding)
 */
data class NameBinding(
    val name: String,
    var value: Value,
    val rebindable: Boolean,
)