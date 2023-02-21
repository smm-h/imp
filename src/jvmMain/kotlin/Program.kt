import ir.smmh.imp.expressions.*
import ir.smmh.imp.statements.Block
import ir.smmh.imp.statements.If
import ir.smmh.imp.statements.OneCall
import ir.smmh.imp.statements.Statement

fun print(text: String) =
    print(StringValue(text))

fun print(expression: Expression) =
    OneCall(Call().apply { callable = Variable("print"); arguments.add(expression) })

fun block(vararg statements: Statement) =
    Block().apply { statements.forEach { list.add(it) } }

val program = block(
//    print("Hello, world!"),
//    NameDeclaration("x", true, false, IntValue(5)),
//    NameDeclaration("y", true, false, null),
//    Assignment("y", StringValue("FIRST")),
//    Assignment("y", StringValue("LAST")),
//    print(Variable("y")),
//    print("END"),
    If(BooleanValue(true), block(print("T")), block(print("F")))
)