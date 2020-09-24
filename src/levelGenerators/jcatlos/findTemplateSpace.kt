package levelGenerators.jcatlos

fun findTemplateSpace(room: StringBuilder, char: Char): Space{
    var upperBound: Int = -1
    var lowerBound: Int = Int.MAX_VALUE
    var leftBound: Int = -1
    var rightBound: Int = Int.MAX_VALUE

    var x: Int = 0
    var y: Int = room.lines().size - 1

    for(line in room.lines()){
        for(c in line){
            if (c == char) {
                if (x < rightBound) {
                    //println("found rb: $x")
                    rightBound = x
                }
                if (x > leftBound) {
                    leftBound = x
                }
                if (y > upperBound){
                    upperBound = y
                }
                if (y < lowerBound){
                    //println("found lb: $y")
                    lowerBound = y
                }
            }
            x++
        }
        y--
        x = 0
    }

    return Space(leftBound - rightBound + 1,
            upperBound - lowerBound + 1,
            Coords(rightBound, lowerBound))
}