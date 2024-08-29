package thread.collection.simple;

public class SimpleListV3 implements SimpleList {
    private final SimpleList target;

    public SimpleListV3(SimpleList target) {
        this.target = target;
    }

    @Override
    public synchronized int size() {
        return target.size();
    }

    @Override
    public synchronized void add(Object e) {
        target.add(e);
    }

    @Override
    public synchronized Object get(int index) {
        return target.get(index);
    }

    @Override
    public synchronized String toString() {
        return target.toString() + " by " + this.getClass().getSimpleName();
    }
}
