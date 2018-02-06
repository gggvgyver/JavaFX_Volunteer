package models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;

public class Volunteer {
    private String firstName, lastName, phoneNumber, password;
    private LocalDate birthday;
    private File imageFile;
    private int volunteerID;
    private byte[] salt;
    private boolean admin;

    public Volunteer(String firstName, String lastName, String phoneNumber, LocalDate birthday, String password, boolean admin) throws NoSuchAlgorithmException {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.imageFile = new File("./src/imgs/Nature Full HD Wallpapers 1.jpg");
        salt = PasswordGenerator.getSalt();
        this.password = PasswordGenerator.getSHA512Password(password, salt);
        this.admin = admin;
    }

    public Volunteer(String firstName, String lastName, String phoneNumber, LocalDate birthday, File imageFile, boolean admin, String password) throws IOException, NoSuchAlgorithmException {
        this(firstName, lastName, phoneNumber, birthday, password, admin);
        this.imageFile = imageFile;
        copyImageFile();
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public int getVolunteerID() {
        return volunteerID;
    }

    public void setVolunteerID(int volunteerID) {
        if (volunteerID >= 0)
            this.volunteerID = volunteerID;
        else
            throw new IllegalArgumentException("VolunteerID must be >= 0");
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void copyImageFile() throws IOException
    {
        //create a new Path to copy the image into a local directory
        Path sourcePath = imageFile.toPath();

        String uniqueFileName = getUniqueFileName(imageFile.getName());

        Path targetPath = Paths.get("./src/imgs/"+uniqueFileName);

        //copy the file to the new directory
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        //update the imageFile to point to the new File
        imageFile = new File(targetPath.toString());
    }

    private String getUniqueFileName(String oldFileName)
    {
        String newName;

        //create a Random Number Generator
        SecureRandom rng = new SecureRandom();

        //loop until we have a unique file name
        do
        {
            newName = "";

            //generate 32 random characters
            for (int count=1; count <=32; count++)
            {
                int nextChar;

                do
                {
                    nextChar = rng.nextInt(123);
                } while(!validCharacterValue(nextChar));

                newName = String.format("%s%c", newName, nextChar);
            }
            newName += oldFileName;

        } while (!uniqueFileInDirectory(newName));

        return newName;
    }

    public boolean validCharacterValue(int asciiValue)
    {

        //0-9 = ASCII range 48 to 57
        if (asciiValue >= 48 && asciiValue <= 57)
            return true;

        //A-Z = ASCII range 65 to 90
        if (asciiValue >= 65 && asciiValue <= 90)
            return true;

        //a-z = ASCII range 97 to 122
        if (asciiValue >= 97 && asciiValue <= 122)
            return true;

        return false;
    }

    public boolean uniqueFileInDirectory(String fileName)
    {
        File directory = new File("./src/imgs/");

        File[] dir_contents = directory.listFiles();

        for (File file: dir_contents)
        {
            if (file.getName().equals(fileName))
                return false;
        }
        return true;
    }
}//End of class
