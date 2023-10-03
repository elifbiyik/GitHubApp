package com.ex.github

import android.net.Uri

data class User(
    var login: String? = null,
    var loginPhone: String?=null,
    var favLogin: String? = null,
    var avatar_url: String? = null,
    var login_avatar_url: Uri? = null,
    var url: String? = null,
    var name: String? = null,
    var public_repos: String? = null,
    var public_gists: String? = null,
    var followers: String? = null,
    var following: String? = null,
    var html_url: String? = null,
    var password: String? = null,
    var phoneNumber: String? = null,
    var storage: Uri? = null,
    var isFirebase: Boolean? = false
)
