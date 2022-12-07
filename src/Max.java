import java.util.*;
import java.util.concurrent.*;

public class Max implements Callable<String> {

    @Override
    public String call() throws Exception {

        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        for (String text : texts) {
            int sizeA = 0;
            int count = 0;
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == 'a') {
                    count++;
                    if (count > sizeA)
                        sizeA = count;
                } else count = 0;
            }
            return text.substring(0, 100) + " -> " + sizeA;
        }
        return null;

    }


    public static void main(String[] args) throws Exception {

        long startTs = System.currentTimeMillis();

        List<Future<String>> futures = new ArrayList<>(25);

        Callable<String> myCallable = new Max();

        final ExecutorService threadPool = Executors.newFixedThreadPool(25);

        for (int i = 0; i < 25; i++) {
            Future<String> future = threadPool.submit(myCallable);
            futures.add(future);
        }

        for (Future<String> a : futures) {
            System.out.println(a.get());
        }

        threadPool.shutdown();

        long endTs = System.currentTimeMillis();

        System.out.println("Time: " + (endTs - startTs) + "ms");
    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

}