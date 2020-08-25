package levelGenerators.jcatlos

class LayerBuilder(private val designer: LevelDesigner = BasicLayerDesigner,
                   private val generator: RoomGenerator = RandomRoomGenerator,
                   private val state: State = State()
                   ){

    fun next(): Layer{
        var out = Layer()
        for(i in designer.next()){
            var room = generator.generate(i, (state.currentDifficulty + state.difficultyIncrease).toInt())
            if(room.type == ROOM_TYPE.DIVIDE) designer.divide(0)
            out.add(room)
        }
        out.getRooms().last().applySafety()
        return out
    }

    fun createStart(): Room{
        //println("in layerbuilder")
        var start = generator.generate(ROOM_TYPE.START)
        start.applySafety()
        return start
    }

    fun createFinish(): Room{
        var finish = generator.generate(ROOM_TYPE.FINISH)
        finish.applySafety()
        return finish
    }
}