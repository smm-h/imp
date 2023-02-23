import ProgramHelpers.ADD
import ProgramHelpers.ASSIGN
import ProgramHelpers.DECLARE
import ProgramHelpers.DECLARE_REBINDABLE
import ProgramHelpers.FOR
import ProgramHelpers.FUNCTION
import ProgramHelpers.IF
import ProgramHelpers.PRINT
import ProgramHelpers.PROGRAM
import ProgramHelpers.RETURN
import ProgramHelpers.TRUE
import ProgramHelpers.call
import ir.smmh.imp.expressions.Variable
import ir.smmh.imp.statements.*

@Suppress("unused")
object Programs {

    val helloWorld = PROGRAM {
        PRINT("Hello, world!")
    }

    val simpleIf = PROGRAM {
        IF(TRUE, {
            PRINT("T")
        }, {
            PRINT("F")
        })
    }

    val e1 = PROGRAM {
        DECLARE("f", FUNCTION("n") {
            DECLARE_REBINDABLE("s", 0)
            FOR("i", 1, ADD("n", 1)) {
                ASSIGN("s", ADD("s", 1))
            }
            RETURN("s")
        })
        PRINT(call("T", Variable("f")))
    }

//    NameDeclaration("x", true, false, IntValue(5)),
//    NameDeclaration("y", true, false, null),
//    Assignment("y", StringValue("FIRST")),
//    Assignment("y", StringValue("LAST")),
//    print(Variable("y")),
//    print("END"),
//
}