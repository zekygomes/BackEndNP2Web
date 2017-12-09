package com.unifor.blog.controller

import com.google.gson.GsonBuilder
import com.unifor.blog.database.DaoFactory
import com.unifor.blog.model.Comment
import com.unifor.blog.model.Post
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response


class CommentsController {
    companion object: IController<Post> {

        override val logger = LoggerFactory.getLogger(CommentsController::class.java)
        override val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val commentDAO = DaoFactory.getDaoInstance<Comment, Long>()

        override val insert = { req: Request, _: Response ->

            val json = req.body()
            val comment = gson.fromJson(json, Comment::class.java)

            val result = commentDAO.create(comment)

            if(result == 1){
                logger.info("Comment created successfully.")
                """{"status":"OK","description":"Comment created successfully"}"""
            }else{
                logger.error("Erro ao criar um post.")
                """{"status":"ERROR","description":"An error occured during the creation process."}"""
            }

        }

        override val update = { req: Request, _: Response ->

            val id = req.params("id").toLong()
            val json = req.body()

            val comment = gson.fromJson(json, Comment::class.java)
            comment.id = id

            val result = commentDAO.update(comment)

            if(result == 1){
                logger.info("Post atualizado com sucesso.")
                """{"status":"OK","description":"Comment created successfully"}"""
            }else{
                logger.error("Error ao atualizar um post.")
                """{"status":"ERROR","description":"An error occured during the creation process."}"""
            }

        }

        override val delete = { req: Request, _: Response ->

            val id = req.params("id").toLong()

            val consult = commentDAO.queryBuilder()
            consult.where().idEq(id)
            val comment = commentDAO.query(consult.prepare())

            val result = commentDAO.delete(comment)

            if(result == 1){
                logger.info("Post excluido com sucesso")
                """{"status":"OK","description":"Comment created successfully"}"""
            }else{
                logger.error("Erro ao excluir post.")
                """{"status":"ERROR","description":"An error occured during the creation process."}"""
            }
        }

        override val get = { req: Request, _: Response ->

            val id = 0L

            val consult = commentDAO.queryBuilder()
            consult.where().idEq(id)
            val comment = commentDAO.query(consult.prepare())

            if(comment != null){
                logger.info("Comment with id ${id} was recovered")
                gson.toJson(comment)
            }else{
                logger.info("There's not ${id} on database")
                """{"status":"ERROR","description":"An error occured during the creation process."}"""
            }
        }


        override val getAll = { req: Request, _: Response ->
            val id = req.params("id").toLong()
            val consult = commentDAO.queryBuilder()
            consult.where().like("post_id", id)

            val comments = commentDAO.query(consult.prepare())

            if(comments.size > 0){
                logger.info("Comments Listados")
                gson.toJson(comments)
            } else {
                logger.info("There's not ${id} on database")
                """{"author":"","createdTime":"","comment":"No Comments"}"""
            }
        }



    }

}
