package com.paper.webscrapper

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

import kotlin.concurrent.thread

class PdfViewerFragment : Fragment() {
    var vista : View? = null
    var pdfUrl: String? = null
    var ARG_URL = "argUrl"
    var webView: WebView? = null
    var progressBar: ConstraintLayout? = null

    fun newInstance(url: String): Fragment {
        var fragment : Fragment = PdfViewerFragment()
        var args : Bundle = Bundle()

        args.putString(ARG_URL, url)
        fragment.arguments = args

        return fragment
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vista = inflater.inflate(R.layout.fragment_pdf_viewer, container, false)

        webView = vista?.findViewById(R.id.wvPdfViewer)
        progressBar = vista?.findViewById(R.id.pbLayoutPDF)


            webView?.settings?.javaScriptEnabled = true
            webView?.settings?.builtInZoomControls = true
            webView?.settings?.displayZoomControls = false
            webView?.webChromeClient = WebChromeClient()

            if (arguments != null){
                pdfUrl = arguments?.getString(ARG_URL)
                //Toast.makeText(context, pdfUrl, Toast.LENGTH_LONG).show()
            }

            webView?.webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    progressBar?.visibility = View.VISIBLE
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView, url: String) {
                    webView?.loadUrl(
                        "javascript:(function() { " +
                                "document.querySelector('[role=\"toolbar\"]').remove();})()"
                    )

                    progressBar?.visibility = View.GONE
                    webView?.visibility = View.VISIBLE
                }
            }

            webView?.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=$pdfUrl")

        return vista
    }


}



