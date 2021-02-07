package levelGenerators.jcatlos

import java.io.BufferedReader
import java.io.File

/**
 * [RoomParser]'s job is to read a file with room design and return a room template instance
    with the data from the file. If it encounters a macro, it parses it and adds it
    to the template
 */

object RoomParser{

    private fun getNextToken(reader: BufferedReader): String{
        var tokenBuilder: StringBuilder = StringBuilder()
        var character: Int = reader.read()
        while(character >=0 && character != '}'.toInt() && character != ';'.toInt()){
            tokenBuilder.append(character.toChar())
            character = reader.read()
        }
        return tokenBuilder.toString()
    }

    /**
     * Converts a [File] containing a valid room template to an instance of [RoomTemplate]
     *
     * The rules for creating a valid room template file are specified in the README
     *
     * @param levelFile [File] to be converted
     * @return [RoomTemplate] corresponding to the provided [levelFile]
     */
    fun fileToTemplate(levelFile: File): RoomTemplate{
        println("      Parsing initialized")

        var roomChunk = Chunk()
        var levelReader: BufferedReader = BufferedReader(levelFile.reader())

        var tags: ArrayList<String> = ArrayList(levelReader.readLine().split(',').map {tag -> tag.trim()})

        var start = Coords(-1, -1)
        var finish: ArrayList<Coords> = ArrayList()

        var macroMap: MutableMap<Coords, Macro> = mutableMapOf()

        var count = 0
        var character = levelReader.read()

        while(character >= 0){
            when (character) {
                // Macro
                '{'.toInt() -> {
                    var macro = Macro()
                    var token = getNextToken(levelReader)
                    while(token != ""){
                        var pair = token.split('=')
                        macro.addPair(MacroPair(pair[0], pair[1].toInt()))
                        token = getNextToken(levelReader)
                    }
                    macroMap[roomChunk.currentPos()] = macro
                    // Make space for the macro to be inserted during generation
                    for(m in 0 until macro.length){
                        roomChunk.append('-')
                    }
                }
                // Start point
                'm'.toInt() -> {
                    start = roomChunk.currentPos()
                    roomChunk.append('-')
                }
                // Finish point
                'f'.toInt() -> {
                    finish.add(roomChunk.currentPos())
                    roomChunk.append('-')
                }
                // Default
                else -> {
                    roomChunk.append(character.toChar())
                }
            }
            print(character.toChar())
            character = levelReader.read()
            count++
        }

        // To make sure that the last row is finished
            // Doesn't affect anything if the last row of the file is empty
        roomChunk.append('\n')


        return RoomTemplate(roomChunk, tags, macroMap.toMutableMap(), start, finish)
    }

}