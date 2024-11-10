package com.kasymzhan.quest.catalog.repository

import com.kasymzhan.quest.catalog.data.Quest
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestRepository: MongoRepository<Quest, ObjectId> {}