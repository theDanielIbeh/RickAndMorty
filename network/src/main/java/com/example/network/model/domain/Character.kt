// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.example.network.model.domain

data class Character (
    val imageUrl: String,
    val gender: CharacterGender,
    val species: String,
    val created: String,
    val origin: Origin,
    val name: String,
    val location: Location,
    val episodeIds: List<Int>,
    val id: Int,
    val type: String,
    val status: CharacterStatus
) {
    data class Origin (
        val name: String,
        val url: String
    )
    data class Location (
        val name: String,
        val url: String
    )
}
