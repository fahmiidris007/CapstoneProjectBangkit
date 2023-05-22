package com.c23ps266.capstoneprojectnew.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.tensorflow.lite.support.label.Category

@RunWith(AndroidJUnit4::class)
class TextClassifierHelperTest {
    // stolen from https://www.tensorflow.org/lite/examples/text_classification/overview
    // https://storage.googleapis.com/download.tensorflow.org/models/tflite/text_classification/text_classification_v2.tflite
    private val modelName = "dummy_movie_review.tflite"

    // https://www.imdb.com/review/rw4085161/?ref_=ext_shr_lnk
    private val testText = """A Silent Voice Is more than just a really good high schol romantic drama. It is a captivating love story that deals with depression and friendship in a remarkably relatable way.

Without spoiling too much. I just really appreciate the believably of most of the film. Unlike Your Name where I found some parts to be overly cheesy or slow paced. A Silent Voice offers up nice character development and introduces a whole array of interesting side characters to the plot.

I do really like the idea of having a deaf friend. I've often thought about what that would be like and the whole learning sign language and writing on a book to communicate. It offers a-not often seen- perspective of that life. I found it quite invigorating and motivating to step up my own life."""

    private val testTextNegativeScore = 0.115879305f
    private val testTextPositiveScore = 0.8841207f

    @Test
    fun textClassifierShouldSuccessAndHaveConsistentResult() {
        val textClassifierHelper = TextClassifierHelper(
            context = InstrumentationRegistry.getInstrumentation().context,
            tfLiteAssetName = modelName,
            listener = object : TextClassifierHelper.TextResultsListener {
                override fun onResult(results: List<Category>) {
                    val (negativeScore, positiveScore) = results.map { it.score }

                    assertEquals(negativeScore, testTextNegativeScore)
                    assertEquals(positiveScore, testTextPositiveScore)
                }

                override fun onError(message: String) {
                    // nothing
                }
            }
        )
        textClassifierHelper.classify(testText)
    }
}
