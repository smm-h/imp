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


@Composable
fun showMenuButton(menuButton: MenuButton, app: App) {
    val onClick = when (val action = menuButton.action) {
        is MenuButton.Action.Simple -> action.onClick
        is MenuButton.Action.HasChildren -> {
            {
                action.onClick?.invoke()
                app.goToMenu(action, true)
            }
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
fun showMenu(app: App) {
    val backButton = remember {
        MenuButton(
            MenuButton.View.Text.ConstantTextMaybeEnabled("\uD83E\uDC68", app::canGoBack),
            MenuButton.Action.Simple(app::goBack),
        )
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
    ) {
        showMenuButton(backButton, app)
        app.menu.children.forEach {
            showMenuButton(it, app)
        }
    }
}