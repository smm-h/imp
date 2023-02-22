import MenuButton.*
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

            var viewModeIndex by mutableStateOf(0)
            var preferredDual = 2

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
                        print(stack.evaluateTo(input.first()), OutputLine.Category.MESSAGE)
                        return Void
                    }
                }, false),
            )

            fun Statement.run() {
                val executionStack = Stack().apply { push().apply { names.forEach(::declare) } }
                try {
                    execute(executionStack)
                } catch (e: Stack.Error) {
                    print(e.message ?: e.toString(), OutputLine.Category.ERROR)
                }
            }

            override var selectedStatement by mutableStateOf<Statement?>(null)
                private set

            val thereIsSelection = { selectedStatement != null }

            val run =
                Leaf(View.Text.ConstantNameAlwaysEnabled("Run")) {
                    print(Output.LineBreak)
                    print("STARTED RUNNING", OutputLine.Category.INFO)
                    program.run()
                    print("FINISHED RUNNING", OutputLine.Category.INFO)
                    if (viewModeIndex == 0) viewModeIndex = preferredDual
//                    val main = executionStack.pop().returnedValue as Function
//                    main.call(emptyList())
                }

            val clearOutput: Leaf =
                Leaf(View.Text.ConstantNameMaybeEnabled("Clear Output", ::hasOutput), ::clearOutput)

            val changeViewMode =
                Leaf(View.Text.VariableNameAlwaysEnabled { "View Mode ${viewMode.title}" }, ::nextViewMode)

            val menuStack = mutableStateListOf<Node>()

            override val menu: Node
                get() = menuStack.lastOrNull() ?: root

            override fun goToMenu(menu: Node, keepStack: Boolean) {
                if (!keepStack) menuStack.clear()
                menuStack.add(menu)
            }

            override fun goBack() {
                menuStack.removeLast()
            }

            override fun canGoBack(): Boolean =
                menuStack.size > 0

            val clearSelection =
                Leaf(View.Text.ConstantNameMaybeEnabled("Clear Selection", thereIsSelection)) {
                    selectedStatement = null
                    menuStack.clear()
                }

            val runSelected =
                Leaf(View.Text.ConstantNameMaybeEnabled("Run Selected", thereIsSelection)) {
                    selectedStatement?.run()
                }

            val deleteSelected =
                Leaf(View.Text.ConstantNameMaybeEnabled("Delete", thereIsSelection)) {
                    selectedStatement?.run()
                }

            override fun select(statement: Statement, keepStack: Boolean) {
                selectedStatement = statement
                goToMenu(statementMenu(statement), keepStack)
            }

            val root =
                Node(
                    View.Text.ConstantNameAlwaysEnabled(""), listOf(
                        changeViewMode,
                        run,
                        clearOutput,
                    )
                )

            fun expressionMenuButton(it: KMutableProperty<Expression?>): MenuButton = TODO()
            fun variableMenuButton(it: KMutableProperty<Variable?>): MenuButton = TODO()
            fun blockMenuButton(it: KMutableProperty<Block?>): MenuButton = TODO()

            fun statementMenu(it: Statement?): Node = Node(View.Text.ConstantNameAlwaysEnabled(""),
                mutableListOf<MenuButton>().apply {
                    add(runSelected)
                    add(deleteSelected)
                    when (it) {

                        is Assertion -> {
                            add(expressionMenuButton(it::expression))
                        }

                        is Assignment -> {
                            add(variableMenuButton(it::variable))
                            add(expressionMenuButton(it::expression))
                        }

                        is Block -> {
                            // TODO
                        }

                        is If -> {
                            // add(expressionMenuButton(it::condition))
                            add(statementMenu(it.ifTrue))
                            add(statementMenu(it.ifFalse))
                        }

                        is For -> {
                            add(variableMenuButton(it::variable))
                            // TODO add(it::start)
                            // TODO add(it::end)
                            add(statementMenu(it.block))
                        }

                        is Repeat -> {
                            add(expressionMenuButton(it::times))
                            add(statementMenu(it.block))
                        }

                        is While -> {
                            add(expressionMenuButton(it::condition))
                            add(statementMenu(it.block))
                        }

                        is NameDeclaration -> {
                            // TODO add(it::rebindable)
                            add(variableMenuButton(it::variable))
                            add(expressionMenuButton(it::initializer))
                        }

                        is OneCall -> {
                            // TODO
                        }

                        is Return -> {
                            add(expressionMenuButton(it::expression))
                        }

                        null -> {
                        }
                    }
                }
            )
        }
    }

    app.print("IMP STARTED", OutputLine.Category.INFO)

    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            @Composable
            fun code() {
                showStatement(program, false, app)
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