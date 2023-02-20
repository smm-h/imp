import ir.smmh.imp.expressions.*
import ir.smmh.imp.statements.Block
import ir.smmh.imp.statements.If
import ir.smmh.imp.statements.OneCall
import ir.smmh.imp.statements.Statement

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