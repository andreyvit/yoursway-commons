package com.yoursway.utils.gemstones;

import java.util.List;

public class GemstoneDefinition<G extends Gemstone<G>> {
    
    private final SlotImpl<? extends Facelet<G>, G>[] slots;
    
    @SuppressWarnings("unchecked")
    GemstoneDefinition(List<SlotImpl<? extends Facelet<G>, G>> slots) {
        if (slots == null)
            throw new NullPointerException("slots is null");
        this.slots = (SlotImpl<? extends Facelet<G>, G>[]) slots.toArray(new SlotImpl<?, ?>[slots.size()]);
    }
    
    @SuppressWarnings("unchecked")
    Facelet<G>[] create(G gemstone) {
        Facelet<G>[] result = (Facelet<G>[]) new Facelet<?>[slots.length];
        for (int i = 0; i < slots.length; i++)
            result[i] = slots[i].create(gemstone);
        return result;
    }
    
}
