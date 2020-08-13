package levelGenerators.jcatlos

import kotlin.random.Random

object RandomRoomGenerator: RoomGenerator{
    private val BONUS_PROB = 0.10
    private val DIVIDE_PROB = 0.20

    private fun generateRoomH1(): RoomH1{
        val randomNumber = Random.nextFloat()
        return if(randomNumber <= BONUS_PROB){
            BonusRoomH1()
        }
        else{
            ChallengeRoomH1()
        }
    }

    private fun generateRoomH2(): RoomH2{
        val randomNumber = Random.nextFloat()
        return when{
            randomNumber <= BONUS_PROB -> BonusRoomH2()
            randomNumber <= DIVIDE_PROB -> DivideHub()
            else -> ChallengeRoomH2()
        }
    }

    override fun generate(size: Int): Room{
        return when(size){
            0 -> EmptyRoomH1()
            1 -> generateRoomH1()
            2 -> generateRoomH2()
            else -> EmptyRoomH2()
        }
    }


}