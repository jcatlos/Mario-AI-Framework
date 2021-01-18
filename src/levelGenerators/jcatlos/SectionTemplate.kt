package levelGenerators.jcatlos

/**
 * Class used to store information about a section from a prototype file
 *
 * - The section itself is constructed at generation of the Section (generate() function)
 * - Prevents multiple file reads during generation
 * - Allows querying templates on properties (they are all stored in [SharedData])
 *
 * @param sectionRoomSpace of the [SectionTemplate] - All addresses inside [Section] are relative to the [Section]
 * @param roomSpaces a Map of the [RoomSpace]s to be filled by [Room]s in the generated [Section]
 * @param sectionTags tags of the section - all [Rooms in the generated [Section] must satisfy these
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
            println("Emplacing room into section- DL corner x: ${rs.DL_Corner().x} y: ${rs.DL_Corner().y}")
            emplaceRoom(out, room, rs)
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

    /**
     * Emplaces a [Room] into a section string
     *
     * @param out the section string a [Room] is placed into
     * @param room to be placed
     * @param dl_corner down-left corner of the emplaced [Room]
     * 
     */

    fun emplaceRoom(out: StringBuilder, room: Room, rs: RoomSpace){
        var dl_corner = rs.DL_Corner();
        dl_corner.y = rs.startAnchor.y
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
                        //println("inserting $char at $index")
                        out[index] = char
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