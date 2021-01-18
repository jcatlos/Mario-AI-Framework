package levelGenerators.jcatlos

/**
 *  Object generating rooms fitting inside a provided [RoomSpace]s (with/without tags)
 *  - Chooses a random [RoomTemplate] from [SharedData] (which satisfies criteria - size/tags)
 */

object RandomRoomGenerator: RoomGenerator{

    /**
     * Chooses a random [RoomTemplate] that satisfies all the required conditions and generates a [Room] from it
     *
     *  @param roomSpace the [RoomSpace] to be used by the output [Room]
     *  @param sectionTags the tags of a [Section] to be satisfied
     *  @return [Room] that satisfies all the conditions
     */

    override fun generateToFitRoomspace(roomSpace: RoomSpace, sectionTags: ArrayList<String>): Room{
        var candidates: ArrayList<RoomTemplate> = ArrayList()
        for(template in SharedData.RoomTemplates){
            if(template.width <= roomSpace.width){
                if(template.height <= roomSpace.height){
                    if(roomSpace.anchorsFit(template.start, template.finish)){
                        if(checkTags(template.tags, sectionTags)){
                            candidates.add(template)
                        }
                    }
                }
            }
        }
        return candidates.random().generate()
    }

    /**
     * checks, whether the tags required/forbidden by the [Section] are satisfied
     *
     * @param roomTags tags of the checked [Room]
     * @param sectionTags tags of the [Section]
     */

    private fun checkTags(roomTags: ArrayList<String>, sectionTags: ArrayList<String>): Boolean{
        var satisfy = true
        for(tag in sectionTags){
            if(tag.isEmpty()) continue
            else if(tag[0] == '!'){
                if(tag.substring(1) in roomTags) return false
            }
            else if(tag !in roomTags) return false
        }
        return true
    }
}