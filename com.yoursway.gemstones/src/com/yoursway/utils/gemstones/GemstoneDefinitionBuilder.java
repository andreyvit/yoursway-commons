package com.yoursway.utils.gemstones;

import static com.google.common.collect.Lists.newArrayList;
import static com.yoursway.utils.JavaStackFrameUtils.callerPackageOutside;
import static com.yoursway.utils.JavaStackFrameUtils.packageName;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.yoursway.utils.JavaStackFrameUtils;

public class GemstoneDefinitionBuilder<G extends Gemstone<G>> {
    
    static class FaceletDefinition<F extends Facelet<G>, G extends Gemstone<G>> {
        
        private final Class<? super F>[] faceletInterfaces;
        private final FaceletFactory<F, G> factory;
        
        public FaceletDefinition(Class<? super F>[] faceletInterfaces, FaceletFactory<F, G> factory) {
            this.faceletInterfaces = faceletInterfaces;
            this.factory = factory;
        }
        
        @SuppressWarnings("unchecked")
        public void create(G gemstone, Map<Class<? extends Facelet<G>>, Facelet<G>> facelets,
                Collection<Facelet<G>> allCreatedFacelets) {
            F facelet = factory.create(gemstone);
            allCreatedFacelets.add(facelet);
            for (Class<? super F> klass : faceletInterfaces)
                facelets.put((Class<? extends Facelet<G>>) klass, facelet);
        }
        
    }
    
    private final List<SlotImpl<? extends Facelet<G>, G>> slots = newArrayList();
    
    public <F extends Facelet<G>> Slot<F> addFacelet(FaceletFactory<F, G> factory, Class<F> interfaceToken) {
        String interfaceClassName = interfaceToken.getName();
        String interfacePackageName = packageName(interfaceClassName);
        String implementationPackageName = callerPackageOutside(getClass());
        String description = JavaStackFrameUtils.isTrivialExtention(interfacePackageName,
                implementationPackageName) ? interfaceClassName : interfaceClassName + " implemented in "
                + implementationPackageName;
        SlotImpl<F, G> slot = new SlotImpl<F, G>(slots.size(), description, factory);
        slots.add(slot);
        return slot;
    }
    
    public GemstoneDefinition<G> build() {
        return new GemstoneDefinition<G>(slots);
    }
    
    public static <G extends Gemstone<G>> GemstoneDefinitionBuilder<G> create() {
        return new GemstoneDefinitionBuilder<G>();
    }
    
}
