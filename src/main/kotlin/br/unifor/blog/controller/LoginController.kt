package com.unifor.blog.controller

import com.google.gson.Gson
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response

class LoginController {

    companion object {

        val logger = LoggerFactory.getLogger(LoginController::class.java)
        val gson = Gson()

        val login = {req:Request, res:Response ->



        }

        val logout = {req:Request, res:Response ->



        }

    }

}