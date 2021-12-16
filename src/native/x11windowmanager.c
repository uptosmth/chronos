#include <X11/extensions/scrnsaver.h>
#include <X11/Xlib.h>

#include "x11windowmanager.h"

int x11windowmanager_screensaver_idle(long *idle) {
    Display *dpy = XOpenDisplay(NULL);

    if (!dpy) {
        return -1;
    }

    XScreenSaverInfo *info = XScreenSaverAllocInfo();
    XScreenSaverQueryInfo(dpy, DefaultRootWindow(dpy), info);

    *idle = info->idle;

    XFree(info);
    XCloseDisplay(dpy);

    return 0;
}

int x11windowmanager_screensaver_state(int *state) {
    Display *dpy = XOpenDisplay(NULL);

    if (!dpy) {
        return -1;
    }

    XScreenSaverInfo *info = XScreenSaverAllocInfo();
    XScreenSaverQueryInfo(dpy, DefaultRootWindow(dpy), info);

    *state = info->state;

    XFree(info);
    XCloseDisplay(dpy);

    return 0;
}