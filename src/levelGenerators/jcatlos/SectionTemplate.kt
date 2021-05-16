package levelGenerators.jcatlos

import kotlin.random.Random

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

    fun generate(input_rs: RoomSpace, roomGenerator: RoomGenerator, inputTags: ArrayList<String>, challengeTags: ArrayList<String>): Section{

        println("genearting section with inputTags: $inputTags, challengeTags: $challengeTags")

        var outChunk = Chunk(sectionChunk.getAsStringBuilder())

        for(roomSpace in roomSpaces){
            var rs = roomSpace.value
            var tags:ArrayList<String> = arrayListOf()
            tags.addAll(inputTags)
            tags.addAll(sectionTags[roomSpace.key]!!)
            if("twist" in tags) tags.addAll(challengeTags)
            else if("challenge" in tags){
                // 30% chance to use a default challenge
                if(Random.nextFloat() < 0.7){
                    tags.remove("challenge")
                    tags.addAll(challengeTags)
                }
            }
            else{
                tags.add("default")
            }
            println("looking for room with tags ${tags.toString()} for rs: $rs")
            var room = roomGenerator.generateToFitRoomspace(rs, tags)
            var ul = rs.UL_Corner()
            ul.y += rs.startAnchor.y - room.start.y
            outChunk.emplaceChunk(room.room, ul)
            println("generated section:")
            println(outChunk.getAsStringBuilder())
        }

        println("generated section:")
        println(outChunk.getAsStringBuilder())

        var section_ul = Coords(
                input_rs.startAnchor.x + input_rs.UL_Corner().x - startAnchor.x,
                input_rs.startAnchor.y + input_rs.UL_Corner().y - startAnchor.y
        )
        /*section_ul.x += input_rs.UL_Corner().x
        section_ul.y += input_rs.UL_Corner().y
        section_ul.x -= startAnchor.x
        section_ul.y -= startAnchor.y*/

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