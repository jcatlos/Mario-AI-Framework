package levelGenerators.jcatlos

interface RoomGenerator {
    fun generateToFitRoomspace(roomSpace: RoomSpace): Room
}