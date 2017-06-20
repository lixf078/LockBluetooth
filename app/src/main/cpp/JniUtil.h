//
// Created by Administrator on 2017/3/15.
//

#ifndef NDKAPPLICATION_JNIUTIL_H
#define NDKAPPLICATION_JNIUTIL_H
#include <jni.h>
#include <sstream>
class JniUtil{

};
std::string returnstring(JNIEnv *env, jstring jstr);
jstring str2jstring(JNIEnv* env,const char* pat);
long getCurrentTime();
std::string ltos(long l);
#endif //NDKAPPLICATION_JNIUTIL_H
