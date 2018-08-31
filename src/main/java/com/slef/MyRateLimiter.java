package com.slef;


import com.google.common.util.concurrent.RateLimiter;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * TODO scs 如果要自己实现一个限速器怎么实现？
 * TODO scs 单 JVM 的限速，分布式限速？
 */
@SuppressWarnings("unused")
public class MyRateLimiter {
    /*
        TODO scs 设置完 QPS 为 1/s 然后一下次获取 10 个 permit 并发去消费，那不等于变相绕过了 QPS 限制？
        TODO scs 为什么会提供一个批量获取 permit 的操作？

        QPS 的限制只限制下一次的 acquire 操作，下一次的 acquire 需要为上一次的 acquire 操作付出代价，哪怕改变了 QPS 阈值
     */
    public static void main(String[] args) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RateLimiter rateLimiter = RateLimiter.create(1);
        int i = 0;

        System.out.println(dateFormat.format(new Date()));
        rateLimiter.acquire(1);
        System.out.println();

        System.out.println(dateFormat.format(new Date()));
        for (; i < 10; i++) {
            System.out.println(dateFormat.format(new Date()) + ", i: " + i);
        }

        System.out.println();

        System.out.println(dateFormat.format(new Date()));
        rateLimiter.acquire(10);

        System.out.println();

        System.out.println(dateFormat.format(new Date()));
        for (; i < 20; i++) {
            System.out.println(dateFormat.format(new Date()) + ", i: " + i);
        }

        System.out.println();

        System.out.println(dateFormat.format(new Date()));
        rateLimiter.acquire(10);

        System.out.println();

        System.out.println(dateFormat.format(new Date()));
        for (; i < 30; i++) {
            System.out.println(dateFormat.format(new Date()) + ", i: " + i);
        }
    }

    /**
     * Creates a {@code MyRateLimiter} with the specified stable throughput, given as "permits per
     * second" (commonly referred to as <i>QPS</i>, queries per second).
     *
     * <p>The returned {@code MyRateLimiter} ensures that on average no more than {@code
     * permitsPerSecond} are issued during any given second, with sustained requests being smoothly
     * spread over each second. When the incoming request rate exceeds {@code permitsPerSecond} the
     * rate limiter will release one permit every {@code (1.0 / permitsPerSecond)} seconds. When the
     * rate limiter is unused, bursts of up to {@code permitsPerSecond} permits will be allowed, with
     * subsequent requests being smoothly limited at the stable rate of {@code permitsPerSecond}.
     *
     * @param permitsPerSecond the rate of the returned {@code MyRateLimiter}, measured in how many
     *                         permits become available per second
     * @throws IllegalArgumentException if {@code permitsPerSecond} is negative or zero
     */
    public static MyRateLimiter create(double permitsPerSecond) {
        return null;
    }

    /**
     * Creates a {@code MyRateLimiter} with the specified stable throughput, given as "permits per
     * second" (commonly referred to as <i>QPS</i>, queries per second), and a <i>warmup period</i>,
     * during which the {@code MyRateLimiter} smoothly ramps up its rate, until it reaches its maximum
     * rate at the end of the period (as long as there are enough requests to saturate it). Similarly,
     * if the {@code MyRateLimiter} is left <i>unused</i> for a duration of {@code warmupPeriod}, it
     * will gradually return to its "cold" state, i.e. it will go through the same warming up process
     * as when it was first created.
     *
     * <p>The returned {@code MyRateLimiter} is intended for cases where the resource that actually
     * fulfills the requests (e.g., a remote server) needs "warmup" time, rather than being
     * immediately accessed at the stable (maximum) rate.
     *
     * <p>The returned {@code MyRateLimiter} starts in a "cold" state (i.e. the warmup period will
     * follow), and if it is left unused for long enough, it will return to that state.
     *
     * @param permitsPerSecond the rate of the returned {@code MyRateLimiter}, measured in how many
     *                         permits become available per second
     * @param warmupPeriod     the duration of the period where the {@code MyRateLimiter} ramps up its rate,
     *                         before reaching its stable (maximum) rate
     * @param unit             the time unit of the warmupPeriod argument
     * @throws IllegalArgumentException if {@code permitsPerSecond} is negative or zero or {@code
     *                                  warmupPeriod} is negative
     */
    public static MyRateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit) {
        return null;
    }

    /**
     * Acquires a single permit from this {@code MyRateLimiter}, blocking until the request can be
     * granted. Tells the amount of time slept, if any.
     *
     * <p>This method is equivalent to {@code acquire(1)}.
     *
     * @return time spent sleeping to enforce rate, in seconds; 0.0 if not rate-limited
     * @since 16.0 (present in 13.0 with {@code void} return type})
     */
    @CanIgnoreReturnValue
    public double acquire() {
        return 0;
    }

    /**
     * Acquires the given number of permits from this {@code MyRateLimiter}, blocking until the request
     * can be granted. Tells the amount of time slept, if any.
     *
     * @param permits the number of permits to acquire
     * @return time spent sleeping to enforce rate, in seconds; 0.0 if not rate-limited
     * @throws IllegalArgumentException if the requested number of permits is negative or zero
     * @since 16.0 (present in 13.0 with {@code void} return type})
     */
    @CanIgnoreReturnValue
    public double acquire(int permits) {
        return 0;
    }

    /**
     * Returns the stable rate (as {@code permits per seconds}) with which this {@code MyRateLimiter} is
     * configured with. The initial value of this is the same as the {@code permitsPerSecond} argument
     * passed in the factory method that produced this {@code MyRateLimiter}, and it is only updated
     * after invocations to {@linkplain #setRate}.
     */
    public final double getRate() {
        return 0;
    }

    /**
     * Updates the stable rate of this {@code MyRateLimiter}, that is, the {@code permitsPerSecond}
     * argument provided in the factory method that constructed the {@code MyRateLimiter}. Currently
     * throttled threads will <b>not</b> be awakened as a result of this invocation, thus they do not
     * observe the new rate; only subsequent requests will.
     *
     * <p>Note though that, since each request repays (by waiting, if necessary) the cost of the
     * <i>previous</i> request, this means that the very next request after an invocation to {@code
     * setRate} will not be affected by the new rate; it will pay the cost of the previous request,
     * which is in terms of the previous rate.
     *
     * <p>The behavior of the {@code MyRateLimiter} is not modified in any other way, e.g. if the {@code
     * MyRateLimiter} was configured with a warmup period of 20 seconds, it still has a warmup period of
     * 20 seconds after this method invocation.
     *
     * @param permitsPerSecond the new stable rate of this {@code MyRateLimiter}
     * @throws IllegalArgumentException if {@code permitsPerSecond} is negative or zero
     */
    public final void setRate(double permitsPerSecond) {

    }

    /**
     * Acquires a permit from this {@link MyRateLimiter} if it can be acquired immediately without
     * delay.
     *
     * <p>This method is equivalent to {@code tryAcquire(1)}.
     *
     * @return {@code true} if the permit was acquired, {@code false} otherwise
     * @since 14.0
     */
    public boolean tryAcquire() {
        return false;
    }

    public boolean tryAcquire(int permits) {
        return false;
    }

    /**
     * Acquires the given number of permits from this {@code MyRateLimiter} if it can be obtained
     * without exceeding the specified {@code timeout}, or returns {@code false} immediately (without
     * waiting) if the permits would not have been granted before the timeout expired.
     *
     * @param permits the number of permits to acquire
     * @param timeout the maximum time to wait for the permits. Negative values are treated as zero.
     * @param unit    the time unit of the timeout argument
     * @return {@code true} if the permits were acquired, {@code false} otherwise
     * @throws IllegalArgumentException if the requested number of permits is negative or zero
     */
    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
        return false;
    }

    /**
     * Acquires a permit from this {@code MyRateLimiter} if it can be obtained without exceeding the
     * specified {@code timeout}, or returns {@code false} immediately (without waiting) if the permit
     * would not have been granted before the timeout expired.
     *
     * <p>This method is equivalent to {@code tryAcquire(1, timeout, unit)}.
     *
     * @param timeout the maximum time to wait for the permit. Negative values are treated as zero.
     * @param unit    the time unit of the timeout argument
     * @return {@code true} if the permit was acquired, {@code false} otherwise
     * @throws IllegalArgumentException if the requested number of permits is negative or zero
     */
    public boolean tryAcquire(long timeout, TimeUnit unit) {
        return false;
    }


}
