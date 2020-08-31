package design

sealed class Palette {

    companion object {

        /**
         * @return [Palette] for given [ColorScheme]
         */
        operator fun invoke(colorScheme: ColorScheme): Palette =
            when(colorScheme) {
                ColorScheme.Light -> Light
                ColorScheme.Dark -> Dark
            }
    }

    abstract val primary: Color
    abstract val primaryLight: Color
    abstract val primaryDark: Color

    abstract val secondary: Color
    abstract val secondaryLight: Color
    abstract val secondaryDark: Color

    abstract val onSecondary: Color

    abstract val tertiary: Color
    abstract val tertiaryLight: Color
    abstract val tertiaryDark: Color

    abstract val background: Color
    abstract val surface: Color

    object Light : Palette() {

        override val primary = Color.CeladonBlue
        override val primaryLight = Color.CeruleanFrost
        override val primaryDark = Color.BlueSapphire

        override val secondary = Color.Bittersweet
        override val secondaryLight = Color.SalmonPink
        override val secondaryDark = Color.OrangeRedCrayola

        override val onSecondary: Color = Color.White

        override val tertiary = Color.EtonBlue
        override val tertiaryLight = Color.CambridgeBlue
        override val tertiaryDark = Color.ShinyShamrock

        override val background = Color.Cultured
        override val surface = Color.White
    }

    object Dark : Palette() {

        override val primary = Color.CeladonBlue
        override val primaryLight = Color.CeruleanFrost
        override val primaryDark = Color.BlueSapphire

        override val secondary = Color.Bittersweet
        override val secondaryLight = Color.SalmonPink
        override val secondaryDark = Color.OrangeRedCrayola

        override val onSecondary: Color = Color.White

        override val tertiary = Color.EtonBlue
        override val tertiaryLight = Color.CambridgeBlue
        override val tertiaryDark = Color.ShinyShamrock

        override val background = Color.RichBlack
        override val surface = Color.EerieBlack
    }
}

enum class ColorScheme { Light, Dark }

inline class Color(val hex: Long) {

    companion object {
        val Bittersweet = Color(0xFFff6b6b)
        val BlueSapphire = Color(0xFF386480)
        val CeladonBlue = Color(0xFF457b9d)
        val CambridgeBlue = Color(0xFFa4ccB8)
        val CeruleanFrost = Color(0xFF5590b4)
        val Cultured = Color(0xFFf9f9f9)
        val EerieBlack = Color(0xFF1c1c1c)
        val EtonBlue = Color(0xFF87bba2)
        val OrangeRedCrayola = Color(0xFFff5c5c)
        val RichBlack = Color(0xFF0c0c0c)
        val SalmonPink = Color(0xFFff9999)
        val ShinyShamrock = Color(0xFF6fae8f)
        val White = Color(0xFFffffff)
    }
}
