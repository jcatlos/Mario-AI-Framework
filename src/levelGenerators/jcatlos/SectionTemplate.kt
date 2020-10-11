package levelGenerators.jcatlos

/*
    Class used to store information about a section from a prototype file
        - The section itself is constructed at generation of the Section (generate() function)
        - Prevents multiple file reads during generation
        - Allows querying templates on properties (they are all stored in SharedData)
 */

class SectionTemplate(var sectionRoomSpace: RoomSpace,
                      var roomSpaces: MutableMap<Char, RoomSpace>,
                      var sectionTags: MutableMap<Char, ArrayList<String>>)
{

    fun generate(dl_corner: Coords): Section{
        var out: StringBuilder = StringBuilder((".".repeat(sectionRoomSpace.width) + '\n').repeat(sectionRoomSpace.height))
        for(roomSpace in roomSpaces){
            var rs = roomSpace.value
            var room = SharedData.roomGenerator.generateToFitRoomspace(rs, sectionTags[roomSpace.key]!!)
            emplaceRoom(out, room, rs.DL_Corner())
        }

        //adding finish-es to the output
        for(f in sectionRoomSpace.finishAnchors){
            out[f.x + (sectionRoomSpace.height - f.y) * (sectionRoomSpace.width +1)] = 'f'
        }

        println("generated section:")
        println(out)

        return Section(
                out,
                RoomSpace(
                        sectionRoomSpace.width,
                        sectionRoomSpace.height,
                        dl_corner,
                        sectionRoomSpace.startAnchor,
                        sectionRoomSpace.finishAnchors
                )
        )
    }

    fun emplaceRoom(out: StringBuilder, room: Room, dl_corner: Coords){
        var xCounter = 0
        var yCounter = 0
        for(line in room.room.lines().reversed()){
            for(char in line){
                when (char) {
                    '.' -> {}
                    else -> {
                        //println(out.toString())
                        //println("x: $xCounter")
                        var index = xCounter + dl_corner.x + (sectionRoomSpace.height - yCounter - dl_corner.y) * (sectionRoomSpace.width+1)
                        if(out[index] == '\n') println("rewriting end line at $index")
                        //println("inserting $char at ${xCounter + dl_corner.x + (yCounter + dl_corner.y) * (sectionRoomSpace.width+1)}")
                        out[xCounter + dl_corner.x + (sectionRoomSpace.height - yCounter - dl_corner.y) * (sectionRoomSpace.width+1)] = char
                        //out.append(char)
                    }
                }
                xCounter++
            }
            xCounter = 0
            yCounter++
            //out.append('\n')
        }
    }


}