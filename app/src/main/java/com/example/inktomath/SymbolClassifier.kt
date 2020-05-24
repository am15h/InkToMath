package com.example.inktomath

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.flex.FlexDelegate
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class DigitClassifier(private val context: Context) {
    private var interpreter: Interpreter? = null
    var isInitialized = false
        private set

    private var inputImageWidth: Int = 28 // will be inferred from TF Lite model
    private var inputImageHeight: Int = 28 // will be inferred from TF Lite model
    private var modelInputSize = FLOAT_TYPE_SIZE * inputImageWidth * inputImageHeight * PIXEL_SIZE
// will be inferred from TF Lite model

    public fun initializeInterpreter() {
        // Load the TF Lite model

        val assetManager = context.assets
        val model = loadModelFile(assetManager)

        val delegate = FlexDelegate();
        val options = Interpreter.Options().addDelegate(delegate)
        val interpreter = Interpreter(model, options)


        // Read input shape from model file
        val inputTensor = interpreter.getInputTensor(0)
        val outputTensor = interpreter.getOutputTensor(0)

        val tensor = "TENSOR"

        // Finish interpreter initialization
        this.interpreter = interpreter
        isInitialized = true
        Log.d(TAG, "Initialized TFLite interpreter.")
    }

    private fun loadModelFile(assetManager: AssetManager): ByteBuffer {
        val fileDescriptor = assetManager.openFd(MODEL_FILE)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    public fun classify(bitmap: Bitmap): String {
        if (!isInitialized) {
            throw IllegalStateException("TF Lite Interpreter is not initialized yet.")
        }

        var startTime: Long
        var elapsedTime: Long

        startTime = System.nanoTime()
        val resizedImage = Bitmap.createScaledBitmap(bitmap, inputImageWidth, inputImageHeight, true)
        Log.d(TAG, "DIMS: ${resizedImage.width} ${resizedImage.height}")

        val byteBuffer = convertBitmapToByteBuffer(resizedImage)

        Log.d(TAG, "BYTE BUFFER: ${byteBuffer.capacity().toString()}")

        elapsedTime = (System.nanoTime() - startTime) / 1000000
        Log.d(TAG, "Preprocessing time = " + elapsedTime + "ms")

        startTime = System.nanoTime()
        val result = Array(1) { FloatArray(OUTPUT_CLASSES_COUNT) }
        try {
            interpreter?.run(byteBuffer, result)
        }catch (e: Exception){
            Log.d("EXCEPTION INTERPRETER", e.toString());
        }
        elapsedTime = (System.nanoTime() - startTime) / 1000000
        Log.d(TAG, "Inference time = " + elapsedTime + "ms")

        Log.d(TAG, "Result =${result.contentDeepToString()}")


        return getOutputString(result[0])
    }

    private fun twoDPixelArray(bitmap: Bitmap): Array<Array<Float>>{
        val pixels = IntArray(inputImageWidth * inputImageHeight)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixelsBitmap: Array<Array<Float>> = Array(inputImageWidth){ Array(inputImageHeight){0.0f} }

        for(i in 0 until inputImageWidth){
            for(j in 0 until inputImageHeight){
                pixelsBitmap[i][j] = normalize(pixels[(j * inputImageWidth) + i])
            }
        }
        return pixelsBitmap
    }

    private fun normalize(pixelValue: Int): Float{
        val r = (pixelValue shr 16 and 0xFF)
        val g = (pixelValue shr 8 and 0xFF)
        val b = (pixelValue and 0xFF)

        return (r + g + b) / 3.0f / 255.0f
    }
    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(modelInputSize)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(inputImageWidth * inputImageHeight)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (pixelValue in pixels) {
            val r = (pixelValue shr 16 and 0xFF)
            val g = (pixelValue shr 8 and 0xFF)
            val b = (pixelValue and 0xFF)

            // Convert RGB to grayscale and normalize pixel value to [0..1]
            val normalizedPixelValue = ( r + g + b) / 3.0f / 255.0f
            byteBuffer.putFloat(normalizedPixelValue)
        }

        return byteBuffer
    }

    private fun getOutputString(output: FloatArray): String {
        var maxIndex = output.indices.maxBy { output[it] } ?: -1
        if (output[maxIndex] < 0.5){
            maxIndex = -1
        }
        return maxIndex.toString()
//        return "Prediction Result: %d\nConfidence: %2f".format(maxIndex, output[maxIndex])
    }

    companion object {
        private const val TAG = "DigitClassifier"

        private const val MODEL_FILE = "mnist_final_4.tflite"

        private const val FLOAT_TYPE_SIZE = 4
        private const val PIXEL_SIZE = 1

        private const val OUTPUT_CLASSES_COUNT = 13
    }
}