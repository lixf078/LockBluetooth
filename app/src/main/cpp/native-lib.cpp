#include <jni.h>
#include <string>
//#include "AES_doorOpen.c"

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
        jcharArray dat,
        jcharArray chainCipherBlock) {
    jchar buf[32];
    jchar chainBuf[16];
    unsigned char buffer[32];
    unsigned char chainBuffer[32];
    (env)->GetCharArrayRegion(dat, 0, 32, buf);
    (env)->GetCharArrayRegion(chainCipherBlock, 0, 32, chainBuf);
    for (int i = 0; i < 32; ++i) {
        buffer[i] = buf[i];
    }
    for (int i = 0; i < 32; ++i) {
        chainBuffer[i] = chainBuf[i];
    }

//    aesEncrypt(buffer, chainBuffer);
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
