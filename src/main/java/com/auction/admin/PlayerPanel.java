package com.auction.admin;

import com.auction.dao.PlayerDAO;
import com.auction.models.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PlayerPanel extends JPanel {

    private JList<String> playerList;
    private DefaultListModel<String> listModel;
    private List<Player> players;

    public PlayerPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Players"));
        setPreferredSize(new Dimension(300, 0));

        listModel = new DefaultListModel<>();
        playerList = new JList<>(listModel);
        playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(playerList), BorderLayout.CENTER);

        loadPlayers();
    }

    private void loadPlayers() {
        PlayerDAO playerDAO = new PlayerDAO();
        players = playerDAO.getAllPlayers();
        listModel.clear();

        for (Player p : players) {
            listModel.addElement(
                    p.getPlayerId() + " - " + p.getName() + " (" + p.getPosition() + ")"
            );
        }
    }

    public Player getSelectedPlayer() {
        int idx = playerList.getSelectedIndex();
        if (idx >= 0 && idx < players.size()) {
            return players.get(idx);
        }
        return null;
    }
}
