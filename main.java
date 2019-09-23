import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.BadPaddingException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
// BEGIN SOLUTION
// please import only standard libraries and make sure that your code compiles and runs without unhandled exceptions 
// END SOLUTION
 
public class HW2 {    
  static void P1() throws Exception {
    byte[] cipherBMP = Files.readAllBytes(Paths.get("cipher1.bmp"));
    
    // BEGIN SOLUTION
    byte[] key = new byte[] { 1, 2, 3, 4, 
                              5, 6, 7, 8, 
                              9, 10, 11, 12, 
                              13, 14, 15, 16 };

    byte [] iv = new byte[] {0, 0, 0, 0,
                              0, 0, 0, 0,
                              0, 0, 0, 0,
                              0, 0, 0, 0};
                              
    SecretKeySpec keyspec = new SecretKeySpec(key, 0, 16, "AES");
    IvParameterSpec ivspec = new IvParameterSpec(iv, 0, 16);
    Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
    cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

    byte[] plainBMP = cipher.doFinal(cipherBMP);    
    // END SOLUTION
    
    Files.write(Paths.get("plain1.bmp"), plainBMP);
    
  }

  static void P2() throws Exception {
    byte[] cipher = Files.readAllBytes(Paths.get("cipher2.bin"));
    // BEGIN SOLUTION
    byte[] modifiedCipher = cipher;

    

    for (int i=0;i<16;i++)
    {
      byte temp = cipher[i];
      modifiedCipher[i] = cipher[i+32];
      modifiedCipher[i+32] = temp;
    }
   
    
    byte[] key = new byte[] { 1, 2, 3, 4, 
      5, 6, 7, 8, 
      9, 10, 11, 12, 
      13, 14, 15, 16 };

    byte [] iv = new byte[] {0, 0, 0, 0,
          0, 0, 0, 0,
          0, 0, 0, 0,
          0, 0, 0, 0};
          
    SecretKeySpec keyspec = new SecretKeySpec(key, 0, 16, "AES");
    IvParameterSpec ivspec = new IvParameterSpec(iv, 0, 16);
    Cipher cipher2 = Cipher.getInstance("AES/CBC/NoPadding");
    cipher2.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

    byte[] plain = cipher2.doFinal(modifiedCipher);
    // END SOLUTION
    
    Files.write(Paths.get("plain2.txt"), plain);
  }

  static void P3() throws Exception {
    byte[] cipherBMP = Files.readAllBytes(Paths.get("cipher3.bmp"));
    byte[] otherBMP = Files.readAllBytes(Paths.get("plain1.bmp"));
    
    // BEGIN SOLUTION
    for (int i =0;i<2500;i++)
    {
      cipherBMP[i] = otherBMP[i];
    }
    byte[] modifiedBMP = (cipherBMP);
    // END SOLUTION
    
    Files.write(Paths.get("cipher3_modified.bmp"), modifiedBMP);
  }

  static void P4() throws Exception {
    byte[] plainA = Files.readAllBytes(Paths.get("plain4A.txt"));
    byte[] cipherA = Files.readAllBytes(Paths.get("cipher4A.bin"));
    byte[] cipherB = Files.readAllBytes(Paths.get("cipher4B.bin"));
    
    // BEGIN SOLUTION
    byte[] plainB = cipherB;
    byte[] secretKey = cipherA;

    for (int i=0;i<plainA.length;i++)
    {
      secretKey[i] = (byte)(plainA[i]^cipherA[i]);
    }
    for (int j=0;j<cipherB.length;j++)
    {
      plainB[j] = (byte)(cipherB[j]^secretKey[j]);
    }
    // END SOLUTION
    
    Files.write(Paths.get("plain4B.txt"), plainB);
  }

  static void P5() throws Exception {
    byte[] cipherBMP = Files.readAllBytes(Paths.get("cipher5.bmp"));
    byte[] otherBMP = Files.readAllBytes(Paths.get("plain1.bmp"));
    
    
    // BEGIN SOLUTION
    byte[] plainBMP = cipherBMP;
    byte[] key = new byte[] {   0,   0,    0,   0, 
                                0,   0,    0,   0,
                                0,   0,    0,   0,
                                0,   0,    0,   0 }; 
    

    IvParameterSpec iv = new IvParameterSpec(key);
    byte[] birthday = new byte[16];
    byte [] array1 = new byte[54];
    byte [] array2 = new byte[54];

    for (int i=0;i<54;i++)
    {
      array1[i] = otherBMP[i];
    }
    boolean reset = false;
    for (int i=0;i<99;i++)
    {
      if (reset == true) break;
      for (int j=1;j<=12;j++)
      {
        if (reset == true) break;
        for (int k=1;k<=31;k++)
        {
          if (reset == true) break;
          try 
          {
            birthday[0] = (byte)i;
            birthday[1] = (byte)j;
            birthday[2] = (byte)k;

            SecretKeySpec skey = new SecretKeySpec(birthday, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey, iv);

            plainBMP = cipher.doFinal(cipherBMP);

            for (int m=0;m<54;m++) array2[m] = plainBMP[m];
            if (Arrays.equals(array1,array2))
            {
              reset = true;
              break;
            }
          } catch (BadPaddingException e) {}
        }
      }
    }
    // try {
      // decryption might throw a BadPaddingException!
    // }
    // catch (BadPaddingException e) {
    // }
    // END SOLUTION
    
    Files.write(Paths.get("plain5.bmp"), plainBMP);
  }

  public static void main(String [] args) {
    try {  
      //P1();
      //P2();
      //P3();
      //P4();
      P5();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}
