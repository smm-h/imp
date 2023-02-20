sealed class MenuButton(val view: View) {
    class Node(view: View, val children: List<MenuButton>) : MenuButton(view)
    class Leaf(view: View, val action: () -> Unit) : MenuButton(view)

    sealed class View {
        abstract val enabled: Boolean

        sealed class Text : View() {
            abstract val text: String

            class ConstantNameAlwaysEnabled(
                override val text: String,
            ) : Text() {
                override val enabled: Boolean get() = true
            }

            class ConstantNameMaybeEnabled(
                override val text: String,
                val enabledLambda: () -> Boolean,
            ) : Text() {
                override val enabled: Boolean get() = enabledLambda()
            }

            class VariableNameAlwaysEnabled(
                val textLambda: () -> String,
            ) : Text() {
                override val text: String get() = textLambda()
                override val enabled: Boolean get() = true
            }

            class VariableNameMaybeEnabled(
                val textLambda: () -> String,
                val enabledLambda: () -> Boolean,
            ) : Text() {
                override val text: String get() = textLambda()
                override val enabled: Boolean get() = enabledLambda()
            }
        }
    }
}