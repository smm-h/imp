package ir.smmh.imp

import ir.smmh.imp.expressions.*

class Stack : Namespace {
    private val stack = ArrayDeque<Frame>()

    fun push(): Frame {
        val frame = Frame()
        stack.addLast(frame)
        return frame
    }

    fun pop(): Frame {
        return stack.removeLast()
    }

    fun top(): Frame {
        return stack.last()
    }

    override fun retrieve(name: String): NameBinding? {
        for (frame in stack) {
            val binding = frame.retrieve(name)
            if (binding != null) return binding
        }
        return null
    }

    override fun declare(nameBinding: NameBinding) {
        val name = nameBinding.name
        if (retrieve(name) == null) {
            top().declare(nameBinding)
        } else {
            report("name is already declared: $name")
        }
    }

    class Error(message: String) :
        kotlin.Exception(message)

    fun report(error: String): Nothing {
        throw Error(error)
    }

    companion object {
        inline fun <reified T> Stack.evaluateTo(expression: Expression): T {
            val v = expression.evaluate(this)
            try {
                @Suppress("IMPLICIT_CAST_TO_ANY")
                return when (T::class) {
                    Boolean::class -> (v as BooleanValue).value
                    Double::class -> (v as DoubleValue).value
                    String::class -> (v as StringValue).value
                    else -> v
                } as T
            } catch (_: ClassCastException) {
                report("expecting Int; found $v")
            }
        }
    }
}