package com.yoursway.fsmonitor;

enum State {
    
    INACTIVE {
        
        public boolean shouldInitiateRescheduling() {
            return true;
        }
        
    },
    
    ACTIVE {
        
        public boolean shouldInitiateRescheduling() {
            return true;
        }
        
    },
    
    RESCHEDULING,

    INOPERATIONAL {
        
        public boolean canChangeToAnotherState() {
            return false;
        }
        
        public boolean canActivate() {
            return false;
        }
        
    },
    
    DISPOSED {
        
        public boolean canChangeToAnotherState() {
            return false;
        }
        
        public boolean canActivate() {
            return false;
        }
        
    };
    
    public boolean shouldInitiateRescheduling() {
        return false;
    }
    
    public boolean canChangeToAnotherState() {
        return true;
    }
    
    public boolean canActivate() {
        return true;
    }
    
}