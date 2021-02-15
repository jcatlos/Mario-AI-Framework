package levelGenerators.jcatlos

import java.io.BufferedReader
import java.io.File

object MacroParser{
    private fun parseOption(macroReader: BufferedReader, height: Int) : MacroPair?{
        var line = macroReader.readLine()
        if(line == null) return null
        var prob = line.toFloat()
        var macroSB = StringBuilder()
        for(i in 0 until height){
            macroSB.append(macroReader.readLine())
            macroSB.append('\n')
        }
        return MacroPair(Chunk(macroSB), prob)
    }

    fun parseFile(macroFile: File): Macro{
        var macroReader = macroFile.bufferedReader()

        // Parse first line
        var firstLine = macroReader.readLine().split(" ")
        var name = firstLine[0]
        var width: Int = firstLine[1].toInt()
        var height: Int = firstLine[2].toInt()

        var out: Macro = Macro(name, width, height, arrayListOf())

        var macroPair = parseOption(macroReader, height)
        while(macroPair != null){
            out.addPair(macroPair)
            macroPair = parseOption(macroReader, height)
        }
        return out
    }
}