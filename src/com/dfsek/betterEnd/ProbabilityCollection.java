package com.dfsek.betterend;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

public class ProbabilityCollection<E> {

    protected final Comparator<ProbabilitySetElement<E>> comparator =
            (o1, o2)-> Integer.compare(o1.getIndex(), o2.getIndex());

    private final TreeSet<ProbabilitySetElement<E>> collection;

    private int totalProbability;

    /**
     * Construct a new Probability Collection
     */
    public ProbabilityCollection() {
        this.collection = new TreeSet<>(this.comparator);
        this.totalProbability = 0;
    }

    /**
     * @return Number of objects inside the collection
     */
    public int size() {
        return this.collection.size();
    }

    /**
     * @return Collection contains no elements
     */
    public boolean isEmpty() {
        return this.collection.isEmpty();
    }

    /**
     * @param object
     * @return True if the collection contains the object, else False
     */
    public boolean contains(E object) {
        return this.collection.stream()
                .anyMatch(entry -> entry.getObject().equals(object));
    }

    /**
     * @return Iterator over collection
     */
    public Iterator<ProbabilitySetElement<E>> iterator() {
        return this.collection.iterator();
    }

    /**
     * Add an object to this collection
     *
     * @param object
     * @param probability share
     */
    public void add(E object, int probability) {
        this.collection.add(new ProbabilitySetElement<E>(object, probability));
        this.totalProbability += probability;
        this.updateIndexes();
    }

    /**
     * Remove a object from this collection
     *
     * @param object
     * @return True if object was removed, else False.
     */
    public boolean remove(E object) {
        Iterator<ProbabilitySetElement<E>> it = this.iterator();
        boolean removed = false;

        while(it.hasNext()) {
            ProbabilitySetElement<E> element = it.next();
            if(element.getObject().equals(object)) {
                removed = true;
                this.totalProbability -= element.getProbability();
                it.remove();
            }
        }

        this.updateIndexes();
        return removed;
    }

    /**
     * @return Random object based on probability
     */
    public E get() {
        ProbabilitySetElement<E> toFind = new ProbabilitySetElement<>(null, 0);
        toFind.setIndex(ThreadLocalRandom.current().nextInt(1, this.totalProbability + 1));

        return this.collection.floor(toFind).getObject();
    }
    public E get(Random random) {
        ProbabilitySetElement<E> toFind = new ProbabilitySetElement<>(null, 0);
        toFind.setIndex(random.nextInt(this.totalProbability) + 1);

        return this.collection.floor(toFind).getObject();
    }

    /**
     * @return Sum of all element's probability
     */
    public final int getTotalProbability() {
        return this.totalProbability;
    }

    /*
     * Calculate the size of all element's "block" of space:
     * i.e 1-5, 6-10, 11-14, 15, 16
     *
     * We then only need to store the start index of each element
     */
    private void updateIndexes() {
        int previousIndex = 0;

        for(ProbabilitySetElement<E> entry : this.collection) {
            previousIndex = entry.setIndex(previousIndex + 1) + (entry.getProbability() - 1);
        }
    }

    /**
     * Used internally to store information about a object's
     * state in a collection. Specifically, the probability
     * and index within the collection.
     *
     * Indexes refer to the start position of this element's "block" of space.
     * The space between element "block"s represents their probability of being selected
     *
     * @author Lewys Davies
     *
     * @param <T> Type of element
     */
    final static class ProbabilitySetElement<T> {
        private final T object;
        private final int probability;
        private int index;

        /**
         * @param object
         * @param probability
         */
        protected ProbabilitySetElement(T object, int probability) {
            this.object = object;
            this.probability = probability;
        }

        /**
         * @return The actual object
         */
        public final T getObject() {
            return this.object;
        }

        /**
         * @return Probability share in this collection
         */
        public final int getProbability() {
            return this.probability;
        }

        // Used internally, see this class's documentation
        protected final int getIndex() {
            return this.index;
        }

        // Used Internally, see this class's documentation
        protected final int setIndex(int index) {
            this.index = index;
            return this.index;
        }
    }
}
