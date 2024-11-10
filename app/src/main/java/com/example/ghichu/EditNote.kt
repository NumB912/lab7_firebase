package com.example.ghichu
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class EditNoteActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var noteId: String
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        // Khởi tạo Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("notes")

        // Lấy ID ghi chú từ Intent
        noteId = intent.getStringExtra("id") ?: ""

        // Khởi tạo các view
        titleEditText = findViewById(R.id.editNoteTitle)
        contentEditText = findViewById(R.id.editNoteContent)
        saveButton = findViewById(R.id.saveNoteButton)

        // Tải dữ liệu ghi chú từ Firebase
        loadNoteData()

        // Xử lý sự kiện lưu ghi chú
        saveButton.setOnClickListener {
            val updatedTitle = titleEditText.text.toString()
            val updatedContent = contentEditText.text.toString()

            if (updatedTitle.isNotEmpty() && updatedContent.isNotEmpty()) {
                // Cập nhật ghi chú trong Firebase
                updateNoteInFirebase(updatedTitle, updatedContent)
            } else {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Tải dữ liệu ghi chú từ Firebase
    private fun loadNoteData() {
        val noteRef = database.child(noteId)

        noteRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val note = snapshot.getValue(Note::class.java)
                if (note != null) {
                    titleEditText.setText(note.title)
                    contentEditText.setText(note.content)
                } else {
                    Toast.makeText(this@EditNoteActivity, "Không tìm thấy ghi chú", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditNoteActivity, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Cập nhật ghi chú trong Firebase
    private fun updateNoteInFirebase(updatedTitle: String, updatedContent: String) {
        val noteRef = database.child(noteId)
        val updatedNote = Note(noteId, updatedTitle, updatedContent)

        noteRef.setValue(updatedNote).addOnSuccessListener {
            Toast.makeText(this, "Ghi chú đã được cập nhật", Toast.LENGTH_SHORT).show()
            finish() // Quay lại màn hình trước
        }.addOnFailureListener {
            Toast.makeText(this, "Lỗi khi cập nhật ghi chú", Toast.LENGTH_SHORT).show()
        }
    }
}
