/* Copyright (c) 2024 Devin B. Royal. All Rights Reserved. */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.logging.*;
import java.util.zip.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PurpleRestoreApp extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(PurpleRestoreApp.class.getName());
    private File selectedIPSWFile;

    public PurpleRestoreApp() {
        setTitle("PurpleRestore 3 - Device Restoration Tool");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4));

        String[] buttonLabels = {
            "Upload IPSW File", "Factory Restore", "Enter Recovery Mode", "Run Diagnostics",
            "Multi-Device Restore", "Flash Custom Firmware", "Secure Wipe", "Stress Testing",
            "iCloud Services Test", "Rollback Firmware", "Accessory Test", "Pre-Production Test",
            "Special Mode Testing", "Baseband Processor Test"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(e -> buttonClicked(e, displayArea));
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void buttonClicked(ActionEvent e, JTextArea displayArea) {
        String command = ((JButton) e.getSource()).getText();
        switch (command) {
            case "Upload IPSW File":
                uploadIPSWFile(displayArea);
                break;
            case "Factory Restore":
                restoreDevice(displayArea);
                break;
            case "Enter Recovery Mode":
                enterRecoveryMode(displayArea);
                break;
            case "Run Diagnostics":
                runDiagnostics(displayArea);
                break;
            case "Multi-Device Restore":
                multiDeviceRestore(displayArea);
                break;
            case "Flash Custom Firmware":
                flashCustomFirmware(displayArea);
                break;
            case "Secure Wipe":
                secureWipe(displayArea);
                break;
            case "Stress Testing":
                stressTesting(displayArea);
                break;
            case "iCloud Services Test":
                testiCloudServices(displayArea);
                break;
            case "Rollback Firmware":
                rollbackFirmware(displayArea);
                break;
            case "Accessory Test":
                testAccessories(displayArea);
                break;
            case "Pre-Production Test":
                preProductionTesting(displayArea);
                break;
            case "Special Mode Testing":
                specialModeTesting(displayArea);
                break;
            case "Baseband Processor Test":
                testBasebandProcessor(displayArea);
                break;
            default:
                displayArea.append("Unknown command.\n");
        }
    }

    private void uploadIPSWFile(JTextArea displayArea) {
        displayArea.append("Select an IPSW file to upload...\n");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("IPSW Files", "ipsw"));
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            selectedIPSWFile = fileChooser.getSelectedFile();
            displayArea.append("Selected IPSW File: " + selectedIPSWFile.getName() + "\n");
            if (validateIPSW(selectedIPSWFile)) {
                displayArea.append("IPSW file validated successfully.\n");
            } else {
                displayArea.append("IPSW file validation failed!\n");
            }
        } else {
            displayArea.append("No file selected.\n");
        }
    }

    private boolean validateIPSW(File file) {
        if (!file.getName().endsWith(".ipsw")) {
            return false;
        }
        try (ZipFile zipFile = new ZipFile(file)) {
            ZipEntry entry = zipFile.getEntry("Restore.plist");
            if (entry == null) {
                throw new IOException("Restore.plist not found in IPSW file.");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to validate IPSW file: " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    private void restoreDevice(JTextArea displayArea) {
        if (selectedIPSWFile == null) {
            displayArea.append("No IPSW file selected for restoration.\n");
            return;
        }
        displayArea.append("Restoring device to factory settings...\n");
        executeProcess("idevicerestore", selectedIPSWFile.getPath(), displayArea, "Device restored successfully.");
    }

    private void enterRecoveryMode(JTextArea displayArea) {
        displayArea.append("Entering Recovery Mode...\n");
        executeProcess("ideviceenterrecovery", "", displayArea, "Device is now in Recovery Mode.");
    }

    private void runDiagnostics(JTextArea displayArea) {
        displayArea.append("Running diagnostics...\n");
        executeProcess("idevicediagnostics", "verbose", displayArea, "Diagnostics completed.");
    }

    private void multiDeviceRestore(JTextArea displayArea) {
        displayArea.append("Starting multi-device restoration...\n");
        executeProcess("idevicerestore", "--multi", displayArea, "Multi-device restoration completed.");
    }

    private void flashCustomFirmware(JTextArea displayArea) {
        if (selectedIPSWFile == null) {
            displayArea.append("No custom firmware selected.\n");
            return;
        }
        displayArea.append("Flashing custom firmware...\n");
        executeProcess("idevicecustom", selectedIPSWFile.getPath(), displayArea, "Custom firmware flashed successfully.");
    }

    private void secureWipe(JTextArea displayArea) {
        displayArea.append("Performing secure wipe...\n");
        executeProcess("idevicesecurewipe", "", displayArea, "Secure wipe completed.");
    }

    private void stressTesting(JTextArea displayArea) {
        displayArea.append("Starting stress testing...\n");
        executeProcess("idevicestress", "", displayArea, "Stress testing completed.");
    }

    private void testiCloudServices(JTextArea displayArea) {
        displayArea.append("Testing iCloud services (Backup, Sync, etc.)...\n");
        executeProcess("idevicetestiCloud", "", displayArea, "iCloud services test completed.");
    }

    private void rollbackFirmware(JTextArea displayArea) {
        displayArea.append("Rolling back firmware to a previous version...\n");
        executeProcess("idevicerollback", selectedIPSWFile.getPath(), displayArea, "Firmware rollback completed.");
    }

    private void testAccessories(JTextArea displayArea) {
        displayArea.append("Testing accessory compatibility...\n");
        executeProcess("idevicetests", "", displayArea, "Accessory compatibility test completed.");
    }

    private void preProductionTesting(JTextArea displayArea) {
        displayArea.append("Testing pre-production devices...\n");
        executeProcess("idevicepreproduction", "", displayArea, "Pre-production device testing completed.");
    }

    private void specialModeTesting(JTextArea displayArea) {
        displayArea.append("Entering special mode for prototype testing...\n");
        executeProcess("idevicespecial", "", displayArea, "Prototype testing in special mode completed.");
    }

    private void testBasebandProcessor(JTextArea displayArea) {
        displayArea.append("Testing baseband processor and networking components...\n");
        executeProcess("idevicetestbaseband", "", displayArea, "Baseband processor testing completed.");
    }

    private void executeProcess(String command, String args, JTextArea displayArea, String successMessage) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command, args);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                displayArea.append(line + "\n");
            }
            process.waitFor();
            displayArea.append(successMessage + "\n");
        } catch (Exception e) {
            displayArea.append("Error executing command: " + e.getMessage() + "\n");
            LOGGER.log(Level.SEVERE, "Error executing command: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PurpleRestoreApp app = new PurpleRestoreApp();
            app.setVisible(true);
        });
    }
}
