@file:Suppress("FunctionName")

import Colors.Statement
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.smmh.imp.statements.*

@Composable
fun StatementView(
    statement: ir.smmh.imp.statements.Statement,
    tabbed: Boolean,
    selectedStatement: MutableState<ir.smmh.imp.statements.Statement?>,
    parentIsSelected: Boolean,
) {
    val shape = RoundedCornerShape(8.dp)
    val isSelected = selectedStatement.value == statement
    val isBlock = statement is Block || statement is FunctionBody
    val onClick = if (isSelected) {
        { selectedStatement.value = null }
    } else {
        { selectedStatement.value = statement }
    }
    val borderColor =
        if (isSelected) Statement.Border.isSelected
        else if (parentIsSelected) Statement.Border.parentIsSelected
        else if (isBlock) Statement.Border.isNotSelected_isBlock
        else Statement.Border.isNotSelected_isNotBlock
    val backColor =
        if (isSelected) Statement.Background.isSelected
        else Statement.Background.isNotSelected
    Row {
        if (tabbed) Code("    ")
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
                        Keyword("assert ")
                        ExpressionView(statement.expression)
                        Keyword(";")
                    }

                    is Assignment -> Row(verticalAlignment = Alignment.CenterVertically) {
                        Code(statement.name, color = Colors.Code.variables, bold = true)
                        Keyword(" = ")
                        ExpressionView(statement.expression)
                        Keyword(";")
                    }

                    is Block -> Column {
                        statement.list.forEach {
                            StatementView(it, false, selectedStatement, isSelected)
                        }
                    }

                    is FunctionBody -> Column {
                        statement.list.forEach {
                            StatementView(it, false, selectedStatement, isSelected)
                        }
                    }

                    is If -> Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Keyword("if (")
                            ExpressionView(statement.condition)
                            Keyword(") {")
                        }
                        StatementView(statement.ifTrue, true, selectedStatement, isSelected)
                        statement.ifFalse?.let {
                            Keyword("} else {")
                            StatementView(it, true, selectedStatement, isSelected)
                        }
                        Keyword("}")
                    }

                    is For -> Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Keyword("for (")
                            ExpressionView(statement.variable)
                            Keyword(" : ")
                            ExpressionView(statement.iterable)
                            Keyword(") {")
                        }
                        StatementView(statement.block, true, selectedStatement, isSelected)
                        Keyword("}")
                    }

                    is Repeat -> Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Keyword("repeat (")
                            ExpressionView(statement.times)
                            Keyword(") {")
                        }
                        StatementView(statement.block, true, selectedStatement, isSelected)
                        Keyword("}")
                    }

                    is While -> Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Keyword("while (")
                            ExpressionView(statement.condition)
                            Keyword(") {")
                        }
                        StatementView(statement.block, true, selectedStatement, isSelected)
                        Keyword("}")
                    }

                    is NameDeclaration -> Row(verticalAlignment = Alignment.CenterVertically) {
                        Keyword((if (statement.canBeRebound) "var" else "val") + (if (statement.canBeMutated) "mut" else "imm"))
                        Code(" ")
                        Code(statement.name, color = Colors.Code.variables, bold = true)
                        statement.initializer?.let {
                            Keyword(" = ")
                            ExpressionView(it)
                        }
                        Keyword(";")
                    }

                    is OneCall -> Row(verticalAlignment = Alignment.CenterVertically) {
                        ExpressionView(statement.call)
                        Keyword(";")
                    }

                    is Return -> Row(verticalAlignment = Alignment.CenterVertically) {
                        Keyword("return ")
                        ExpressionView(statement.expression)
                        Keyword(";")
                    }
                }
            }
        }
    }
}