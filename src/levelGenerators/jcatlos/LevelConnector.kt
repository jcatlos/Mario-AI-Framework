package levelGenerators.jcatlos

/**
    Object containing all global functions used to connect [Section]s or [Room]s
 */

object LevelConnector {

    /**
     * Finds the [Coords] of the lowest exit anchor of the level
     *
     * @param level the [Level] on which we want to find the lowest exit
     * @return [Coords] opf the lowest exit anchor of the [Level]. If none found, returns null
     */
    fun findLowestExit(level: Level): Coords?{
        // If none found, returns null
        for (y in 0 until level.state.maxHeight){
            for(x in 0 until level.state.maxLength){
                if(level.levelColumns[x][y] == 'f'){
                    return Coords(x,y)
                }
            }
        }
        return null
    }

    /**
     * Constructs the largest possible rectangular space available for a room to be connected to an exit anchor
     *
     * @param level on which we want to perform the calculation
     * @param entryCoords the coordinates of the block where the start anchor would be connected
     * @return a [RoomSpace] where a [Room] can be placed. If the [entryCoords] are out of bounds, returns null
     */

    fun calculateFreeRoomSpace(level: Level, entryCoords: Coords): RoomSpace?{
        if(entryCoords.x >= level.state.maxLength) return null

        var x = entryCoords.x+1
        var dl: Coords = Coords(-1,-1)
        var width = 0
        var downHeight = -1
        var upHeight = 0
        // Calculate free space under entry point
        for(y in entryCoords.y downTo 0){
            if(level.levelColumns[x][y] != '.') break
            else downHeight ++
        }
        // Calculate free space above entry point
        for(y in entryCoords.y+1 until level.state.maxHeight){
            if(level.levelColumns[x][y] != '.') break
            else upHeight ++
        }

        // Calculate how long is the free space (only '.' is allowed)
        var xIt = x
        while(xIt < level.state.maxLength){
            var col = level.levelColumns[xIt]

            var substr = String(chars = col.copyOfRange(entryCoords.y - downHeight, entryCoords.y + upHeight + 1).toCharArray())
            //var substr = col.getBuilder().substring(entryCoords.y - downHeight, entryCoords.y + upHeight + 1)
            if(substr != ".".repeat(substr.length)) break
            width ++
            xIt++
        }
        println("width: $width")
        //println("height: ${upHeight + downHeight + 1}")
        println("downheight: $downHeight")

        return RoomSpace(width,
                upHeight + downHeight + 1,
                Coords(x, entryCoords.y - downHeight),
                Coords(0, entryCoords.y),
                ArrayList()
        )

    }

    /**
     * Constructs the smallest possible rectangular [Space] containing all occurences of the provided character
     *
     * @param room a room [StringBuilder] on which we want to perform the calculation
     * @param char the character to be looked for
     */

    fun findTemplateSpace(room: StringBuilder, char: Char): Space{
        var upperBound: Int = -1
        var lowerBound: Int = Int.MAX_VALUE
        var leftBound: Int = -1
        var rightBound: Int = Int.MAX_VALUE

        var x: Int = 0
        var y: Int = room.lines().size - 1

        for(line in room.lines()){
            for(c in line){
                if (c == char) {
                    if (x < rightBound) {
                        //println("found rb: $x")
                        rightBound = x
                    }
                    if (x > leftBound) {
                        leftBound = x
                    }
                    if (y > upperBound){
                        upperBound = y
                    }
                    if (y < lowerBound){
                        //println("found lb: $y")
                        lowerBound = y
                    }
                }
                x++
            }
            y--
            x = 0
        }

        return Space(leftBound - rightBound + 1,
                upperBound - lowerBound + 1,
                Coords(rightBound, lowerBound))
    }
}