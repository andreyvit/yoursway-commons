package com.yoursway.utils.dependencies;

import com.yoursway.utils.annotations.CallFromAnyThread_NonReentrant;

public interface DependeeListener {
    
    /**
     * Called by a dependee to announce that it got changed, and thus any
     * dependents should be reevaluated.
     */
    @CallFromAnyThread_NonReentrant
    void changed(Dependee dependee);
    
    /**
     * Called by a dependee to announce that it has disappeared / is no longer
     * valid, and thus any dependents should be reevaluated.
     * 
     * <p>
     * Any listeners installed on this <code>dependee</code> are automatically
     * removed after this call.
     * </p>
     */
    @CallFromAnyThread_NonReentrant
    void removed(Dependee dependee);
    
}
