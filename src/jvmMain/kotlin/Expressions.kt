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
fun showCode(
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
fun showKeyword(text: String) =
    showCode(text, color = Colors.Code.keywords, bold = true)

@Composable
fun showExpression(expression: Expression?) {
    Box(
        Modifier
//            .border(1.dp, Color.LightGray)
//            .padding(2.dp)
    ) {
        when (expression) {
            is Call -> Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                showExpression(expression.callable)
                showKeyword("(")
                expression.arguments.forEach { showExpression(it) }
                showKeyword(")")
            }

            is Variable ->
                showCode(expression.name, color = Colors.Code.variables, bold = true)

            is StringValue ->
                showCode("\"${expression.value}\"", color = Colors.Code.stringLiterals)

            is BooleanValue ->
                showCode(if (expression.value) "true" else "false", color = Colors.Code.booleanLiterals)

            is DoubleValue ->
                showCode(expression.value.toString(), color = Colors.Code.numberLiterals)

            null ->
                showCode("TODO", color = Color.Blue, italic = true)

            else ->
                showCode(expression.toString())
        }
    }
}