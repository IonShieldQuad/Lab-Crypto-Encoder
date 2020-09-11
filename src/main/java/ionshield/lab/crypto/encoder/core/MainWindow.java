package ionshield.lab.crypto.encoder.core;


import com.bulenkov.darcula.DarculaLaf;
import ionshield.lab.crypto.encoder.modules.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicLookAndFeel;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow {
    private JPanel rootPanel;
    private JTextArea log;

    private JButton saveButton;
    private JTextField fileNameField;
    private JButton loadButton;
    private JButton loadKeyButton;
    private JTextField keyFileNameField;
    private JTabbedPane tabbedPane;
    private JButton variantEncryptButton;
    private JButton crossroadsEncryptButton;
    private JButton xorEncryptButton;
    private JButton adfgxEncryptButton;

    private static String TITLE = "Lab-Crypto-Encoder";
    
    private int precision = 3;
    
    private List<String> lines = new ArrayList<>();
    private List<String> keyLines = new ArrayList<>();
    private List<String> encrypted = new ArrayList<>();
    private BufferedImage image;
    
    private MainWindow() {
        initComponents();
    }
    
    private void initComponents() {
        
        loadButton.addActionListener(e -> {
            File file = loadFile();
            fileNameField.setText(file != null ? file.getName() : "");
            if (file == null) return;
            lines = readFileRows(file);
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                sb.append(line).append(System.lineSeparator());
            }
            log.setText(sb.toString());
        });

        loadKeyButton.addActionListener(e -> {
            File file = loadFile();
            keyFileNameField.setText(file != null ? file.getName() : "");
            if (file == null) return;
            keyLines = readFileRows(file);
        });
        
        saveButton.addActionListener(e -> saveFile(encrypted));

        variantEncryptButton.addActionListener(e -> {
            CryptoEncoder encoder = new VariantEncoder();
            if (encoder.readKey(keyLines)) {
                encrypted = encoder.encode(lines);
                if (encrypted == null) {
                    log.setText("Encoding error");
                }
                else {
                    StringBuilder sb = new StringBuilder();
                    for (String line : encrypted) {
                        sb.append(line).append(System.lineSeparator());
                    }
                    log.setText(sb.toString());
                }
            }
            else {
                log.setText("Error: The key is invalid");
            }
        });

        crossroadsEncryptButton.addActionListener(e -> {
            CryptoEncoder encoder = new CrossroadsEncoder();
            if (encoder.readKey(keyLines)) {
                encrypted = encoder.encode(lines);
                if (encrypted == null) {
                    log.setText("Encoding error");
                }
                else {
                    StringBuilder sb = new StringBuilder();
                    for (String line : encrypted) {
                        sb.append(line).append(System.lineSeparator());
                    }
                    log.setText(sb.toString());
                }
            }
            else {
                log.setText("Error: The key is invalid");
            }
        });

        xorEncryptButton.addActionListener(e -> {
            CryptoEncoder encoder = new XOREncoder();
            if (encoder.readKey(keyLines)) {
                encrypted = encoder.encode(lines);
                if (encrypted == null) {
                    log.setText("Encoding error");
                }
                else {
                    StringBuilder sb = new StringBuilder();
                    for (String line : encrypted) {
                        sb.append(line).append(System.lineSeparator());
                    }
                    log.setText(sb.toString());
                }
            }
            else {
                log.setText("Error: The key is invalid");
            }
        });

        adfgxEncryptButton.addActionListener(e -> {
            CryptoEncoder encoder = new ADFGXEncoder();
            if (encoder.readKey(keyLines)) {
                encrypted = encoder.encode(lines);
                if (encrypted == null) {
                    log.setText("Encoding error");
                }
                else {
                    StringBuilder sb = new StringBuilder();
                    for (String line : encrypted) {
                        sb.append(line).append(System.lineSeparator());
                    }
                    log.setText(sb.toString());
                }
            }
            else {
                log.setText("Error: The key is invalid");
            }
        });

    }

    
    private File loadFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "TXT",  "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(rootPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    private List<String> readFileRows(File file) {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> l = new ArrayList<>();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                l.add(line);
            }
            return l;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void saveFile(List<String> lines) {
        if (lines == null || lines.isEmpty()) return;
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showSaveDialog(rootPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (int i = 0; i < lines.size(); i++) {
                    writer.write(lines.get(i));
                    if (i < lines.size() - 1) {
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    
    
    public static void main(String[] args) {
        BasicLookAndFeel darcula = new DarculaLaf();
        try {
            UIManager.setLookAndFeel(darcula);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        JFrame frame = new JFrame(TITLE);
        MainWindow gui = new MainWindow();
        frame.setContentPane(gui.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
