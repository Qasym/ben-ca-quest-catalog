package com.kasymzhan.quest.catalog.controller

import com.kasymzhan.quest.catalog.data.Quest
import com.kasymzhan.quest.catalog.data.QuestRequest
import com.kasymzhan.quest.catalog.repository.QuestRepository
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/quests")
class QuestCatalogController(val questRepository: QuestRepository) {
    @GetMapping("/get/all")
    fun getAllQuests(): List<Quest> {
        return questRepository.findAll()
    }

    @PostMapping("/add")
    fun addQuest(@RequestBody questReq: QuestRequest): ResponseEntity<String> {
        val quest = questReq.toQuest()
        questRepository.save(quest)
        return ResponseEntity("Created quest ${quest.name}", HttpStatus.CREATED)
    }

    private fun QuestRequest.toQuest(): Quest =
        Quest(
            id = ObjectId(),
            name = this.name,
            description = this.description,
            streak = this.streak,
            duplication = this.duplication,
            autoClaim = this.autoClaim,
            rewardId = this.rewardId
        )
}