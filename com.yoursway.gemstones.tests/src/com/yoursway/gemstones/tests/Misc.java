package com.yoursway.gemstones.tests;

import org.junit.Test;

import com.yoursway.utils.gemstones.Gemstone;
import com.yoursway.utils.gemstones.GemstoneDefinition;
import com.yoursway.utils.gemstones.GemstoneImpl;

public class Misc {
    
    static interface Gem extends Gemstone<Gem> {
        
    }
    
    static class GemImpl extends GemstoneImpl<Gem> implements Gem {

        public GemImpl(GemstoneDefinition<Gem> definition) {
            super(definition);
        }
        
    }
    
    @Test
    public void foo() {
//        GemstoneDefinitionBuilder<Gem> builder = new GemstoneDefinitionBuilder<Gem>();
    }
    
}
