package com.example.ghichu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var noteAdapter: NoteAdapter
    private val noteList = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        database = FirebaseDatabase.getInstance().reference.child("notes")
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val btnAddNote = findViewById<Button>(R.id.btnAddNote)


        recyclerView.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(noteList, this)
        recyclerView.adapter = noteAdapter

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                noteList.clear()
                for (noteSnapshot in snapshot.children) {
                    val note = noteSnapshot.getValue(Note::class.java)
                    if (note != null) {
                        noteList.add(note)
                    }
                }
                noteAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to read value.", Toast.LENGTH_SHORT).show()
            }
        })

        btnAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    fun onEditClick(noteID:String){
        val intent = Intent(this, EditNoteActivity::class.java)
        intent.putExtra("id",noteID)
        startActivity(intent)
    }
    // Xử lý khi nhấn giữ để xóa ghi chú
    fun onDeleteClick(noteId: String) {
        // Hiển thị hộp thoại xác nhận
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Bạn có chắc chắn muốn xóa ghi chú này?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // Khi chọn Yes, xóa ghi chú
                val noteRef = database.child(noteId)
                noteRef.removeValue()
            }
            .setNegativeButton("No") { dialog, id ->
                // Khi chọn No, đóng hộp thoại và không làm gì cả
                dialog.dismiss()
            }
        // Hiển thị hộp thoại
        builder.create().show()
    }
}
