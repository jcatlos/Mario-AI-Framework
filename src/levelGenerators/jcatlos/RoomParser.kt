package levelGenerators.jcatlos

import java.io.BufferedReader
import java.io.File
import kotlin.random.Random

/*
    RoomParser's job is to read a file with room design and return a room template instance
    with the data from the file. If it encounters a macro, it parses it and adds it
    to the template
 */

object RoomParser{

    private fun getNextToken(reader: BufferedReader): String{
        var tokenBuilder: StringBuilder = StringBuilder()
        var character: Int = reader.read()
        while(character >=0 && character != '}'.toInt() && character != ';'.toInt()){
            //println(character.toChar())
            tokenBuilder.append(character.toChar())
            character = reader.read()
        }
        //println(tokenBuilder.toString())
        return tokenBuilder.toString()
    }


    fun fileToTemplate(levelFile: File): RoomTemplate{
        println("      Parsing initialized")

        var levelBuilder: StringBuilder = StringBuilder()
        var levelReader: BufferedReader = BufferedReader(levelFile.reader())

        var diff = levelReader.readLine().toInt()
        var tags: ArrayList<String> = ArrayList(levelReader.readLine().split(',').map {tag -> tag.trim()})

        var start = Coords(-1, -1)
        var finish: ArrayList<Coords> = ArrayList()

        var macroMap: MutableMap<Coords, Macro> = mutableMapOf()

        var width = 0
        var firstLine = true

        var count = 0
        var character = levelReader.read()

        while(character >= 0){
            if(character == 13 || character == 10 || character == '\n'.toInt()){
                firstLine = false
                count--
            }
            if(firstLine){
                width++
            }

            when (character) {
                '{'.toInt() -> {
                    //println("macro started")
                    //levelBuilder.append('-')
                    var macro = Macro()
                    var token = getNextToken(levelReader)
                    while(token != ""){
                        //println(token)
                        var pair = token.split('=')
                        macro.addPair(MacroPair(pair[0], pair[1].toInt()))
                        token = getNextToken(levelReader)
                    }
                    macroMap[Coords(count%width, count/width)] = macro
                    // Trying to fix buggy generation
                        //levelBuilder.append("-".repeat(macro.length))
                    //println("macro finished")
                }
                'm'.toInt() -> {
                    start = Coords(count%width, count/width)
                    //println("start $start")
                    //println("count = $count width = $width")
                    levelBuilder.append('-')
                }
                'f'.toInt() -> {
                    finish.add(Coords(count%width, count/width))
                    levelBuilder.append('-')
                }
                else -> {
                    levelBuilder.append(character.toChar())
                }
            }
            character = levelReader.read()
            count++
        }

        // Inverting height (when reading Coords(0,0) is top-left corner and we need it to be bottom-left corner)
        var height = levelBuilder.lines().size
        if(start != Coords(-1,-1)){
            start.y = height - 1 - start.y
        }
        // Inverting finish
        for(f in finish){
            f.y = height - 1 -f.y
            //println("finish modified to $f")
        }

        return RoomTemplate(levelBuilder, width, diff, tags, macroMap.toMutableMap(), start, finish)
    }

}