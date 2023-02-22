import ProgramHelpers.IF
import ProgramHelpers.PRINT
import ProgramHelpers.PROGRAM
import ProgramHelpers.TRUE


val program = PROGRAM {
//    PRINT("Hello, world!")
    IF(TRUE, {
        PRINT("T")
    }, {
        PRINT("F")
    })
}

//    NameDeclaration("x", true, false, IntValue(5)),
//    NameDeclaration("y", true, false, null),
//    Assignment("y", StringValue("FIRST")),
//    Assignment("y", StringValue("LAST")),
//    print(Variable("y")),
//    print("END"),