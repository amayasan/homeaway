package com.amayasan.exploreaway.model

object Model {
    data class Result(val response : Response)
    data class Response(val venues : List<Venue>)
    data class Venue(val id : String, val name : String)
}