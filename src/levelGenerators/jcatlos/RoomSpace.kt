package levelGenerators.jcatlos

/**
 * A descendant of class [Space]
 *
 * Specifies a [Space] which is to be occupied by a room/section
 *
 * @param width the width of the space
 * @param height the height of the space
 * @param DL_Corner the down-left corner of the room
 * @param startAnchor the coordinates of the entering point into the room
 * @param finishAnchors a list of the exit points from the room
 */

class RoomSpace(
        width: Int = 0,
        height: Int = 0,
        UL_Corner: Coords,
        var startAnchor: Coords,
        var finishAnchors: ArrayList<Coords>
): Space(width, height, UL_Corner)
{
    private var anchorDiffs: ArrayList<Coords> = ArrayList()

    init{
        for(finishAnchor in finishAnchors){
            anchorDiffs.add(Coords(finishAnchor.x - startAnchor.x, finishAnchor.y - startAnchor.y))
        }
    }

    /**
     * Checks whether the provided anchors align with the anchors of the [RoomSpace]
     *
     * The provided anchors must  be a superset of the [RoomSpace] anchors
     *
     * @param start coordinates of the entry point to the room
     * @param finish list of the coordinates of exit points from the room
     * @return Boolean value whether the check is passed
     */

    fun anchorsFit(start: Coords, finish: ArrayList<Coords>): Boolean{
        // If starts are not alignable return false
        if(start.x != startAnchor.x || start.y > startAnchor.y) return false
        var inputAnchorDiffs: ArrayList<Coords> = ArrayList()
        for(f in finish){
            //println(Coords(f.x - start.x, f.y - start.y))
            inputAnchorDiffs.add(Coords(f.x - start.x, f.y - start.y))
        }

        // Every exit from roomspace must be in the template's exit
        for(diff in anchorDiffs){
            if(diff !in inputAnchorDiffs) return false
        }

        return true
    }

    /**
     * Missing annotation
      */
    fun ULByEntryPoint(entryPoint: Coords): Coords{
        var outPoint = entryPoint
        outPoint.x -= startAnchor.x
        outPoint.y -= startAnchor.y
        //println("outpoint is $outPoint")
        return outPoint
    }

    fun moveTo(ul: Coords){
        UL_Corner = ul
    }

}