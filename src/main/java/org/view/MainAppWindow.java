package org.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;

import org.controler.WekaControler;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class MainAppWindow {

    private JFrame frame;
    private JTextField txtSourceDataset;
    private JTextField txtOptions;
    private WekaControler controler;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainAppWindow window = new MainAppWindow();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public MainAppWindow() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 950, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblArchivoOrigen = new JLabel("Archivo origen:");
        lblArchivoOrigen.setBounds(6, 24, 96, 16);
        frame.getContentPane().add(lblArchivoOrigen);

        txtSourceDataset = new JTextField();
        txtSourceDataset.setText(
                "/Users/martinmineo/Desarrollo/Tesis/workspace/ChatClassifier/labeled/MAPEO - T1 WG01 -WG02.xlsx");
        txtSourceDataset.setBounds(114, 19, 701, 26);
        frame.getContentPane().add(txtSourceDataset);
        txtSourceDataset.setColumns(10);

        JButton btnOpen = new JButton("Abrir");
        btnOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileChooser.getSelectedFile();
                    txtSourceDataset.setText(file.getPath());
                }
            }
        });
        btnOpen.setBounds(827, 19, 117, 29);
        frame.getContentPane().add(btnOpen);

        JSeparator separator = new JSeparator();
        separator.setBounds(6, 52, 938, 16);
        frame.getContentPane().add(separator);

        JComboBox<String> cbBoxTechnique = new JComboBox<String>();

        cbBoxTechnique.setModel(
                new DefaultComboBoxModel<String>(new String[] { "Seleccione una técnica", "J48", "Naive Bayes" }));
        cbBoxTechnique.setBounds(62, 65, 198, 27);
        frame.getContentPane().add(cbBoxTechnique);

        JLabel lblTcnica = new JLabel("Técnica:");
        lblTcnica.setBounds(6, 69, 61, 16);
        frame.getContentPane().add(lblTcnica);

        JLabel lblParmetros = new JLabel("Parámetros:");
        lblParmetros.setBounds(272, 69, 84, 16);
        frame.getContentPane().add(lblParmetros);

        txtOptions = new JTextField();
        txtOptions.setBounds(355, 64, 278, 26);
        frame.getContentPane().add(txtOptions);
        txtOptions.setColumns(10);

        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(6, 170, 938, 16);
        frame.getContentPane().add(separator_1);

        JLabel lblResultados = new JLabel("Resultados:");
        lblResultados.setBounds(6, 184, 96, 16);
        frame.getContentPane().add(lblResultados);

        JLabel lblTechniqueMessage = new JLabel("Seleccione una técnica");
        lblTechniqueMessage.setHorizontalAlignment(SwingConstants.CENTER);
        lblTechniqueMessage.setForeground(Color.RED);
        lblTechniqueMessage.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
        lblTechniqueMessage.setBounds(66, 86, 173, 16);
        lblTechniqueMessage.setVisible(false);
        frame.getContentPane().add(lblTechniqueMessage);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(6, 208, 938, 364);
        frame.getContentPane().add(scrollPane);

        JTextArea textAreaResults = new JTextArea();
        scrollPane.setViewportView(textAreaResults);
        textAreaResults.setEditable(false);

        JButton btnStart = new JButton("Comenzar");
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                lblTechniqueMessage.setVisible(false);
                controler = new WekaControler(cbBoxTechnique.getSelectedIndex());
                if (cbBoxTechnique.getSelectedIndex() != 0) {
                    String result = controler.trainClassifier(txtSourceDataset.getText(), txtOptions.getText());
                    textAreaResults.setText(result);
                    textAreaResults.setCaretPosition(0);
                } else
                    lblTechniqueMessage.setVisible(true);
            }
        });
        btnStart.setBounds(516, 129, 117, 29);
        frame.getContentPane().add(btnStart);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(645, 61, 299, 111);
        frame.getContentPane().add(scrollPane_1);

        JTextPane textOptions = new JTextPane();
        textOptions.setEditable(false);
        scrollPane_1.setViewportView(textOptions);

        cbBoxTechnique.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (cbBoxTechnique.getSelectedIndex() != 0) {
                    controler = new WekaControler(cbBoxTechnique.getSelectedIndex());
                    StringBuilder options = controler.getOptions();

                    txtOptions.setText(options.toString());
                    textOptions.setText(controler.getValidOptions());
                    textOptions.setCaretPosition(0);
                } else
                    textOptions.setText("");;
            }
        });

    }
}
