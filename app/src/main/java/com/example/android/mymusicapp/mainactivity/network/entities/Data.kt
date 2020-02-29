data class Data(

    val id: Int = 0,
    val readable: Boolean = true,
    val title: String = String(),
    val title_short: String = String(),
    val title_version: String = String(),
    val link: String = String(),
    val duration: Int = 0,
    val rank: Int = 0,
    val explicit_lyrics: Boolean = false,
    val explicit_content_lyrics: Int = 0,
    val explicit_content_cover: Int = 0,
    val preview: String = String(),
    val artist: Artist? = null,
    val album: Album? = null,
    val type: String = String()
)
