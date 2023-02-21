import androidx.compose.runtime.MutableState
import ir.smmh.imp.statements.Statement

interface App {
    var viewMode: Int
    val outputLines: MutableList<OutputLine>
    var selectedStatement: Statement?
    //parentIsSelected: Boolean
}