### **9. Итоговый проект: Консольный трекер задач**
**Цель**: Объединить все навыки в приложении с сохранением состояния.

#### **План решения и обоснование**
1. **Структура данных** (процедурный стиль):
    - Используем `List<Map<String, String>>` вместо класса `Task` (как в п.3).  
      *Обоснование*: Избегаем ООП, работаем с коллекциями как в п.3.

2. **Ввод/вывод и обработка ошибок** (п.4, п.8):
    - Меню через `Scanner` (п.4).
    - Сохранение в `tasks.csv` через `BufferedWriter` (п.8).  
      *Обоснование*: Повторяем шаблоны из п.4 и п.8, но объединяем их.

3. **Сборка** (п.6, п.7):
    - Сборка через Maven (п.7) для упрощения запуска.

#### **Пример структуры кода**
```java
import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_NAME = "tasks.csv";

    public static void main(String[] args) {
        List<Map<String, String>> tasks = loadTasks(); // Из п.8
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            int choice = getValidIntInput(scanner); // Обработка ошибок из п.4

            if (choice == 1) {
                addTask(tasks, scanner);
                saveTasks(tasks); // Из п.8
            } else if (choice == 2) {
                printTasks(tasks);
            } else if (choice == 3) {
                break;
            }
        }
    }

    // Методы из п.3, п.4, п.8
    private static List<Map<String, String>> loadTasks() { ... }
    private static void saveTasks(List<Map<String, String>> tasks) { ... }
    private static int getValidIntInput(Scanner scanner) { ... }
}
```

#### **Критерии проверки**
- При запуске `java -jar tracker.jar` выводится меню.
- Данные сохраняются в `tasks.csv` и загружаются при повторном запуске.
- Обработка ошибок: некорректный ввод числа, отсутствие файла.

#### **Каверзные вопросы**
- ❓ Что произойдет, если в `tasks.csv` будет строка `Buy milk,high` (без статуса)?  
  *Ответ*: Программа пропустит строку (как в п.8 при `FileNotFoundException`).
- ❓ Почему `try-with-resources` безопаснее ручного закрытия файла?  
  *Ответ*: Гарантирует закрытие даже при ошибках (как в п.4).  