This project is an implementation of a symmetric key algorithim in Java. It generates a key, and then will securely 
encrypt and decrypt Strings with that key.
## How It Works
### Generating the Key
The first step to the algorithim is to generate a key. The key can have 3 sizes, 128, 196, or 256 bits. 
The bits are randomly generated and put into an integer array of the given size. 
The call can be made as: 
```java
int[] key = pad.generateKey(OneTimePad.KEY_SIZE.ONE_HUNDRED_NINETY_SIX); 
```
Key sizes can be specified using the `KEY_SIZE` enum.
### Encrypting the String
Encrypting the String has multiple steps. The first of which is to make the call to the `encryptString` method and pass in the String and the generated Key.
```Java
int[] encrypted = pad.encryptString("Hello world, I heard that you enjoy wacki mac on your pasta!" , key);
```
The call will return a new array of encrypted bits. It does this in a few steps.
#### Convert the String to Binary
The first step of encryption is to convert the given String into binary. The program takes advantage of the `Integer`'s class
`toBinaryString()` method and simply converts each character into it's corresponding binary value. 
#### Block the Binary Array
The next step is to convert the long binary array into 128 bit chunks. This is so the array is not too long 
and unwieldly. This allows the generated key to be reapplied to each block. 
#### XOR The First Block
The first block would have the XOR operation applied to it. Then, it's encrypted value would be stored.
#### Use the Previous Blocks Encrypted Value and Repeat Until Fully Encrypted
The next block would then use the previously encrypted blocks value as a key. Then, that encrypted value would also be XOR'ed using the original key. This process would be repeated until all the blocks were encrypted.
#### XOR operation
The XOR operation is relatively simple. It iterates through every value in the Strings binary value, and the key and compares them. 
It then uses Java's built in XOR operator, `^`, and the correct value is returned. The XOR operator provides the following computation:
 <br>
 <br> 0 ⊕ 0 = 0
 <br> 0 ⊕ 1 = 1
 <br> 1 ⊕ 0 = 1
 <br> 1 ⊕ 1 = 0
 <br>

Essentially x ⊕ 0 = x, and x ⊕ 1 = opposite of x. 

After the binary value is encrypted with the key each block is concatenated and returned.

### Decrypting the Binary
Decrypting the binary is essentially the same process as encrypting the binary. A similair call to the encryption must be made: 
```Java
String decrypted = pad.decryptBinary(encrypted, key);
```
#### Block the Binary Array
Same as encrypting, the binary array must first be blocked.

#### XOR the First Block
Also same as encrypting, the first block must be decrypted with the key.

#### Use the Previous Blocks Encrypted Value and Repeat Until Fully Encrypted
This process is similair to the encrypting method, but has a minor difference. First, the encrypted value
is XOR'ed using the key, and then it is XOR'ed using the previous block.

#### Convert the binary back to a String
The final step is to convert the binary back to a String. This process takes advantage of the `Integer`'s class `parseInt()` method.

## Sample Code

```Java
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        
        OneTimePad pad = new OneTimePad();

        int[] key = pad.generateKey(OneTimePad.KEY_SIZE.ONE_HUNDRED_NINETY_SIX);

        int[] encrypted = pad.encryptString("Hello World, I heard that you enjoy wacki mac on your pasta!" , key);

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
```

Sample output: 

```
Encrypted: 
±XkÅÍÕØwµþMeú®\uÍçyùãÑ¿¨R~Õìsù÷ Çµ¨ÙDhÜÐÕÿy­û@

Decrypted: 
Hello World, I heard that you enjoy wacki mac on your pasta!
```

Thanks for checking out my project!
