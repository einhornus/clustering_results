void startRecentsActivity() {
        // Check if the top task is in the home stack, and start the recents activity
        ActivityManager.RunningTaskInfo topTask = getTopMostTask();
        AtomicBoolean isTopTaskHome = new AtomicBoolean();
        if (!isRecentsTopMost(topTask, isTopTaskHome)) {
            startRecentsActivity(topTask, isTopTaskHome.get());
        }
    }
--------------------

void setFocusedStack(ActivityRecord r) {
        if (r == null) {
            return;
        }
        if (!r.isApplicationActivity() || (r.task != null && !r.task.isApplicationTask())) {
            if (mStackState != STACK_STATE_HOME_IN_FRONT) {
                if (DEBUG_STACK || DEBUG_FOCUS) Slog.d(TAG, "setFocusedStack: mStackState old=" +
                        stackStateToString(mStackState) + " new=" +
                        stackStateToString(STACK_STATE_HOME_TO_FRONT) +
                        " Callers=" + Debug.getCallers(3));
                mStackState = STACK_STATE_HOME_TO_FRONT;
            }
        } else {
            if (DEBUG_FOCUS || DEBUG_STACK) Slog.d(TAG,
                    "setFocusedStack: Setting focused stack to r=" + r + " task=" + r.task +
                    " Callers=" + Debug.getCallers(3));
            final ActivityStack taskStack = r.task.stack;
            mFocusedStack = taskStack.isHomeStack() ? null : taskStack;
            if (mStackState != STACK_STATE_HOME_IN_BACK) {
                if (DEBUG_STACK) Slog.d(TAG, "setFocusedStack: mStackState old=" +
                        stackStateToString(mStackState) + " new=" +
                        stackStateToString(STACK_STATE_HOME_TO_BACK) +
                        " Callers=" + Debug.getCallers(3));
                mStackState = STACK_STATE_HOME_TO_BACK;
            }
        }
    }
--------------------

boolean resumeHomeStackTask(int homeStackTaskType, ActivityRecord prev, String reason) {
        if (!mService.mBooting && !mService.mBooted) {
            // Not ready yet!
            return false;
        }

        if (homeStackTaskType == RECENTS_ACTIVITY_TYPE) {
            mWindowManager.showRecentApps();
            return false;
        }

        if (prev != null) {
            prev.task.setTaskToReturnTo(APPLICATION_ACTIVITY_TYPE);
        }

        mHomeStack.moveHomeStackTaskToTop(homeStackTaskType);
        ActivityRecord r = getHomeActivity();
        // Only resume home activity if isn't finishing.
        if (r != null && !r.finishing) {
            mService.setFocusedActivityLocked(r, reason);
            return resumeTopActivitiesLocked(mHomeStack, prev, null);
        }
        return mService.startHomeActivityLocked(mCurrentUser, reason);
    }
--------------------

