package br.unifor.blog

import com.unifor.blog.controller.AuthController
import com.unifor.blog.controller.UserController
import com.unifor.blog.database.DaoFactory
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date
import com.unifor.blog.controller.CommentsController
import com.unifor.blog.controller.PostsController
import com.unifor.blog.model.*
import org.slf4j.LoggerFactory
import spark.ModelAndView
import spark.Request
import spark.Spark.*
import java.text.SimpleDateFormat
import java.util.*


fun main(args: Array<String>) {

    val DATABASE_URL = "jdbc:h2:./blog.db"
    DaoFactory.connSource = JdbcConnectionSource(DATABASE_URL)

    val logger = LoggerFactory.getLogger("App")

    //secure("""C:\web\keystore.jks""", "password", null, null)

    //### IMPORTANTE ### Por favor, Após a primeira execução, comente a próxima linha
    justPopulating()

    path("/api", {

        before("/admin/*", { req, _ ->
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

        path("/admin", {
            path("/user", {
                get("/", UserController.getAll)
                get("/:id", UserController.get)
                post("/", UserController.insert)
                put("/:id", UserController.update)
                delete("/:id", UserController.delete)
            })
            path("/post", {
                get("/", PostsController.getAll)
                get("/:id", PostsController.get)
                post("/", PostsController.insert)
                put("/:id", PostsController.update)
                delete("/:id", PostsController.delete)
            })
            path("/comment", {
                get("/:id", CommentsController.getAll)
                post("/", CommentsController.insert)
                put("/:id", CommentsController.update)
                delete("/:id", CommentsController.delete)
            })
            path("/register", {
                post("/", AuthController.login)
            })
        })

        path("/public", {
            path("/user", {
                get("/", UserController.getAll)
                get("/:id", UserController.get)
                post("/", UserController.insert)
            })
            path("/post", {
                get("/", PostsController.getAll)
                get("/:id", PostsController.get)
                post("/", PostsController.insert)
                put("/:id", PostsController.update)
                delete("/:id", PostsController.delete)
            })
            path("/comment", {
                get("/:id", CommentsController.getAll)
                post("/", CommentsController.insert)
            })
        })

        path("/login", {
            post("/", AuthController.login)
        })
    })

}
    fun justPopulating() {
        DaoFactory.createTable<Comment>()
        DaoFactory.createTable<Post>()
        DaoFactory.createTable<Token>()
        DaoFactory.createTable<User>()

        val data1 = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dataHora = sdf.format(data1)

        val userAdmin = User()
        userAdmin.id = 100
        userAdmin.name = "Admin"
        userAdmin.email = "admin@admin.com"
        userAdmin.password = "teste123"
        userAdmin.salt = "${System.currentTimeMillis()}"
        val hashedPassword = AuthController.hashPassword(userAdmin.email, userAdmin.password, userAdmin.salt)
        userAdmin.password = hashedPassword
        val ret = UserController.userDAO.create(userAdmin)

        do {
            val postTest = Post()
            postTest.content = "Some long text here, but im not in the mood right now to do, so Ill try do something more quickly."
            postTest.createdTime = dataHora
            postTest.title = "Some text"
            postTest.viewCount = "0"
            postTest.author = userAdmin
            val result = PostsController.postDAO.create(postTest)

            val commentTest = Comment()
            commentTest.comment = "Some long text here, but im not in the mood right now to do, so i'm finish."
            commentTest.createdTime = dataHora
            commentTest.post = postTest
            val result2 = CommentsController.commentDAO.create(commentTest)
            i++
        }while(i < 5)
    }



