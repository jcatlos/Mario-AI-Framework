package levelGenerators.jcatlos

import engine.sprites.Mario
import java.io.BufferedReader
import java.io.File
import kotlin.random.Random

/*
    RoomParser's job is to read a file with room design and return a StringBuilder()
    with the contents of the room. If it encounters a macro, it parses it and adds its
    output instead
 */

object RoomParser{

    fun getNextToken(reader: BufferedReader): String{
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

    private fun parseMacro(reader: BufferedReader): String{

        fun addTokenToOptions(tokenPair: List<String>, options: MutableMap<String, Int>): Unit{
            require(tokenPair.size == 2 &&
                    tokenPair[1].toIntOrNull() != null) {"Invalid Macro"}
            options[tokenPair[0]] = tokenPair[1].toInt()
            //println(tokenPair[1])
        }

        var options: MutableMap<String, Int> = mutableMapOf()

        var token: String = getNextToken(reader)
        while(token != ""){
            var tokenPair = token.split('=')
            addTokenToOptions(tokenPair, options)
            token = getNextToken(reader)
        }

        val prob = Random.nextFloat() * 100
        var currentProb = 0
        for(entry in options.entries){
            currentProb += entry.value
            if(prob < currentProb) return entry.key
            if(currentProb >= 100) return "-"
        }
        return "-"
    }

    fun parseRoom(levelFile: File): StringBuilder{
        var levelBuilder: StringBuilder = StringBuilder()
        var levelReader: BufferedReader = BufferedReader(levelFile.reader())

        var character = levelReader.read()
        while(character >= 0){
            if(character == '{'.toInt()){
                levelBuilder.append(parseMacro(levelReader))
            }
            else{
                levelBuilder.append(character.toChar())
            }
            character = levelReader.read()
        }

        return levelBuilder
    }

    fun fileToTemplate(levelFile: File): RoomTemplate{
        println("      Parsing initialized")

        var levelBuilder: StringBuilder = StringBuilder()
        var levelReader: BufferedReader = BufferedReader(levelFile.reader())

        var diff = levelReader.readLine().toInt()
        var tags: ArrayList<String> = ArrayList(levelReader.readLine().split(',').map {tag -> tag.trim()})

        var start: Coords = Coords(-1, -1)
        var finish: ArrayList<Coords> = ArrayList()

        var macroMap: MutableMap<Coords, Macro> = mutableMapOf()

        var width = 0
        var firstLine = true

        var count = 0
        var character = levelReader.read()

        //var sb = StringBuilder()

        while(character >= 0){
            //print(character.toChar())
            //count++
            if(character == 13 || character == 10 || character == '\n'.toInt()){
                firstLine = false
                count--
            }
            if(firstLine){
                //sb.append(character.toChar())
                //sb.append(width.toString())
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

        /*println("first line:")
        for(c in sb){
            print(c.toInt())
        }
        println()*/

        // Inverting height (when reading Coords(0,0) is top-left corner and we need it to be bottom-left corner)
        var height = levelBuilder.lines().size
        if(start != Coords(-1,-1)){
            start.y = height - 1 - start.y
        }
        //Ak by nefungovalo to dole finish.map { f -> Coords(f.x, height - 1 -f.y) }
        for(f in finish){
            f.y = height - 1 -f.y
            //println("finish modified to $f")
        }

        var type = fileToType[File(levelFile.parent)]
        if(type == null){
            type = ROOM_TYPE.EMPTY_H2
        }
        println()
        return RoomTemplate(levelBuilder, width, type, diff, tags, macroMap.toMutableMap(), start, finish)
    }

}