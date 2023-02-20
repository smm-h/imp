@file:Suppress("FunctionName")

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

typealias MenuStack = MutableList<MenuButton.Node>

@Composable
fun MenuButtonView(menuButton: MenuButton, menuStack: MenuStack) {
    val onClick = when (menuButton) {
        is MenuButton.Leaf -> menuButton.action
        is MenuButton.Node -> {
            { menuStack.add(menuButton); Unit }
        }
    }
    Button(
        onClick = onClick,
        shape = CircleShape,
        enabled = menuButton.enabled
    ) {
        Text(menuButton.name)
    }
}

@Composable
fun MenuView(menuStack: MenuStack) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
    ) {
//        if (menuStack.size > 1) {
        MenuButtonView(MenuButton.Leaf("Back", menuStack.size > 1) { menuStack.removeLast() }, menuStack)
//        }
        menuStack.last().children.forEach {
            MenuButtonView(it, menuStack)
        }
    }
}