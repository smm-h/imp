data class MenuButton(val view: View, val action: Action) {

    sealed class Action {
        class Simple(
            val onClick: () -> Unit,
        ) : Action()

        class HasChildren(
            val children: List<MenuButton>,
            val onReTop: (() -> Unit)? = null,
            val onClick: (() -> Unit)? = null,
        ) : Action()
    }

    sealed class View {
        abstract val enabled: Boolean

        sealed class Text : View() {
            abstract val text: String

            class ConstantText(
                override val text: String,
            ) : Text() {
                override val enabled: Boolean get() = true
            }

            class ConstantTextMaybeEnabled(
                override val text: String,
                val enabledLambda: () -> Boolean,
            ) : Text() {
                override val enabled: Boolean get() = enabledLambda()
            }

            class VariableText(
                val textLambda: () -> String,
            ) : Text() {
                override val text: String get() = textLambda()
                override val enabled: Boolean get() = true
            }
        }
    }
}