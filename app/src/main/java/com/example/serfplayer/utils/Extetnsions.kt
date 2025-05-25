package com.example.serfplayer.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.snapshots.SnapshotStateList

@OptIn(ExperimentalMaterial3Api::class)
val SheetState.currentFraction: Float
    get() {
        return when (currentValue) {
            SheetValue.Hidden -> 0f
            SheetValue.PartiallyExpanded -> 0.5f
            SheetValue.Expanded -> 1f
        }
    }

fun <T> Collection<T>.move(from: Int, to: Int) : List<T> {
    if(from == to) return this.toList()
    return ArrayList(this).apply {
        val temp = get(from)
        removeAt(from)
        add(to, temp)
    }
}

fun <T> SnapshotStateList<T>.swap(newList: List<T>) : SnapshotStateList<T> {
    clear()
    addAll(newList)

    return this
}