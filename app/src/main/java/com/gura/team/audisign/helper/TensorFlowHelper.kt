package com.gura.team.audisign.helper

import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.util.Log
import com.gura.team.audisign.ml.Model
import com.gura.team.audisign.ml.Model1
import com.gura.team.audisign.ml.Model2
import com.gura.team.audisign.ml.Model3
import com.gura.team.audisign.ml.Model4
import com.gura.team.audisign.model.PredictionResult
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.min

class TensorFlowHelper(context: Context) {

    private val size: Int = 224
    private lateinit var bitmap: Bitmap

    private val model: Model
    private val model1: Model1
    private val model2: Model2
    private val model3: Model3
    private val model4: Model4
    private val inputFeature: TensorBuffer
    private val byteBuffer: ByteBuffer
    private val labels1: List<String>
    private val labels2: List<String>

    init {
        model = Model.newInstance(context)
        model1 = Model1.newInstance(context)
        model2 = Model2.newInstance(context)
        model3 = Model3.newInstance(context)
        model4 = Model4.newInstance(context)
        inputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        byteBuffer = ByteBuffer.allocateDirect(4 * size * size * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        labels1 = FileUtil.loadLabels(context, "labels1.txt")
        labels2 = FileUtil.loadLabels(context, "labels2.txt")
    }

    fun predict(): PredictionResult {
        val output = ArrayList<PredictionResult>()
        val score = IntArray(labels1.size) { _ -> 0 }
        output.add(predictModel1())
        output.add(predictModel2())
        output.add(predictModel3())
        output.add(predictModel4())

        Log.d("Predict", "------------------")
        Log.d("Predict", output[0].classes)
        Log.d("Predict", output[1].classes)
        Log.d("Predict", output[2].classes)
        Log.d("Predict", output[3].classes)

        for(j in output.indices){
            for(i in labels1.indices) {
                if(labels1[i] == output[j].classes){
                    score[i] += 1
                    break
                }
            }
        }

        var max = 0

        for(i in labels1.indices) {
            if(max < score[i]) {
                max = i
            }
        }

        if(score[max] == 1) {
            return PredictionResult("", 0.00f)
        }
        var sumConfident = 0.0
        var count = 0
        for (i in output) {
            if(labels1[max] == i.classes) {
                sumConfident+=i.confident
                count++
            }
        }

        val confident = (sumConfident/count).toFloat()
        return PredictionResult(labels1[max], confident)
    }

    private fun predictModel1(): PredictionResult {
        // Runs model inference and gets result.
        val outputs: Model1.Outputs = model1.process(inputFeature)
        val outputFeature0: TensorBuffer = outputs.outputFeature0AsTensorBuffer
        val confidences = outputFeature0.floatArray

        // find the index of the class with the biggest confidence.
        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = labels1[maxPos]
        val confident = maxConfidence
        return PredictionResult(classes, confident)
    }

    private fun predictModel2(): PredictionResult {
        // Runs model inference and gets result.
        val outputs: Model2.Outputs = model2.process(inputFeature)
        val outputFeature0: TensorBuffer = outputs.outputFeature0AsTensorBuffer
        val confidences = outputFeature0.floatArray

        // find the index of the class with the biggest confidence.
        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = labels1[maxPos]
        val confident = maxConfidence
        return PredictionResult(classes, confident)
    }

    private fun predictModel3(): PredictionResult {
        // Runs model inference and gets result.
        val outputs: Model3.Outputs = model3.process(inputFeature)
        val outputFeature0: TensorBuffer = outputs.outputFeature0AsTensorBuffer
        val confidences = outputFeature0.floatArray

        // find the index of the class with the biggest confidence.
        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = labels1[maxPos]
        val confident = maxConfidence
        return PredictionResult(classes, confident)
    }

    private fun predictModel4(): PredictionResult {
        // Runs model inference and gets result.
        val outputs: Model4.Outputs = model4.process(inputFeature)
        val outputFeature0: TensorBuffer = outputs.outputFeature0AsTensorBuffer
        val confidences = outputFeature0.floatArray

        // find the index of the class with the biggest confidence.
        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = labels2[maxPos]
        val confident = maxConfidence
        return PredictionResult(classes, confident)
    }

    fun load(bitmap: Bitmap) {
        val bitmapBuffer = resize(bitmap)

        val intValues = IntArray(size * size)
        bitmapBuffer.getPixels(
            intValues,
            0,
            bitmapBuffer.width,
            0,
            0,
            bitmapBuffer.width,
            bitmapBuffer.height
        )

        // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
        var pixel = 0
        for (i in 0 until size) {
            for (j in 0 until size) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255f))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 255f))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 255f))
            }
        }
        inputFeature.loadBuffer(byteBuffer)

        this.bitmap = bitmapBuffer
    }

    fun getLabels(): List<String> {
        return labels1
    }

    fun stop() {
        model.close()
    }

    private fun resize(bitmap: Bitmap): Bitmap {
        val dimension: Int = min(bitmap.width, bitmap.height)
        val thumbnail = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension)
        return Bitmap.createScaledBitmap(thumbnail, size, size, false)
    }
}