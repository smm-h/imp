import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.smmh.imp.statements.*

@Composable
fun showStatement(
    statement: Statement,
    tabbed: Boolean,
    app: App,
) {
    val shape = RoundedCornerShape(8.dp)
    val isSelected = app.selectedStatement == statement
    val isBlock = statement is Block
    val borderWidth =
        if (isSelected) 3.dp
        else 1.dp
    val borderColor =
        if (isSelected) Colors.Statement.Border.isSelected
        else if (isBlock) Colors.Statement.Border.isBlock
        else Colors.Statement.Border.isNeither
    Row {
        if (tabbed) showCode("    ")
        Box(
            Modifier
                .padding(8.dp)
                .border(borderWidth, borderColor, shape)
                .clickable { app.select(statement, false) }
        ) {
            Box(Modifier.padding(if (isBlock) 2.dp else 8.dp)) {
                when (statement) {

                    is Assertion -> Row(verticalAlignment = Alignment.CenterVertically) {
                        showKeyword("assert ")
                        showExpression(statement.expression, app)
                        showKeyword(";")
                    }

                    is Assignment -> Row(verticalAlignment = Alignment.CenterVertically) {
                        showExpression(statement.variable, app)
                        showKeyword(" = ")
                        showExpression(statement.expression, app)
                        showKeyword(";")
                    }

                    is Block -> Column {
                        statement.list.forEach {
                            showStatement(it, false, app)
                        }
                    }

                    is If -> Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            showKeyword("if (")
                            showExpression(statement.condition, app)
                            showKeyword(") then {")
                        }
                        showStatement(statement.ifTrue, true, app)
                        if (statement.ifFalse.list.isNotEmpty()) {
                            showKeyword("} else {")
                            showStatement(statement.ifFalse, true, app)
                        }
                        showKeyword("}")
                    }

                    is For -> Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            showKeyword("for (")
                            showExpression(statement.start, app)
                            showKeyword(" <= ")
                            showExpression(statement.variable, app)
                            showKeyword(" < ")
                            showExpression(statement.end, app)
                            showKeyword(") {")
                        }
                        showStatement(statement.block, true, app)
                        showKeyword("}")
                    }

                    is Repeat -> Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            showKeyword("repeat (")
                            showExpression(statement.times, app)
                            showKeyword(") {")
                        }
                        showStatement(statement.block, true, app)
                        showKeyword("}")
                    }

                    is While -> Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            showKeyword("while (")
                            showExpression(statement.condition, app)
                            showKeyword(") {")
                        }
                        showStatement(statement.block, true, app)
                        showKeyword("}")
                    }

                    is NameDeclaration -> Row(verticalAlignment = Alignment.CenterVertically) {
                        showKeyword(if (statement.rebindable) "var" else "val")
                        showCode(" ")
                        showExpression(statement.variable, app)
                        statement.initializer?.let {
                            showKeyword(" = ")
                            showExpression(it, app)
                        }
                        showKeyword(";")
                    }

                    is OneCall -> Row(verticalAlignment = Alignment.CenterVertically) {
                        showExpression(statement.call, app)
                        showKeyword(";")
                    }

                    is Return -> Row(verticalAlignment = Alignment.CenterVertically) {
                        showKeyword("return ")
                        showExpression(statement.expression, app)
                        showKeyword(";")
                    }
                }
            }
        }
    }
}