package org.example;

import java.util.Objects;
import java.util.Objects;              // Импортируем класс для удобного сравнения объектов

public class MyHashMap<K, V> {         // Объявляем обобщённый класс MyHashMap с параметрами ключа K и значения V
    private static class Entry<K, V> {  // Внутренний статический класс Entry для хранения пары ключ-значение
        final K key;                   // Ключ, объявлен как final — после создания не меняется
        V value;                       // Значение, связанное с ключом,которое может меняться
        Entry<K, V> next;              // Ссылка на следующий элемент в цепочке при коллизии(если совпаадют хэш ключа)

        Entry(K key, V value) {        // Конструктор Entry
            this.key = key;
            this.value = value;
        }
    }

    private static final int DEFAULT_CAPACITY = 16; // Размер массива по умолчанию
    private Entry<K, V>[] table;                     // Массив корзин
    private int size;                                // Текущее количество элементов в HashMap

    @SuppressWarnings("unchecked")                   // Подавляем предупреждение о небезопасном приведении типа
    public MyHashMap() {                              // Конструктор MyHashMap не имеющий параметров
        table = (Entry<K, V>[]) new Entry[DEFAULT_CAPACITY]; // Создаём массив корзин нужного размера
    }

    private int hash(K key) {                         // Метод для вычисления индекса корзины из ключа
        return key == null ? 0 : Math.abs(key.hashCode() % table.length); // Если ключ null, возвращаем 0, иначе берём остаток от деления hashCode на размер массива
    }

    public void put(K key, V value) {                 // Метод добавления пары ключ-значение
        int index = hash(key);                        // Вычисляем индекс корзины по ключу
        Entry<K, V> newEntry = new Entry<>(key, value); // Создаём новую запись с ключом и значением
        Entry<K, V> current = table[index];           // Берём начало цепочки в корзине

        if (current == null) {                         // Если в корзине пусто
            table[index] = newEntry;                   // Помещаем новую запись прямо в корзину
            size++;                                    // Увеличиваем счётчик элементов
            return;
        }

        Entry<K, V> prev = null;                        // Переменная для хранения предыдущего элемента при обходе цепочки
        while (current != null) {                       // Пока не достигли конца цепочки
            if (Objects.equals(current.key, key)) {   // Если ключ уже есть (сравниваем корректно null и объекты)
                current.value = value;                  // Обновляем значение для существующего ключа
                return;
            }
            prev = current;                            // Сохраняем текущий элемент как предыдущий
            current = current.next;                    // Переход к следующему элементу цепочки
        }

        prev.next = newEntry;                           // Добавляем новую запись в конец цепочки
        size++;
    }

    public V get(K key) {                               //  получаем значение по ключу
        int index = hash(key);                          // Вычисляем индекс корзины
        Entry<K, V> current = table[index];             // Начинаем с начала цепочки корзины

        while (current != null) {
            if (Objects.equals(current.key, key)) {    // Если нашли элемент с нужным ключом
                return current.value;                   // Возвращаем его значение
            }
            current = current.next;                      // Переходим к следующему элементу
        }

        return null;                                    // Если ключ не найден, возвращаем null
    }

    public void remove(K key) {                          // Метод  удаления элемента по ключу
        int index = hash(key);                           // Вычисляем индекс корзины
        Entry<K, V> current = table[index];              // Начинаем с головы цепочки
        Entry<K, V> prev = null;                          // Предыдущий элемент, нужен для корректного удаления

        while (current != null) {
            if (Objects.equals(current.key, key)) {      // Если нашли нужный ключ
                if (prev == null) {                       // Если удаляем первый элемент в цепочке то ->
                    table[index] = current.next;         // Переназначаем начало цепочки на следующий элемент
                } else {
                    prev.next = current.next;            // Иначе связываем предыдущий элемент с следующим, удаляя текущий
                }
                size--;
                return;
            }
            prev = current;                               // Сохраняем текущий элемент как предыдущий
            current = current.next;                       // Переход к следующему элементу
        }
    }

    public int size() {                                   // Возвращаем количество элементов в HashMap
        return size;
    }

    public void printTable() {                            //  метод для вывода содержимого HashMap
        for (int i = 0; i < table.length; i++) {         // Проходим по всем корзинам
            Entry<K, V> current = table[i];               // Начинаем с головы цепочки в корзине
            System.out.print("[" + i + "]: ");            //  индекс корзины
            while (current != null) {                      // Пока в цепочке есть элементы
                System.out.print( current.key + ", " + current.value + "-> "); // Выводим пару ключ-значение
                current = current.next;                    // Переходим к следующему элементу цепочки
            }
            System.out.println("null");                     // Отмечаем конец цепочки
        }
    }

    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        map.put("apple", 1);
        map.put("banana", 2);
        map.put("orange", 3);
        map.put("banana", 4);                               // Обновляем значение для ключа banana

        System.out.println("Всего banana: " + map.get("banana"));  // будет значение 4, так как значение обновлено
        map.remove("apple");                                 // Удаляем ключ apple
        System.out.println("После удаления apple: " + map.get("apple")); // будет null, так как удалили ключ apple

        map.printTable();
    }
}

