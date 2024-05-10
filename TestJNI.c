#include <jni.h>
#include <stdio.h>
#include <string.h>
#include "TestJNI.h"

JNIEXPORT jint JNICALL Java_TestJNI_add(JNIEnv *env, jobject thisObj, jint n1, jint n2) {
    return n1 + n2;
}

JNIEXPORT jint JNICALL Java_TestJNI_sub(JNIEnv *env, jobject thisObj, jint n1, jint n2) {
    return n1 - n2;
}

JNIEXPORT jint JNICALL Java_TestJNI_mul(JNIEnv *env, jobject thisObj, jint n1, jint n2) {
    return n1 * n2;
}

JNIEXPORT jint JNICALL Java_TestJNI_div(JNIEnv *env, jobject thisObj, jint n1, jint n2) {
    if (n2 == 0) {
        fprintf(stderr, "Error: Division by zero\n");
        return 0;
    }
    return n1 / n2;
}

JNIEXPORT jstring JNICALL Java_TestJNI_concatStrings(JNIEnv *env, jobject thisObj, jstring str1, jstring str2) {
    const char *s1 = (*env)->GetStringUTFChars(env, str1, NULL);
    const char *s2 = (*env)->GetStringUTFChars(env, str2, NULL);

    char result[256]; // Adjust the size based on your requirements
    strcpy(result, s1);
    strcat(result, s2);

    (*env)->ReleaseStringUTFChars(env, str1, s1);
    (*env)->ReleaseStringUTFChars(env, str2, s2);

    return (*env)->NewStringUTF(env, result);
}

JNIEXPORT jstring JNICALL Java_TestJNI_substring(JNIEnv *env, jobject thisObj, jstring str, jint start, jint end) {
    const char *s = (*env)->GetStringUTFChars(env, str, NULL);
    int len = strlen(s);
    
    char result[end - start + 1];
    strncpy(result, s + start, end - start);
    result[end - start] = '\0';

    (*env)->ReleaseStringUTFChars(env, str, s);

    return (*env)->NewStringUTF(env, result);
}

JNIEXPORT jint JNICALL Java_TestJNI_stringLength(JNIEnv *env, jobject thisObj, jstring str) {
    const char *s = (*env)->GetStringUTFChars(env, str, NULL);
    int len = strlen(s);

    (*env)->ReleaseStringUTFChars(env, str, s);

    return len;
}

JNIEXPORT jstring JNICALL Java_TestJNI_toUpperCase(JNIEnv *env, jobject thisObj, jstring str) {
    const char *s = (*env)->GetStringUTFChars(env, str, NULL);
    int len = strlen(s);

    char result[len + 1];

    for (int i = 0; i < len; ++i) {
        result[i] = toupper(s[i]);
    }
    result[len] = '\0';

    (*env)->ReleaseStringUTFChars(env, str, s);

    return (*env)->NewStringUTF(env, result);
}
/*gaurav@gaurav-VirtualBox:~/Downloads$ javac TestJNI.java
gaurav@gaurav-VirtualBox:~/Downloads$ javah -jni TestJNI
gaurav@gaurav-VirtualBox:~/Downloads$ gcc -fPIC -I/usr/lib/jvm/java-8-openjdk-amd64/include -I/usr/lib/jvm/java-8-openjdk-amd64/include/linux -o libcal.so -shared TestJNI.c
TestJNI.c: In function ‘Java_TestJNI_toUpperCase’:
TestJNI.c:69:21: warning: implicit declaration of function ‘toupper’ [-Wimplicit-function-declaration]
   69 |         result[i] = toupper(s[i]);
      |                     ^~~~~~~
TestJNI.c:5:1: note: include ‘<ctype.h>’ or provide a declaration of ‘toupper’
    4 | #include "TestJNI.h"
  +++ |+#include <ctype.h>
    5 | 
gaurav@gaurav-VirtualBox:~/Downloads$ java -Djava.library.path=. TestJNI
Simple Calculator and String Operations
Enter Your choice
1. ADD
2. SUB
3. MUL
4. DIV
5. Concat Strings
6. Substring
7. String Length
8. Uppercase
0. Exit
5
Enter a String
gaurav
bhagwat
Concatenated String = gauravbhagwat
Enter Your choice
1. ADD
2. SUB
3. MUL
4. DIV
5. Concat Strings
6. Substring
7. String Length
8. Uppercase
0. Exit
*/
