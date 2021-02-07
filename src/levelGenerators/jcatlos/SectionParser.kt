package levelGenerators.jcatlos

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
    fun fileToSectionTemplate(sectionFile: File): SectionTemplate{
        var sectionChunk = Chunk()
        var roomSpaces: MutableMap<Char, RoomSpace> = mutableMapOf()
        var sectionTags: MutableMap<Char, ArrayList<String>> = mutableMapOf()

        var characters: MutableSet<Char> = mutableSetOf()

        var parsingHead = true;

        for(line in sectionFile.readText().lines()){
            when {
                line.trim() == "---".trim() -> parsingHead = false
                parsingHead -> {
                    var tokens = line.split(':').map {token -> token.trim()}
                    var char = tokens[0][0]
                    characters.add(char)
                    var tags = tokens[1].split(',').map {tag -> tag.trim()}
                    sectionTags[char] = ArrayList(tags)
                }
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

        // Find starting and finishing points of each room
            // Cant use the Chunk.findChar() function because we are looking at parts of the sectionChunk
        for(char in characters){
            var space = LevelConnector.findTemplateSpace(sectionChunk, char)
            sectionChunk.maskChar(char);
            var start = Coords(-1, -1)
            //var foundStart = sectionChunk.findChar('m') + sectionChunk.findChar('M')
            //if(foundStart.isNotEmpty()) start = foundStart.first()
            var finish: ArrayList<Coords> = ArrayList()
            //finish.addAll(sectionChunk.findChar('f'))
            //finish.addAll(sectionChunk.findChar('F'))
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
            roomSpaces[char] = RoomSpace(space.width, space.height, space.UL_Corner(), start, finish)
        }

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