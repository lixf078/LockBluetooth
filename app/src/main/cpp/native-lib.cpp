#include <jni.h>
#include <string>
#include "AES_doorOpen.h"
#include "JniUtil.h"
#include <android/log.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bluetooth_le_DeviceScanActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bluetooth_le_DeviceScanActivity_aesEncrypt(
        JNIEnv* env,
        jobject ,
        jstring data,
        jstring key) {
    unsigned char* buffer;
    unsigned char* keyBuffer;

    std::string dataStr = returnstring(env, data);
    std::string keyStr = returnstring(env, key);
    __android_log_print(ANDROID_LOG_ERROR, "JNITag", "aesEncrypt dataStr 1");
    __android_log_print(ANDROID_LOG_ERROR, "JNITag", "aesEncrypt keyStr 2");
    buffer = (unsigned char *)(dataStr.c_str());
    keyBuffer = (unsigned char *)(keyStr.c_str());
    __android_log_print(ANDROID_LOG_ERROR, "JNITag", "aesEncrypt buffer 3" );
    __android_log_print(ANDROID_LOG_ERROR, "JNITag", "aesEncrypt keyBuffer 4 ");
    aesEncInit();
    aesEncrypt(buffer, keyBuffer);
    __android_log_print(ANDROID_LOG_ERROR, "JNITag", "aesEncrypt c++ buffer 5");

    std::string hello = "Hello from C++";
//    return env->NewStringUTF(hello.c_str());
    return str2jstring(env, (const char*)buffer);
    return env->NewStringUTF((const char *)buffer);
//    return env->NewStringUTF((char*)buffer);
}
