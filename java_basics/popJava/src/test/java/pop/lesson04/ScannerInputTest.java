package pop.lesson04;

import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;

import static org.testng.Assert.assertEquals;
import static pop.lesson04.ScannerInput.*;

/**
 * Принцип проверки методов, применяющих ввод/вывод консоли
 * 1. Строку ожидаемого результата конвертирую в ByteArray `expected`
 *  ByteArrayInputStream testInput = new ByteArrayInputStream("expected result".getBytes());
 * 2. Создаю поток вывода для значения `expected`
 *  PrintStream customOut = new PrintStream(testInput);
 * 3. Сохраняю ссылку на поток stdout
 *  PrintStream originalOut = System.out;
 * 4. Устанавливаю в качестве потока вывода Java `customOut`
 *  System.setOut(customOut);
 * 5. Стартую выполнение тестируемого метода
 *  doSmthThatPrintsToConsole();
 *  // здесь вывод метода записывает в `customOut`
 * 6. Очищаю содержимое потока - сейчас для System.out активен `customOut`
 *  System.out.flush();
 * 7. Возвращаю stdout для направления System.out
 *  System.setOut(originalOut);
 * 8. Декодирую `customOut`
 *  customOut.toString()
*/
public class ScannerInputTest {

    @Test
    public void testGetName() {
        final String userInput = "UserName";
        final ByteArrayInputStream testInput = new ByteArrayInputStream(userInput.getBytes());
        PrintStream originalOut = System.out;
        
        final String expectedOutput = "UserName";
        final String actualOutput = getName(new Scanner(testInput), originalOut);

        logger.log(Level.INFO, String.format("ожидание: %s", expectedOutput));
        logger.log(Level.INFO, String.format("реальность: %s", actualOutput));
        assertEquals(actualOutput, expectedOutput);
    }

    @Test
    public void testGetCommand() {
        final String userInput = "/add";
        final ByteArrayInputStream testInput = new ByteArrayInputStream(userInput.getBytes());
        PrintStream originalOut = System.out;
        final String expectedOutput = "/add";

        final Scanner scanner = new Scanner(testInput);
        final String actualOutput = getCommand(scanner, originalOut);

        logger.log(Level.INFO, String.format("ожидание: %s", expectedOutput));
        logger.log(Level.INFO, String.format("реальность: %s", actualOutput));

        assertEquals(actualOutput, expectedOutput);
    }

    @Test
    public void testGetAge() {
        final String userInput = "123";
        final ByteArrayInputStream testInput = new ByteArrayInputStream(userInput.getBytes());
        PrintStream originalOut = System.out;
        final int expectedOutput = 123;

        final Scanner scanner = new Scanner(testInput);
        final int actualOutput = getAge(scanner, originalOut);

        logger.log(Level.INFO, String.format("ожидание: %s", expectedOutput));
        logger.log(Level.INFO, String.format("реальность: %s", actualOutput));

        assertEquals(actualOutput, expectedOutput);
    }

    @Test
    public void testFormatLinkedHashSet() {
        final LinkedHashSet<String> commands = new LinkedHashSet<>() {{
            add("/cmd1");
            add("/cmd2");
            add("/cmd3");
        }};

        final String expectedOutput = """
            /cmd1
            /cmd2
            /cmd3""";

        final String actualOutput = formatLinkedHashSet(commands);

        logger.log(Level.INFO, String.format("ожидание: %s", expectedOutput));
        logger.log(Level.INFO, String.format("реальность: %s", actualOutput));

        assertEquals(actualOutput, expectedOutput);
    }

    @Test
    public void testPrintWelcome() {
        String expectedOutput = """
            Программа создает словарь имя->возраст
            и позволяет добавить записи в словарь
            
            Чтобы начать добавление записи, выполни /add
            Чтобы посмотреть существующие записи, выполни /show
            Для вывода всех поддерживаемых комманд, выполни /help
            
            """;

        PrintStream originalOut = System.out;
        // создаю поток для считывания реального вывода
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        PrintStream customStreamOut = new PrintStream(bytesOut);
        // заменяю stdout своим потоком
        System.setOut(customStreamOut);

        printWelcome(System.out);

        // очищаю поток
        System.out.flush();
        // возвращаю stdout
        System.setOut(originalOut);

        String actualOutput = bytesOut.toString();

        logger.log(Level.INFO, String.format("ожидание: %s", expectedOutput));
        logger.log(Level.INFO, String.format("реальность: %s", actualOutput));

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintHelp() {
        String expectedOutput = """
            ==========
            Поддерживаемые команды
            """ +
            "\nдобавить запись:\n" + formatLinkedHashSet(addRecordCommands) +
            "\n" +
            "\nнапечатать сохраненные записи:\n" + formatLinkedHashSet(printRecordsCommands) +
            "\n" +
            "\nзавершить работу приложения:\n" + formatLinkedHashSet(exitCommands) +
            "\n" +
            "\nнапечатать это сообщение:\n" + formatLinkedHashSet(printHelpCommands) +
            "\n";

        PrintStream originalOut = System.out;
        // создаю поток для считывания реального вывода
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        PrintStream customStreamOut = new PrintStream(bytesOut);
        // заменяю stdout своим потоком
        System.setOut(customStreamOut);

        printHelp(System.out);

        // очищаю поток
        System.out.flush();
        // возвращаю stdout
        System.setOut(originalOut);

        String actualOutput = bytesOut.toString();

        logger.log(Level.INFO, String.format("ожидание: %s", expectedOutput));
        logger.log(Level.INFO, String.format("реальность: %s", actualOutput));

        assertEquals(actualOutput, expectedOutput);
    }

    @Test
    public void testPrintRecordsEmpty() {
        SortedMap<String, Integer> emptyRecords = new TreeMap<>();
        String expectedOutput = "Каталог записей пуст";

        PrintStream originalOut = System.out;
        // создаю поток для считывания реального вывода
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        PrintStream customStreamOut = new PrintStream(bytesOut);
        // заменяю stdout своим потоком
        System.setOut(customStreamOut);

        printRecords(emptyRecords, System.out);

        // очищаю поток
        System.out.flush();
        // возвращаю stdout
        System.setOut(originalOut);

        String actualOutput = bytesOut.toString();

        logger.log(Level.INFO, String.format("ожидание: %s", expectedOutput));
        logger.log(Level.INFO, String.format("реальность: %s", actualOutput));

        assertEquals(actualOutput, expectedOutput);
    }

    @Test
    public void testPrintRecordsOneUser() {
        String expectedOutput = "{Username=123}";

        SortedMap<String, Integer> recordsOneUser = new TreeMap<>();
        recordsOneUser.put("Username", 123);

        PrintStream originalOut = System.out;
        // создаю поток для считывания реального вывода
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        PrintStream customStreamOut = new PrintStream(bytesOut);
        // заменяю stdout своим потоком
        System.setOut(customStreamOut);

        printRecords(recordsOneUser, System.out);

        // очищаю поток
        System.out.flush();
        // возвращаю stdout
        System.setOut(originalOut);

        String actualOutput = bytesOut.toString();

        logger.log(Level.INFO, String.format("ожидание: %s", expectedOutput));
        logger.log(Level.INFO, String.format("реальность: %s", actualOutput));

        assertEquals(actualOutput, expectedOutput);
    }

    @Test
    public void testPrintRecordsThreeUsers() {
        String expectedOutput = "{Username1=64, Username2=32, Username3=16}";

        SortedMap<String, Integer> recordsThreeUsers = new TreeMap<>();
        recordsThreeUsers.put("Username1", 64);
        recordsThreeUsers.put("Username2", 32);
        recordsThreeUsers.put("Username3", 16);

        PrintStream originalOut = System.out;
        // создаю поток для считывания реального вывода
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        PrintStream customStreamOut = new PrintStream(bytesOut);
        // заменяю stdout своим потоком
        System.setOut(customStreamOut);

        printRecords(recordsThreeUsers, System.out);

        // очищаю поток
        System.out.flush();
        // возвращаю stdout
        System.setOut(originalOut);

        String actualOutput = bytesOut.toString();

        logger.log(Level.INFO, String.format("ожидание: %s", expectedOutput));
        logger.log(Level.INFO, String.format("реальность: %s", actualOutput));

        assertEquals(actualOutput, expectedOutput);
    }
}