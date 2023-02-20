@file:Suppress("FunctionName")

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ir.smmh.imp.NameBinding
import ir.smmh.imp.RuntimeError
import ir.smmh.imp.Stack
import ir.smmh.imp.expressions.*
import ir.smmh.imp.statements.*

val welcome = OutputLine("WELCOME TO IMP (VERSION ALPHA)", OutputLine.Category.INFO)

@Composable
fun App(program: Statement) {
    val outputLines = remember { mutableStateListOf(welcome) }
    val print = object : Callable {
        override fun call(input: List<Value>): Value {
            outputLines.add(OutputLine((input.first() as StringValue).value, OutputLine.Category.MESSAGE))
            return Void
        }
    }
    val executionStack = Stack().apply {
        push().apply {
            declare(NameBinding("print", print, false, false))
        }
    }

    fun run(program: Statement) {
        try {
            program.execute(executionStack)
        } catch (e: RuntimeError) {
            val message = e.message ?: e.toString()
            outputLines.add(OutputLine(message, OutputLine.Category.ERROR))
        }
//        val main = executionStack.pop().returnedValue as Function
//        main.call(emptyList())
    }

    val selectedStatement = remember { mutableStateOf<Statement?>(null) }

    val thereIsSelection = selectedStatement.value != null
    val thereIsOutput = outputLines.size > 1

    val run =
        MenuButton.Leaf("Run", true) { run(program) }
    val runSelected =
        MenuButton.Leaf("Run Selected", thereIsSelection) { selectedStatement.value?.let { run(it) } }
    val clearSelection =
        MenuButton.Leaf("Clear Selection", thereIsSelection) { selectedStatement.value = null }

    val clearOutput =
        MenuButton.Leaf("Clear Output", thereIsOutput) { outputLines.apply { clear(); add(welcome) } }

    val root =
//        if (selectedStatement.value == null)
//        MenuButton.Node("", true, listOf(run)) else
        MenuButton.Node("", true, listOf(run, runSelected, clearSelection, clearOutput))

    val menuStack = mutableStateListOf(root)

    MaterialTheme {
        Column(
            Modifier
                .fillMaxHeight(),
            //verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                Modifier
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
                    .horizontalScroll(rememberScrollState())
                    .weight(1F)
                    .fillMaxSize()
            ) {
                StatementView(program, false, selectedStatement, false)
            }
            Divider()
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .horizontalScroll(rememberScrollState())
                    .weight(1F)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                outputLines.forEach {
                    Code(it.toString(), it.category.color)
                }
            }
            Column {
                Divider()
                MenuView(menuStack)
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App(program)
    }
}
