package krawczyk.grzegorz.controllers.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class is responsible for saving and loading valid accounts
 * (email accounts to which user logged in the application) from local file
 */
public class PersistenceAccess {

    /**
     * Path to store date
     */
    private String VALID_ACCOUNTS_LOCATION = System.getProperty("user.home") + File.separator + "validAccounts.ser";

    /**
     * Method reads list of valid accounts from local file.
     * Method is called the earliest possible - in the Launcher.start() method
     * @return List<ValidAccount> - list of valid accounts read from file
     */
    public List<ValidAccount> loadFromPersistence() {
        List <ValidAccount> resultList = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(this.VALID_ACCOUNTS_LOCATION);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            List<ValidAccount> persistedList = (List<ValidAccount>) objectInputStream.readObject();
            resultList.addAll(persistedList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    /**
     * Method adds valid accounts from passed list to local file.
     * @param validAccounts List<ValidAccount> - list of valid accounts
     */
    public void saveToPersistence(List <ValidAccount> validAccounts) {

        try {
            System.out.println(VALID_ACCOUNTS_LOCATION);
            File file = new File(this.VALID_ACCOUNTS_LOCATION);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(validAccounts);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
