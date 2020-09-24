package levelGenerators.jcatlos

data class Column(private var text: StringBuilder = StringBuilder()) {
    private var height: Int = text.length

    fun getHeight(): Int{
        return height
    }

    fun getBuilder(): StringBuilder{
        return text
    }

    fun setBuilder(sb: StringBuilder): Unit{
        text = sb
        height = sb.length
    }

    operator fun get(i: Int): Char{
        return text[i]
    }

    operator fun set(i: Int, c: Char): Unit{
        text[i] = c
    }
}