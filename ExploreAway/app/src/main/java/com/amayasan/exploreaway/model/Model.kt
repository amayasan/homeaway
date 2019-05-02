package com.amayasan.exploreaway.model

object Model {
    data class Result(val response : Response)
    data class Response(val venues : List<Venue>)
    data class Venue(val id : String,
                     val name : String,
                     val location : Location,
                     val categories : List<Category>)
    data class Location(val address : String,
                        val crossStreet : String,
                        val lat : Float,
                        val lng : Float,
                        val postalCode : String,
                        val cc : String,
                        val city : String,
                        val state : String,
                        val country : String)
    data class Category(val id : String,
                        val name : String,
                        val pluralName : String,
                        val shortName : String,
                        val icon : Icon)
    data class Icon(val prefix : String,
                    val suffix : String)
}