package com.blade.testopencv

import android.Manifest
import android.app.Activity
import android.content.Context
import com.blade.testopencv.MyGLSurfaceViewKt
import androidx.annotation.RequiresApi
import android.os.Build
import android.os.Bundle
import android.content.pm.PackageManager
import android.widget.Toast
import android.view.WindowManager
import android.content.pm.ActivityInfo
import android.os.Process
import android.view.View
import android.view.Window
import com.blade.testopencv.R
import android.widget.CompoundButton
import android.widget.Switch
import org.opencv.android.CameraBridgeViewBase

class CameraActivity : Activity() {
    companion object {
        private const val MY_PERMISSIONS_REQUEST_CAMERA = 1337
        private fun checkSelfPermission(context: Context, permission: String?): Int {
            requireNotNull(permission) { "permission is null" }
            return context.checkPermission(permission, Process.myPid(), Process.myUid())
        }

        // Used to load the 'native-lib' and 'opencv' libraries on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    private var mView: MyGLSurfaceViewKt? = null
    private var mSwitch: Switch? = null
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Check for camera permissions
        val context = applicationContext
        if (checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                // Show some text
                Toast.makeText(context, "Need access to your camera to proceed", Toast.LENGTH_LONG)
                    .show()
                finish()
            } else {
                // No explanation needed; request the permission
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA
                )
            }
        } else {
            setupApplication()
        }
    }

    private fun setupApplication() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity)

        // Setup switch to swap between back and front camera
        mSwitch = findViewById<View>(R.id.camera_switch) as Switch
        mSwitch!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mView!!.frontFacing = true
                mView!!.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT)
            } else {
                mView!!.frontFacing = false
                mView!!.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK)
            }
        }
        mView = findViewById<View>(R.id.my_gl_surface_view) as MyGLSurfaceViewKt
        mView!!.setMaxCameraPreviewSize(1280, 920)
        mView!!.cameraTextureListener = mView
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    //Permission granted
                }
            }
        }
    }

    override fun onResume() {
        mView!!.onResume()
        super.onResume()
    }

    public override fun onPause() {
        mView!!.onPause()
        super.onPause()
    }
}