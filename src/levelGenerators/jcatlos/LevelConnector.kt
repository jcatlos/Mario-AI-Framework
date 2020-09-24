package levelGenerators.jcatlos

open class Space(
        var width: Int = 0,
        var height: Int = 0,
        protected var DL_Corner: Coords
)
{
    fun DL_Corner(): Coords{
        return DL_Corner
    }

    fun DR_Corner(): Coords{
        return Coords(DL_Corner.x + width, DL_Corner.y)
    }

    fun UL_Corner(): Coords{
        return Coords(DL_Corner.x, DL_Corner.y + height)
    }

    fun UR_Corner(): Coords{
        return Coords(DL_Corner.x + width, DL_Corner.y + height)
    }
}

class RoomSpace(
        width: Int = 0,
        height: Int = 0,
        DL_Corner: Coords,
        var startAnchor: Coords,
        var finishAnchors: ArrayList<Coords>
    ): Space(width, height, DL_Corner)
{
    var anchorDiffs: ArrayList<Coords> = ArrayList()
    init{
        for(finishAnchor in finishAnchors){
            anchorDiffs.add(Coords(finishAnchor.x - startAnchor.x, finishAnchor.y - startAnchor.y))
        }
    }



    fun anchorsFit(start: Coords, finish: ArrayList<Coords>): Boolean{
        // If starts are not alignable return false
        //println("space anchor = ${this.startAnchor} template anchor = ${start}")
        if(start.x != startAnchor.x && start.y > startAnchor.y) return false
        var inputAnchorDiffs: ArrayList<Coords> = ArrayList()
        for(f in finish){
            inputAnchorDiffs.add(Coords(f.x - start.x, f.y - start.y))
            //println("diffs: ")
            //println(Coords(f.x - start.x, f.y - start.y))
        }
        // Every exit from roomspace must be in the template's exit
        //println("anchor diffs:")
        for(diff in anchorDiffs){
            //println(diff)
            if(diff !in inputAnchorDiffs) return false
        }

        return true
    }

}

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
        var substr = col.getBuilder().substring(entryCoords.y - downHeight, entryCoords.y + upHeight + 1)
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

object LevelConnector {

}