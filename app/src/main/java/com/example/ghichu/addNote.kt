package com.example.ghichu


import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class AddNoteActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        // Khởi tạo Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("notes")

        // Khởi tạo các view
        titleEditText = findViewById(R.id.addNoteTitle)
        contentEditText = findViewById(R.id.addNoteContent)
        saveButton = findViewById(R.id.saveNoteButton)

        // Xử lý sự kiện khi nhấn nút Lưu
        saveButton.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        val title = titleEditText.text.toString().trim()
        val content = contentEditText.text.toString().trim()

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Vui lòng nhập tiêu đề và nội dung!", Toast.LENGTH_SHORT).show()
            return
        }

        // Tạo ID mới cho ghi chú
        val noteId = database.push().key ?: return

        // Tạo đối tượng ghi chú mới
        val newNote = Note(id = noteId, title = title, content = content)

        // Lưu ghi chú vào Firebase
        database.child(noteId).setValue(newNote).addOnSuccessListener {
            Toast.makeText(this, "Ghi chú đã được thêm!", Toast.LENGTH_SHORT).show()
            finish() // Đóng Activity và quay lại MainActivity
        }.addOnFailureListener {
            Toast.makeText(this, "Không thể thêm ghi chú", Toast.LENGTH_SHORT).show()
        }
    }
}
