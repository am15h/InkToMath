package com.example.inktomath

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import com.divyanshu.draw.widget.DrawView

class MainActivity : AppCompatActivity() {

    private var drawView: DrawView? = null
    private var clearButton: Button? = null
    private var classifyButton: Button? = null
    private var predictedTextView: TextView? = null
    private var digitClassifier = DigitClassifier(this)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup view instances
        drawView = findViewById(R.id.draw_view)
        drawView?.setStrokeWidth(70.0f)
        drawView?.setColor(Color.WHITE)
        drawView?.setBackgroundColor(Color.BLACK)
        clearButton = findViewById(R.id.clear_button)
        predictedTextView = findViewById(R.id.predicted_text)
        classifyButton = findViewById(R.id.classify_button)

        // Setup clear drawing button
        clearButton?.setOnClickListener {
            drawView?.clearCanvas()
            predictedTextView?.text = getString(R.string.tfe_dc_prediction_text_placeholder);
        }

        // Setup classification trigger so that it classify after every stroke drew
        /*drawView?.setOnTouchListener { _, event ->
            // As we have interrupted DrawView's touch event,
            // we first need to pass touch events through to the instance for the drawing to show up
            drawView?.onTouchEvent(event)

            // Then if user finished a touch event, run classification
            if (event.action == MotionEvent.ACTION_UP) {
                classifyDrawing()
            }

            true
        }*/

        classifyButton?.setOnClickListener {
            predictedTextView?.text = classifyDrawing();
        }



        // Setup digit classifier
        digitClassifier.initializeInterpreter();
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
}