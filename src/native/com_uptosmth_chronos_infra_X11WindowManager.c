#include "com_uptosmth_chronos_infra_X11WindowManager.h"

#include "x11windowmanager.h"

JNIEXPORT jlong JNICALL Java_com_uptosmth_chronos_infra_X11WindowManager_getIdleMilli(JNIEnv *, jobject) {
    long idle = 0;

    x11windowmanager_screensaver_idle(&idle);

    return idle;
}

JNIEXPORT jboolean JNICALL Java_com_uptosmth_chronos_infra_X11WindowManager_isAway(JNIEnv *, jobject) {
    int state = 0;

    x11windowmanager_screensaver_state(&state);

    return state == 0 ? 0 : 1;
}
