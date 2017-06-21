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
//
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_bluetooth_le_DeviceScanActivity_aesEncrypt(
        JNIEnv* env,
        jobject ,
        jbyteArray data,
        jbyteArray key) {

    jbyte * dataByte = (jbyte*)env->GetByteArrayElements(data, 0);

    char* byArray = (char*)dataByte;

    jbyte * keyByte = (jbyte*)env->GetByteArrayElements(key, 0);

    char* keyArray = (char*)keyByte;

    for (int i = 0; i < 16; ++i) {
        AES_Key_Table[i] = keyArray[i];
    }

    unsigned char dat[16];
    memcpy(dat, dataByte, 16);

    unsigned char chainCipherBlock[16];
    memset(chainCipherBlock, 0x00, sizeof(chainCipherBlock));
    aesEncInit();
    aesEncrypt(dat, chainCipherBlock);


//    jbyteArray  result = env->NewByteArray(16);
//
//    jbyte * resultBuffer;
//    memcpy(resultBuffer, dat, 16);
//
//    env->SetByteArrayRegion(result, 0, 16, resultBuffer);
    return  NULL;
//    return result;
//    result
//    return str2jstring(env, (const char*)dat);
}
//
//extern "C"
//JNIEXPORT jstring JNICALL
//Java_com_example_bluetooth_le_DeviceScanActivity_aesEncrypt(
//        JNIEnv* env,
//        jobject ,
//        jstring data,
//        jstring key) {
//    unsigned char* buffer;
//    unsigned char* keyBuffer;
//
//    std::string dataStr = returnstring(env, data);
//    std::string keyStr = returnstring(env, key);
//    buffer = (unsigned char *)(dataStr.c_str());
//    keyBuffer = (unsigned char *)(keyStr.c_str());
//    aesEncInit();
//    aesEncrypt(buffer, keyBuffer);
//
//    return str2jstring(env, (const char*)buffer);
//}
