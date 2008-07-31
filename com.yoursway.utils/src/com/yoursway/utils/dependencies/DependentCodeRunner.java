package com.yoursway.utils.dependencies;

import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static com.google.common.collect.Maps.newIdentityHashMap;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.yoursway.utils.annotations.MeaningfulWhen;
import com.yoursway.utils.annotations.NonReentrant_SynchronizeExternallyOrUseFromSingleThread;
import com.yoursway.utils.annotations.Reentrant_CallFromAnyThread;
import com.yoursway.utils.annotations.SynchronizedWithMonitorOfField;
import com.yoursway.utils.annotations.CallFromAnyThread_NonReentrant;
import com.yoursway.utils.annotations.WhenNotMeaningfulHasUndefinedValue;
import com.yoursway.utils.bugs.Bugs;

public class DependentCodeRunner {
    
    private static final Dependee[] NO_DEPENDEES = new Dependee[0];

    private enum DependeeState {
        
        OLD_AND_ALIVE(false, false),

        REMOVED(false, true),

        ADDED(true, false);
        
        public final boolean add;
        
        public final boolean remove;
        
        public final boolean alive;
        
        private DependeeState(boolean add, boolean remove) {
            this.add = add;
            this.remove = remove;
            this.alive = !remove;
        }
        
    }
    
    private static class DependenciesUpdater implements DependeesRequestor {
        
        private Map<Dependee, DependeeState> states;
        private final DependeeListener listener;
        
        public DependenciesUpdater(Dependee[] dependees, DependeeListener listener) {
            if (listener == null)
                throw new NullPointerException("listener is null");
            this.listener = listener;
            Map<Dependee, DependeeState> states = newIdentityHashMap();
            for (Dependee dependee : dependees)
                states.put(dependee, DependeeState.REMOVED);
            this.states = states;
        }
        
        @CallFromAnyThread_NonReentrant
        public synchronized void dependsOn(Dependee dependee) {
            DependeeState oldState = states.put(dependee, DependeeState.OLD_AND_ALIVE);
            if (oldState == null) {
                states.put(dependee, DependeeState.ADDED);
                dependee.dependeeEvents().addListener(listener);
            }
        }
        
        @NonReentrant_SynchronizeExternallyOrUseFromSingleThread
        public Dependee[] update() {
            List<Dependee> dependees = newArrayListWithCapacity(states.size());
            for (Entry<Dependee, DependeeState> entry : states.entrySet()) {
                Dependee dependee = entry.getKey();
                DependeeState state = entry.getValue();
                if (state.remove)
                    dependee.dependeeEvents().removeListener(listener);
                if (state.alive)
                    dependees.add(dependee);
            }
            return dependees.toArray(new Dependee[dependees.size()]);
        }
        
    }
    
    private DependeeListener listener = new DependeeListener() {
        
        @CallFromAnyThread_NonReentrant
        public void changed(Dependee dependee) {
            recalculate();
        }
        
        @CallFromAnyThread_NonReentrant
        public void removed(Dependee dependee) {
            recalculate();
        }
        
    };
    
    @SynchronizedWithMonitorOfField("listener")
    private Dependee[] dependees = NO_DEPENDEES;
    
    @SynchronizedWithMonitorOfField("listener")
    private boolean recalculationInProgress = false;
    
    @SynchronizedWithMonitorOfField("listener")
    @MeaningfulWhen("recalculationInProgress == true")
    @WhenNotMeaningfulHasUndefinedValue
    private boolean recalculateAgain = false;
    
    @SynchronizedWithMonitorOfField("listener")
    private boolean disposed = false;

    private final Runnable runnable;
    
    public DependentCodeRunner(Runnable runnable) {
        this.runnable = runnable;
        recalculate();
    }
    
    @Reentrant_CallFromAnyThread
    public void dispose() {
        Dependee[] oldDependees;
        synchronized (listener) {
            if (disposed)
                return;
            disposed = true;
            oldDependees = this.dependees;
            this.dependees = NO_DEPENDEES;
        }
        for (Dependee dependee : oldDependees)
            try {
                dependee.dependeeEvents().removeListener(listener);
            } catch (Throwable e) {
                Bugs.cleanupFailed(e, dependee);
            }
    }
    
    @Reentrant_CallFromAnyThread
    public void recalculate() {
        Dependee[] oldDependees;
        synchronized (listener) {
            if (disposed)
                return;
            if (recalculationInProgress) {
                recalculateAgain = true;
                return;
            }
            recalculationInProgress = true;
            recalculateAgain = false;
            oldDependees = this.dependees;
        }
        // if another thread demands recalculation from now on, it will set recalculateAgain to true 
        
        boolean again;
        do {
            DependenciesUpdater updater = new DependenciesUpdater(oldDependees, listener);
            Tracker.runAndTrack(runnable, updater);
            synchronized (listener) {
                if (disposed)
                    return;
                this.dependees = updater.update();
                again = recalculateAgain;
                if (again) {
                    recalculateAgain = false;
                    // if another thread demands recalculation from now on, it will set recalculateAgain to true
                } else {
                    recalculationInProgress = false;
                    // if another thread demands recalculation from now on, it will successfully start it
                }
            }
        } while (again);
    }
    
}
