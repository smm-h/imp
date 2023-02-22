import ir.smmh.imp.statements.Statement

interface App {
    val viewMode: ViewMode
    fun nextViewMode()

    val selectedStatement: Statement?
    fun select(statement: Statement, keepStack: Boolean)

    fun print(output: Output)
    fun print(text: String, category: OutputLine.Category) = print(OutputLine(text, category))
    fun clearOutput()
    fun hasOutput(): Boolean

    val menu: MenuButton.Node
    fun goToMenu(menu: MenuButton.Node, keepStack: Boolean)
    fun goBack()
    fun canGoBack(): Boolean
}