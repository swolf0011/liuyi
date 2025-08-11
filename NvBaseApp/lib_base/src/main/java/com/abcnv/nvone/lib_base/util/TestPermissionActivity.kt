//package com.abcnv.nvone.lib_base.util
//
//class TestPermissionActivity : AppCompatActivity() {
//    private lateinit var viewBinding: ActivityMainBinding
//
//    private val ps = if (NvPermissionUtil.isTIRAMISU_33) {
//        arrayOf(
//        Manifest.permission.READ_MEDIA_AUDIO,
//        Manifest.permission.READ_MEDIA_IMAGES,
//        Manifest.permission.READ_MEDIA_VIDEO,
//        Manifest.permission.RECORD_AUDIO,
//        Manifest.permission.FOREGROUND_SERVICE
//    )
//    } else {
//        arrayOf(
//        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        Manifest.permission.READ_EXTERNAL_STORAGE,
//        Manifest.permission.RECORD_AUDIO,
//        Manifest.permission.FOREGROUND_SERVICE
//    )
//    }
//    private val registerForActivityResult =
//        NvPermissionUtil.registerRequestMultiplePermissions(this, ps) {
//           //有权限后的操作。
//        }
//    private val activityResultLauncher = NvPermissionUtil.registerStartActivityForResult(this) {
//        registerForActivityResult.launch(ps)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
////        setContentView(R.layout.activity_main)
//        viewBinding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(viewBinding.root)
////
//        if (NvPermissionUtil.isR_30 && !NvPermissionUtil.isExternalStorageManager()) {
//            NvPermissionUtil.externalStorageManager(activityResultLauncher)
//        }else{
//            registerForActivityResult.launch(ps)
//        }
//    }
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK && viewBinding.webview.canGoBack()) {
//            viewBinding.webview.goBack()
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }
//}