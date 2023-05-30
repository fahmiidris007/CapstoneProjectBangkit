package com.c23ps266.capstoneprojectnew.util

import android.content.Context
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier.createFromFileAndOptions
import java.util.concurrent.ScheduledThreadPoolExecutor

// https://www.tensorflow.org/lite/inference_with_metadata/task_library/nl_classifier
class NLClassifierHelper(
    context: Context,
    tfLiteAssetName: String,
    private val listener: TextResultsListener,
) {
    private val classifier: NLClassifier
    private val executor: ScheduledThreadPoolExecutor

    init {
        val baseOptions = BaseOptions.builder().run {
            useNnapi()          // or should I not?
            build()
        }
        val options = NLClassifier.NLClassifierOptions.builder().run {
            setBaseOptions(baseOptions)
            build()
        }
        classifier = createFromFileAndOptions(context, tfLiteAssetName, options)

        executor = ScheduledThreadPoolExecutor(1)
    }

    /**
     * Runs in other thread using ScheduledThreadPoolExecutor
     */
    fun classify(text: String) = executor.execute {
        if (text.isBlank()) {
            listener.onError("Text can't be blank!")
        } else {
            val results = classifier.classify(text)
            listener.onResult(results)
        }
    }

    interface TextResultsListener {
        /**
         * If you want to update UI from this function, you need to do that from Activity#runOnUiThread method
         */
        fun onResult(results: List<Category>)

        /**
         * If you want to update UI from this function, you need to do that from Activity#runOnUiThread method
         */
        fun onError(message: String)
    }
}
