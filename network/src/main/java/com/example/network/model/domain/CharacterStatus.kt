package com.example.network.model.domain

import android.graphics.Color

sealed class CharacterStatus(val displayName: String, val color: Int) {
    object Alive : CharacterStatus("Alive", color = Color.GREEN)
    object Dead : CharacterStatus("Dead", color = Color.RED)
    object Unknown : CharacterStatus("Unknown", color = Color.YELLOW)
}

fun CharacterStatus.asColor(): Int = this.color