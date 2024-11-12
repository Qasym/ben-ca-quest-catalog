package com.kasymzhan.quest.catalog.data

data class QuestResponse(
    val id: String,
    val name: String,
    val description: String,
    val duplication: Int,
    val streak: Int,
    val reward: QuestReward
)