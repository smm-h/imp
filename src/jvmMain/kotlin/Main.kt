@file:Suppress("FunctionName")

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val run = MenuButton.HasAction("Run") {
        outputLines.clear()
        outputLines.add(welcome)
        try {
            program.execute(executionStack)
        } catch (e: RuntimeError) {
            val message = e.message ?: e.toString()
            outputLines.add(OutputLine(message, OutputLine.Category.ERROR))
        }
//        val main = executionStack.pop().returnedValue as Function
//        main.call(emptyList())
    }
    val root = MenuButton.HasSubButtons("", listOf(run))
    //val list: MutableList<String> = remember { mutableStateListOf("A", "B", "C") }
    val menuStack = remember { mutableStateListOf(root) }
    MaterialTheme {
        Column(
            Modifier
//                .padding(16.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn {
                item {
                    StatementView(program, false)
                }
            }
            LazyColumn {
                outputLines.forEach {
                    item {
                        Code(it.toString(), it.category.color)
                    }
                }
            }
            MenuView(menuStack)
        }
    }
}

@Composable
fun MenuButtonView(it: MenuButton, menuStack: MutableList<MenuButton.HasSubButtons>) {
    val onClick = when (it) {
        is MenuButton.HasAction -> it.action
        is MenuButton.HasSubButtons -> {
            { menuStack.add(it); Unit }
        }
    }
    Button(
        onClick = onClick,
        shape = CircleShape,
    ) {
        Text(it.name)
    }
}

@Composable
fun MenuView(menuStack: MutableList<MenuButton.HasSubButtons>) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Colors.menuBackground)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (menuStack.size > 1) {
            MenuButtonView(MenuButton.HasAction("Back") {
                menuStack.removeLast()
            }, menuStack)
        }
        menuStack.last().items.forEach {
            MenuButtonView(it, menuStack)
        }
    }
}

@Composable
fun Code(
    text: String,
    color: Color = Color.Red,
    bold: Boolean = false,
    italic: Boolean = false,
) = Text(
    text = text,
    color = color,
    fontSize = 18.sp,
    fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
    fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
    fontFamily = FontFamily.Monospace,
)

@Composable
fun Keyword(text: String) = Code(text, color = Colors.keywords, bold = true)

@Composable
fun StatementView(it: Statement, tabbed: Boolean) {
    Row(
        Modifier
//            .padding(1.dp)
            //.border(1.dp, Color.LightGray)
//            .drawBehind {
//                drawRoundRect(
//                    color = Constants.Colors.statementBorder,
//                    cornerRadius = CornerRadius(4F, 4F),
//                    style = Constants.statementBorderStroke,
//                )
//            }
            .padding(1.dp)
//    Button({},
//        enabled = false,
    ) {
        if (tabbed) Code("    ")
        when (it) {
            is Assertion -> Row(verticalAlignment = Alignment.CenterVertically) {
                Keyword("assert ")
                ExpressionView(it.expression)
                Keyword(";")
            }

            is Assignment -> Row(verticalAlignment = Alignment.CenterVertically) {
                Code(it.name, color = Colors.variables, bold = true)
                Keyword(" = ")
                ExpressionView(it.expression)
                Keyword(";")
            }

            is Block -> Column {
                it.list.forEach {
                    StatementView(it, false)
                }
            }

            is FunctionBody -> Column {
                it.list.forEach {
                    StatementView(it, false)
                }
            }

            is If -> Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Keyword("if (")
                    ExpressionView(it.condition)
                    Keyword(") {")
                }
                StatementView(it.ifTrue, true)
                it.ifFalse?.let {
                    Keyword("} else {")
                    StatementView(it, true)
                }
                Keyword("}")
            }

            is For -> Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Keyword("for (")
                    ExpressionView(it.variable)
                    Keyword(" : ")
                    ExpressionView(it.iterable)
                    Keyword(") {")
                }
                StatementView(it.block, true)
                Keyword("}")
            }

            is Repeat -> Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Keyword("repeat (")
                    ExpressionView(it.times)
                    Keyword(") {")
                }
                StatementView(it.block, true)
                Keyword("}")
            }

            is While -> Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Keyword("while (")
                    ExpressionView(it.condition)
                    Keyword(") {")
                }
                StatementView(it.block, true)
                Keyword("}")
            }

            is NameDeclaration -> Row(verticalAlignment = Alignment.CenterVertically) {
                Keyword((if (it.canBeRebound) "var" else "val") + (if (it.canBeMutated) "mut" else "imm"))
                Code(" ")
                Code(it.name, color = Colors.variables, bold = true)
                it.initializer?.let {
                    Keyword(" = ")
                    ExpressionView(it)
                }
                Keyword(";")
            }

            is OneCall -> Row(verticalAlignment = Alignment.CenterVertically) {
                ExpressionView(it.call)
                Keyword(";")
            }

            is Return -> Row(verticalAlignment = Alignment.CenterVertically) {
                Keyword("return ")
                ExpressionView(it.expression)
                Keyword(";")
            }
        }
    }
}

@Composable
fun ExpressionView(it: Expression) {
    Box(
        Modifier
//            .border(1.dp, Color.LightGray)
//            .padding(2.dp)
    ) {
        when (it) {
            is Call -> Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExpressionView(it.callable)
                Keyword("(")
                it.arguments.forEach { ExpressionView(it) }
                Keyword(")")
            }

            is Variable -> Code(it.name, color = Colors.variables, bold = true)

            is StringValue -> Code("\"${it.value}\"", color = Colors.stringLiterals)
            is BooleanValue -> Code(if (it.value) "true" else "false", color = Colors.booleanLiterals)
            is IntValue -> Code(it.value.toString(), color = Colors.numberLiterals)

            else -> Code(it.toString())
        }
    }
}

fun print(text: String) =
    print(StringValue(text))

fun print(expression: Expression) =
    OneCall(Call(Variable("print"), listOf(expression)))

fun block(vararg statements: Statement) =
    Block(statements.asList())

val program = block(
//        print("START"),
//        NameDeclaration("x", true, false, IntValue(5)),
//        NameDeclaration("y", true, false, null),
//        Assignment("y", StringValue("FIRST")),
//        Assignment("y", StringValue("LAST")),
//        print(Variable("y")),
//        print("END"),
    If(BooleanValue(true), block(print("T")), block(print("F")))
)

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App(program)
    }
}
