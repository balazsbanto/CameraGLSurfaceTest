#include <jni.h>

#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>

#include <GLES2/gl2.h>
#include <opencv2/imgproc/types_c.h>

#include "common.hpp"

using namespace cv;

extern "C" JNIEXPORT void

JNICALL
Java_com_blade_testopencv_MyGLSurfaceViewKt_processFrame(JNIEnv *env, jobject /* this */,
                                                                      jint texIn, jint texOut,
                                                                      jint w, jint h,
                                                                      jboolean frontFacing) {
    static UMat m;

    LOGD("Processing on CPU");
    int64_t t;
    m.create(h, w, CV_8UC4);

    // expecting FBO to be bound, read pixels to mat
    glReadPixels(0, 0, m.cols, m.rows, GL_RGBA, GL_UNSIGNED_BYTE, m.getMat(ACCESS_WRITE).data);

    // Check if we should flip image due to frontFacing
    // I don't think this should be required, but I can't find
    // a way to get the OpenCV Android SDK to do this properly
    // (also, time taken to flip image is negligible)
    if(frontFacing){
        flip(m, m, 1);
    }

    // modify
    cvtColor(m, m, CV_BGRA2GRAY);
    cvtColor(m, m, CV_GRAY2BGRA);

    // write back
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, texOut);
    glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, m.cols, m.rows, GL_RGBA, GL_UNSIGNED_BYTE, m.getMat(ACCESS_READ).data);
}