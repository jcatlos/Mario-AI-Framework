package levelGenerators.jcatlos

import java.io.BufferedReader
import java.io.File
import kotlin.random.Random

/*
    RoomParser's job is to read a file with room design and return a StringBuilder()
    with the contents of the room. If it encounters a macro, it parses it and adds its
    output instead
 */

object RoomParser{

    private fun parseMacro(reader: BufferedReader): String{

        fun getNextToken(): String{
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

        fun addTokenToOptions(tokenPair: List<String>, options: MutableMap<String, Int>): Unit{
            require(tokenPair.size == 2 &&
                    tokenPair[1].toIntOrNull() != null) {"Invalid Macro"}
            options[tokenPair[0]] = tokenPair[1].toInt()
            //println(tokenPair[1])
        }

        var options: MutableMap<String, Int> = mutableMapOf()

        var token: String = getNextToken()
        while(token != ""){
            var tokenPair = token.split('=')
            addTokenToOptions(tokenPair, options)
            token = getNextToken()
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
}