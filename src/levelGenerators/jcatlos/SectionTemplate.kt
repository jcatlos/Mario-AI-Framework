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

        var outChunk = Chunk(sectionChunk.getAsStringBuilder())

        println("sectionchunk = \n${outChunk.getAsStringBuilder().toString()}")
        println("Filling sction with ${roomSpaces.size} rooms")
        for(roomSpace in roomSpaces){
            var rs = roomSpace.value
            println("Roomspace ${roomSpace.key} ul = ${rs.UL_Corner()}")
            //println("looking for room for ${roomSpace.key} with anchor at ${rs.startAnchor}")
            var room = SharedData.roomGenerator.generateToFitRoomspace(rs, sectionTags[roomSpace.key]!!)
            println("UL corner = ${rs.UL_Corner()}\nspace start anchor = ${rs.startAnchor}\nroom start = ${room.start}")
            var ul = rs.UL_Corner()
            ul.y += rs.startAnchor.y - room.start.y
            println("After addition rs ul = ${rs.UL_Corner()}")
            println("emplacing room \n${room.room.getAsStringBuilder()}")
            //println("Roomspace ul = ${rs.UL_Corner()}")
            outChunk.emplaceChunk(room.room, ul)
            //println("emplaced \n${sectionChunk.getAsStringBuilder()}")
        }

        println("generated section:")
        println(outChunk.getAsStringBuilder())

        var section_ul = Coords(input_rs.startAnchor.x, input_rs.startAnchor.y)
        section_ul.x += input_rs.UL_Corner().x
        section_ul.y += input_rs.UL_Corner().y
        section_ul.x -= startAnchor.x
        section_ul.y -= startAnchor.y

        return Section(
                outChunk,
                RoomSpace(
                        sectionChunk.width,
                        sectionChunk.height,
                        section_ul,
                        startAnchor,
                        finishAnchors
                )
        )
    }

}