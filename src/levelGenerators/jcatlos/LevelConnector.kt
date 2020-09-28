package levelGenerators.jcatlos

object LevelConnector {
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