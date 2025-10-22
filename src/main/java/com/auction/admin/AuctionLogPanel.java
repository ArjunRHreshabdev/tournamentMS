package com.auction.admin;

import com.auction.dao.AuctionLogDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AuctionLogPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;

    public AuctionLogPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Auction Log"));

        tableModel = new DefaultTableModel(new String[]{"Player", "Team", "Price"}, 0);
        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadLogs();
    }

    private void loadLogs() {
        // This can be extended to fetch past logs from AuctionLogDAO if required
    }

    public void addAuctionLog(String player, String team, double price) {
        tableModel.addRow(new Object[]{player, team, price});
    }
}
