package com.jdw.diet_memo

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btn)

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

                val database = Firebase.database
                val myRef = database.getReference("memo")

                myRef.push().setValue(dateModel)
            }
        }

    }
}