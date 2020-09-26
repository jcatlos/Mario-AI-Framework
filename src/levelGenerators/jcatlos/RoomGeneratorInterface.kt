package levelGenerators.jcatlos

interface RoomGenerator {
    fun generateToFitRoomspace(roomSpace: RoomSpace, sectionTags: ArrayList<String>): Room
}