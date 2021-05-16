package levelGenerators.jcatlos

import java.io.BufferedReader
import java.io.File

/**
 * RoomParser's job is to read a section with section design and return a section template instance with the data from the file
 *
 * More in documentation
 */

object SectionParser {
    /**
     * Parses the provided file to a [SectionTemplate]
     *
     * @param sectionFile the file to be parsed
     */
    fun fileToSectionTemplate(sectionReader: BufferedReader): SectionTemplate{
        var sectionChunk = Chunk()
        var roomSpaces: MutableMap<Char, RoomSpace> = mutableMapOf()
        var sectionTags: MutableMap<Char, ArrayList<String>> = mutableMapOf()

        var characters: MutableSet<Char> = mutableSetOf()

        var parsingHead = true;

        for(line in sectionReader.lines()){
            when {
                // Ending of the header part of the file
                line.trim() == "---".trim() -> parsingHead = false

                // Still parsing the header part of the part
                parsingHead -> {
                    var tokens = line.split(':').map {token -> token.trim()}
                    var char = tokens[0][0]
                    characters.add(char)
                    var tags = tokens[1].split(',').map {tag -> tag.trim()}
                    sectionTags[char] = ArrayList(tags)
                }

                // Parsing the section body
                else -> {
                    // Escaping endline chars and adding an endline manually prevents naughty CRLF/LF situations
                    for(char in line){
                        if(char == '\n' || char == '\r') continue;
                        sectionChunk.append(char)
                    }
                    sectionChunk.append('\n')
                }
            }
        }

        //Find starting and finishing points of the whole section
        var sectionStart = Coords(-1, -1)
        var foundStarts = sectionChunk.findChar('M')
        if(foundStarts.isNotEmpty()) sectionStart = foundStarts.first()

        var sectionFinish = sectionChunk.findChar('F')


        // Handle each room from the section
        for(char in characters){
            // Find its space
            var space = LevelConnector.findTemplateSpace(sectionChunk, char)

            // Remove definition characters
            sectionChunk.maskChar(char);

            // Find start and finish
                // Dont forget to remove m and f of each room
            var start = Coords(-1, -1)
            var finish: ArrayList<Coords> = ArrayList()
            for(x in 0 until space.width){
                for(y in 0 until space.height){
                    var currentChar: Char = sectionChunk.content[x+space.UL_Corner().x][y+space.UL_Corner().y]
                    if(currentChar == 'm' || currentChar == 'M'){
                        start = Coords(x, y)
                        sectionChunk.content[x+space.UL_Corner().x][y+space.UL_Corner().y] = '.'
                    }
                    else if(currentChar == 'f' || currentChar == 'F'){
                        finish.add(Coords(x, y))
                        sectionChunk.content[x+space.UL_Corner().x][y+space.UL_Corner().y] = '.'
                    }
                }
            }

            // Add it to the roomSpaces of the whole section
            roomSpaces[char] = RoomSpace(space.width, space.height, space.UL_Corner(), start, finish)
        }

        // Remove the starting and finishing points of the whole section
            // Might not have been done before (Section start is not a start of any room)

        sectionChunk.content[sectionStart.x][sectionStart.y] = '.'
        for(finish in sectionFinish){
            sectionChunk.content[finish.x][finish.y] = '.'
        }


        return SectionTemplate(
                sectionChunk,
                sectionStart,
                sectionFinish,
                roomSpaces,
                sectionTags)
    }
}