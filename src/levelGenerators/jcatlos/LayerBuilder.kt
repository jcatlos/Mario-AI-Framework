package levelGenerators.jcatlos

class LayerBuilder(){
    val designer = BasicLayerDesigner
    val generator = RandomRoomGenerator

    fun next(): Layer{
        var out = Layer()
        for(i in designer.next()){
            var room = generator.generate(i)
            if(room is DivideHub) designer.divide(0)
            out.add(room)
        }
        return out
    }
}