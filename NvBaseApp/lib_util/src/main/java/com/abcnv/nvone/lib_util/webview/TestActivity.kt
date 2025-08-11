//package com.abcnv.nvone.lib_util.webview
//
//
//class TestActivity : AppCompatActivity() {
//    private lateinit var viewBinding: ActivityMainBinding
//    private val url = "file:///android_asset/index.html"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        viewBinding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(viewBinding.root)
//
//        initWebView()
//        initView()
//    }
//
//    var isRecording = false
//    private fun initWebView() {
//        val nvObj = NvObj() {
//            val ls = it.split(",")
//            if (ls.size >= 1) {
//                val code = ls[0]
//                when (code) {
//                    "1001" -> {
//                        if (!isRecording) {
//                            isRecording = true
//                        }
//                    }
//                    "1002" -> {
//                        if (isRecording) {
//                            isRecording = false
//                        }
//                    }
//                }
//            }
//            showToast("js发送点击过来的信息：" + it)
//        }
//        NvWebViewUtil.initWebView(viewBinding.webview, nvObj, "jsObj")
//        viewBinding.webview.loadUrl(url)
//    }
//
//    private fun showToast(msg: String) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//    }
//
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK && viewBinding.webview.canGoBack()) {
//            viewBinding.webview.goBack()
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }

//}