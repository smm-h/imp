import ir.smmh.imp.statements.Statement

interface App {
    val viewMode: ViewMode
    fun nextViewMode()

    var selectedStatement: Statement?

    fun print(text: String, category: OutputLine.Category)
    fun clearOutput()
    fun hasOutput(): Boolean
}