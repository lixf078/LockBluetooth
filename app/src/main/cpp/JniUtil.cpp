//
// Created by Administrator on 2016/12/29.
//
#include "JniUtil.h"
#include <android/log.h>
//jstring 转换 string
std::string returnstring(JNIEnv *env, jstring jstr) {
    char *rtn = NULL;
//     获取java下的string类
    jclass clsstring = env->FindClass("java/lang/String");
//     创建一个JNI下的jstring类
    jstring strencode = env->NewStringUTF("UTF-8");
//     获取java下的函数getBytes的ID,参数为函数名称和string类
    jmethodID mid = env->GetMethodID(clsstring, "getBytes",
                                     "(Ljava/lang/String;)[B");
//     调用getBytes函数,参数为string字符串,函数ID和函数的参数类型,返回一个byte数组
    jbyteArray barr = (jbyteArray) env->CallObjectMethod(jstr, mid, strencode);
//     获取返回的Byte数组的长度
    jsize alen = env->GetArrayLength(barr);
//     获取数组元素
    jbyte *ba = env->GetByteArrayElements(barr, JNI_FALSE);
    if (alen > 0) {
//         分配内存
        rtn = (char *) malloc(alen + 1);
//         字符串的拷贝
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    env->ReleaseByteArrayElements(barr, ba, 0);
    std::string stemp(rtn);
//    释放指针内存
    free(rtn);
    return stemp;
}

jstring str2jstring(JNIEnv* env,const char* pat)
{
    __android_log_print(ANDROID_LOG_ERROR, "JNITag", "str2jstring dataStr 1");
    //定义java String类 strClass
    jclass strClass = env->FindClass("Ljava/lang/String;");
    __android_log_print(ANDROID_LOG_ERROR, "JNITag", "str2jstring dataStr 2");
    //获取String(byte[],String)的构造器,用于将本地byte[]数组转换为一个新String
    jmethodID ctorID = (env)->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
    __android_log_print(ANDROID_LOG_ERROR, "JNITag", "str2jstring dataStr 3");
    //建立byte数组
    jbyteArray bytes = (env)->NewByteArray(strlen(pat));
    __android_log_print(ANDROID_LOG_ERROR, "JNITag", "str2jstring dataStr 4");
    //将char* 转换为byte数组
    (env)->SetByteArrayRegion(bytes, 0, strlen(pat), (jbyte*)pat);
    __android_log_print(ANDROID_LOG_ERROR, "JNITag", "str2jstring dataStr 5");
    // 设置String, 保存语言类型,用于byte数组转换至String时的参数
    jstring encoding = (env)->NewStringUTF("UTF-8");
    //将byte数组转换为java String,并输出
    return (jstring)(env)->NewObject(strClass, ctorID, bytes, encoding);
}


//将windows类型转换成jstring类型
jstring WindowsTojstring( JNIEnv* env, const char* str )
{
    jstring rtn = 0;
    int slen = strlen(str);
    unsigned char * buffer = 0;
    if( slen == 0 )
        rtn = (env)->NewStringUTF(str );
    else
    {
        buffer = (unsigned char *)malloc( slen*2 + 1 );
            rtn = (env)->NewString( (jchar*)buffer, slen*2 + 1 );
    }
    if( buffer )
        free( buffer );
    return rtn;
}

long getCurrentTime()
{
    struct timeval tv;
    gettimeofday(&tv,NULL);
    return tv.tv_sec * 1000 + tv.tv_usec / 1000;
}

//long型转string
std::string ltos(long l)
{
    std::ostringstream os;
    os<<l;
    std::string result;
    std::istringstream is(os.str());
    is>>result;
    return result;
}
