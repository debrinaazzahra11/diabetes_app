package com.example.diabetes

import android.content.res.AssetManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import android.util.Log

class SimulasiFragment : Fragment() {

    private lateinit var editGender: EditText
    private lateinit var editAge: EditText
    private lateinit var editHypertension: EditText
    private lateinit var editHeartDisease: EditText
    private lateinit var editSmokingHistory: EditText
    private lateinit var editBMI: EditText
    private lateinit var editHbA1cLevel: EditText
    private lateinit var editBloodGlucoseLevel: EditText
    private lateinit var btnCheck: Button
    private lateinit var txtResult: TextView

    private var interpreter: Interpreter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_simulasi, container, false)

        // Initialize UI elements
        editGender = view.findViewById(R.id.editGender)
        editAge = view.findViewById(R.id.editAge)
        editHypertension = view.findViewById(R.id.editHypertension)
        editHeartDisease = view.findViewById(R.id.editHeartDisease)
        editSmokingHistory = view.findViewById(R.id.editSmokingHistory)
        editBMI = view.findViewById(R.id.editBMI)
        editHbA1cLevel = view.findViewById(R.id.editHbA1cLevel)
        editBloodGlucoseLevel = view.findViewById(R.id.editBloodGlucoseLevel)
        btnCheck = view.findViewById(R.id.btnCheck)
        txtResult = view.findViewById(R.id.txtResult)

        // Load TFLite model
        try {
            interpreter = Interpreter(loadModelFile(requireContext().assets, "diabetes.tflite"))
        } catch (e: IOException) {
            Log.e("SimulasiFragment", "Error loading model", e)
            txtResult.text = "Error loading model: ${e.message}"
        }

        btnCheck.setOnClickListener {
            performPrediction()
        }

        return view
    }

    private fun performPrediction() {
        if (interpreter == null) {
            txtResult.text = "Model not loaded"
            return
        }

        val gender = editGender.text.toString().toFloatOrNull() ?: 0f
        val age = editAge.text.toString().toFloatOrNull() ?: 0f
        val hypertension = editHypertension.text.toString().toFloatOrNull() ?: 0f
        val heartDisease = editHeartDisease.text.toString().toFloatOrNull() ?: 0f
        val smokingHistory = editSmokingHistory.text.toString().toFloatOrNull() ?: 0f
        val bmi = editBMI.text.toString().toFloatOrNull() ?: 0f
        val hba1cLevel = editHbA1cLevel.text.toString().toFloatOrNull() ?: 0f
        val bloodGlucoseLevel = editBloodGlucoseLevel.text.toString().toFloatOrNull() ?: 0f

        val input = floatArrayOf(gender, age, hypertension, heartDisease, smokingHistory, bmi, hba1cLevel, bloodGlucoseLevel)
        val output = Array(1) { FloatArray(2) }
        interpreter?.run(arrayOf(input), output)

        // Assuming the second value in the output array represents the probability of being diabetic
        val resultText = if (output[0][1] > 0.5) "Pasien diabetes" else "Pasien tidak diabetes"
        txtResult.text = "$resultText"
    }

    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}
