package com.yoursway.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A <code>java.util.Timer</code> wrapper that makes it easy to cancel and
 * reschedule tasks by associating a set of <em>tags</em> with each task. Both
 * one-time and periodical tasks are available.
 * 
 * <p>
 * Any non-<code>null</code> object can serve as a tag. For example, tasks
 * associated with a specific window can be tagged with that window object. When
 * the window is closed, it would be easy to cancel all corresponding tasks.
 * </p>
 * 
 * <p>
 * You can schedule new one-time tasks using either <code>schedule</code> or
 * <code>reschedule</code>. The difference is that <code>reschedule</code>
 * cancels any existing tasks that match the given tags before scheduling a new
 * task. The tags you pass to <code>reschedule</code> must match no more than
 * one task. Additionally, the matched task, if any, has to be a one-time task,
 * i.e. cannot be a periodical task.
 * </p>
 * 
 * <p>
 * It is recommended that you always use <code>reschedule</code> for both
 * initial scheduling and rescheduling of tasks that you intend to only have a
 * single copy of. Please limit the use of <code>schedule</code> to only the
 * tasks that you want to have multiple copies of. (This recommendation is not
 * enforced, however, to avoid complicating API contracts.)
 * </p>
 * 
 * <p>
 * Periodical tasks are scheduled using <code>reschedulePeriodical</code>. There
 * is no analog of <code>schedule</code> for periodical tasks, because we
 * figured that adding multiple copies of a repeated task by mistake is too
 * dangerous opportunity to allow.
 * </p>
 * 
 * <p>
 * Note that tags passed to <code>reschedule</code>,
 * <code>reschedulePeriodical</code> and <code>cancel</code> match tasks that
 * were tagged with all of the given tags. I.e.
 * <code>reschedule(..., "foo", "bar")</code> will match (and cancel) a task
 * tagged with <code>"foo"</code>, <code>"bar"</code> and <code>"boz"</code>,
 * but won't match a task tagged only with <code>"foo"</code>.
 * </p>
 * 
 * <p>
 * If you're planning to use <code>reschedule</code> or
 * <code>reschedulePeriodical</code> with multiple kinds of tasks, a recommended
 * approach is to also tag tasks with string constants (defined as static final
 * <code>String</code>-typed members) according to their kind.
 * </p>
 * 
 * <p>
 * Also note that this class never calls <code>hashCode</code> or
 * <code>equals</code> on the runnables that you specify, and never compares
 * them for equality. It does not care if the runnables are equal or not; in
 * particular, rescheduling is based solely on tags.
 * </p>
 * 
 * <p>
 * You are free to use whatever runnables suit your purpose. We can, however,
 * recommend the following pattern for your tasks:
 * </p>
 * 
 * <pre>
 * class MyTask implements Runnable {
 *     
 *     private static final String TAG = MyTask.class.getName();
 *     
 *     private final DataType1 value1;
 *     private final DataType2 value2;
 *     
 *     public MyTask(DataType1 value1, DataType2 value2) {
 *         this.value1 = value1;
 *         this.value2 = value2;
 *     }
 *     
 *     public void scheduleIn(TagTimer timer, long delay) {
 *         timer.reschedule(delay, 60000, this, value1, value2, TAG);
 *     }
 *     
 *     public static void cancelAllTasks(TagTimer timer, DataType1 value1) {
 *         timer.cancel(value1, TAG);
 *     }
 *     
 *     public static void cancelAllTasks(TagTimer timer, DataType2 value2) {
 *         timer.cancel(value2, TAG);
 *     }
 *     
 *     public void run() {
 *         // ...
 *     }
 *     
 * }
 * </pre>
 * 
 * @author Andrey Tarantsov <andreyvit@gmail.com>
 */
public final class TagTimer {
    
    public static final long INFINITE = Long.MAX_VALUE;
    
    private final Timer timer;
    
    private Map<Object, Set<Task>> tagsToTasks = new HashMap<Object, Set<Task>>();
    
    public TagTimer(String threadName, boolean daemonThread) {
        this(new Timer(threadName, daemonThread));
    }
    
    public TagTimer(Timer timer) {
        if (timer == null)
            throw new NullPointerException("timer is null");
        this.timer = timer;
    }
    
    /**
     * Schedules the specified task for execution after the specified delay
     * 
     * @param delayMillis
     *            delay in milliseconds before task is to be executed.
     * @param runnable
     *            task to be scheduled.
     * @param tags
     *            tags to associate with the scheduled task.
     */
    public synchronized final void schedule(long delayMillis, Runnable runnable, Object... tags) {
        if (runnable == null)
            throw new NullPointerException("runnable is null");
        checkNoNullTags(tags);
        
        timer.schedule(index(new OneTimeTask(this, runnable, tags(tags), System.currentTimeMillis())),
            delayMillis);
    }
    
    /**
     * Schedules the specified task for execution after the specified delay,
     * cancelling any existing scheduled tasks matching the specified tags. At
     * least one tags thus has to be specified.
     * 
     * <p>
     * The tags you pass to <code>reschedule</code> must match no more than one
     * task. Additionally, the matched task, if any, has to be a one-time task,
     * i.e. cannot be a periodical task.
     * </p>
     * 
     * <p>
     * Additionally you can limit the total delay a task will have to wait in
     * the queue because of rescheduling. For example, if you schedule a task to
     * run in 5 seconds, but will then be rescheduling it to run in 5 seconds
     * every 2 seconds, the task will never have a chance to run. By specifying
     * e.g. a limit of 20 seconds, you can guarantee that after waiting for 20
     * seconds the task will run anyway.
     * </p>
     * 
     * @param delayMillis
     *            delay in milliseconds before task is to be executed.
     * @param maxTotalDelayMillis
     *            the maximal delay before the task will be executed in spite of
     *            any further attempts to reschedule it for a later lime.
     * @param runnable
     *            task to be scheduled.
     * @param firstTag
     *            the first tag to match and to associate with the scheduled
     *            task.
     * @param otherTags
     *            other tags to match and to associate with the scheduled task.
     */
    public synchronized final void reschedule(long delayMillis, long maxTotalDelayMillis, Runnable runnable,
            Object firstTag, Object... otherTags) {
        if (maxTotalDelayMillis <= 0)
            throw new IllegalArgumentException(
                    "maxTotalDelayMillis must be > 0; use TagTimer.INFINITE for no delay");
        if (runnable == null)
            throw new NullPointerException("runnable is null");
        if (firstTag == null)
            throw new NullPointerException("firstTag is null");
        checkNoNullTags(otherTags);
        
        long effectiveDelay = delayMillis;
        long initiallyScheduledAt = System.currentTimeMillis();
        Set<Object> tags = tags(firstTag, otherTags);
        
        Set<Task> tasks = findTasksByTags(firstTag, otherTags);
        if (tasks.size() > 1)
            throw new IllegalArgumentException(
                    "TagTimer.reschedule must be invoked with a set of tags that matches at most one task");
        if (!tasks.isEmpty()) {
            Task matchedTask = tasks.iterator().next();
            if (matchedTask instanceof PeriodicTask)
                throw new IllegalArgumentException(
                        "TagTimer.reschedule must be invoked with a set of tags that match a one-time task (periodical task has been matched instead)");
            OneTimeTask st = (OneTimeTask) matchedTask;
            if (st.cancel()) {
                effectiveDelay = st.limitScheduleDelay(delayMillis, maxTotalDelayMillis);
                initiallyScheduledAt = st.initiallyScheduledAt;
            }
        }
        
        timer.schedule(index(new OneTimeTask(this, runnable, tags, initiallyScheduledAt)), effectiveDelay);
    }
    
    /**
     * Schedules the specified task for repeated <i>fixed-delay execution</i>,
     * beginning after the specified delay. Subsequent executions take place at
     * approximately regular intervals separated by the specified period. Any
     * existing scheduled tasks matching the specified tags are cancelled.
     * 
     * <p>
     * The tags you pass to <code>reschedulePeriodical</code> must match no more
     * than one task.
     * </p>
     * 
     * @param delayMillis
     *            delay in milliseconds before task is to be executed for the
     *            first time.
     * @param periodMillis
     *            time in milliseconds between successive task executions.
     * @param runnable
     *            task to be scheduled.
     * @param firstTag
     *            the first tag to match and to associate with the scheduled
     *            task.
     * @param otherTags
     *            other tags to match and to associate with the scheduled task.
     */
    public synchronized final void reschedulePeriodical(long delayMillis, long periodMillis,
            Runnable runnable, Object firstTag, Object... otherTags) {
        if (periodMillis <= 0)
            throw new IllegalArgumentException("periodMillis must be > 0");
        if (runnable == null)
            throw new NullPointerException("runnable is null");
        if (firstTag == null)
            throw new NullPointerException("firstTag is null");
        checkNoNullTags(otherTags);
        
        Set<Object> tags = tags(firstTag, otherTags);
        
        Set<Task> tasks = findTasksByTags(firstTag, otherTags);
        if (tasks.size() > 1)
            throw new IllegalArgumentException(
                    "TagTimer.reschedulePeriodical must be invoked with a set of tags that matches at most one task");
        for (Task task : tasks)
            task.cancel();
        
        timer.schedule(index(new PeriodicTask(this, runnable, tags)), delayMillis, periodMillis);
    }
    
    /**
     * Cancels any scheduled tasks that were tagged with all of the specified
     * tags.
     * 
     * @param firstTag
     *            the first tag to match.
     * @param otherTags
     *            other tags to match.
     */
    public synchronized void cancel(Object firstTag, Object... otherTags) {
        if (firstTag == null)
            throw new NullPointerException("firstTag is null");
        checkNoNullTags(otherTags);
        
        for (Task task : findTasksByTags(firstTag, otherTags))
            task.cancel();
    }
    
    private Set<Object> tags(Object[] tags) {
        return new HashSet<Object>(Arrays.asList(tags));
    }
    
    private Set<Object> tags(Object firstTag, Object[] otherTags) {
        HashSet<Object> result = new HashSet<Object>(Arrays.asList(otherTags));
        result.add(firstTag);
        return result;
    }
    
    private Set<Task> findTasksByTags(Object firstTag, Object[] otherTags) {
        Set<Task> result = tagsToTasks.get(firstTag);
        if (result == null)
            return Collections.emptySet();
        for (Object tag : otherTags) {
            Set<Task> tasksThisTime = tagsToTasks.get(tag);
            if (tasksThisTime == null)
                return Collections.emptySet();
            result.retainAll(tasksThisTime);
        }
        return result;
    }
    
    private Task index(Task task) {
        for (Object tag : task.getTags()) {
            Set<Task> tasks = tagsToTasks.get(tag);
            if (tasks == null) {
                tasks = new HashSet<Task>();
                tagsToTasks.put(tag, tasks);
            }
            tasks.add(task);
        }
        return task;
    }
    
    void unindex(Task task) {
        for (Object tag : task.getTags()) {
            Set<Task> tasks = tagsToTasks.get(tag);
            if (tasks != null)
                tasks.remove(task);
        }
    }
    
    static void checkNoNullTags(Object[] tags) {
        for (Object tag : tags)
            if (tag == null)
                throw new NullPointerException("tags contains null, which is not allowed by TagTimer");
    }
    
    static abstract class Task extends TimerTask {
        
        private final Runnable runnable;
        
        protected final Set<Object> tags;
        
        private final TagTimer scheduledInTimer;
        
        public Task(TagTimer timer, Runnable runnable, Set<Object> tags) {
            if (timer == null)
                throw new NullPointerException("timer is null");
            if (runnable == null)
                throw new NullPointerException("runnable is null");
            scheduledInTimer = timer;
            this.runnable = runnable;
            this.tags = tags;
        }
        
        Set<Object> getTags() {
            return tags;
        }
        
        @Override
        public void run() {
            runnable.run();
        }
        
        @Override
        public final boolean cancel() {
            boolean result = super.cancel();
            if (result)
                unindex();
            return result;
        }
        
        protected final void unindex() {
            scheduledInTimer.unindex(this);
        }
        
    }
    
    static final class OneTimeTask extends Task {
        
        final long initiallyScheduledAt;
        
        public OneTimeTask(TagTimer timer, Runnable runnable, Set<Object> tags, long initiallyScheduledAt) {
            super(timer, runnable, tags);
            this.initiallyScheduledAt = initiallyScheduledAt;
        }
        
        public long limitScheduleDelay(long delay, long maxTotalDelay) {
            long now = System.currentTimeMillis();
            return Math.max(0, limitScheduledTime(now + delay, maxTotalDelay) - now);
        }
        
        public long limitScheduledTime(long wantToScheduleAt, long maxTotalDelay) {
            if (maxTotalDelay == INFINITE)
                return wantToScheduleAt;
            return Math.min(initiallyScheduledAt + maxTotalDelay, wantToScheduleAt);
        }
        
        @Override
        public final void run() {
            unindex();
            super.run();
        }
        
    }
    
    public static final class PeriodicTask extends Task {
        
        public PeriodicTask(TagTimer timer, Runnable runnable, Set<Object> tags) {
            super(timer, runnable, tags);
        }
        
    }
    
}
