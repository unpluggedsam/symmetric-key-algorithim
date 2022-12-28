import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        OneTimePad pad = new OneTimePad();

        int[] key = pad.generateKey(OneTimePad.KEY_SIZE.ONE_HUNDRED_NINETY_SIX);

        int[] encrypted = pad.encryptString("Hello world, I heard that you enjoy wacki mac on your pasta!" , key);

        System.out.println("\nEncrypted: ");
        System.out.println(convertBinaryToString(encrypted));

        String decrypted = pad.decryptBinary(encrypted, key);

        System.out.println("\nDecrypted: ");
        System.out.println(decrypted);
    }

    private static String convertBinaryToString(int[] binaryArray) {
        String binary = Arrays.stream(binaryArray)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());


        StringBuilder sb = new StringBuilder(); // Some place to store the chars

        Arrays.stream( // Create a Stream
                binary.split("(?<=\\G.{8})") // Splits the input string into 8-char-sections (Since a char has 8 bits = 1 byte)
        ).forEach(s -> // Go through each 8-char-section...
                sb.append((char) Integer.parseInt(s, 2)) // ...and turn it into an int and then to a char
        );

        return sb.toString();
    }

}