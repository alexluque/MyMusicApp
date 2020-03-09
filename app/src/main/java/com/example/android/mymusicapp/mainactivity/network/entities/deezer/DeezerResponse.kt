data class DeezerResponse (

	val data : MutableList<SongData> = ArrayList(),
	val total : Int = 0,
	val next : String = String()
)