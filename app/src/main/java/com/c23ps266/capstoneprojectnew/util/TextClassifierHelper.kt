package com.c23ps266.capstoneprojectnew.util

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.label.Category
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.concurrent.ScheduledThreadPoolExecutor

class TextClassifierHelper(
    context: Context,
    private val modelDetail: ModelDetail,
    onLoadCompleted: TextClassifierHelper.() -> Unit,
) {
    private lateinit var labels: List<String>
    private lateinit var interpreter: Interpreter

    private val executor: ScheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(1)

    init {
        Log.d(TAG, "INIT")
        CoroutineScope(Dispatchers.IO).launch {
            val (modelFileName,  labelJson) = modelDetail
            labels = Gson().fromJson(loadJSONFromAsset(context, labelJson), Array<String>::class.java).toList()
            Log.d(TAG, "INIT LABELS: $labels")
            interpreter = Interpreter(loadModelFile(context, modelFileName)).also {
                val sigKey = it.signatureKeys[0]
                val input = it.getSignatureInputs(sigKey).joinToString(" - ")
                val output = it.getSignatureOutputs(sigKey).joinToString(" - ")
                Log.d(TAG, "INIT MODEL : sigKey = $sigKey | input = $input | output = $output")
            }
            CoroutineScope(Dispatchers.Main).launch { this@TextClassifierHelper.onLoadCompleted() }
        }
    }

    @Throws(IOException::class)
    private fun loadModelFile(context: Context, modelAssetFileName: String): MappedByteBuffer {
        val assetFileDescriptor = context.assets.openFd(modelAssetFileName)
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun classify(message: String, onResult: (result: List<Category>) -> Unit) = executor.execute {
        Log.d(TAG, "Message: $message")

        val inputs = message
        val outputs: Array<FloatArray> = arrayOf(FloatArray(labels.size))
        interpreter.run(inputs, outputs)

        val results = outputs[0].mapIndexed { index, fl ->
            Category(labels[index], fl)
        }
        CoroutineScope(Dispatchers.Main).launch { onResult(results) }
    }

    data class ModelDetail(
        val modelFileName: String,
        val labelJsonFileName: String,
        val inputMaxLen: Int,
    )

    companion object {
        private const val TAG = "TextClassifierHelper"

        @Throws(IOException::class)
        private fun loadJSONFromAsset(context: Context, filename: String): String {
            val inputStream = context.assets.open(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            return String(buffer)
        }
    }
}
