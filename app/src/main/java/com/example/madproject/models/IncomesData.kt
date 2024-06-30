package com.example.madproject.models

class IncomesData {
    var id : String? = null
    var date : String? = null
    var amount : String? = null
    var category : String? = null
    var description : String? = null
    var dataImage: String? = null


    constructor(id:String?,date : String?, amount : String?, category : String?, description : String?, dataImage: String?){
        this.id = id
        this.date = date
        this.amount = amount
        this.category = category
        this.description = description
        this.dataImage = dataImage
    }
    constructor(){

    }



}