package levelGenerators.jcatlos

import java.io.File

object SectionParser {
    fun fileToSectionTemplate(sectionFile: File): SectionTemplate{
        var sectionString: StringBuilder = StringBuilder()
        var roomSpaces: MutableMap<Char, RoomSpace> = mutableMapOf()
        var sectionTags: MutableMap<Char, ArrayList<String>> = mutableMapOf()

        var characters: MutableSet<Char> = mutableSetOf()

        var parsingHead = true;

        for(line in sectionFile.readText().lines()){
        //while(line != "---".trim()){
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
                    sectionString.appendln(line)
                }
            }
        }

        //Find starting and finishing points of the whole section
        var sectionList = sectionString.lines().reversed()
        var sectionStart = Coords(-1, -1)
        var sectionFinish: ArrayList<Coords> = ArrayList()

        for(y in sectionList.indices){
            for(x in sectionList[y].indices){
                if(sectionList[y][x] == 'M'){
                    sectionStart = Coords(x, y)
                }
                if(sectionList[y][x] == 'F'){
                    sectionFinish.add(Coords(x,y))
                }
            }
        }
        //println("section start = $sectionStart")

        // Find starting and finishing points of each room (using the same sectionList)
        for(char in characters){
            //println("parsing for $char")
            var space = findTemplateSpace(sectionString, char)
            //println("found ${space.DL_Corner()}, ${space.width}, ${space.height}")
            var start = Coords(-1, -1)
            var finish: ArrayList<Coords> = ArrayList()
            for(x in 0 until space.width){
                for(y in 0 until space.height){
                    var currentChar: Char = sectionList[y+space.DL_Corner().y][x+space.DL_Corner().x]
                    //print(currentChar)
                    if(currentChar == 'm' || currentChar == 'M'){
                        start = Coords(x, y)
                    }
                    else if(currentChar == 'f' || currentChar == 'F'){
                        finish.add(Coords(x, y))
                    }
                }

            }
            //println("roomSpace start = $start")
            roomSpaces[char] = RoomSpace(space.width, space.height, space.DL_Corner(), start, finish)
        }

        return SectionTemplate(
                RoomSpace(sectionString.lines().first().length,
                        sectionString.lines().size,
                        Coords(0,0),
                        sectionStart,
                        sectionFinish),
                roomSpaces,
                sectionTags)
    }
}