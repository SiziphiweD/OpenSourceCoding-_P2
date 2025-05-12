package com.example.opensourcepart2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class ExpenseManagerActivity : AppCompatActivity() {

    private lateinit var editCategoryName: EditText
    private lateinit var btnAddCategory: Button
    private lateinit var spinnerCategory: Spinner
    private lateinit var editDescription: EditText
    private lateinit var editAmount: EditText
    private lateinit var editDate: EditText
    private lateinit var editStartTime: EditText
    private lateinit var editEndTime: EditText
    private lateinit var btnPickPhoto: Button
    private lateinit var imagePreview: ImageView
    private lateinit var btnAddExpense: Button
    private lateinit var editMinGoal: EditText
    private lateinit var editMaxGoal: EditText
    private lateinit var btnSetGoals: Button
    private lateinit var editFilterStart: EditText
    private lateinit var editFilterEnd: EditText
    private lateinit var btnViewExpenses: Button
    private lateinit var textResults: TextView

    private val categoryList = mutableListOf<String>()
    private val expenses = mutableListOf<ExpenseData>()
    private var selectedImageUri: Uri? = null

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    private val photoPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            val inputStream = contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imagePreview.setImageBitmap(bitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_manager)

        // Bind views
        editCategoryName = findViewById(R.id.editCategoryName)
        btnAddCategory = findViewById(R.id.btnAddCategory)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        editDescription = findViewById(R.id.editDescription)
        editAmount = findViewById(R.id.editAmount)
        editDate = findViewById(R.id.editDate)
        editStartTime = findViewById(R.id.editStartTime)
        editEndTime = findViewById(R.id.editEndTime)
        btnPickPhoto = findViewById(R.id.btnPickPhoto)
        imagePreview = findViewById(R.id.imagePreview)
        btnAddExpense = findViewById(R.id.btnAddExpense)
        editMinGoal = findViewById(R.id.editMinGoal)
        editMaxGoal = findViewById(R.id.editMaxGoal)
        btnSetGoals = findViewById(R.id.btnSetGoals)
        editFilterStart = findViewById(R.id.editFilterStart)
        editFilterEnd = findViewById(R.id.editFilterEnd)
        btnViewExpenses = findViewById(R.id.btnViewExpenses)
        textResults = findViewById(R.id.textResults)

        // Setup
        updateCategorySpinner()

        btnAddCategory.setOnClickListener {
            val category = editCategoryName.text.toString().trim()
            if (category.isNotEmpty()) {
                categoryList.add(category)
                updateCategorySpinner()
                editCategoryName.text.clear()
            }
        }

        btnPickPhoto.setOnClickListener {
            photoPicker.launch("image/*")
        }

        editDate.setOnClickListener { pickDate(editDate) }
        editStartTime.setOnClickListener { pickTime(editStartTime) }
        editEndTime.setOnClickListener { pickTime(editEndTime) }
        editFilterStart.setOnClickListener { pickDate(editFilterStart) }
        editFilterEnd.setOnClickListener { pickDate(editFilterEnd) }

        btnAddExpense.setOnClickListener {
            val expense = ExpenseData(
                description = editDescription.text.toString(),
                category = spinnerCategory.selectedItem.toString(),
                amount = editAmount.text.toString().toDoubleOrNull() ?: 0.0,
                date = editDate.text.toString(),
                startTime = editStartTime.text.toString(),
                endTime = editEndTime.text.toString(),
                photoUri = selectedImageUri?.toString()
            )
            expenses.add(expense)
            Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show()
            clearExpenseInputs()
        }

        // View Expenses button logic
        btnViewExpenses.setOnClickListener {
            val start = editFilterStart.text.toString() // Get start date from user input
            val end = editFilterEnd.text.toString() // Get end date from user input

            // Filter the expenses by the date range entered
            val filtered = expenses.filter {
                it.date >= start && it.date <= end
            }

            // Group and sum expenses by category, and format them into a string
            val summary = filtered.groupBy { it.category }
                .map { (cat, list) ->
                    "$cat: R${list.sumOf { it.amount }}"
                }.joinToString("\n") // Join the summary as one string

            // Display the results in the textResults TextView
            textResults.text = summary.ifEmpty { "No expenses found in selected range." }
        }

        btnSetGoals.setOnClickListener {
            val min = editMinGoal.text.toString().toDoubleOrNull()
            val max = editMaxGoal.text.toString().toDoubleOrNull()
            if (min != null && max != null) {
                Toast.makeText(this, "Goals set: R$min - R$max", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickDate(target: EditText) {
        val cal = Calendar.getInstance()
        DatePickerDialog(this,
            { _, y, m, d ->
                cal.set(y, m, d)
                target.setText(dateFormatter.format(cal.time))
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun pickTime(target: EditText) {
        val cal = Calendar.getInstance()
        TimePickerDialog(this,
            { _, h, m ->
                cal.set(Calendar.HOUR_OF_DAY, h)
                cal.set(Calendar.MINUTE, m)
                target.setText(timeFormatter.format(cal.time))
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun updateCategorySpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter
    }

    private fun clearExpenseInputs() {
        editDescription.text.clear()
        editAmount.text.clear()
        editDate.text.clear()
        editStartTime.text.clear()
        editEndTime.text.clear()
        imagePreview.setImageDrawable(null)
        selectedImageUri = null
    }

    data class ExpenseData(
        val description: String,
        val category: String,
        val amount: Double,
        val date: String,
        val startTime: String,
        val endTime: String,
        val photoUri: String?
    )
}
