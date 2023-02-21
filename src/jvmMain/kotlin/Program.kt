import ProgramHelpers.IF
import ProgramHelpers.PROGRAM
import ProgramHelpers.TRUE
import ProgramHelpers.PRINT


val program = PROGRAM {
    IF(TRUE, {
        PRINT("T")
    }, {
        PRINT("F")
    })
}

//    print("Hello, world!"),
//    NameDeclaration("x", true, false, IntValue(5)),
//    NameDeclaration("y", true, false, null),
//    Assignment("y", StringValue("FIRST")),
//    Assignment("y", StringValue("LAST")),
//    print(Variable("y")),
//    print("END"),
//