package hw02;

import java.util.*;

/*
DIY ArrayList
Написать свою реализацию ArrayList на основе массива.
class DIYarrayList<T> implements List<T>{...}

Проверить, что на ней работают методы из java.util.Collections:
Collections.addAll(Collection<? super T> c, T... elements)
Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)
Collections.static <T> void sort(List<T> list, Comparator<? super T> c)

1) Проверяйте на коллекциях с 20 и больше элементами.
2) DIYarrayList должен имплементировать ТОЛЬКО ОДИН интерфейс - List.
3) Если метод не имплементирован, то он должен выбрасывать исключение UnsupportedOperationException.
 */

public class Test {
    public static void main(String[] args) {

        List<Integer> list = new DIYarrayList<>();

        // fill the array with random data
        Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            list.add(rand.nextInt(100));
        }
        System.out.println("initial list:\n" + list + ", size = " + list.size());

        Collections.addAll(list, 1, 2, 3 );
        System.out.println("\naddAll() test:\n" + list + ", size = " + list.size());

        List<Integer> src = Arrays.asList(new Integer[]{1111, 222, 3333, 4444, 5555});
        Collections.copy(list, src);
        System.out.println("\ncopy() test:\n" + list + ", size = " + list.size());

        Collections.sort(list);
        System.out.println("\nsort() test:\n" + list + ", size = " + list.size());
    }
}
