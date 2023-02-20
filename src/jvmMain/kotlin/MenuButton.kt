sealed class MenuButton(
    val name: String,
    val enabled: Boolean,
) {
    class Node(
        name: String,
        enabled: Boolean,
        val children: List<MenuButton>,
    ) : MenuButton(name, enabled)

    class Leaf(
        name: String,
        enabled: Boolean,
        val action: () -> Unit,
    ) : MenuButton(name, enabled)
}