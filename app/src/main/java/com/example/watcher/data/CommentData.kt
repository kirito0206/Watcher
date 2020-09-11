package com.example.watcher.data

data class CommentData(
    val `data`: DataX,
    val status: Int
)

data class DataX(
    val comments: List<Comment>
)

data class Comment(
    val commentid: Int,
    val star: Int,
    val subcomments: List<Subcomment>,
    val text: String,
    val uploadtime: String,
    val userid: Int,
    val username: String
)

data class Subcomment(
    val star: Int,
    val subcommentid: Int,
    val text: String,
    val uploadtime: String,
    val userid: Int,
    val username: String
)