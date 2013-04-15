package nrgscoutclient;

import java.awt.Color;
import java.net.InetAddress;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ClientGUI extends javax.swing.JFrame {

    public int preferredX, preferredY;
    public boolean detached = false;
    TCPClient tcpClient;

    public ClientGUI() {
        this.setType(Type.UTILITY);
        initComponents();
        this.setMinimumSize(this.getSize());
        tcpClient = new TCPClient("", 0);
        append("Ready to connect to server...");
    }

    public void setLoc(int x, int y) {
        preferredX = x;
        preferredY = y;
        if (!detached) {
            this.setLocation(x, y);
        }
    }

    public TCPClient getClient() {
        return tcpClient;
    }

    public void refresh() {
        SwingUtilities.updateComponentTreeUI(getRootPane());
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel1 = new javax.swing.JPanel();
        portNUD = new javax.swing.JSpinner();
        hostTB = new javax.swing.JTextField();
        connectButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        panel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();

        setTitle("Server Connection");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                moved(evt);
            }
        });

        panel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Socket"));
        panel1.setAlignmentX(0.0F);
        panel1.setAlignmentY(0.0F);

        portNUD.setModel(new javax.swing.SpinnerNumberModel(18250, 1, 65535, 1));
        portNUD.setToolTipText("The outgoing port (1-65535)");
        portNUD.setEditor(new javax.swing.JSpinner.NumberEditor(portNUD, ""));

        hostTB.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        hostTB.setText("localhost");
        hostTB.setToolTipText("The host address of the server (i.e IP address)");

        connectButton.setText("Connect");
        connectButton.setToolTipText("Connect to the server on the specified socket");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Server Host Address:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Server Port:");

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(connectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                            .addComponent(hostTB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                            .addComponent(portNUD, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(hostTB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portNUD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(connectButton)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        statusLabel.setFont(new java.awt.Font("Tahoma", 1, 10));
        statusLabel.setForeground(new java.awt.Color(255, 0, 0));
        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statusLabel.setText("Status: Disconnected");
        statusLabel.setToolTipText("Current connection status");
        statusLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        statusLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        panel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Console"));

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));

        console.setBackground(new java.awt.Color(0, 0, 0));
        console.setEditable(false);
        console.setFont(new java.awt.Font("Consolas", 0, 11));
        console.setForeground(new java.awt.Color(250, 250, 250));
        console.setLineWrap(true);
        console.setRows(5);
        console.setToolTipText("");
        console.setWrapStyleWord(true);
        jScrollPane1.setViewportView(console);

        javax.swing.GroupLayout panel2Layout = new javax.swing.GroupLayout(panel2);
        panel2.setLayout(panel2Layout);
        panel2Layout.setHorizontalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        panel2Layout.setVerticalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        if (!tcpClient.isConnected()) {
            tcpClient = new TCPClient(hostTB.getText(), Integer.parseInt(portNUD.getValue().toString()));
            boolean connected = tcpClient.start();
            if (connected) {
                try {
                    append("\nConnection established with host " + InetAddress.getByName(hostTB.getText()) + " on port " + Integer.parseInt(portNUD.getValue().toString()) + "!\n");
                }
                catch (Exception e) {
                    append("\nConnection established with host " + hostTB.getText() + " on port " + Integer.parseInt(portNUD.getValue().toString()) + "!\n");
                }
                statusLabel.setText("Status: Connected");
                statusLabel.setForeground(Color.GREEN);
                connectButton.setText("Disconnect");
                tcpClient.listen(console, statusLabel, connectButton);
            }
            else {
                try {
                    JOptionPane.showMessageDialog(rootPane, "Host " + InetAddress.getByName(hostTB.getText()) + " is unreachable on port " + Integer.parseInt(portNUD.getValue().toString()) + "!", "Connection Error", JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception e) {
                    JOptionPane.showMessageDialog(rootPane, "Host " + hostTB.getText() + " is unreachable on port " + Integer.parseInt(portNUD.getValue().toString()) + "!", "Connection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else {
            tcpClient.stop();
            statusLabel.setText("Status: Disconnected");
            statusLabel.setForeground(Color.RED);
            connectButton.setText("Reconnect");
            append("\nDisconnected from server.\n");
        }
    }//GEN-LAST:event_connectButtonActionPerformed

    private void moved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_moved
        if (detached == true) {
            if (this.getLocation().x > preferredX - 20 && this.getLocation().x < preferredX + 20) {
                if (this.getLocation().y > preferredY - 20 && this.getLocation().y < preferredY + 20) {
                    detached = false;
                }
            }
        }
        else {
            if (this.getLocation().x < preferredX - 20 || this.getLocation().x > preferredX + 20) {
                if (this.getLocation().y < preferredY - 20 || this.getLocation().y > preferredY + 20) {
                    detached = true;
                }
            }
        }
        setLoc(preferredX, preferredY);
    }//GEN-LAST:event_moved

    public void attach() {
        this.setVisible(true);
        detached = false;
        setLoc(preferredX, preferredY);
    }

    public void forwardToClient(String s) {
        boolean b = tcpClient.write(s);
        if (!b) {
            append("\nFailed to send data to server.\n");
        }
    }

    public void append(String s) {
        console.append(s);
        console.setCaretPosition(console.getText().length() - 1);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectButton;
    private javax.swing.JTextArea console;
    private javax.swing.JTextField hostTB;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panel1;
    private javax.swing.JPanel panel2;
    private javax.swing.JSpinner portNUD;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
}
