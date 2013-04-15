package nrgscoutclient;

import java.awt.Color;
import javax.swing.JOptionPane;

public class AddParamDialog extends javax.swing.JDialog {

    private int result = -1;
    private int type = 0;
    public final int ADD = 0;
    public final int CANCEL = 1;
    public final int STRING = 2;
    public final int SLIDER = 3;
    public final int NUMBER = 0;
    public final int BOOLEAN = 1;
    private int[] numParams = {0, 0, 0}, sliderParams = {0, 0, 0};

    public AddParamDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        titleTB.setForeground(Color.lightGray);
        setLocation(parent.getLocation().x + parent.getSize().width / 2 - this.getSize().width / 2, parent.getLocation().y + parent.getSize().height / 2 - this.getSize().height / 2);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        numberTab = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        minNUD = new javax.swing.JSpinner();
        initialNUD = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        maxNUD = new javax.swing.JSpinner();
        booleanTab = new javax.swing.JPanel();
        initiallyChecked = new javax.swing.JRadioButton();
        initiallyUnchecked = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        trueTB = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        falseTB = new javax.swing.JTextField();
        stringTab = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        sliderTab = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        minNUD1 = new javax.swing.JSpinner();
        initialNUD1 = new javax.swing.JSpinner();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        maxNUD1 = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        titleTB = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add new parameter");
        setBackground(new java.awt.Color(240, 240, 240));
        setModal(true);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameter Type"));

        tabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setToolTipText("");

        jLabel6.setText("Min:");

        minNUD.setModel(new javax.swing.SpinnerNumberModel());

        initialNUD.setModel(new javax.swing.SpinnerNumberModel());

        jLabel7.setText("Initial:");

        jLabel9.setText("Max:");

        maxNUD.setModel(new javax.swing.SpinnerNumberModel());

        javax.swing.GroupLayout numberTabLayout = new javax.swing.GroupLayout(numberTab);
        numberTab.setLayout(numberTabLayout);
        numberTabLayout.setHorizontalGroup(
            numberTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(numberTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(numberTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(numberTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(maxNUD, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(minNUD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(initialNUD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
                .addContainerGap())
        );
        numberTabLayout.setVerticalGroup(
            numberTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(numberTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(numberTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(minNUD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(numberTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(initialNUD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(numberTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(maxNUD))
                .addContainerGap())
        );

        tabbedPane.addTab("Number", numberTab);

        initiallyChecked.setText("True");
        initiallyChecked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initiallyCheckedActionPerformed(evt);
            }
        });

        initiallyUnchecked.setSelected(true);
        initiallyUnchecked.setText("False");
        initiallyUnchecked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initiallyUncheckedActionPerformed(evt);
            }
        });

        jLabel10.setText("Initial value:");

        jLabel14.setText("True text:");

        trueTB.setText("This Parameter is True");

        jLabel15.setText("False text:");

        falseTB.setText("This Parameter is False");

        javax.swing.GroupLayout booleanTabLayout = new javax.swing.GroupLayout(booleanTab);
        booleanTab.setLayout(booleanTabLayout);
        booleanTabLayout.setHorizontalGroup(
            booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(booleanTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(booleanTabLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(initiallyChecked)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(initiallyUnchecked)
                        .addGap(0, 58, Short.MAX_VALUE))
                    .addGroup(booleanTabLayout.createSequentialGroup()
                        .addGroup(booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(trueTB, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(falseTB, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addContainerGap())
        );
        booleanTabLayout.setVerticalGroup(
            booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(booleanTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(initiallyChecked, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(initiallyUnchecked, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(trueTB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(falseTB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("True/False", booleanTab);

        jLabel8.setForeground(new java.awt.Color(153, 153, 153));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("No configuration required.");

        javax.swing.GroupLayout stringTabLayout = new javax.swing.GroupLayout(stringTab);
        stringTab.setLayout(stringTabLayout);
        stringTabLayout.setHorizontalGroup(
            stringTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stringTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                .addContainerGap())
        );
        stringTabLayout.setVerticalGroup(
            stringTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stringTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Text", stringTab);

        jLabel11.setText("Min:");

        minNUD1.setModel(new javax.swing.SpinnerNumberModel());

        initialNUD1.setModel(new javax.swing.SpinnerNumberModel());

        jLabel12.setText("Initial:");

        jLabel13.setText("Max:");

        maxNUD1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(10), null, null, Integer.valueOf(1)));

        javax.swing.GroupLayout sliderTabLayout = new javax.swing.GroupLayout(sliderTab);
        sliderTab.setLayout(sliderTabLayout);
        sliderTabLayout.setHorizontalGroup(
            sliderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sliderTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sliderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sliderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(minNUD1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(initialNUD1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(maxNUD1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
                .addContainerGap())
        );
        sliderTabLayout.setVerticalGroup(
            sliderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sliderTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sliderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(minNUD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(sliderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(initialNUD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(sliderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(maxNUD1)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Slider", sliderTab);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );

        jLabel5.setText("Title:");
        jLabel5.setToolTipText("");

        titleTB.setForeground(new java.awt.Color(204, 204, 204));
        titleTB.setText("Enter a title");
        titleTB.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                titleFocus(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                titleNoFocus(evt);
            }
        });
        titleTB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                titleTBKeyReleased(evt);
            }
        });

        addButton.setText("Apply");
        addButton.setEnabled(false);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(titleTB, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleTB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(addButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addButtonActionPerformed
    {//GEN-HEADEREND:event_addButtonActionPerformed
        type = tabbedPane.getSelectedIndex();
        boolean approved = false;
        switch (type) {
            case NUMBER:
                numParams = new int[]{
                    Integer.parseInt(minNUD.getValue().toString()),
                    Integer.parseInt(initialNUD.getValue().toString()),
                    Integer.parseInt(maxNUD.getValue().toString())
                };
                if (numParams[1] >= numParams[0] && numParams[1] <= numParams[2]) {
                    approved = true;
                }
                else {
                    JOptionPane.showMessageDialog(rootPane, "Initial, Minimum and Maximum values are incorrect!\nPlease try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case BOOLEAN:
                approved = true;
                break;
            case STRING:
                approved = true;
                break;
            case SLIDER:
                sliderParams = new int[]{
                    Integer.parseInt(minNUD1.getValue().toString()),
                    Integer.parseInt(initialNUD1.getValue().toString()),
                    Integer.parseInt(maxNUD1.getValue().toString())
                };
                if (sliderParams[1] >= sliderParams[0] && sliderParams[0] < sliderParams[2] && sliderParams[1] <= sliderParams[2]) {
                    approved = true;
                }
                else {
                    JOptionPane.showMessageDialog(rootPane, "Initial, Minimum and Maximum values are incorrect!\nPlease try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
        }
        if (approved) {
            result = ADD;
            dispose();
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void titleTBKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_titleTBKeyReleased
    {//GEN-HEADEREND:event_titleTBKeyReleased
        if (titleTB.getText().length() > 0 && !titleTB.getText().equals("Enter a title")) {
            addButton.setEnabled(true);
        }
        else {
            addButton.setEnabled(false);
        }
    }//GEN-LAST:event_titleTBKeyReleased

    private void initiallyCheckedActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_initiallyCheckedActionPerformed
    {//GEN-HEADEREND:event_initiallyCheckedActionPerformed
        initiallyUnchecked.setSelected(!initiallyChecked.isSelected());
    }//GEN-LAST:event_initiallyCheckedActionPerformed

    private void initiallyUncheckedActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_initiallyUncheckedActionPerformed
    {//GEN-HEADEREND:event_initiallyUncheckedActionPerformed
        initiallyChecked.setSelected(!initiallyUnchecked.isSelected());
    }//GEN-LAST:event_initiallyUncheckedActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
    {//GEN-HEADEREND:event_cancelButtonActionPerformed
        result = CANCEL;
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

private void titleFocus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_titleFocus
    if (titleTB.getText().equals("Enter a title") && titleTB.getForeground() == Color.lightGray) {
        titleTB.setText("");
        titleTB.setForeground(Color.black);
    }
}//GEN-LAST:event_titleFocus

private void titleNoFocus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_titleNoFocus
    if (titleTB.getText().length() < 1) {
        titleTB.setText("Enter a title");
        titleTB.setForeground(Color.lightGray);
    }
}//GEN-LAST:event_titleNoFocus

    public String getInfo() {
        if (type == NUMBER) {
            return Integer.toString(numParams[0]) + "," + Integer.toString(numParams[1]) + "," + Integer.toString(numParams[2]);
        }
        else if (type == BOOLEAN) {
            String trueText = trueTB.getText(), falseText = falseTB.getText();
            if (trueText.length() < 1) {
                trueText = "This parameter is true";
            }
            if (falseText.length() < 1) {
                falseText = "This parameter is false";
            }
            if ((falseText + trueText).contains(",")) {
                JOptionPane.showMessageDialog(rootPane, "Sorry, no commas are allowed within the true/false texts.\nThey have been removed.", "Error", JOptionPane.OK_OPTION);
                falseText = falseText.replaceAll(",", "");
                trueText = trueText.replaceAll(",", "");
            }
            return Boolean.toString(initiallyChecked.isSelected()) + "," + trueText + "," + falseText;
        }
        else if (type == SLIDER) {
            return Integer.toString(sliderParams[0]) + "," + Integer.toString(sliderParams[1]) + "," + Integer.toString(sliderParams[2]);
        }
        return "";
    }

    public int getParameterType() {
        return type;
    }

    public String getParameter() {
        return titleTB.getText();
    }

    public int getResult() {
        return result;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel booleanTab;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField falseTB;
    private javax.swing.JSpinner initialNUD;
    private javax.swing.JSpinner initialNUD1;
    private javax.swing.JRadioButton initiallyChecked;
    private javax.swing.JRadioButton initiallyUnchecked;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner maxNUD;
    private javax.swing.JSpinner maxNUD1;
    private javax.swing.JSpinner minNUD;
    private javax.swing.JSpinner minNUD1;
    private javax.swing.JPanel numberTab;
    private javax.swing.JPanel sliderTab;
    private javax.swing.JPanel stringTab;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTextField titleTB;
    private javax.swing.JTextField trueTB;
    // End of variables declaration//GEN-END:variables
}
