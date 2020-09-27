package levelGenerators.jcatlos


object RandomRoomGenerator: RoomGenerator{

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