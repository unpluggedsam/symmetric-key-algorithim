import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OneTimePad {

    enum KEY_SIZE {
        ONE_HUNDRED_TWENTY_EIGHT(128), ONE_HUNDRED_NINETY_SIX(196), TWO_HUNDRED_FIFTY_SIX(256);

        final int size;

        KEY_SIZE(int size) {
            this.size = size;
        }

        int getSize() {
            return size;
        }
    }

    public int[] encryptString(String value, int[] key) {
        int[] binary = convertStringToBinary(value);
        List<int[]> blockBits = blockBinaryBits(binary);
        return XORBlockBits(blockBits, key, binary.length);
    }

    public String decryptBinary(int[] binary, int[] key) {
        List<int[]> blockBits = blockBinaryBits(binary);
        int[] decryptedBits = XORBlockBits(blockBits, key, binary.length);
        return convertBinaryToString(decryptedBits);
    }

    private int[] convertStringToBinary(String value) {

        StringBuilder strBuilder = new StringBuilder();
        char[] chars = value.toCharArray();
        for (char aChar : chars) {
            strBuilder.append(
                    String.format("%8s", Integer.toBinaryString(aChar))   // char -> int, auto-cast
                            .replaceAll(" ", "0")
            );
        }

        String str = strBuilder.toString();
        int[] result = new int[str.length()];

        for (int i = 0; i < str.length(); i++) {
            result[i] = str.charAt(i) - '0';
        }

        return result;
    }

    // applys XOR operation to each block
    private int[] XORBlockBits(List<int[]> blockBits, int[] key, int size) {
        int[] result = new int[size];

        AtomicInteger i = new AtomicInteger();
        blockBits.forEach(block -> {
            System.arraycopy(XORBinary(block, key), 0, result, i.get(), XORBinary(block, key).length);
            i.addAndGet(XORBinary(block, key).length);
        });
        return result;
    }

    /**
     * XOR operation.
     * 0 ⊕ 0 = 0
     * 0 ⊕ 1 = 1
     * 1 ⊕ 0 = 1
     * 1 ⊕ 1 = 0
     * essentially  x ⊕ 0 = x
     * and x ⊕ 1 = opposite of x
     * ^ is the XOR operator
     **/
    private int[] XORBinary(int[] binary, int[] key) {

        int[] result = new int[binary.length];

        int i = 0;

        for (int z = 0; z < binary.length; z++) {
            if (i >= key.length) i = 0;
            result[z] = binary[z] ^ key[i];
            i++;
        }
        return result;
    }

    /**
     * Chops up the binary into a bunch of blocks,
     * this makes it so the key can be reapplied.
     * If the binary does not reach the 128 block chunk
     * then a smaller array is created.
     */
    private List<int[]> blockBinaryBits(int[] binary) {
        List<int[]> blockBits = new ArrayList<>();
        for (int i = 0; i < binary.length; i += 128) {
            int[] block;
            if (binary.length - i < 128) {
                block = new int[binary.length - i];
                System.arraycopy(binary, i, block, 0, binary.length - i);
            } else {
                block = new int[128];
                System.arraycopy(binary, i, block, 0, 128);
            }
            blockBits.add(block);
        }
        return blockBits;
    }

    private String convertBinaryToString(int[] binaryArray) {
        String binary = Arrays.stream(binaryArray)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());


        StringBuilder sb = new StringBuilder();

        Arrays.stream(
                binary.split("(?<=\\G.{8})")
        ).forEach(s ->
                sb.append((char) Integer.parseInt(s, 2))
        );

        return sb.toString();
    }


   // very simply generates a key.
   // if the random number is > 0.5 then it's 0 or else it's 1
    public int[] generateKey(KEY_SIZE keySize) {

        int[] key = new int[keySize.getSize()];

        for (int i = 0; i < keySize.getSize(); i++) {
            if (Math.random() < 0.5) {
                key[i] = 0;
            } else {
                key[i] = 1;
            }
        }
        return key;
    }

}
