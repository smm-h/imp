package ir.smmh.imp

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
}