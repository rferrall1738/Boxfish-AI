package dots.foureighty.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class SkippableIterator<T> implements Iterator<T> {
    /***
     * Prunes the current branch of the iterator.
     * @throws NoSuchElementException if not on a branch.
     */
    public abstract void pruneCurrentBranch() throws NoSuchElementException;
}
