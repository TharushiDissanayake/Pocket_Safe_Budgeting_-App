package com.example.madproject.models

data class BillReminderModel(
    var billId: String? = null,
    var billTitle: String? = null,
    var billAmount: String,
    var billDate: Long = 0,
    var billCategory: String? = null,
    var billNote: String? = null,
    var userId: String? = null,
    var billType: String? = null
) {
    // Add a no-argument constructor
    constructor() : this(null, null, "", 0, null, null, null, null)
}