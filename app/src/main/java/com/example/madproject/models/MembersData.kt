package com.example.madproject.models

class MembersData {
    var fid: String? = null
    var email: String? = null
    var status: String="Pending"

    constructor(fid: String?, email: String?){
        this.fid = fid
        this.email = email
    }

}