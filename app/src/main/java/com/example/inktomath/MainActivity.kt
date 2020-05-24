package com.example.inktomath

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.divyanshu.draw.widget.DrawView
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var drawView: DrawView? = null
    private var clearButton: Button? = null
    private var classifyButton: Button? = null
    private var predictedTextView: TextView? = null
    private var equationText: TextView? = null
    private var digitClassifier = SymbolClassifier(this)
    private var calculate_button: Button? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup view instances
        drawView = findViewById(R.id.draw_view)
        drawView?.setStrokeWidth(55.0f)
        drawView?.setColor(Color.WHITE)
        drawView?.setBackgroundColor(Color.BLACK)
        clearButton = findViewById(R.id.clear_button)
        predictedTextView = findViewById(R.id.predicted_text)
        classifyButton = findViewById(R.id.classify_button)
        equationText = findViewById(R.id.equation_text)
        calculate_button = findViewById(R.id.calculate_button)


        var eqn = ""

        // Setup clear drawing button
        clearButton?.setOnClickListener {
            drawView?.clearCanvas()
            eqn = ""
            predictedTextView?.text = getString(R.string.tfe_dc_prediction_text_placeholder);

        }

        drawView?.setOnTouchListener { _, event ->
            drawView?.onTouchEvent(event)

            if (event.action == MotionEvent.ACTION_UP) {
                classifyDrawing()
            }

            true
        }

        classifyButton?.setOnClickListener {
            val symbol = classifyDrawing();
            if(symbol == "-1"){
                predictedTextView?.text = "Invalid Drawing Trying again"
            }else{
                eqn += getOperator(symbol)
                predictedTextView?.text = eqn
            }
            drawView?.clearCanvas()
        }


        calculate_button?.setOnClickListener {
            try {
                var result: Double;
                val e : Expression = ExpressionBuilder(eqn).build()
                result = e.evaluate();
                eqn = "$eqn = ${result.toInt()}"
                predictedTextView?.text = eqn
            }catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
        }

        // Setup digit classifier
        digitClassifier.initializeInterpreter();
    }

    private  fun getOperator(s: String): String{
        if(s == "10"){
            return "-";
        }else if (s == "11"){
            return "+";
        }else if (s == "12"){
            return "*";
        }
        return s;
    }

    private fun classifyDrawing() : String {
        val bitmap = drawView?.getBitmap()

        var string = "not classifed yet";
        if ((bitmap != null) && (digitClassifier.isInitialized)) {
            string = digitClassifier.classify(bitmap);
            Log.d(TAG, string);

        }
        return string;
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    fun eval(str: String) : Int{

        var posa = -1
        var poss = -1
        var posm = -1
        var pos = -1

        for(i in 0..str.length-1)
        {
            if(str.get(i) == '+')
            {
                posa = i
                pos = i
                break
            }

            if(str.get(i) == '*')
            {
                posm = i
                pos = i
                break
            }

            if(str.get(i) == '-')
            {
                poss = i
                pos = i
                break
            }
        }

        var n1 = 0
        var n2 = 0

        n1 = (str.substring(0..pos - 1)).toInt()
        n2 = (str.substring(pos + 1..str.length - 1)).toInt()

        var ans : Int = 0

        if(posa != -1)
            ans = n1 + n2
        else if(posm != -1)
            ans = n1*n2
        else
            ans = n1 - n2

        return ans;

    }
}