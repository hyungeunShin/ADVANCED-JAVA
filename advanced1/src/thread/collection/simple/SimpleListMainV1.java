package thread.collection.simple;

public class SimpleListMainV1 {
    //단일 스레드로 실행했기 때문에 아무런 문제가 없다.
    public static void main(String[] args) {
        SimpleList list = new SimpleListV1();
        list.add("A");
        list.add("B");
        System.out.println(list);
    }
}
