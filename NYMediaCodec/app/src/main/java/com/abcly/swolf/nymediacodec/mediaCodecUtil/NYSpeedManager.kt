package com.abcly.swolf.nymediacodec.mediaCodecUtil

/**
 * @Description: TODO
 *
 * @Use:{非必须}
 *
 * @property TODO
 *
 * @Author liuyi

 * @DATE 2023/5/9 14:06
 */
class NYSpeedManager {
    private val CHECK_SLEEP_TIME = false
    private val ONE_MILLION = 1000000L

    private var mPrevPresentUsec: Long = 0
    private var mPrevMonoUsec: Long = 0
    private var mFixedFrameDurationUsec: Long = 0
    private var mLoopReset = false


    fun setFixedPlaybackRate(fps: Int) {
        mFixedFrameDurationUsec = ONE_MILLION / fps
    }
    fun preRender(presentationTimeUsec: Long) {
        if (mPrevMonoUsec == 0L) {
            mPrevMonoUsec = System.nanoTime() / 1000
            mPrevPresentUsec = presentationTimeUsec
        } else {
            var frameDelta: Long
            if (mLoopReset) {
                mPrevPresentUsec = presentationTimeUsec - ONE_MILLION / 30
                mLoopReset = false
            }
            if (mFixedFrameDurationUsec != 0L) {
                frameDelta = mFixedFrameDurationUsec
            } else {
                frameDelta = presentationTimeUsec - mPrevPresentUsec
            }
            if (frameDelta < 0) {
                frameDelta = 0
            } else if (frameDelta == 0L) {
            } else if (frameDelta > 10 * ONE_MILLION) {
                frameDelta = 5 * ONE_MILLION
            }
            val desiredUsec = mPrevMonoUsec + frameDelta // when we want to wake up
            var nowUsec = System.nanoTime() / 1000
            while (nowUsec < desiredUsec - 100 /*&& mState == RUNNING*/) {

                var sleepTimeUsec = desiredUsec - nowUsec
                if (sleepTimeUsec > 500000) {
                    sleepTimeUsec = 500000
                }
                try {
                    if (CHECK_SLEEP_TIME) {
                        val startNsec = System.nanoTime()
                        Thread.sleep(sleepTimeUsec / 1000, (sleepTimeUsec % 1000).toInt() * 1000)
                        val actualSleepNsec = System.nanoTime() - startNsec
                    } else {
                        val time = sleepTimeUsec / 1000
                        Thread.sleep(time, (sleepTimeUsec % 1000).toInt() * 1000)
                    }
                } catch (ie: InterruptedException) {
                }
                nowUsec = System.nanoTime() / 1000
            }
            mPrevMonoUsec += frameDelta
            mPrevPresentUsec += frameDelta
        }
    }
    fun postRender() {}

    fun loopReset() {
        mLoopReset = true
    }

    fun reset() {
        mPrevPresentUsec = 0
        mPrevMonoUsec = 0
        mFixedFrameDurationUsec = 0
        mLoopReset = false
    }
}