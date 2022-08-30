package com.example.ep3faith.network

class DataTransferObjects {

    /*
    @JsonClass(generateAdapter = true)
    data class NetworkPostContainer(val posts: List<NetworkPost>)

    @JsonClass(generateAdapter = true)
    data class NetworkPost(
        var postId: Long,
        var username: String,
        var textContent: String,
        var picture: String,
        var webLink: String
    )

     */

    /*
    fun NetworkPostContainer.asDatabaseModel(): Array<DatabasePost> {
        return posts.map {
            DatabasePost (
                postId = it.postId,
                username = it.username,
                emailUser = it.emailUser,
                caption = it.picture,
                link = it.link)
        }.toTypedArray()
    }


     */
}
