package com.kasymzhan.quest.catalog.data

import org.bson.types.ObjectId

data class QuestRequest(
    val name: String,
    val description: String,
    val rewardId: ObjectId,
    val autoClaim: Boolean,
    val duplication: Int,
    val streak: Int,
)
