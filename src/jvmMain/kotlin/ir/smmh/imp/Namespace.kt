package ir.smmh.imp

/**
 * A [Namespace] is a collection of [NameBinding]s organized in a way that
 * it can [retrieve] existing ones and [declare] new ones.
 *
 * [Wikipedia](https://en.wikipedia.org/wiki/Namespace#In_programming_languages)
 */
interface Namespace {
    fun retrieve(name: String): NameBinding?
    fun declare(nameBinding: NameBinding)
}