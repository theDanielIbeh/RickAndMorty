// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.example.network.model.remote

import com.example.network.model.domain.Character
import com.example.network.model.domain.CharacterGender
import com.example.network.model.domain.CharacterStatus
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCharacter(
    val image: String,
    val gender: String,
    val species: String,
    val created: String,
    val origin: Origin,
    val name: String,
    val location: Location,
    val episode: List<String>,
    val id: Int,
    val type: String,
    val url: String,
    val status: String
) {
    @Serializable
    data class Origin(
        val name: String, val url: String
    )

    @Serializable
    data class Location(
        val name: String, val url: String
    )
}

fun RemoteCharacter.toDomainCharacter(): Character {
    val characterGender = when (gender.lowercase()) {
        "male" -> CharacterGender.Male
        "female" -> CharacterGender.Female
        "Genderless" -> CharacterGender.Genderless
        else -> CharacterGender.Unknown
    }
    val characterStatus = when (status.lowercase()) {
        "alive" -> CharacterStatus.Alive
        "dead" -> CharacterStatus.Dead
        else -> CharacterStatus.Unknown
    }

    return Character(
        imageUrl = image,
        gender = characterGender,
        species = species,
        created = created,
        origin = Character.Origin(
            name = origin.name, url = origin.url
        ),
        name = name,
        location = Character.Location(
            name = location.name, url = location.url
        ),
        episodeIds = episode.map { it.substring(it.lastIndexOf("/") + 1).toInt() },
        id = id,
        type = type,
        status = characterStatus
    )
}