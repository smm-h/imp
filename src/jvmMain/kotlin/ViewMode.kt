enum class ViewMode(val title: String, val isDual: Boolean) {
    CODE_ONLY("▣", false),
    OUTPUT_ONLY("▤", false),
    SIDE_BY_SIDE("◫", true),
    STACKED("⊟", true),
}