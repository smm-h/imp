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
import ir.smmh.imp.expressions.Callable
import ir.smmh.imp.expressions.StringValue
import ir.smmh.imp.expressions.Value
import ir.smmh.imp.expressions.Void
import ir.smmh.imp.statements.Statement

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
            override fun call(input: List<Value>): Value {
                outputLines.add(0, OutputLine((input.first() as StringValue).value, OutputLine.Category.MESSAGE))
                return Void
            }
        }, false),
    )

    fun run(program: Statement) {
        val executionStack = Stack().apply { push().apply { names.forEach(::declare) } }
        try {
            program.execute(executionStack)
        } catch (e: Stack.Error) {
            val message = e.message ?: e.toString()
            outputLines.add(OutputLine(message, OutputLine.Category.ERROR))
        }
//        val main = executionStack.pop().returnedValue as Function
//        main.call(emptyList())
    }

    val selectedStatement = remember { mutableStateOf<Statement?>(null) }

    val thereIsSelection = { selectedStatement.value != null }
    val thereIsOutput = { outputLines.size > 1 }

    val run =
        remember {
            Leaf(View.Text.ConstantNameAlwaysEnabled("Run")) {
                run(program)
            }
        }
    val runSelected =
        remember {
            Leaf(View.Text.ConstantNameMaybeEnabled("Run Selected", thereIsSelection)) {
                selectedStatement.value?.let {
                    run(it)
                }
            }
        }
    val clearSelection =
        remember {
            Leaf(View.Text.ConstantNameMaybeEnabled("Clear Selection", thereIsSelection)) {
                selectedStatement.value = null
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
                    runSelected,
                    clearSelection,
                    clearOutput,
                )
            )
        }

    val menuStack: MenuStack = remember { mutableStateListOf(root) }

    MaterialTheme {
        @Composable
        fun code() {
            showStatement(program, false, selectedStatement, false)
        }

        @Composable
        fun output() {
            outputLines.forEach {
                showCode(it.toString(), it.category.color)
            }
        }

        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {

            Column {
                showMenu(menuStack)
                Divider(color = Colors.divider)
            }
            when (ViewMode.values()[viewMode]) {
                ViewMode.CODE_ONLY -> {
                    Box(
                        Modifier
                            .weight(1F)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .horizontalScroll(rememberScrollState())
                            .padding(8.dp)
                    ) { code() }
                }

                ViewMode.OUTPUT_ONLY -> {
                    Column(
                        Modifier
                            .weight(1F)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .horizontalScroll(rememberScrollState())
                            .padding(16.dp)
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
                        Column(
                            Modifier
                                .weight(1F)
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .horizontalScroll(rememberScrollState())
                                .padding(16.dp)
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
                        Column(
                            Modifier
                                .weight(1F)
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .horizontalScroll(rememberScrollState())
                                .padding(16.dp)
                        ) { output() }
                    }
                }
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        app(program)
    }
}
