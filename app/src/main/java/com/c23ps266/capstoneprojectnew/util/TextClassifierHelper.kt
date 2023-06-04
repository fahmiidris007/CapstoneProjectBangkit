package com.c23ps266.capstoneprojectnew.util

import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.label.Category
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class TextClassifierHelper : DefaultLifecycleObserver {
    private lateinit var labels: List<String>
    private lateinit var interpreter: Interpreter
    private lateinit var initJob: Job

    val id = counter++
    val modelDetail: ModelDetail

    /**
     * Initialization is done asynchronously.
     * If initialization is unfinished at the time of TextClassifierHelper.classify function called, It will
     * block that initialization at that function before continuing.
     * You can use the other overload if you want to be immediately notified when initialization finished.
     */
    constructor(modelDetail: ModelDetail, context: Context) {
        Log.i(TAG, "($id) INIT")
        this.modelDetail = modelDetail
        init(context)
    }

    /**
     * Initialization is done asynchronously.
     * onLoadCompleted will be called at the time initialization completed.
     * Call this constructor only from onStart or after! If you call it before onStart, it might crash your app!
     * @param onLoadCompleted will not be called if lifecycle reached DESTROYED state before loading complete
     * @param lifecycle lifecycle to be observed
     */
    constructor(
        modelDetail: ModelDetail,
        context: Context,
        lifecycle: Lifecycle,
        onLoadCompleted: TextClassifierHelper.() -> Unit,
    ) {
        Log.i(TAG, "($id) INIT")
        this.modelDetail = modelDetail
        lifecycle.addObserver(this)
        init(context, onLoadCompleted)
    }

    private fun init(
        context: Context,
        onLoadCompleted: (TextClassifierHelper.() -> Unit)? = null,
    ) {
        initJob = CoroutineScope(Dispatchers.IO).launch {
            val (modelFileName, labelJson) = modelDetail

            labels = Gson().fromJson(loadJSONFromAsset(context, labelJson), Array<String>::class.java).toList()
            Log.i(TAG, "($id) INIT Labels: $labels")

            interpreter = Interpreter(loadModelFile(context, modelFileName)).also {
                val sigKey = it.signatureKeys[0]
                val input = it.getSignatureInputs(sigKey).joinToString(" - ")
                val output = it.getSignatureOutputs(sigKey).joinToString(" - ")
                Log.i(TAG, "($id) INIT Model : sigKey = $sigKey | input = $input | output = $output")
            }

            Log.i(TAG, "($id) INIT Completed")
            if (isActive && onLoadCompleted != null) {
                CoroutineScope(Dispatchers.Main).launch { this@TextClassifierHelper.onLoadCompleted() }
            }
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

    fun classify(message: String, onResult: (result: List<Category>) -> Unit) =
        CoroutineScope(Dispatchers.Default).launch {
            if (initJob.isActive) {
                Log.i(TAG, "waiting for init job to complete...")
                initJob.join()
            }

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


    override fun onDestroy(owner: LifecycleOwner) {
        if (!initJob.isCompleted) {
            initJob.cancel(CancellationException("observed lifecycle ends"))
            Log.i(TAG, "($id) INIT Job cancelled")
            owner.lifecycle.removeObserver(this)
        }
    }

    companion object {
        private const val TAG = "TextClassifierHelper"
        private var counter: Int = 0

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
