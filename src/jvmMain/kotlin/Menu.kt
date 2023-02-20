import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

typealias MenuStack = MutableList<MenuButton.Node>

@Composable
fun showMenuButton(menuButton: MenuButton, menuStack: MenuStack) {
    val onClick = when (menuButton) {
        is MenuButton.Leaf -> menuButton.action
        is MenuButton.Node -> {
            { menuStack.add(menuButton); Unit }
        }
    }
    Button(
        onClick = onClick,
        shape = CircleShape,
        enabled = menuButton.view.enabled
    ) {
        when (menuButton.view) {
            is MenuButton.View.Text -> {
                Text(menuButton.view.text)
            }
        }
    }
}

@Composable
fun showMenu(menuStack: MenuStack) {
    val backButton = remember {
        val view = MenuButton.View.Text.ConstantNameMaybeEnabled("\uD83E\uDC68") { menuStack.size > 1 }
        MenuButton.Leaf(view) { menuStack.removeLast() }
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
    ) {
        showMenuButton(backButton, menuStack)
        menuStack.last().children.forEach {
            showMenuButton(it, menuStack)
        }
    }
}