package com.example.xogame


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class BoardActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var cells: Array<String?>
    private var currentPlayer: String = "X"
    private var scoreX = 0
    private var scoreO = 0
    private lateinit var currentPlayerTextView: TextView
    private lateinit var scoreTextView: TextView
    private var playWithBot = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        val appContext = applicationContext

        gridLayout = findViewById(R.id.gridLayout)

        val size = intent.getIntExtra("GRID_SIZE", 3)
        playWithBot = intent.getBooleanExtra("PLAY_WITH_BOT", false)
        createGrid(size)

        currentPlayerTextView = findViewById(R.id.currentPlayerTextView)
        scoreTextView = findViewById(R.id.scoreTextView)

        val homeButton: Button = findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            homeButton()
        }
        val historyButton = findViewById<Button>(R.id.history_button)
        historyButton.setOnClickListener {
            showHistory(it)
        }
    }

    fun showHistory(view: View) {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    private fun homeButton() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun botMove() {
        val emptyCells = mutableListOf<Int>()
        for (i in cells.indices) {
            if (cells[i].isNullOrEmpty()) {
                emptyCells.add(i)
            }
        }

        if (emptyCells.isNotEmpty()) {
            val index = emptyCells.random()
            val button = gridLayout.getChildAt(index) as Button
            button.performClick()
        }
    }

    private fun switchPlayer() {
        currentPlayer = if (currentPlayer == "X") "O" else "X"
        currentPlayerTextView.text = "Next Player is $currentPlayer"

        if (playWithBot && currentPlayer == "O") {
            Handler(Looper.getMainLooper()).postDelayed({
                botMove()
            }, 1000)
        }
    }

    private fun resetGame() {
        cells.fill(null)
        currentPlayer = "X"

        for (i in 0 until gridLayout.childCount) {
            (gridLayout.getChildAt(i) as Button).text = ""
        }
        scoreX = 0
        scoreO = 0
        scoreTextView.text = "X: $scoreX - O: $scoreO"

        Toast.makeText(this, "Reset Game!", Toast.LENGTH_SHORT).show()

        if (Random.nextBoolean()) {
            currentPlayer = "O"
            switchPlayer() // ให้บอทเริ่มเล่นถ้าเลือกได้ว่า O เริ่มก่อน
        }
    }

    private fun createGrid(size: Int) {
        cells = Array(size * size) { null }
        val screenWidth = resources.displayMetrics.widthPixels
        val screenDensity = resources.displayMetrics.density
        val buttonMargin = (5 * screenDensity).toInt()
        val buttonSize = (screenWidth / size) - (buttonMargin * 2)

        gridLayout.apply {
            removeAllViews()
            columnCount = size
            rowCount = size

            for (i in 0 until size * size) {
                val button = Button(this@BoardActivity).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = buttonSize
                        height = buttonSize
                        setMargins(buttonMargin)
                        columnSpec = GridLayout.spec(i % size)
                        rowSpec = GridLayout.spec(i / size)
                    }
                    textSize = buttonSize / 3 / screenDensity
                    setOnClickListener { onGridButtonClick(this, size) }
                }
                addView(button)
            }
        }
    }

    private fun onGridButtonClick(button: Button, size: Int) {
        val index = gridLayout.indexOfChild(button)

        if (cells[index].isNullOrEmpty()) {
            cells[index] = currentPlayer
            button.text = currentPlayer
            if (checkForWin(size)) {
                handleWin()
            } else if (checkForDraw()) {
                handleDraw()
            } else {
                switchPlayer()
            }
        }
    }

    private fun handleWin() {
        showAlert("Player $currentPlayer wins!")
        if (currentPlayer == "X") {
            scoreX++
        } else {
            scoreO++
        }
        scoreTextView.text = "X: $scoreX - O: $scoreO"
        insertGameHistory(currentPlayer, scoreX, scoreO)
        disableGridButtons()
        resetGameAfterDelay()
    }

    private fun handleDraw() {
        showAlert("The game is a draw!")
        insertGameHistory("Draw", scoreX, scoreO)
        disableGridButtons()
        resetGameAfterDelay()
    }
    private fun disableGridButtons() {
        for (i in 0 until gridLayout.childCount) {
            gridLayout.getChildAt(i).isEnabled = false
        }
    }
    private fun insertGameHistory(winner: String, scoreX: Int, scoreO: Int) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val currentTime = timeFormat.format(Date())
        val gameHistory = GameHistory(winner = winner, date = currentDate, time = currentTime, scoreX = scoreX, scoreO = scoreO)
        CoroutineScope(Dispatchers.IO).launch {
            val db = GameHistoryDatabase.getDatabase(applicationContext)
            db.gameHistoryDao().insertGameHistory(gameHistory)
        }
    }


    private fun resetGameAfterDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            resetGame()
        }, 5000)
    }

    private fun showAlert(s: String) {
        Toast.makeText(this@BoardActivity, s, Toast.LENGTH_SHORT).show()
    }


    private fun checkForWin(size: Int): Boolean {
        for (i in 0 until size) {
            if ((0 until size).all { cells[i * size + it] == currentPlayer }) return true
            if ((0 until size).all { cells[it * size + i] == currentPlayer }) return true
        }

        if ((0 until size).all { cells[it * size + it] == currentPlayer }) return true
        if ((0 until size).all { cells[it * size + (size - it - 1)] == currentPlayer }) return true
        return false
    }

    private fun checkForDraw(): Boolean {
        return cells.all { !it.isNullOrEmpty() }
    }
}
