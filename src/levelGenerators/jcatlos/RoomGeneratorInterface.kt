package levelGenerators.jcatlos

/*
    Interface for RoomGenerators - allows to change RoomGenerator
        - Only required function is generateToFitRoomspace()
        - Does not need to use the whole RoomTemplate process
 */

interface RoomGenerator {
    fun generateToFitRoomspace(roomSpace: RoomSpace, sectionTags: ArrayList<String>): Room
}