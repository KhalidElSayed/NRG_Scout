package nrgscoutclient;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URI;

public class AboutDialog extends javax.swing.JDialog {

    public AboutDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setCenterScreen();
        text1.setCaretPosition(0);
        text2.setCaretPosition(0);
    }

    private void setCenterScreen() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int locX = dim.width / 2 - this.getWidth() / 2;
        int locY = dim.height / 2 - this.getHeight() / 2;
        this.setLocation(locX, locY);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        text1 = new javax.swing.JTextArea();
        infoButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        text2 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About");
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);

        infoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nrgscoutclient/icon.png"))); // NOI18N
        infoButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        infoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("NRG 948");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Programmed by Mohammad Adib 2012");

        text1.setBackground(new java.awt.Color(240, 240, 240));
        text1.setColumns(20);
        text1.setEditable(false);
        text1.setFont(new java.awt.Font("Monospaced", 0, 12));
        text1.setLineWrap(true);
        text1.setRows(5);
        text1.setText("NRG, the Newport High School Robotics Group, is a FRC (First Robotics Competition) team in Bellevue, Washington. Each year we work as a team of skilled individuals to design a robot capable of competing in the FIRST Robotics Competition. Such a task is only accomplished through the hard work and dedication of the group’s members and mentors who face immense engineering and financial challenges.\nThe experiences they have help to develop these individuals into people who are passionate about the technological advances of our generation and guide their lives in the greater academic world as they tackle grasping and utilizing cutting edge innovations of every kind they could ever want to participate in\n");
        text1.setWrapStyleWord(true);
        text1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane1.setViewportView(text1);

        infoButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nrgscoutclient/FIRSTLogo.png"))); // NOI18N
        infoButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        infoButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoButton2ActionPerformed(evt);
            }
        });

        text2.setBackground(new java.awt.Color(240, 240, 240));
        text2.setColumns(20);
        text2.setEditable(false);
        text2.setFont(new java.awt.Font("Monospaced", 0, 12));
        text2.setLineWrap(true);
        text2.setRows(5);
        text2.setText("We are a team of proud students under the FIRST Organization. FIRST stands for “For Inspiration of Science and Technology.” Founded by Dean Kamen, the inventor of the Segway Scooter, this organization works together with students of all ages across the nation and around the world to bring a new spin to an old pursuit. Dean Kamen created FIRST “to transform our culture by creating a world where science and technology are celebrated and where young people dream of becoming science and technology heroes.” It is a worthy alternative to sporting events in its competitive, yet friendly methods of spreading knowledge about science, technology, and engineering. The core tenet and catchphrase of FIRST is “Gracious Professionalism,” an idea which encapsulates the camaraderie found at FIRST Events. As a phrase, it conjures images of kind and friendly interaction with other teams. As a lifestyle, it begs competition, but not in a vicious way. Rather, it brings teams together to achieve a common goal and together develop innovative ways to solve difficult problems.\nFIRST’s Mission Statement: “Our mission is to inspire young people to be science and technology leaders, by engaging them in exciting mentor-based programs that build science, engineering and technology skills, that inspire innovation, and that foster well-rounded life capabilities including self-confidence, communication and leadership.”");
        text2.setWrapStyleWord(true);
        text2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane2.setViewportView(text2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(infoButton, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                            .addComponent(infoButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(infoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void infoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoButtonActionPerformed
        try {
            Desktop.getDesktop().browse(new URI("http://nrg948.org"));
        }
        catch (Exception ex) {
        }
    }//GEN-LAST:event_infoButtonActionPerformed

    private void infoButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoButton2ActionPerformed
        try {
            Desktop.getDesktop().browse(new URI("http://nrg948.org/wp/?page_id=51"));
        }
        catch (Exception ex) {
        }
    }//GEN-LAST:event_infoButton2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton infoButton;
    private javax.swing.JButton infoButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea text1;
    private javax.swing.JTextArea text2;
    // End of variables declaration//GEN-END:variables
}
