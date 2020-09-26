package levelGenerators.jcatlos

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
        if(start.x != startAnchor.x && start.y > startAnchor.y) return false
        var inputAnchorDiffs: ArrayList<Coords> = ArrayList()
        for(f in finish){
            inputAnchorDiffs.add(Coords(f.x - start.x, f.y - start.y))
        }

        // Every exit from roomspace must be in the template's exit
        for(diff in anchorDiffs){
            if(diff !in inputAnchorDiffs) return false
        }

        return true
    }

}