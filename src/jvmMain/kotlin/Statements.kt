import androidx.compose.foundation.background
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
    //val isParentSelected = app.selectedStatement == statement.parent
    val isBlock = statement is Block
    val onClick = if (isSelected) {
        { app.selectedStatement = null }
    } else {
        { app.selectedStatement = statement }
    }
    val borderColor =
        if (isSelected) Colors.Statement.Border.isSelected
//        else if (isParentSelected) Colors.Statement.Border.isParentSelected
        else if (isBlock) Colors.Statement.Border.isNotSelected_isBlock
        else Colors.Statement.Border.isNotSelected_isNotBlock
    val backColor =
//        if (isSelected) Colors.Statement.Background.isSelected
//        else
        Colors.Statement.Background.isNotSelected
    Row {
        if (tabbed) showCode("    ")
        Box(
            Modifier
                .padding(8.dp)
                .border(1.dp, borderColor, shape)
                .background(backColor, shape)
                .clickable(onClick = onClick)
        ) {
            Box(Modifier.padding(if (isBlock) 2.dp else 8.dp)) {
                when (statement) {

                    is Assertion -> Row(verticalAlignment = Alignment.CenterVertically) {
                        showKeyword("assert ")
                        showExpression(statement.expression)
                        showKeyword(";")
                    }

                    is Assignment -> Row(verticalAlignment = Alignment.CenterVertically) {
                        showExpression(statement.variable)
                        showKeyword(" = ")
                        showExpression(statement.expression)
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
                            showExpression(statement.condition)
                            showKeyword(") {")
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
                            showExpression(statement.start)
                            showKeyword(" <= ")
                            showExpression(statement.variable)
                            showKeyword(" < ")
                            showExpression(statement.end)
                            showKeyword(") {")
                        }
                        showStatement(statement.block, true, app)
                        showKeyword("}")
                    }

                    is Repeat -> Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            showKeyword("repeat (")
                            showExpression(statement.times)
                            showKeyword(") {")
                        }
                        showStatement(statement.block, true, app)
                        showKeyword("}")
                    }

                    is While -> Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            showKeyword("while (")
                            showExpression(statement.condition)
                            showKeyword(") {")
                        }
                        showStatement(statement.block, true, app)
                        showKeyword("}")
                    }

                    is NameDeclaration -> Row(verticalAlignment = Alignment.CenterVertically) {
                        showKeyword(if (statement.rebindable) "var" else "val")
                        showCode(" ")
                        showExpression(statement.variable)
                        statement.initializer?.let {
                            showKeyword(" = ")
                            showExpression(it)
                        }
                        showKeyword(";")
                    }

                    is OneCall -> Row(verticalAlignment = Alignment.CenterVertically) {
                        showExpression(statement.call)
                        showKeyword(";")
                    }

                    is Return -> Row(verticalAlignment = Alignment.CenterVertically) {
                        showKeyword("return ")
                        showExpression(statement.expression)
                        showKeyword(";")
                    }
                }
            }
        }
    }
}