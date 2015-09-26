package org.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextPane;

import org.controler.WekaControler;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;

public class MainWindow extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtSourceDataset;
    private JTextField txtOptions;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainWindow frame = new MainWindow();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblArchivoOrigen = new JLabel("Archivo origen:");
        lblArchivoOrigen.setBounds(7, 30, 102, 16);
        contentPane.add(lblArchivoOrigen);

        txtSourceDataset = new JTextField();
        txtSourceDataset.setBounds(108, 25, 260, 26);
        contentPane.add(txtSourceDataset);
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
        btnOpen.setBounds(369, 25, 75, 29);
        contentPane.add(btnOpen);

        final JComboBox<String> cbBoxTechnique = new JComboBox<String>();
        cbBoxTechnique.setBounds(18, 74, 136, 26);
        cbBoxTechnique.setModel(
                new DefaultComboBoxModel<String>(new String[] { "Seleccione técnica", "J48", "Naive Bayes" }));
        contentPane.add(cbBoxTechnique);

        JLabel lblParmetros = new JLabel("Parámetros:");
        lblParmetros.setBounds(166, 78, 75, 16);
        contentPane.add(lblParmetros);

        txtOptions = new JTextField();
        txtOptions.setBounds(243, 73, 201, 26);
        contentPane.add(txtOptions);
        txtOptions.setColumns(10);

        JLabel lblTechniqueMessage = new JLabel("Seleccione una técnica");
        lblTechniqueMessage.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
        lblTechniqueMessage.setForeground(Color.RED);
        lblTechniqueMessage.setBounds(30, 58, 117, 16);
        lblTechniqueMessage.setVisible(false);
        contentPane.add(lblTechniqueMessage);

        JButton btnStart = new JButton("Comenzar");

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setBounds(7, 152, 437, 81);
        contentPane.add(textPane);
        contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(
                new Component[] { txtSourceDataset, btnOpen, cbBoxTechnique, txtOptions, btnStart, textPane }));

        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                lblTechniqueMessage.setVisible(false);
                WekaControler controler = new WekaControler();
                if (cbBoxTechnique.getSelectedIndex() != 0) {
                    String result = controler.trainClassifier(cbBoxTechnique.getSelectedIndex(),
                            txtSourceDataset.getText(), txtOptions.getText());
                    textPane.setText(result);
                } else
                    lblTechniqueMessage.setVisible(true);

            }
        });
        btnStart.setBounds(327, 111, 117, 29);
        contentPane.add(btnStart);
    }
}
