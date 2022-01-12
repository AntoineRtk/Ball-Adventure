
package states;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.swing.JOptionPane;

public class GameSave {
    
    private static int flowers;
    
    public static void init() {
        File f = new File(System.getenv("TEMP")+"/BallAdventure");
        File save = new File(System.getenv("TEMP")+"/BallAdventure/save.sba");
        if(!f.exists()) {
            f.mkdir();
        }
        if(!save.exists()) {
            try {
                save.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(GameSave.class.getName()).log(Level.SEVERE, null, ex);
            }
            write("BallAdventure\n1=N\n2=N\n3=N\n4=N");
        }
    }
    
    public static void changeFlowers(int flowers) {
        write("BallAdventure\nF="+flowers);
    }
    
    public static boolean have(int level) {
        try {
            FileInputStream fis = new FileInputStream(System.getenv("TEMP")+"/BallAdventure/save.sba");
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            fis.close();
            SecretKeyFactory sk = SecretKeyFactory.getInstance("DES");
            SecretKey key = sk.generateSecret(new DESKeySpec("BallAdventure".getBytes()));
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] t = cipher.doFinal(bytes);
            String decrypt = new String(cipher.doFinal(t));
            return decrypt.substring(decrypt.indexOf(level) + 2, decrypt.indexOf(level) + 3).equals("O");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidKeySpecException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "ERREUR CRITIQUE LORS DE LA LECTURE DE LA SAUVEGARDE : "+ex+". Veuillez vous reférez au propriétaire du jeu.", "Ball Adventure", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalBlockSizeException | BadPaddingException | IOException ex) {
            JOptionPane.showMessageDialog(null, "ERREUR CRITIQUE LORS DE LA LECTURE DE LA SAUVEGARDE : "+ex+". Veuillez vous reférez au propriétaire du jeu.", "Ball Adventure", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    public static void write(String toWrite) {
        try {
            SecretKeyFactory sk = SecretKeyFactory.getInstance("DES");
            SecretKey key = sk.generateSecret(new DESKeySpec("BallAdventure".getBytes()));
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = cipher.doFinal(toWrite.getBytes());
            CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(System.getenv("TEMP")+"/BallAdventure/save.sba"), cipher);
            cos.write(bytes);
            cos.flush();
            cos.close();
            System.out.println("Wrote "+new String(bytes));
            loadSave();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
            JOptionPane.showMessageDialog(null, "ERREUR CRITIQUE LORS DE LA CREATION DE LA SAUVEGARDE : "+ex+". Veuillez vous reférez au propriétaire du jeu.", "Ball Adventure", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void loadSave() {
        try {
            FileInputStream fis = new FileInputStream(System.getenv("TEMP")+"/BallAdventure/save.sba");
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            fis.close();
            SecretKeyFactory sk = SecretKeyFactory.getInstance("DES");
            SecretKey key = sk.generateSecret(new DESKeySpec("BallAdventure".getBytes()));
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] t = cipher.doFinal(bytes);
            String decrypt = new String(cipher.doFinal(t));
            System.out.println("Decrypt : "+decrypt);
            flowers = 1;
            //flowers = Integer.parseInt(decrypt.split("\n")[1].substring(2));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidKeySpecException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "ERREUR CRITIQUE LORS DE LA LECTURE DE LA SAUVEGARDE : "+ex+". Veuillez vous reférez au propriétaire du jeu.", "Ball Adventure", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalBlockSizeException | BadPaddingException | IOException ex) {
            JOptionPane.showMessageDialog(null, "ERREUR CRITIQUE LORS DE LA LECTURE DE LA SAUVEGARDE : "+ex+". Veuillez vous reférez au propriétaire du jeu.", "Ball Adventure", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static int getFlowers() {
        return flowers;
    }
    
    public static String decryptMap() {
        return null;
    }
}