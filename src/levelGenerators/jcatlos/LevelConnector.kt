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
    /*fun findLowestExit(level: Level): Coords?{
        for (y in 0 until level.state.maxHeight){
            for(x in 0 until level.state.maxLength){
                if(level.levelChunk.content[x][y] == 'f'){
                    return Coords(x,y)
                }
            }
        }
        return null
    }*/

    /**
     * Constructs the largest possible rectangular space available for a room to be connected to an exit anchor
     *
     * @param level on which we want to perform the calculation
     * @param entryCoords the coordinates of the block where the start anchor would be connected
     * @return a [RoomSpace] where a [Room] can be placed. If the [entryCoords] are out of bounds, returns null
     */

    fun calculateFreeRoomSpace(level: Level, entryCoords: Coords): RoomSpace?{
        if(entryCoords.x >= level.config.maxLength) return null

        var x = entryCoords.x
        var dl: Coords = Coords(-1,-1)
        var width = 0
        var downIndex =  entryCoords.y
        var upIndex =  entryCoords.y

        // Calculate free space under entry point
        for(y in entryCoords.y until level.config.maxHeight){
            downIndex = y
            if(level.levelChunk.content[x][y] != '.') break
        }
        if(level.levelChunk.content[x][downIndex] == '.'){
            downIndex++
        }
        // Calculate free space above entry point
        for(y in entryCoords.y downTo 0){
            if(level.levelChunk.content[x][y] != '.') break
            else upIndex = y
        }

        // Calculate how long is the free space (only '.' is allowed)
        var xIt = x
        while(xIt < level.config.maxLength){
            var col = level.levelChunk.content[xIt]

            var substr = String(chars = col.subList(upIndex, downIndex).toCharArray())
            //var substr = col.getBuilder().substring(entryCoords.y - downHeight, entryCoords.y + upHeight + 1)
            if(substr != ".".repeat(substr.length)) break
            width ++
            xIt++
        }

        var out = RoomSpace(
                width,
                downIndex - upIndex,
                Coords(x, upIndex),
                Coords(0, entryCoords.y - upIndex),
                ArrayList()
        )

        return out
    }

    /**
     * Constructs the smallest possible rectangular [Space] containing all occurrences of the provided character
     *
     * @param chunk a room [Chunk] on which we want to perform the calculation
     * @param char the character to be looked for
     */

    fun findTemplateSpace(chunk: Chunk, char: Char): Space{
        var upperBound: Int = Int.MAX_VALUE
        var lowerBound: Int = -1
        var leftBound: Int = Int.MAX_VALUE
        var rightBound: Int = -1

        for(x in 0 until chunk.width){
            for(y in 0 until chunk.height){
                if(chunk.content[x][y] == char){
                    if (x > rightBound) {
                        rightBound = x
                    }
                    if (x < leftBound) {
                        leftBound = x
                    }
                    if (y < upperBound){
                        upperBound = y
                    }
                    if (y > lowerBound){
                        lowerBound = y
                    }
                }
            }
        }

        lowerBound++
        rightBound++

        return Space(rightBound - leftBound,
                lowerBound - upperBound,
                Coords(leftBound, upperBound))
    }
}


