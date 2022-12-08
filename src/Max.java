import java.util.*;
import java.util.concurrent.*;

public class Max {

    public static void main(String[] args) throws Exception {

        long startTs = System.currentTimeMillis();

        List<Future<String>> futures = new ArrayList<>(25);

        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        final ExecutorService threadPool = Executors.newFixedThreadPool(25);

        for (String text : texts) {
            Future<String> future = threadPool.submit(() -> {
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
            });

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
