import ir.smmh.imp.statements.Block
import ir.smmh.imp.statements.Statement

interface App {
    val program: Block

    val viewMode: ViewMode
    fun nextViewMode()

    val selectedStatement: Statement?
    fun select(statement: Statement, keepStack: Boolean)

    fun print(output: Output)
    fun printEmptyLine() = print(Output.LineBreak)
    fun printLine(text: String, category: OutputLine.Category) = print(OutputLine(text, category))
    fun clearOutput()
    fun hasOutput(): Boolean

    val menu: MenuButton.Action.HasChildren
    fun goToMenu(menu: MenuButton.Action.HasChildren, keepStack: Boolean)
    fun goBack()
    fun canGoBack(): Boolean
}