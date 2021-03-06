import java.io.*;
import java.nio.file.*;
import javax.swing.*;
import java.util.*;
import java.nio.file.attribute.PosixFilePermission;

public class Installer
{
    private static final String TERRARIA_VERSION = "v1.3.5.3";
    private static final int TERRARIA_SIZE = 10786816; // Windows only: We only want to make a backup of the official release

    public static void tryInstall(String[] files, String[] filesToDelete, File directory, boolean WindowsInstall)
    {
        try
        {
            install(files, filesToDelete, directory, WindowsInstall);
        }
        catch (IOException e)
        {
            messageBox("A problem was encountered while installing!\n" + e, JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void install(String[] files, String[] filesToDelete, File directory, boolean WindowsInstall) throws IOException
    {
        if (directory == null || !directory.exists())
        {
            messageBox("Could not find place to install to!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File terraria = new File(directory, "Terraria.exe");
        if (!terraria.exists())
        {
            messageBox("Could not find your Terraria.exe file!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(WindowsInstall)
        {
            File terrariaBackup = new File(directory, "Terraria_" + TERRARIA_VERSION + ".exe");
            File terrariaUnknown = new File(directory, "Terraria_Unknown.exe");
            if (!terrariaBackup.exists() && terraria.length() == TERRARIA_SIZE)
            {
                copy(terraria, terrariaBackup);
            }
            else if (!terrariaUnknown.exists())
            {
                File tModLoader = new File("Terraria.exe");
                if(terraria.length() == tModLoader.length()){
                    // Double install. Might be a mistake or an attempt to fix an install.
                }
                else{
                    copy(terraria, terrariaUnknown);
                }
            }
        }
        for (String file : filesToDelete)
        {
            File source = new File(directory, file);
            if (source.exists())
            {
                source.delete();
            }
        }
        String badFiles = "\n";
        for (String file : files)
        {
            File source = new File(file);
            if (source.exists())
            {
                File destination = new File(directory, file);
                File parent = destination.getParentFile();
                if (parent != null)
                {
                    parent.mkdirs();
                }
                copy(source, destination);
                if(file == "tModLoaderServer" || file == "Terraria"){
                    // Alt: file.setExecutable(true, false);
                    Set<PosixFilePermission> permissions = new HashSet<>();
                    permissions.add(PosixFilePermission.OWNER_EXECUTE);
                    Files.setPosixFilePermissions(destination.toPath(), permissions);
                }
            }
            else
            {
                badFiles += file + "\n";
            }
        }
        if (badFiles.length() > 1)
        {
            if (badFiles.length() > 8)
                messageBox("The following files were missing and could not be installed:" + badFiles + "All the other files have been installed properly. \n\n DID YOU FORGET TO UNZIP THE ZIP ARCHIVE BEFORE ATTEMPTING TO INSTALL?", JOptionPane.ERROR_MESSAGE);
            else
                messageBox("The following files were missing and could not be installed:" + badFiles + "All the other files have been installed properly.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        messageBox("Installation successful!", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void copy(File source, File destination) throws IOException
    {
        Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private static void messageBox(String message, int messageType)
    {
        JOptionPane.showMessageDialog(null, message, "tModLoader Installer", messageType);
    }
}
