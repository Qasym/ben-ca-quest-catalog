package com.kasymzhan.quest.catalog.controller

import com.kasymzhan.quest.catalog.data.Quest
import com.kasymzhan.quest.catalog.data.QuestRequest
import com.kasymzhan.quest.catalog.data.QuestResponse
import com.kasymzhan.quest.catalog.data.QuestReward
import com.kasymzhan.quest.catalog.repository.QuestRepository
import com.kasymzhan.quest.catalog.service.JwtTokenService
import jakarta.servlet.http.HttpServletRequest
import org.bson.types.ObjectId
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

@RestController
@RequestMapping("/quests")
class QuestCatalogController(
    val questRepository: QuestRepository,
    val webClient: WebClient,
    val tokenService: JwtTokenService
) {
    @GetMapping
    fun getQuests(httpReq: HttpServletRequest): List<QuestResponse> {
        val authentication = SecurityContextHolder.getContext().authentication
        val token = tokenService.tryParseToken(httpReq) ?: println("accessing without token")
        val quests = questRepository.findAll()
        val url = "http://localhost:2004/rewards"
        val rewardList: List<QuestReward> = webClient.get()
            .uri(url)
            .header("Authorization", "Bearer $token")
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<QuestReward>>() {})
            .block() ?: emptyList()
        val rewardMap = rewardList.associateBy { it.id }
        return try {
            quests.map {
                val reward = rewardMap[it.rewardId.toString()] ?: throw Exception("Reward not found")
                it.toQuestResponse(reward)
            }
        } catch (e: Exception) {
            println("exception $e")
            emptyList()
        }
    }

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

    private fun Quest.toQuestResponse(reward: QuestReward): QuestResponse =
        QuestResponse(
            id = this.id.toString(),
            name = this.name,
            description = this.description,
            duplication = this.duplication,
            streak = this.streak,
            reward = reward,
            autoClaim = this.autoClaim,
        )
}