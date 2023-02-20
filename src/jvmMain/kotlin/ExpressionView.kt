@file:Suppress("FunctionName")

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ir.smmh.imp.expressions.*

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
fun Keyword(text: String) =
    Code(text, color = Colors.Code.keywords, bold = true)

@Composable
fun ExpressionView(expression: Expression) {
    Box(
        Modifier
//            .border(1.dp, Color.LightGray)
//            .padding(2.dp)
    ) {
        when (expression) {
            is Call -> Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExpressionView(expression.callable)
                Keyword("(")
                expression.arguments.forEach { ExpressionView(it) }
                Keyword(")")
            }

            is Variable ->
                Code(expression.name, color = Colors.Code.variables, bold = true)

            is StringValue ->
                Code("\"${expression.value}\"", color = Colors.Code.stringLiterals)

            is BooleanValue ->
                Code(if (expression.value) "true" else "false", color = Colors.Code.booleanLiterals)

            is IntValue ->
                Code(expression.value.toString(), color = Colors.Code.numberLiterals)

            else ->
                Code(expression.toString())
        }
    }
}