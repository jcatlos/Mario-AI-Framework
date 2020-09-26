package levelGenerators.jcatlos

import java.io.File
import kotlin.random.Random

object RandomRoomGenerator: RoomGenerator{
    private val BONUS_PROB = 0.10
    private val DIVIDE_PROB = 0.20


    override fun generateToFitRoomspace(roomSpace: RoomSpace, sectionTags: ArrayList<String>): Room{
        var candidates: ArrayList<RoomTemplate> = ArrayList()
        //println("Roomspace start anchor: ${roomSpace.startAnchor}")
        //println("Roomspace width: ${roomSpace.width}")
        //println("Roomspace height: ${roomSpace.height}")
        for(template in SharedData.RoomTemplates){
            //println(template.width)
            if(template.width <= roomSpace.width){
                //println("passed width")
                if(template.height <= roomSpace.height){
                    //println("passed height")
                    if(roomSpace.anchorsFit(template.start, template.finish)){
                        println("passed anchors")
                        if(checkTags(template.tags, sectionTags)){
                            println("passed tags")
                            candidates.add(template)
                        }
                    }
                }
            }
        }
        /*for(c in candidates){
            println("candidate:")
            println(c.room)
        }*/
        return candidates.random().generate()
    }

    fun generateStartRoom(): Room{
        var candidates: ArrayList<RoomTemplate> = ArrayList()
        for(template in SharedData.RoomTemplates){
            if("start" in template.tags){
                candidates.add(template)
            }
        }
        var start = candidates.random()
        var generated = start.generate()
        for(f in start.finish){
            generated.room[f.x + (start.height - f.y - 1) * (start.width + 1)] = 'f'
        }
        return generated
    }

    fun checkTags(roomTags: ArrayList<String>, sectionTags: ArrayList<String>): Boolean{
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