package com.ex.github

data class Repositories (
    var name : String,
    var full_name : String? = null,
    var repoIsWhose : String? = null,
    var language : String? = null,
    var stargazers_count : String? = null,
    var watchers_count : Int? = null,
    var visibility : String? = null,
    var isFavorite : Boolean = false
)
