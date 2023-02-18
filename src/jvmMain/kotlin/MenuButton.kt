sealed class MenuButton(val name: String) {

    class HasSubButtons
        (name: String, val items: List<MenuButton>) :
        MenuButton(name)

    class HasAction
        (name: String, val action: () -> Unit) :
        MenuButton(name)
}