package io.codedigest.contacts.ui.main.model

data class Contact(
    val id: String,
    val lookupKey: String,
    val name: String,
    var phone: String? = null,
    var email: String? = null
)
