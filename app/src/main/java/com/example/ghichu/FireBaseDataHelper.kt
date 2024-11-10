package com.example.ghichu

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
object FirebaseDatabaseHelper {

    private val database = FirebaseDatabase.getInstance().getReference("notes")

    // Lấy tất cả các ghi chú từ Firebase
    fun getAllNotes(callback: (List<Note>) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notesList = mutableListOf<Note>()
                for (noteSnapshot in snapshot.children) {
                    val note = noteSnapshot.getValue(Note::class.java)
                    note?.let { notesList.add(it) }
                }
                callback(notesList)  // Trả dữ liệu về callback
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        })
    }
}

