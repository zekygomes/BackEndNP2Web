package br.unifor.blog

import com.unifor.blog.controller.AuthController
import com.unifor.blog.controller.UserController
import com.unifor.blog.database.DaoFactory
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.unifor.blog.controller.PostsController
import com.unifor.blog.model.*
import org.slf4j.LoggerFactory
import spark.ModelAndView
import spark.Request
import spark.Spark.*
import java.util.HashMap

fun main(args: Array<String>) {

    val DATABASE_URL = "jdbc:h2:./blog.db"
    DaoFactory.connSource = JdbcConnectionSource(DATABASE_URL)

    DaoFactory.createTable<Comment>()
    DaoFactory.createTable<Post>()
    DaoFactory.createTable<Tag>()
    DaoFactory.createTable<Token>()
    DaoFactory.createTable<User>()

    val logger = LoggerFactory.getLogger("App")


    secure("""C:\web\keystore.jks""", "password", null, null)

    path("/api", {

        before("/*", { req, _ ->
            logger.info("A requesdt from ${req.host()} was received.")

            if (req.cookie("auth-key") != null) {



                val key = req.cookie("auth-key")

                val tokenDao = DaoFactory.getDaoInstance<Token, Long>()
                val tokenstatementBuilder = AuthController.tokenDAO.queryBuilder()
                tokenstatementBuilder.where().eq("key", key)
                val token = tokenDao.query(tokenstatementBuilder.prepare()).firstOrNull()

                req.attribute("username", token)
                logger.info("A requesdt from ${req.host()} to ${req.uri()} is authenticated.")
            }else{
                halt(404, """{"status":"ERROR", "description":"You need to be Admin User!"}""")
            }

        })

        path("/user", {
            get("/", UserController.getAll)
            get("/:id", UserController.get)
            post("/", UserController.insert)
            put("/:id", UserController.update)
            delete("/:id", UserController.delete)
        })
    })

    path("/login", {
        post("/", AuthController.login)
    })

    path("/register", {
        post("/", AuthController.login)
    })

    val blogPosts = PostsController()



    passwordProtection("/new")
    passwordProtection("/detail/:slug/edit")



}

    private fun passwordProtection(path: String) {
        val logger = LoggerFactory.getLogger("App")
        before(path) { req, res ->
            if (req.attribute<Any>("username") == null) {
                logger.info("You must be signed in.")
                res.redirect("/password")
                halt()
            }

            if (req.attribute<Any>("username") != "admin") {
                logger.info("Incorrect username.")
                res.redirect("/password")
                halt()
            }
        }
    }

