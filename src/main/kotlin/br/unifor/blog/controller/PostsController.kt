package com.unifor.blog.controller

import com.google.gson.GsonBuilder
import com.unifor.blog.database.DaoFactory
import com.unifor.blog.model.Post
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response


class PostsController {
    companion object: IController<Post> {

        override val logger = LoggerFactory.getLogger(PostsController::class.java)
        override val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val postDAO = DaoFactory.getDaoInstance<Post, Long>()

        override val insert = { req: Request, _: Response ->

            val json = req.body()
            val post = gson.fromJson(json, Post::class.java)

            val result = postDAO.create(post)

            if(result == 1){
                logger.info("Post criado com sucesso.")
                """{"status":"OK","description":"Post created successfully"}"""
            }else{
                logger.error("Erro ao criar um post.")
                """{"status":"ERROR","description":"An error occured during the creation process."}"""
            }

        }

        override val update = { req: Request, _: Response ->

            val id = req.params("id").toLong()
            val json = req.body()

            val post = gson.fromJson(json, Post::class.java)
            post.id = id

            val result = postDAO.update(post)

            if(result == 1){
                logger.info("Post atualizado com sucesso.")
                """{"status":"OK","description":"Post created successfully"}"""
            }else{
                logger.error("Error ao atualizar um post.")
                """{"status":"ERROR","description":"An error occured during the creation process."}"""
            }

        }

        override val delete = { req: Request, _: Response ->

            val id = req.params("id").toLong()

            val consult = postDAO.queryBuilder()
            consult.where().idEq(id)
            val post = postDAO.query(consult.prepare())

            val result = postDAO.delete(post)

            if(result == 1){
                logger.info("Post excluido com sucesso")
                """{"status":"OK","description":"Post created successfully"}"""
            }else{
                logger.error("Erro ao excluir post.")
                """{"status":"ERROR","description":"An error occured during the creation process."}"""
            }
        }

        override val get = { req: Request, _: Response ->

            val id = req.params("id").toLong()

            val consult = postDAO.queryBuilder()
            consult.where().idEq(id)
            val post = postDAO.query(consult.prepare())

            if(post != null){
                logger.info("Post com o id ${id} foi recuperado")
                gson.toJson(post)
            }else{
                logger.info("Não há post com o id ${id} no banco")
                """{"status":"ERROR","description":"An error occured during the creation process."}"""
            }
        }

        override val getAll = { _: Request, _: Response ->

            val users = PostsController.postDAO.queryForAll()

            if(users.size > 0){
                PostsController.logger.info("Post Listados")
                PostsController.gson.toJson(users)
            } else {
                UserController.logger.info("Nenhum post na base de dados")
                "{}"
            }
        }



    }

}
