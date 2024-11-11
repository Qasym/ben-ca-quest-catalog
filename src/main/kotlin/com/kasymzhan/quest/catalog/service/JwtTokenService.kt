package com.kasymzhan.quest.catalog.service

import com.kasymzhan.quest.catalog.config.JwtConfig
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service

@Service
class JwtTokenService(jwtConfig: JwtConfig) {
    private val secretKey = Keys.hmacShaKeyFor(
        jwtConfig.secret.toByteArray()
    )

    fun tryParseToken(request: HttpServletRequest): String? {
        val authHeader: String? = request.getHeader("Authorization")
        if (!authHeader.containsToken())
            return null
        val token = authHeader!!.extractToken()
        return token
    }

    fun isValid(token: String?): Boolean {
        if (token.isNullOrBlank())
            return false
        try {
            getAllClaims(token)
            return true
        } catch (e: Exception) {
            println("exception: $e")
            return false
        }
    }

    fun getAllClaims(token: String): Claims {
        val decoder = Jwts.parser().verifyWith(secretKey).build()
        return decoder.parseSignedClaims(token).payload
    }

    private fun String?.containsToken() =
        this != null && this.startsWith("Bearer ")

    private fun String.extractToken(): String =
        this.substringAfter("Bearer ")
}