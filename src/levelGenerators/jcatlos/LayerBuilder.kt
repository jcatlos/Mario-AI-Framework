package levelGenerators.jcatlos

class LayerBuilder(private val designer: LevelDesigner = BasicLayerDesigner,
                   private val generator: RoomGenerator = RandomRoomGenerator,
                   private val state: State = State()
                   ){

    var difficulty: Int = 0

    fun next(): Layer{
        var out = Layer()
        for(i in designer.next()){
            var room = generator.generate(i)
            if(room.type == ROOM_TYPE.DIVIDE) designer.divide(0)
            out.add(room)
            difficulty += room.difficulty
        }
        difficulty /= out.getRooms().count()
        state.updateByLayer(out)
        println("layer difficulty = $difficulty")
        //println("safe = ${state.layerSafety()}")
        out.getRooms().last().applySafety(state.layerSafety())
        return out
    }

    fun createStart(): Room{
        //println("in layerbuilder")
        var start = generator.generate(ROOM_TYPE.START)
        start.applySafety(true)
        return start
    }

    fun createFinish(): Room{
        var finish = generator.generate(ROOM_TYPE.FINISH)
        finish.applySafety(true)
        return finish
    }
}