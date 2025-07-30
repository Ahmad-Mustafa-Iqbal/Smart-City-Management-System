package src.model;
import java.io.*;
import java.util.ArrayList;

public class FileManager {

    public static <T> void saveToFile(ArrayList<T> data, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(data);
            System.out.println("Saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving to file: " + fileName);
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> loadFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (ArrayList<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading from file: " + fileName);
            return new ArrayList<>();
        }
    }
}
