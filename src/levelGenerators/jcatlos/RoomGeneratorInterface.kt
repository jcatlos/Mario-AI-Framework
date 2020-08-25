package levelGenerators.jcatlos

interface RoomGenerator {
    fun generate(size: Int, difficulty: Int): Room
    fun generate(type: ROOM_TYPE): Room
}