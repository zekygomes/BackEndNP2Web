package com.unifor.blog.controller

import com.unifor.blog.database.DaoFactory
import com.unifor.blog.model.Auth
import com.unifor.blog.model.Token
import com.unifor.blog.model.User
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import java.security.MessageDigest
import java.util.*

class AuthController {

    companion object {

        val logger = LoggerFactory.getLogger(AuthController::class.java)
        val gson = Gson()
        val userDAO = DaoFactory.getDaoInstance<User, Long>()
        val tokenDAO = DaoFactory.getDaoInstance<Token, Long>()

        val login = { req: Request, resp: Response ->

            val body = req.body()
            val auth = gson.fromJson(body, Auth::class.java)

            val userStatementBuilder = userDAO.queryBuilder()
            userStatementBuilder.where().eq("email", auth.email)
            val user = userDAO.query(userStatementBuilder.prepare()).first()

            val digest = MessageDigest.getInstance("SHA-256")
            val hashedPassword = String(digest.digest("${user.email}:${auth.password}:${user.salt}".toByteArray()))

            if (user.password.equals(hashedPassword)) {

                val tokenstatementBuilder = tokenDAO.queryBuilder()
                tokenstatementBuilder.where().eq("active", true)
                val tokens = tokenDAO.query(tokenstatementBuilder.prepare())
                tokens.forEach {
                    it.active = false
                    tokenDAO.update(it)
                    logger.info("The token ${it.key} was invalidated.")
                }

                val token = Token()
                token.key = UUID.randomUUID().toString()
                token.active = true
                token.user = user
                token.validity = 86400
                token.createdAt = Date()

                tokenDAO.create(token)

                logger.info("Password match: ${token.key}")
                """"{"status":"Authenticated", "key":"${token.key}"}"""

            } else {

                logger.info("Password not match")
                """"{"status":"Error", "description":"An error occurred during the authentication process"}"""

            }


        }

    }

}