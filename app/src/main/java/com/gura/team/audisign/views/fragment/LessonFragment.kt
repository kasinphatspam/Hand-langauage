package com.gura.team.audisign.views.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gura.team.audisign.R
import com.gura.team.audisign.model.Vocab
import com.gura.team.audisign.views.LessonActivity
import com.squareup.picasso.Picasso
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val CATEGORY_PARAM = "category"

/**
 * A simple [Fragment] subclass.
 * Use the [LessonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LessonFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var vocab: Vocab? = null
    private var id: Int = 1
    private var max: Int = 1000

    interface ButtonClickListener {
        fun onClick(i: Int)
    }

    val STT_CODE = 999
    private lateinit var listener: ButtonClickListener
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lesson, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val example: TextView = view.findViewById(R.id.example)
        val webView: WebView = view.findViewById(R.id.webView)
        val nextImageButton: ImageButton = view.findViewById(R.id.nextImageButton)
        val previousImageButton: ImageButton = view.findViewById(R.id.beforeImageButton)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val hearImageButton: ImageButton = view.findViewById(R.id.hearImageButton)

        textToSpeech = TextToSpeech(context, {
            if(it == TextToSpeech.SUCCESS){
                textToSpeech.language = Locale.forLanguageTag("th")
            }else{
                Log.d("TextToSpeech", "ERROR")
            }
        },"com.google.android.tts")


        hearImageButton.setOnClickListener {
            val text = vocab!!.vocab
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1")
        }
        val video =
            "<iframe width=\"100%\" height=\"100%\" src=\"${vocab!!.link}\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        webView.loadData(video, "text/html", "utf-8")
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
        example.text = vocab!!.vocab

        Picasso.with(context)
            .load(vocab!!.image)
            .placeholder(R.color.black)
            .into(imageView)

        if(id == max){
            nextImageButton.visibility = View.INVISIBLE
        }

        if(id == 1) {
            previousImageButton.visibility = View.INVISIBLE
        }

        nextImageButton.setOnClickListener {
            listener.onClick(1)
        }

        previousImageButton.setOnClickListener {
            listener.onClick(0)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as ButtonClickListener
    }

    fun newInstance(vocab: Vocab, id: Int, max: Int) {
        this.max = max
        this.id = id
        this.vocab = vocab
    }
}