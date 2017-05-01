package org.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.controler.Controller;

public class GroupsMembersView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private Controller controller;
    private JButton btnRemove;
    private JButton btnCreategroups;
    private Set<String> members;

    public GroupsMembersView(Controller controller) {
        
        this.controller = controller;
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        btnRemove = new JButton("Eliminar");
        
        btnRemove.setBounds(288, 61, 156, 29);
        contentPane.add(btnRemove);
        
        btnCreategroups = new JButton("Crear grupos");
        
        btnCreategroups.setBounds(327, 243, 117, 29);
        contentPane.add(btnCreategroups);
        
        initiallizeActionListeners();
        
        addMembers(controller.getGroupsMembers());
    }
    
    private void initiallizeActionListeners() {
        
        btnRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSelectedRows();
            }
        });
        btnCreategroups.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getValidMembers();
                controller.btnCreateGroupsPressed(members);
            }
        });
    }

    private void addMembers(Set<String> members) {
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(5, 5, 240, 268);
        contentPane.add(scrollPane);
        
        DefaultTableModel defaultTableModel = createNewDefaultTableModel();
        JTable table = createNewTable(defaultTableModel);
        scrollPane.setViewportView(table);
        
        if (members != null) {
            for (String member : members) {
                defaultTableModel.addRow(new String [] {member});
            }
        }
    }
    
    private DefaultTableModel createNewDefaultTableModel() {
        
        DefaultTableModel defaultTableModel = new DefaultTableModel(0, 0);
        String header0 = "<html><center>Nombre</center></html>";
        String strHeader[] = new String[] {header0};
        
        defaultTableModel.setColumnIdentifiers(strHeader);
        
        return defaultTableModel;
    }
    
    private JTable createNewTable(DefaultTableModel defaultTableModel) {
        
        table = new JTable();
        
        table.setCellSelectionEnabled(true);   
        table.setModel(defaultTableModel);
        table.getTableHeader().setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 32));
        
        table.getColumnModel().getColumn(0).setPreferredWidth(160);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        
        return table;
    }
    
    public void removeSelectedRows() {
        
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();
        int[] rows = table.getSelectedRows();
        
        for(int i=0; i < rows.length; i++) {
          model.removeRow(rows[i]-i);
        }
    }
    
    public void getValidMembers() {
        
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();
        
        members = new HashSet<String>();
        for(int row=0; row < model.getRowCount(); row++) {
            String name = (String)model.getValueAt(row, 0);
            members.add(name);
        }
    }
}
