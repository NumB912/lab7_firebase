package com.example.ghichu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.zip.Inflater

class NoteAdapter(val list:List<Note>,val context: Context):RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note,parent,false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NoteAdapter.NoteViewHolder, position: Int) {
        var note = list[position]
        holder.textNote.text = note.title
        holder.textContent.text = note.content
        holder.itemView.setOnLongClickListener {
            (context as MainActivity).onDeleteClick(note.id) // Gọi hàm xóa ghi chú trong MainActivity
            true // Trả về true để xử lý sự kiện nhấn giữ
        }

        holder.itemView.setOnClickListener {
            (context as MainActivity).onEditClick(note.id) // Gọi hàm xóa ghi chú trong MainActivity
            true
        }

    }

    class NoteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val textNote = itemView.findViewById<TextView>(R.id.noteTitle)
        val textContent = itemView.findViewById<TextView>(R.id.noteContent)
    }


}
