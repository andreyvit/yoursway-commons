package com.yoursway.databinding;


/**
 * @since 1.0
 * 
 */
public class Diffs {
    
    /**
     * @param oldList
     * @param newList
     * @return the differences between oldList and newList
     */
//    public static ListDiff computeListDiff(List oldList, List newList) {
//        List diffEntries = new ArrayList();
//        createListDiffs(new ArrayList(oldList), newList, diffEntries);
//        ListDiff listDiff = createListDiff((ListDiffEntry[]) diffEntries
//                .toArray(new ListDiffEntry[diffEntries.size()]));
//        return listDiff;
//    }
    
//    /**
//     * adapted from EMF's ListDifferenceAnalyzer
//     */
//    private static void createListDiffs(List oldList, List newList, List listDiffs) {
//        int index = 0;
//        for (Iterator it = newList.iterator(); it.hasNext();) {
//            Object newValue = it.next();
//            if (oldList.size() <= index) {
//                // append newValue to newList 
//                listDiffs.add(createListDiffEntry(index, true, newValue));
//            } else {
//                boolean done;
//                do {
//                    done = true;
//                    Object oldValue = oldList.get(index);
//                    if (oldValue == null ? newValue != null : !oldValue.equals(newValue)) {
//                        int oldIndexOfNewValue = listIndexOf(oldList, newValue, index);
//                        if (oldIndexOfNewValue != -1) {
//                            int newIndexOfOldValue = listIndexOf(newList, oldValue, index);
//                            if (newIndexOfOldValue == -1) {
//                                // removing oldValue from list[index]
//                                listDiffs.add(createListDiffEntry(index, false, oldValue));
//                                oldList.remove(index);
//                                done = false;
//                            } else if (newIndexOfOldValue > oldIndexOfNewValue) {
//                                // moving oldValue from list[index] to [newIndexOfOldValue] 
//                                if (oldList.size() <= newIndexOfOldValue) {
//                                    // The element cannot be moved to the correct index
//                                    // now, however later iterations will insert elements
//                                    // in front of it, eventually moving it into the
//                                    // correct spot.
//                                    newIndexOfOldValue = oldList.size() - 1;
//                                }
//                                listDiffs.add(createListDiffEntry(index, false, oldValue));
//                                oldList.remove(index);
//                                listDiffs.add(createListDiffEntry(newIndexOfOldValue, true, oldValue));
//                                oldList.add(newIndexOfOldValue, oldValue);
//                                done = false;
//                            } else {
//                                // move newValue from list[oldIndexOfNewValue] to [index]
//                                listDiffs.add(createListDiffEntry(oldIndexOfNewValue, false, newValue));
//                                oldList.remove(oldIndexOfNewValue);
//                                listDiffs.add(createListDiffEntry(index, true, newValue));
//                                oldList.add(index, newValue);
//                            }
//                        } else {
//                            // add newValue at list[index]
//                            oldList.add(index, newValue);
//                            listDiffs.add(createListDiffEntry(index, true, newValue));
//                        }
//                    }
//                } while (!done);
//            }
//            ++index;
//        }
//        for (int i = oldList.size(); i > index;) {
//            // remove excess trailing elements not present in newList
//            listDiffs.add(createListDiffEntry(--i, false, oldList.get(i)));
//        }
//    }
//    
//    /**
//     * @param list
//     * @param object
//     * @param index
//     * @return the index, or -1 if not found
//     */
//    private static int listIndexOf(List list, Object object, int index) {
//        int size = list.size();
//        for (int i = index; i < size; i++) {
//            Object candidate = list.get(i);
//            if (candidate == null ? object == null : candidate.equals(object)) {
//                return i;
//            }
//        }
//        return -1;
//    }
    
    /**
     * Checks whether the two objects are <code>null</code> -- allowing for
     * <code>null</code>.
     * 
     * @param left
     *      The left object to compare; may be <code>null</code>.
     * @param right
     *      The right object to compare; may be <code>null</code>.
     * @return <code>true</code> if the two objects are equivalent;
     *  <code>false</code> otherwise.
     */
    public static final boolean equals(final Object left, final Object right) {
        return left == null ? right == null : ((right != null) && left.equals(right));
    }
    
//    /**
//     * @param oldSet
//     * @param newSet
//     * @return a set diff
//     */
//    public static SetDiff computeSetDiff(Set oldSet, Set newSet) {
//        Set additions = new HashSet(newSet);
//        additions.removeAll(oldSet);
//        Set removals = new HashSet(oldSet);
//        removals.removeAll(newSet);
//        return createSetDiff(additions, removals);
//    }
    
//    /**
//     * Computes the difference between two maps.
//     * 
//     * @param oldMap
//     * @param newMap
//     * @return a map diff representing the changes needed to turn oldMap into
//     *  newMap
//     */
//    public static MapDiff computeMapDiff(Map oldMap, Map newMap) {
//        // starts out with all keys from the new map, we will remove keys from
//        // the old map as we go
//        final Set addedKeys = new HashSet(newMap.keySet());
//        final Set removedKeys = new HashSet();
//        final Set changedKeys = new HashSet();
//        final Map oldValues = new HashMap();
//        final Map newValues = new HashMap();
//        for (Iterator it = oldMap.entrySet().iterator(); it.hasNext();) {
//            Map.Entry oldEntry = (Entry) it.next();
//            Object oldKey = oldEntry.getKey();
//            if (addedKeys.remove(oldKey)) {
//                // potentially changed key since it is in oldMap and newMap
//                Object oldValue = oldEntry.getValue();
//                Object newValue = newMap.get(oldKey);
//                if (!Util.equals(oldValue, newValue)) {
//                    changedKeys.add(oldKey);
//                    oldValues.put(oldKey, oldValue);
//                    newValues.put(oldKey, newValue);
//                }
//            } else {
//                removedKeys.add(oldKey);
//                oldValues.put(oldKey, oldEntry.getValue());
//            }
//        }
//        for (Iterator it = addedKeys.iterator(); it.hasNext();) {
//            Object newKey = it.next();
//            newValues.put(newKey, newMap.get(newKey));
//        }
//        return new MapDiff() {
//            public Set getAddedKeys() {
//                return addedKeys;
//            }
//            
//            public Set getChangedKeys() {
//                return changedKeys;
//            }
//            
//            public Set getRemovedKeys() {
//                return removedKeys;
//            }
//            
//            public Object getNewValue(Object key) {
//                return newValues.get(key);
//            }
//            
//            public Object getOldValue(Object key) {
//                return oldValues.get(key);
//            }
//        };
//    }
    
    /**
     * @param oldValue
     * @param newValue
     * @return a value diff
     */
    public static <T> ValueDiff<T> createValueDiff(final T oldValue, final T newValue) {
        return new ValueDiff<T>() {
            
            public T getOldValue() {
                return oldValue;
            }
            
            public T getNewValue() {
                return newValue;
            }
        };
    }
    
//    /**
//     * @param additions
//     * @param removals
//     * @return a set diff
//     */
//    public static SetDiff createSetDiff(Set additions, Set removals) {
//        final Set unmodifiableAdditions = Collections.unmodifiableSet(additions);
//        final Set unmodifiableRemovals = Collections.unmodifiableSet(removals);
//        return new SetDiff() {
//            
//            public Set getAdditions() {
//                return unmodifiableAdditions;
//            }
//            
//            public Set getRemovals() {
//                return unmodifiableRemovals;
//            }
//        };
//    }
    
//    /**
//     * @param difference
//     * @return a list diff with one differing entry
//     */
//    public static ListDiff createListDiff(ListDiffEntry difference) {
//        return createListDiff(new ListDiffEntry[] { difference });
//    }
    
//    /**
//     * @param difference1
//     * @param difference2
//     * @return a list diff with two differing entries
//     */
//    public static ListDiff createListDiff(ListDiffEntry difference1, ListDiffEntry difference2) {
//        return createListDiff(new ListDiffEntry[] { difference1, difference2 });
//    }
    
//    /**
//     * @param differences
//     * @return a list diff with the given entries
//     */
//    public static ListDiff createListDiff(final ListDiffEntry[] differences) {
//        return new ListDiff() {
//            public ListDiffEntry[] getDifferences() {
//                return differences;
//            }
//        };
//    }
    
//    /**
//     * @param position
//     * @param isAddition
//     * @param element
//     * @return a list diff entry
//     */
//    public static ListDiffEntry createListDiffEntry(final int position, final boolean isAddition,
//            final Object element) {
//        return new ListDiffEntry() {
//            
//            public int getPosition() {
//                return position;
//            }
//            
//            public boolean isAddition() {
//                return isAddition;
//            }
//            
//            public Object getElement() {
//                return element;
//            }
//        };
//    }
    
//    /**
//     * @param addedKey
//     * @param newValue
//     * @return a map diff
//     */
//    public static MapDiff createMapDiffSingleAdd(final Object addedKey, final Object newValue) {
//        return new MapDiff() {
//            
//            public Set getAddedKeys() {
//                return Collections.singleton(addedKey);
//            }
//            
//            public Set getChangedKeys() {
//                return Collections.EMPTY_SET;
//            }
//            
//            public Object getNewValue(Object key) {
//                return newValue;
//            }
//            
//            public Object getOldValue(Object key) {
//                return null;
//            }
//            
//            public Set getRemovedKeys() {
//                return Collections.EMPTY_SET;
//            }
//        };
//    }
    
//    /**
//     * @param existingKey
//     * @param oldValue
//     * @param newValue
//     * @return a map diff
//     */
//    public static MapDiff createMapDiffSingleChange(final Object existingKey, final Object oldValue,
//            final Object newValue) {
//        return new MapDiff() {
//            
//            public Set getAddedKeys() {
//                return Collections.EMPTY_SET;
//            }
//            
//            public Set getChangedKeys() {
//                return Collections.singleton(existingKey);
//            }
//            
//            public Object getNewValue(Object key) {
//                return newValue;
//            }
//            
//            public Object getOldValue(Object key) {
//                return oldValue;
//            }
//            
//            public Set getRemovedKeys() {
//                return Collections.EMPTY_SET;
//            }
//        };
//    }
    
//    /**
//     * @param removedKey
//     * @param oldValue
//     * @return a map diff
//     */
//    public static MapDiff createMapDiffSingleRemove(final Object removedKey, final Object oldValue) {
//        return new MapDiff() {
//            
//            public Set getAddedKeys() {
//                return Collections.EMPTY_SET;
//            }
//            
//            public Set getChangedKeys() {
//                return Collections.EMPTY_SET;
//            }
//            
//            public Object getNewValue(Object key) {
//                return null;
//            }
//            
//            public Object getOldValue(Object key) {
//                return oldValue;
//            }
//            
//            public Set getRemovedKeys() {
//                return Collections.singleton(removedKey);
//            }
//        };
//    }
    
//    /**
//     * @param copyOfOldMap
//     * @return a map diff
//     */
//    public static MapDiff createMapDiffRemoveAll(final Map copyOfOldMap) {
//        return new MapDiff() {
//            
//            public Set getAddedKeys() {
//                return Collections.EMPTY_SET;
//            }
//            
//            public Set getChangedKeys() {
//                return Collections.EMPTY_SET;
//            }
//            
//            public Object getNewValue(Object key) {
//                return null;
//            }
//            
//            public Object getOldValue(Object key) {
//                return copyOfOldMap.get(key);
//            }
//            
//            public Set getRemovedKeys() {
//                return copyOfOldMap.keySet();
//            }
//        };
//    }
    
//    /**
//     * @param addedKeys
//     * @param removedKeys
//     * @param changedKeys
//     * @param oldValues
//     * @param newValues
//     * @return a map diff
//     */
//    public static MapDiff createMapDiff(final Set addedKeys, final Set removedKeys, final Set changedKeys,
//            final Map oldValues, final Map newValues) {
//        return new MapDiff() {
//            
//            public Set getAddedKeys() {
//                return addedKeys;
//            }
//            
//            public Set getChangedKeys() {
//                return changedKeys;
//            }
//            
//            public Object getNewValue(Object key) {
//                return newValues.get(key);
//            }
//            
//            public Object getOldValue(Object key) {
//                return oldValues.get(key);
//            }
//            
//            public Set getRemovedKeys() {
//                return removedKeys;
//            }
//        };
//    }
}
