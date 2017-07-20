#include <jni.h>
#include <string>
#include "AES_doorOpen.h"
#include <android/log.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_lock_bluetooth_le_BleConnectUtil_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
//
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_lock_bluetooth_le_BleConnectUtil_aesEncrypt(
        JNIEnv* env,
        jobject ,
        jbyteArray data,
        jbyteArray key) {

    jbyte * dataByte = (jbyte*)env->GetByteArrayElements(data, 0);

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
    int size = sizeof(dat);
    jbyte *by = (jbyte*)dat;
    jbyteArray jarray = env->NewByteArray(size);
    env->SetByteArrayRegion(jarray, 0, size, by);
    return  jarray;
}



extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_lock_bluetooth_le_BleConnectUtil_aesDecrypt(
        JNIEnv* env,
        jobject ,
        jbyteArray data,
        jbyteArray key) {

    jbyte * dataByte = (jbyte*)env->GetByteArrayElements(data, 0);

    jbyte * keyByte = (jbyte*)env->GetByteArrayElements(key, 0);

    char* keyArray = (char*)keyByte;

    for (int i = 0; i < 16; ++i) {
        AES_Key_Table[i] = keyArray[i];
    }

    unsigned char dat[16];
    memcpy(dat, dataByte, 16);

    unsigned char chainCipherBlock[16];
    memset(chainCipherBlock, 0x00, sizeof(chainCipherBlock));
    aesDecInit();
    aesDecrypt(dat, chainCipherBlock);

    int size = sizeof(dat);

    jbyte *by = (jbyte*)dat;
    jbyteArray jarray = env->NewByteArray(size);
    env->SetByteArrayRegion(jarray, 0, size, by);

    return  jarray;

}

