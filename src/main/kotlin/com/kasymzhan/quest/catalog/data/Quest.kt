package com.kasymzhan.quest.catalog.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "quests")
data class Quest(
    @Id
    val id: ObjectId,
    val rewardId: ObjectId,
    val autoClaim: Boolean,
    val streak: Int,
    val duplication: Int,
    val name: String,
    val description: String,
)
