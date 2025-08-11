#include <jni.h>
#include <string>
extern "C" JNIEXPORT jstring JNICALL stringFromJNI(JNIEnv *env, jobject jobj) {
    std::string hello = "Hello from C++ly3";
    return env->NewStringUTF(hello.c_str());
}

/**
 * 动态注册
 */
JNINativeMethod methods[] = {
        {"stringFromJNI", "()Ljava/lang/String;", (jstring *)stringFromJNI}
};

/**
 * 动态注册
 * @param env
 * @return
 */
jint registerNativeMethod(JNIEnv *env) {
    jclass cl = env->FindClass("com/swolf/ly/myapplication/NativeFun");
    if ((env->RegisterNatives(cl, methods, sizeof(methods) / sizeof(methods[0]))) < 0) {
        return -1;
    }
    return 0;
}

/**
 * 加载默认回调
 * @param vm
 * @param reserved
 * @return
 */
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    //注册方法
    if (registerNativeMethod(env) != JNI_OK) {
        return -1;
    }
    return JNI_VERSION_1_6;
}





