package com.example.rickandmorty.core.network.model.domain

sealed class CharacterGender(val displayName: String) {
    object Male : CharacterGender("Male")
    object Female : CharacterGender("Female")
    object Unknown : CharacterGender("Not specified")
    object Genderless : CharacterGender("Genderless")
}