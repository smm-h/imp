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

val welcome = OutputLine("WELCOME TO IMP (VERSION ALPHA)", OutputLine.Category.INFO)

enum class ViewMode(val title: String) {
    CODE_ONLY("▣"),
    OUTPUT_ONLY("▤"),
    SIDE_BY_SIDE("◫"),
    STACKED("⊟"),
}

@Composable
fun app(program: Statement) {
    var viewMode by remember { mutableStateOf(2) }
    val outputLines = remember { mutableStateListOf(welcome) }

    val names = setOf(
        NameBinding("print", object : Callable {
            override val argumentCount: Int = 1
            override fun call(stack: Stack, input: List<Value>): Value {
                outputLines.add(OutputLine(stack.evaluateTo(input.first()), OutputLine.Category.MESSAGE))
                return Void
            }
        }, false),
    )

    fun Statement.run() {
        val executionStack = Stack().apply { push().apply { names.forEach(::declare) } }
        try {
            execute(executionStack)
        } catch (e: Stack.Error) {
            val message = e.message ?: e.toString()
            outputLines.add(OutputLine(message, OutputLine.Category.ERROR))
        }
    }

    val selectedStatement = remember { mutableStateOf<Statement?>(null) }

    val thereIsSelection = { selectedStatement.value != null }

    val thereIsOutput = { outputLines.size > 1 }

    val run =
        remember {
            Leaf(View.Text.ConstantNameAlwaysEnabled("Run")) {
                program.run()
//                val main = executionStack.pop().returnedValue as Function
//                main.call(emptyList())
            }
        }
    val clearOutput =
        remember {
            Leaf(View.Text.ConstantNameMaybeEnabled("Clear Output", thereIsOutput)) {
                outputLines.apply {
                    clear()
                    add(welcome)
                }
            }
        }
    val changeViewMode =
        remember {
            val array = ViewMode.values()
            Leaf(View.Text.VariableNameAlwaysEnabled { "View Mode ${array[viewMode].title}" }) {
                viewMode = (viewMode + 1) % array.size
            }
        }
    val root =
        remember {
            Node(
                View.Text.ConstantNameAlwaysEnabled(""), listOf(
                    changeViewMode,
                    run,
                    clearOutput,
                )
            )
        }

    val menuStack: MenuStack = remember { mutableStateListOf() }

    val clearSelection =
        remember {
            Leaf(View.Text.ConstantNameMaybeEnabled("Clear Selection", thereIsSelection)) {
                selectedStatement.value = null
                menuStack.clear()
            }
        }

    val runSelected =
        remember {
            Leaf(View.Text.ConstantNameMaybeEnabled("Run Selected", thereIsSelection)) {
                selectedStatement.value?.run()
            }
        }

    val deleteSelected =
        remember {
            Leaf(View.Text.ConstantNameMaybeEnabled("Delete", thereIsSelection)) {
                selectedStatement.value?.run()
            }
        }

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
                    TODO()
                }

                is If -> {
                    add(expressionMenuButton(it::condition))
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
                    TODO()
                }

                is Return -> {
                    add(expressionMenuButton(it::expression))
                }

                null -> {
                }
            }
        }
    )

    MaterialTheme {
        @Composable
        fun code() {
            showStatement(program, false, selectedStatement, false)
        }

        @Composable
        fun output() {
            Column(Modifier.padding(8.dp)) {
                outputLines.forEach {
                    showCode(it.toString(), it.category.color)
                }
            }
        }

        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Box(Modifier.weight(1F)) {
                when (ViewMode.values()[viewMode]) {
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
                showMenu(root, menuStack)
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        app(program)
    }
}
