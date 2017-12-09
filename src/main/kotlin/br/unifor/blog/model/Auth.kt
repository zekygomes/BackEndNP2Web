package com.unifor.blog.model

class Auth {

    var email = ""
    var password = ""

    override fun toString(): String {
        return "Email: "+this.email+"; Pass: "+this.password
    }

}