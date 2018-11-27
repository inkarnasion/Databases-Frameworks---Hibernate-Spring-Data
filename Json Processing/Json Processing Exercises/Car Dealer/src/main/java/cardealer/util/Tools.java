package cardealer.util;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public class Tools {

    public static <T> List<T> getRandomList(JpaRepository<T, Integer> repository, int count) {
        Random random = new Random();

        List<T> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            list.add(repository.getOne(random.nextInt((int) repository.count() - 1) + 1));
        }

        return list;
    }

    public static <T> T getRandom(List<T> list) {
        T result;
        int randomIndex = new Random().nextInt(list.size());

        result = list.get(randomIndex);

        return result;
    }

    public static <T> T getRandomIndex(JpaRepository<T, Integer> repository) {
        T result;
        Random random = new Random();

        result = repository.getOne(random.nextInt((int) repository.count() - 1) + 1);

        return result;
    }

    public static <T> T getRandom(T[] list) {
        T result;
        int randomIndex = new Random().nextInt(list.length);

        result = list[randomIndex];

        return result;
    }

    public static <T> T getRandom(Set<T> set) {
        T result;
        int randomIndex = new Random().nextInt(set.size());

        result = new ArrayList<T>(set).get(randomIndex);

        return result;
    }

    public static int getRandomNumber(int min, int max) {
        return new Random().nextInt(max - min) + min + 1;
    }


    public static String format(String s, int width) {
        StringBuilder result = new StringBuilder();

        result.append("| ");

        for (int i = 0; i < width - s.length(); i++) {
            result.append(" ");
        }

        result.append(s);

        result.append(" |");

        return result.toString();
    }

    public static String printFrame(String s, int width) {
        StringBuilder result = new StringBuilder();

        result.append("+");
        for (int i = 0; i < width - 2; i++) {
            result.append("-");
        }
        result.append("+\n");

        result.append("|");
        for (int i = 0; i < ((width - s.length() - 2) / 2); i++) {
            result.append(" ");
        }
        result.append(s);
        for (int i = 0; i < ((width - s.length() - 2) / 2); i++) {
            result.append(" ");
        }
        result.append("|\n");

        result.append("+");
        for (int i = 0; i < width - 2; i++) {
            result.append("-");
        }
        result.append("+");

        return result.toString();
    }

    public static <K, V> TreeMap<K, V> convertMapToSortedByCriteriaMap(Map<K, V> mapToSort, Comparator<K> comparator) {
        TreeMap<K, V> result = new TreeMap<K, V>(comparator);
        result.putAll(mapToSort);

        return result;
    }

    public static <K, V> List<Map.Entry<K, V>> convertMapToSortedByCriteriaList(Map<K, V> mapToSort, Comparator<Map.Entry<K, V>> comparator) {
        List<Map.Entry<K, V>> result = new ArrayList<>(mapToSort.entrySet());

        Collections.sort(result, comparator);

        return result;
    }

    public static <K, V> K findKey(Map<K, V> map, K keyToSearch) {
        K result = null;

        for (K key : map.keySet()) {
            if (key.equals(keyToSearch)) {
                result = key;
                break;
            }
        }

        return result;
    }

    public static <T> T getElementFromArray(T element, List<T> array) {
        T foundElement = null;

        for (int i = 0; i < array.size() && foundElement == null; i++) {
            if (element.equals(array.get(i))) {
                foundElement = array.get(i);
            }
        }

        return foundElement;
    }
}