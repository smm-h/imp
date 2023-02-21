import androidx.compose.ui.graphics.Color

internal object Colors {
    val divider = Color.LightGray

    internal object Code {
        val stringLiterals =
            Color(0.10F, 0.70F, 0.40F)
        val booleanLiterals =
            Color(0.10F, 0.55F, 0.55F)
        val numberLiterals =
            Color(0.10F, 0.40F, 0.70F)
        val variables =
            Color(0.70F, 0.30F, 0.10F)
        val keywords =
            Color(0.10F, 0.10F, 0.60F)
    }

    internal object Statement {
        internal object Border {
            val isNotSelected_isBlock =
                Color(0.95F, 0.95F, 0.95F)
            val isSelected =
                Color(0.60F, 0.80F, 0.95F)
            val parentIsSelected =
                Color(0.80F, 0.90F, 0.95F)
            val isNotSelected_isNotBlock =
                Color.White
        }

        internal object Background {
            val isSelected =
                Color(0.95F, 0.98F, 0.99F)
            val isNotSelected =
                Color.White
        }
    }
}