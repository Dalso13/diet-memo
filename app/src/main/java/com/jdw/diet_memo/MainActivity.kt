package com.jdw.diet_memo

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val  dateModels = mutableListOf<DateModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btn)

        val database = Firebase.database
        val myRef = database.getReference("memo").child(Firebase.auth.currentUser!!.uid)

        val listView = findViewById<ListView>(R.id.listView)
        val adapter = ListViewAdapter(dateModels)
        listView.adapter = adapter

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 비워주기
                dateModels.clear()

                dataSnapshot.children.forEach { e ->
                    // 추가
                    val date = e.getValue() as Map<*, *>

                    val model = DateModel(
                        date["date"].toString(),
                        date["memo"].toString()
                    )

                    dateModels.add(model)

                }
                // 비동기로 가져오기에 갱신을 해주기
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



        btn.setOnClickListener {
            val dialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
            val builder = AlertDialog.Builder(this)
                .setView(dialog)
                .setTitle("운동 메모 다이얼로그")

            val alertDialog = builder.show()

            val dateBtn = alertDialog.findViewById<Button>(R.id.btn)

            var dateText = ""

            dateBtn?.setOnClickListener {

                val today = GregorianCalendar()
                val year : Int = today.get(Calendar.YEAR)
                val month : Int = today.get(Calendar.MONTH)
                val date : Int = today.get(Calendar.DATE)

                val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener{
                    override fun onDateSet(
                        view: DatePicker?,
                        year: Int,
                        month: Int,
                        dayOfMonth: Int
                    ) {
                        dateText = "${year}.${month}.${date}"
                    }
                }, year, month, date)

                dlg.show()
            }

            val saveBtn = alertDialog.findViewById<Button>(R.id.saveBtn)
            saveBtn?.setOnClickListener {

                val memo = alertDialog.findViewById<EditText>(R.id.memo)?.text.toString()

                val dateModel = DateModel(
                    dateText,
                    memo
                )

                myRef.push().setValue(dateModel)

                alertDialog.dismiss()
            }
        }

    }
}