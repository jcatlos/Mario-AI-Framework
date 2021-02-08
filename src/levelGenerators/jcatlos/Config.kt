package levelGenerators.jcatlos

import kotlin.random.Random

enum class SectionType{
    INTRO,
    HARD,
    TWIST,
    FINISH
}

data class Config(
        var challengeTag: String = "",
        var maxLength: Int,
        var maxHeight: Int,
        var roomGenerator: RoomGenerator,
        var introLength: Int,
        var hardLength: Int,
        var twistMultiplier: Double
) {
    var sectionCount = 0
    var twistChance = 0.1

    fun getNextSectionType(): SectionType{
        sectionCount++
        if(sectionCount <= introLength) return SectionType.INTRO
        if(sectionCount >= introLength+hardLength) return SectionType.FINISH

        twistChance *= twistMultiplier
        if(Random.nextFloat() > twistChance) return SectionType.TWIST

        return SectionType.HARD
    }
}