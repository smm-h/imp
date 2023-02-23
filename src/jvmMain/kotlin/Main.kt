import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ir.smmh.imp.NameBinding
import ir.smmh.imp.Stack
import ir.smmh.imp.Stack.Companion.evaluateTo
import ir.smmh.imp.expressions.*
import ir.smmh.imp.statements.*
import kotlin.reflect.KMutableProperty


fun main() = application {
    val viewModes = ViewMode.values()
    val app = remember {
        object : App {
            override val program = Programs.e1

            var preferredDual = 2
            var viewModeIndex by mutableStateOf(preferredDual)

            override val viewMode: ViewMode
                get() = viewModes[viewModeIndex]

            override fun nextViewMode() {
                viewModeIndex = (viewModeIndex + 1) % viewModes.size
                if (viewMode.isDual) preferredDual = viewModeIndex
            }

            val outputList = mutableStateListOf<Output>()

            override fun clearOutput() {
                outputList.clear()
                viewModeIndex = 0
            }

            override fun print(output: Output) {
                outputList.add(output)
            }

            override fun hasOutput() = outputList.size > 0

            val names = setOf(
                NameBinding("print", object : Callable {
                    override val argumentCount: Int = 1
                    override fun call(stack: Stack, input: List<Value>): Value {
                        printLine(stack.evaluateTo(input.first()), OutputLine.Category.MESSAGE)
                        return Void
                    }
                }, false),
                NameBinding("T", object : Callable {
                    override val argumentCount: Int = 1
                    override fun call(stack: Stack, input: List<Value>): Value {
                        printLine(stack.evaluateTo(input.first()), OutputLine.Category.MESSAGE)
                        return Void
                    }
                }, false),
            )

            fun Statement.run() {
                val executionStack = Stack().apply { push().apply { names.forEach(::declare) } }
                try {
                    execute(executionStack)
                } catch (e: Stack.Error) {
                    printLine(e.message ?: e.toString(), OutputLine.Category.ERROR)
                }
            }

            override var selectedStatement by mutableStateOf<Statement?>(null)
                private set

            val thereIsSelection = { selectedStatement != null }

            val runButton = MenuButton(
                MenuButton.View.Text.ConstantText("Run"),
                MenuButton.Action.Simple {
                    printLine("RUNNING PROGRAM", OutputLine.Category.INFO)
                    program.run()
                    printLine("FINISHED RUNNING", OutputLine.Category.INFO)
                    printEmptyLine()
                    if (viewModeIndex == 0) viewModeIndex = preferredDual
//                    val main = executionStack.pop().returnedValue as Function
//                    main.call(emptyList())
                }
            )

            val clearOutputButton = MenuButton(
                MenuButton.View.Text.ConstantTextMaybeEnabled("Clear Output", ::hasOutput),
                MenuButton.Action.Simple(::clearOutput)
            )

            val changeViewModeButton = MenuButton(
                MenuButton.View.Text.VariableText { "View Mode ${viewMode.title}" },
                MenuButton.Action.Simple(::nextViewMode)
            )

            val menuStack = mutableStateListOf<MenuButton.Action.HasChildren>()

            override val menu: MenuButton.Action.HasChildren
                get() = menuStack.lastOrNull() ?: rootButton.action as MenuButton.Action.HasChildren

            override fun goToMenu(menu: MenuButton.Action.HasChildren, keepStack: Boolean) {
                if (!keepStack) menuStack.clear()
                menuStack.add(menu)
            }

            override fun goBack() {
                menuStack.removeLast()
            }

            override fun canGoBack(): Boolean =
                menuStack.size > 0

            val clearSelection = MenuButton(
                MenuButton.View.Text.ConstantTextMaybeEnabled("Clear Selection", thereIsSelection),
                MenuButton.Action.Simple {
                    selectedStatement = null
                    menuStack.clear()
                })

            val runSelected = MenuButton(
                MenuButton.View.Text.ConstantTextMaybeEnabled("Run Selected", thereIsSelection),
                MenuButton.Action.Simple {
                    printLine("RUNNING SELECTED STATEMENT", OutputLine.Category.INFO)
                    selectedStatement?.run()
                    printLine("FINISHED RUNNING", OutputLine.Category.INFO)
                    printEmptyLine()
                    if (viewModeIndex == 0) viewModeIndex = preferredDual
                })

            override fun select(statement: Statement, keepStack: Boolean) {
                selectedStatement = statement
                goToMenu(statementMenu(statement, "").action as MenuButton.Action.HasChildren, keepStack)
            }

            val rootButton = MenuButton(
                MenuButton.View.Text.ConstantText(""),
                MenuButton.Action.HasChildren(
                    listOf(
                        changeViewModeButton,
                        runButton,
                        clearOutputButton,
                    )
                )
            )

            fun expressionMenuButton(it: KMutableProperty<Expression?>): MenuButton = TODO()
            fun variableMenuButton(it: KMutableProperty<Variable?>): MenuButton = TODO()
            fun blockMenuButton(it: KMutableProperty<Block?>): MenuButton = TODO()

            fun statementMenu(statement: Statement?, text: String): MenuButton {
                val onClick = {
                    if (statement == null) {

                    } else {
                        select(statement, true)
                    }
                }
                return MenuButton(
                    MenuButton.View.Text.ConstantText(text),
                    MenuButton.Action.HasChildren(
                        onClick = onClick,
                        onReTop = onClick,
                        children = mutableListOf<MenuButton>().apply {
                            add(runSelected)
                            //add(deleteSelected)
                            when (statement) {

                                is Assertion -> {
                                    add(expressionMenuButton(statement::expression))
                                }

                                is Assignment -> {
                                    add(variableMenuButton(statement::variable))
                                    add(expressionMenuButton(statement::expression))
                                }

                                is Block -> {
                                    // TODO
                                }

                                is If -> {
                                    // add(expressionMenuButton(it::condition))
                                    add(statementMenu(statement.ifTrue, "Then-clause"))
                                    add(statementMenu(statement.ifFalse, "Else-clause"))
                                }

                                is For -> {
                                    add(variableMenuButton(statement::variable))
                                    // TODO add(it::start)
                                    // TODO add(it::end)
                                    add(statementMenu(statement.block, "Loop body"))
                                }

                                is Repeat -> {
                                    add(expressionMenuButton(statement::times))
                                    add(statementMenu(statement.block, "Loop body"))
                                }

                                is While -> {
                                    add(expressionMenuButton(statement::condition))
                                    add(statementMenu(statement.block, "Loop body"))
                                }

                                is NameDeclaration -> {
                                    // TODO add(it::rebindable)
                                    add(variableMenuButton(statement::variable))
                                    add(expressionMenuButton(statement::initializer))
                                }

                                is OneCall -> {
                                    // TODO
                                }

                                is Return -> {
                                    add(expressionMenuButton(statement::expression))
                                }

                                null -> {
                                }
                            }
                        }
                    )
                )
            }
        }
    }

    app.printLine("IMP STARTED", OutputLine.Category.INFO)
    app.printEmptyLine()

    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            @Composable
            fun code() {
                showStatement(app.program, false, app)
            }

            @Composable
            fun output() {
                Column(Modifier.padding(8.dp)) {
                    app.outputList.forEach {
                        when (it) {
                            Output.LineBreak -> showCode("")
                            is OutputLine -> showCode(it.toString(), it.category.color)
                        }
                    }
                }
            }

            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {

                Divider(color = Colors.divider)

                Box(Modifier.weight(1F)) {
                    when (app.viewMode) {
                        ViewMode.CODE_ONLY -> {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .horizontalScroll(rememberScrollState())
                                    .padding(8.dp)
                            ) { code() }
                        }

                        ViewMode.OUTPUT_ONLY -> {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .horizontalScroll(rememberScrollState())
                                    .padding(8.dp)
                            ) { output() }
                        }

                        ViewMode.SIDE_BY_SIDE -> {
                            Row(Modifier.fillMaxSize()) {
                                Box(
                                    Modifier
                                        .weight(1F)
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                        .horizontalScroll(rememberScrollState())
                                        .padding(8.dp)
                                ) { code() }
                                Box(Modifier.width(1.dp).background(Colors.divider).fillMaxHeight())
                                Box(
                                    Modifier
                                        .weight(1F)
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                        .horizontalScroll(rememberScrollState())
                                        .padding(8.dp)
                                ) { output() }
                            }
                        }

                        ViewMode.STACKED -> {
                            Column {
                                Box(
                                    Modifier
                                        .weight(1F)
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                        .horizontalScroll(rememberScrollState())
                                        .padding(8.dp)
                                ) { code() }
                                Box(Modifier.height(1.dp).background(Colors.divider).fillMaxWidth())
                                Box(
                                    Modifier
                                        .weight(1F)
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                        .horizontalScroll(rememberScrollState())
                                        .padding(8.dp)
                                ) { output() }
                            }
                        }
                    }
                }

                Column(Modifier.wrapContentHeight()) {
                    Divider(color = Colors.divider)
                    showMenu(app)
                }
            }
        }
    }
}