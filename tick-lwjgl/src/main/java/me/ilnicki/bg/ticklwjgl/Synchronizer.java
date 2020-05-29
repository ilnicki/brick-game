/*
 * Copyright (c) 2002-2012 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package me.ilnicki.bg.ticklwjgl;

/**
 * A highly accurate sync method that continually adapts to the system it runs
 * on to provide reliable results.
 *
 * @author Riven
 * @author kappaOne
 * @author ilnicki
 */
class Synchronizer {
    /**
     * Number of nano seconds in a second.
     */
    private static final long NANOS_IN_SECOND = 1000_000_000L;

    /**
     * The desired frame rate, in frames per second.
     */
    private final int fps;

    /**
     * For calculating the averages the previous sleep/yield times are stored.
     */
    private final RunningAvg sleepDurations;
    private final RunningAvg yieldDurations;

    /**
     * The time to sleep/yield until the next frame.
     */
    private long nextFrame;

    Synchronizer(int fps) {
        this.fps = fps;

        sleepDurations = new RunningAvg(10);
        sleepDurations.init(1_000_000);

        yieldDurations = new RunningAvg(10);
        yieldDurations.init((int) (-(getTime() - getTime()) * 1.333));

        nextFrame = getTime();

        if (System.getProperty("os.name").startsWith("Win")) {
            // On windows the sleep functions can be highly inaccurate by
            // over 10ms making in unusable. However it can be forced to
            // be a bit more accurate by running a separate sleeping daemon
            // thread.
            Thread timerAccuracyThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(Integer.MAX_VALUE);
                    } catch (InterruptedException ignored) {
                    }
                }
            });

            timerAccuracyThread.setName("Synchronizer Timer");
            timerAccuracyThread.setDaemon(true);
            timerAccuracyThread.start();
        }
    }

    /**
     * Get the system time in nano seconds.
     *
     * @return will return the current time in nano's
     */
    private static long getTime() {
        return System.nanoTime();
    }

    /**
     * An accurate sync method that will attempt to run at a constant frame
     * rate. It should be called once every frame.
     */
    void sync() {
        try {
            // Sleep until the average sleep time is greater than the time remaining till nextFrame.
            for (long t0 = getTime(), t1; (nextFrame - t0) > sleepDurations.avg(); t0 = t1) {
                Thread.sleep(1);
                sleepDurations.add((t1 = getTime()) - t0); // update average sleep time
            }

            // Slowly dampen sleep average if too high to avoid yielding too much
            sleepDurations.dampenForLowResTicker();

            // Yield until the average yield time is greater than the time remaining till nextFrame.
            for (long t0 = getTime(), t1; (nextFrame - t0) > yieldDurations.avg(); t0 = t1) {
                Thread.yield();
                yieldDurations.add((t1 = getTime()) - t0); // update average yield time
            }
        } catch (InterruptedException ignored) {
        }

        // Schedule next frame, drop frame(s) if already too late for next frame.
        nextFrame = Math.max(nextFrame + (NANOS_IN_SECOND / fps), getTime());
    }

    private static class RunningAvg {
        private static final long DAMPEN_THRESHOLD = 10_000_000L; // 10ms
        private static final float DAMPEN_FACTOR = 0.9f; // don't change: 0.9f is exactly right!
        private final long[] slots;
        private int offset;

        RunningAvg(int slotCount) {
            slots = new long[slotCount];
            offset = 0;
        }

        void init(long value) {
            while (this.offset < this.slots.length) {
                this.slots[this.offset++] = value;
            }
        }

        void add(long value) {
            this.slots[this.offset++ % this.slots.length] = value;
            this.offset %= this.slots.length;
        }

        long avg() {
            long sum = 0;
            for (long slot : slots) {
                sum += slot;
            }
            return sum / slots.length;
        }

        void dampenForLowResTicker() {
            if (this.avg() > DAMPEN_THRESHOLD) {
                for (int i = 0; i < this.slots.length; i++) {
                    this.slots[i] *= DAMPEN_FACTOR;
                }
            }
        }
    }
}
