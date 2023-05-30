package com.example.wishlistapp.navigation

interface Navigable {
    enum class Destination {
        List, Add, Edit, Map
    }

    fun navigate(to: Destination, id: Long = -1)
}