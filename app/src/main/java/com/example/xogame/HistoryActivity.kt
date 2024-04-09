package com.example.xogame

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerView = findViewById(R.id.recyclerView)
        backButton = findViewById(R.id.backButton)

        // สร้าง RecyclerView Adapter และกำหนดให้แสดงข้อมูลประวัติการเล่นที่ดึงมาจาก Room Database
        val adapter = GameHistoryAdapter() // สร้าง GameHistoryAdapter
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        backButton.setOnClickListener {
            finish() // ปิด Activity ปัจจุบันเพื่อย้อนกลับไปยัง Activity ก่อนหน้า
        }
    }


}
