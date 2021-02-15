package levelGenerators.jcatlos

import kotlin.math.max

class Chunk()
{

    // Maybe add functions to clear, copy and create from string

    var content: ArrayList<ArrayList<Char>> = arrayListOf()

    var width: Int = 0
    var height: Int = 0
    private var xCounter: Int = 0
    private var yCounter: Int = 0


    constructor(sb: StringBuilder) : this(){
        for(char in sb){
            append(char)
        }
    }

    constructor(x: Int, y: Int) : this(){
        for(h in 0 until y){
            for(w in 0 until x){
                append('.')
            }
            append('\n')
        }
    }


    /**
     * Return current position of insert
     *
     * @return [Coords] of the point, where the next insertion will occur
     */
    fun currentPos(): Coords{
        return Coords(xCounter, yCounter)
    }

    /**
     * Returns character at given [Coords] in the [Chunk]
     *
     * @param coords [Coords] of the requested character
     * @return [Char] the character at this position
     */
    fun at(coords: Coords): Char{
        return content[coords.x][coords.y]
    }

    /**
     * Returns content as a [StringBuilder] with lines ending by '\n'
     *
     * @return [StringBuilder] Containing the whole Chunk
     */
    fun getAsStringBuilder(): StringBuilder{
        //println(" getAsStringBuilder() start w = $width h = $height")
        var out = StringBuilder()
        for(y in 0 until height){
            for(x in 0 until width){
                if(content.size > x && content[x].size > y) out.append(content[x][y])
                else out.append("B") // DEBUG
            }
            out.append('\n')
        }
        return out
    }

    /**
     * Returns content as a [StringBuilder] with lines ending by '\n'
     *
     * @return [StringBuilder] Containing the whole Chunk
     */
    fun getAsMarioAILevel(from_y: Int = 0, to_y: Int = height): StringBuilder{
        var out = StringBuilder()
        for(y in from_y until to_y){
            for(x in 0 until width){
                if(content.size > x && content[x].size > y){
                    if(content[x][y] == '.') out.append('-')
                    else out.append(content[x][y])
                }
                else out.append("B") // DEBUG
            }
            out.append('\n')
        }
        return out
    }

    /**
     * Finds all occurrences of the given character
     *
     * @param char [Char] to be found
     * @return [ArrayList<Coords>] [Coords] of all occurrences of the character
     */
    fun findChar(char: Char): ArrayList<Coords>{
        var found: ArrayList<Coords> = arrayListOf()
        for(x in 0 until width){
            for(y in 0 until height){
                if(content[x][y] == char){
                    println("found $char at ($x, $y)")
                    found.add(Coords(x, y))
                }
            }
        }
        return found
    }

    /**
     * Removes all occurrences of the provided [Char] in its content
     *
     * @param char [Char] to be removed
     */
    fun maskChar(char: Char) {
        for(x in 0 until width){
            for(y in 0 until height){
                if(content[x][y] == char) content[x][y] = '.'
            }
        }
    }

    /**
     * Missing annotation
     */
    fun getLastLine(): String{
        var out = StringBuilder()
        for(i in 0 until width){
            out.append(content[i][height-1])
        }
        return out.toString()
    }

    /**
     * Appends the given character to the content. Uses newlines as line separators.
     *
     * Works when you need to transfer content from string form.
     * Newline character ('\n' / '\r') create a new line to append to.
     * Uses internal counters to remember the current position - Use ONLY when transferring from a string, character by character.
     */
    fun append(c: Char): Unit{
        if(c == '\n' || c == '\r'){
            if(xCounter != 0){
                yCounter++
                xCounter = 0
                //println("newline")
            }
            //else println("duplicate newline prevented")
        }
        else{
            //println(c)
            if(xCounter == width){
                content.add(arrayListOf())
                for(i in 0 until yCounter){
                    content[xCounter].add('.')
                }
            }
            content[xCounter].add(c)
            xCounter++
        }

        // Check whether we should update width or height
        width = max(width, xCounter)
        height = max(height, yCounter)
    }


    /**
     * Emplaces a [Chunk] into this chunk
     *
     * If a part of the chunk doesn't fit, only the part that fits is emplaced.
     *
     * @param chunk [Chunk] to be placed
     * @param ul [Coords] upper-left corner of the insertion
     *
     */

    fun emplaceChunk(chunk: Chunk, ul: Coords){
        for(xCounter in 0 until chunk.width){
            for(yCounter in 0 until chunk.height){
                when(chunk.content[xCounter][yCounter]){
                    '.' -> {}
                    else -> {
                        var xIndex = ul.x + xCounter
                        var yIndex = ul.y + yCounter
                        if(xIndex in 0 until width && yIndex in 0 until height) {
                            content[ul.x + xCounter][ul.y + yCounter] = chunk.content[xCounter][yCounter]
                        }
                    }
                }
            }
        }
        //println("emplaced \n${this.getAsStringBuilder()}");
    }
}