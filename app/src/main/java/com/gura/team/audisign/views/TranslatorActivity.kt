package com.gura.team.audisign.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gura.team.audisign.R
import com.gura.team.audisign.helper.PermissionHelper
import com.gura.team.audisign.helper.WindowHelper
import com.gura.team.audisign.viewmodels.AppViewModelFactory
import com.gura.team.audisign.viewmodels.TranslatorActivityViewModel


class TranslatorActivity : AppCompatActivity() {

    private lateinit var viewModel: TranslatorActivityViewModel
    private lateinit var factory: AppViewModelFactory

    private val permissionHelper = PermissionHelper(this)
    private lateinit var cameraManager: CameraManager
    private lateinit var captureRequest: CaptureRequest.Builder
    private lateinit var cameraDevice: CameraDevice
    private lateinit var windowHelper: WindowHelper
    private lateinit var textureView: TextureView
    private lateinit var handler: Handler

    private lateinit var progressBar: ProgressBar
    private lateinit var closeImageButton: ImageButton
    private lateinit var resultTextView: TextView
    private lateinit var confidentTextView: TextView
    private lateinit var rotateImageButton: ImageButton

    private var isOpenedFront: Boolean = false

    companion object {
        const val BACK_CAMERA_CODE = 0
        const val FRONT_CAMERA_CODE = 1
        const val REQUEST_CAMERA_PERMISSION = 99
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translator)

        windowHelper = WindowHelper(this, window)
        windowHelper.statusBarColor = R.color.black
        windowHelper.publish()

        progressBar = findViewById(R.id.progressbar)
        closeImageButton = findViewById(R.id.closeImageButton)
        resultTextView = findViewById(R.id.resultTextView)
        confidentTextView = findViewById(R.id.confidentTextView)
        rotateImageButton = findViewById(R.id.rotateImageButton)

        closeImageButton.setOnClickListener {
            finish()
        }

        // check mobile device permission
        permissionHelper.check(Manifest.permission.CAMERA, REQUEST_CAMERA_PERMISSION)
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        val handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        // initialize view model and factory
        factory = AppViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[TranslatorActivityViewModel::class.java]

        textureView = findViewById(R.id.textureView)
        textureView.surfaceTextureListener = mSurfaceTextureListener

        var count = 0;

        viewModel.result.observe(this) {
            val buffer = it.confident * 100
            val confident: Double = String.format("%.2f", buffer).toDouble()
            confidentTextView.text = "${confident}%"
            if(confident> 0) {
                resultTextView.text = it.classes
                count--
            }else{
                if(count > 6){
                    resultTextView.text = ""
                    count = 0
                }
                count++
            }
        }

        rotateImageButton.setOnClickListener {
            cameraDevice.close()
            if (isOpenedFront) {
                openCamera()
            }else{
                openCameraFront()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun openCamera() {
        if (permissionHelper.check(Manifest.permission.CAMERA, REQUEST_CAMERA_PERMISSION)) {
            cameraManager.openCamera(
                cameraManager.cameraIdList[BACK_CAMERA_CODE], mStateCallback, handler
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun openCameraFront() {
        if (permissionHelper.check(Manifest.permission.CAMERA, REQUEST_CAMERA_PERMISSION)) {
            cameraManager.openCamera(
                cameraManager.cameraIdList[FRONT_CAMERA_CODE], mStateCallback, handler
            )
        }
    }

    private val mStateCallback = object: CameraDevice.StateCallback() {
        override fun onOpened(device: CameraDevice) {

            val surfaceTexture = textureView.surfaceTexture
            val surface = Surface(surfaceTexture)

            progressBar.visibility = View.INVISIBLE
            windowHelper.statusBarColor = R.color.white
            windowHelper.publish()

            captureRequest = device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequest.addTarget(surface)

            cameraDevice = device

            device.createCaptureSession(listOf(surface), mCaptureSessionListener, handler)

        }

        override fun onDisconnected(p0: CameraDevice) { }

        override fun onError(p0: CameraDevice, p1: Int) { }

    }

    private var count: Int = 0

    private val mSurfaceTextureListener = object: SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
            val width = textureView.width
            val height = textureView.height
        }

        override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
            return false
        }

        override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
            viewModel.classify(textureView.bitmap!!)
            count = 0
        }
    }

    private val mCaptureSessionListener = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(p0: CameraCaptureSession) {
            p0.setRepeatingRequest(captureRequest.build(), null, null)
        }

        override fun onConfigureFailed(p0: CameraCaptureSession) { }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else {
                    Toast.makeText(
                        this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}