package levelGenerators.jcatlos

/**
 * Class used to store information about a section from a prototype file
 *
 * - The section itself is constructed at generation of the Section (generate() function)
 * - Prevents multiple file reads during generation
 * - Allows querying templates on properties (they are all stored in [SharedData])
 *
 * @param sectionChunk a [Chunk] containing the section
 * @param sectionRoomSpace of the [SectionTemplate] - All addresses inside [Section] are relative to the [Section]
 * @param roomSpaces a Map of the [RoomSpace]s to be filled by [Room]s in the generated [Section]
 * @param sectionTags tags of the section - all [Rooms in the generated [Section] must satisfy these
 */

class SectionTemplate(var sectionChunk: Chunk,
                      var startAnchor: Coords,
                      var finishAnchors: ArrayList<Coords>,
                      //var sectionRoomSpace: RoomSpace,
                      var roomSpaces: MutableMap<Char, RoomSpace>,
                      var sectionTags: MutableMap<Char, ArrayList<String>>)
{

    fun generate(input_rs: RoomSpace): Section{
        //println("sectionchunk = \n${sectionChunk.getAsStringBuilder().toString()}")
        for(roomSpace in roomSpaces){
            var rs = roomSpace.value
            //println("looking for room for ${roomSpace.key} with anchor at ${rs.startAnchor}")
            var room = SharedData.roomGenerator.generateToFitRoomspace(rs, sectionTags[roomSpace.key]!!)
            println("emplacing room \n${room.room.getAsStringBuilder()}")
            //println("Emplacing room into section- DL corner x: ${rs.DL_Corner().x} y: ${rs.DL_Corner().y-1}")
            var ul = rs.UL_Corner()
            ul.y += rs.startAnchor.y - room.start.y
            sectionChunk.emplaceChunk(room.room, rs.UL_Corner())
            println("emplaced \n${sectionChunk.getAsStringBuilder()}")
        }

        //adding finish-es to the output
        /*for(f in sectionRoomSpace.finishAnchors){
            sectionChunk.content[f.x][f.y] = 'f'
        }*/

        println("generated section:")
        println(sectionChunk.getAsStringBuilder())

        var ul = input_rs.startAnchor
        ul.x += input_rs.UL_Corner().x
        ul.y += input_rs.UL_Corner().y
        ul.x -= startAnchor.x
        ul.y -= startAnchor.y

        return Section(
                sectionChunk,
                RoomSpace(
                        sectionChunk.width,
                        sectionChunk.height,
                        ul,
                        startAnchor,
                        finishAnchors
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

    fun emplaceRoom(room: Room, rs: RoomSpace){
        var ul_corner = rs.UL_Corner();
        //dl_corner.y = rs.UL_Corner().y
        //var xCounter = 0
        //var yCounter = 0

        for(xCounter in 0 until room.room.width){
            for(yCounter in 0 until room.room.height){
                when(room.room.content[xCounter][yCounter]){
                    '.' -> {}
                    else -> {
                        sectionChunk.content[ul_corner.x + xCounter][ul_corner.y + yCounter] = room.room.content[xCounter][yCounter]
                    }
                }
            }
        }

        /*for(line in room.room.lines().reversed()){
            for(char in line){
                println("line")
                when (char) {
                    '.' -> {}
                    '\n' -> {}
                    else -> {
                        //println(out.toString())
                        //println("x: $xCounter")
                        var index = xCounter + dl_corner.x + (sectionRoomSpace.height - yCounter - dl_corner.y) * (sectionRoomSpace.width)
                        println("index = $index")
                        if(sectionString[index] == '\n') println("rewriting end line at $index")
                        //println("inserting $char at $index")
                        else sectionString[index] = char
                    }
                }
                xCounter++
            }
            xCounter = 0
            yCounter++
            //out.append('\n')
        }*/
        println("emplaced \n$sectionChunk");
    }


}