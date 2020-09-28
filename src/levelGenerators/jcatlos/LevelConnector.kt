package levelGenerators.jcatlos

object LevelConnector {
    fun findLowestExit(level: Level): Coords?{
        // If none found, returns null
        var out: ArrayList<Coords> = ArrayList()
        for (y in 0 until level.state.maxHeight){
            for(x in 0 until level.state.maxLength){
                if(level.levelColumns[x][y] == 'f'){
                    return Coords(x,y)
                }
            }
        }
        return null
    }

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
        println("height: ${upHeight + downHeight + 1}")

        return RoomSpace(width,
                upHeight + downHeight + 1,
                Coords(x, entryCoords.y - downHeight),
                Coords(0, entryCoords.y),
                ArrayList()
        )

    }
}