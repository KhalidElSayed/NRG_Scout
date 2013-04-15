package nrgscoutserver;

import java.awt.Dimension;
import javax.swing.*;

public class ParameterPanel {

    private String title;
    private int type = 0;
    private String info = "", trueText, falseText;
    public static int BOOL = 2;
    private final JSpinner NUD;
    private final JToggleButton toggleButton;
    private final JSlider slider;
    private JPanel paramPanel;
    private JTextField textField;
    private JButton xButton;
    private boolean isActive = true;
    private JComponent component;
    private String filterInfo;
    private String defaultFilterInfo;

    public ParameterPanel(String[] args) {
        /**
         * @param args: index 1: type (0 = number, 1 = boolean, 2 = string) index 2: info index 0: title
         */
        title = args[0].trim();
        if (title == null || title.length() < 1) {
            title = "Parameter";
        }
        paramPanel = new JPanel();
        textField = new JTextField();
        textField.setHorizontalAlignment(JTextField.TRAILING);
        toggleButton = new JToggleButton();
        toggleButton.setText("This parameter is false");
        toggleButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (toggleButton.isSelected()) {
                    toggleButton.setText(trueText);
                } else {
                    toggleButton.setText(falseText);
                }
            }
        });
        NUD = new JSpinner();
        NUD.setEditor(new javax.swing.JSpinner.NumberEditor(NUD, ""));
        /**
         * Slider
         */
        slider = new JSlider();
        xButton = new JButton();
        xButton.setMinimumSize(new Dimension(20, 20));
        xButton.setIcon(new ImageIcon(getClass().getResource("/nrgscoutserver/X_Button.png")));
        xButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paramPanel.removeAll();
                paramPanel = null;
                isActive = false;
            }
        });
        xButton.setFocusable(false);
        xButton.setToolTipText("Remove this parameter");
        type = Integer.parseInt(args[1]);
        switch (type) {
            case 0:
                int min = Integer.parseInt(args[2].split(",")[0]);
                int initial = Integer.parseInt(args[2].split(",")[1]);
                int max = Integer.parseInt(args[2].split(",")[2]);
                NUD.setModel(new SpinnerNumberModel(
                        initial,
                        min,
                        max,
                        1.0));
                NUD.setValue(initial);
                NUD.setToolTipText("Select a number between " + min + " and " + max);
                info = ":" + min + "," + initial + "," + max + ":";
                filterInfo = min + "," + max;
                component = NUD;
                break;
            case 1:
                boolean checked = Boolean.parseBoolean(args[2].split(",")[0]);
                trueText = args[2].split(",")[1];
                falseText = args[2].split(",")[2];
                toggleButton.setSelected(checked);
                if (checked) {
                    toggleButton.setText(trueText);
                } else {
                    toggleButton.setText(falseText);
                }
                info = ":" + Boolean.toString(checked) + "," + trueText + "," + falseText + ":";
                filterInfo = "none";
                component = toggleButton;
                break;
            case 2:
                component = textField;
                info = ":null:";
                filterInfo = "";
                break;
            case 3:
                int minSlider = Integer.parseInt(args[2].split(",")[0]);
                int initialSlider = Integer.parseInt(args[2].split(",")[1]);
                int maxSlider = Integer.parseInt(args[2].split(",")[2]);
                slider.setMaximum(maxSlider);
                slider.setMinimum(minSlider);
                slider.setValue(initialSlider);
                slider.setMinorTickSpacing(1);
                info = ":" + minSlider + "," + initialSlider + "," + maxSlider + ":";
                slider.setToolTipText("Slider Values: (min=" + slider.getMinimum() + ", max=" + slider.getMaximum() + ")");
                component = slider;
                filterInfo = minSlider + "," + maxSlider;
                break;
        }
        defaultFilterInfo = filterInfo;
        //Initialize ParamPanel
        paramPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), title));
        paramPanel.setFont(new java.awt.Font("Tahoma", 0, 12));
        GroupLayout paramPanelLayout = new GroupLayout(paramPanel);
        paramPanel.setLayout(paramPanelLayout);
        paramPanelLayout.setHorizontalGroup(
                paramPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, paramPanelLayout.createSequentialGroup().addComponent(component, GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(xButton, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)));
        paramPanelLayout.setVerticalGroup(
                paramPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(paramPanelLayout.createSequentialGroup().addGroup(paramPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(xButton, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE).addComponent(component, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    public void setFilterInfo(String filterInfo) {
        this.filterInfo = filterInfo;
        /**
         * For number/slider: min + "," + max
         * For boolean: "none" or "true" or "false"
         * For string: "anything"
         */
    }

    public String getFilterInfo() {
        return filterInfo;
    }

    public ParameterPanel(String[] args, boolean enabled) {
        /**
         * @param args: index 1: type (0 = number, 1 = boolean, 2 = string) index 2: info index 0: title
         */
        title = args[0].trim();
        if (title == null || title.length() < 1) {
            title = "Parameter";
        }
        paramPanel = new JPanel();
        textField = new JTextField();
        textField.setHorizontalAlignment(JTextField.TRAILING);
        toggleButton = new JToggleButton();
        toggleButton.setText("This parameter is false");
        toggleButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (toggleButton.isSelected()) {
                    toggleButton.setText(trueText);
                } else {
                    toggleButton.setText(falseText);
                }
            }
        });
        NUD = new JSpinner();
        /**
         * Slider
         */
        slider = new JSlider();
        xButton = new JButton();
        xButton.setMinimumSize(new Dimension(20, 20));
        xButton.setIcon(new ImageIcon(getClass().getResource("/nrgscoutserver/X_Button.png")));
        xButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paramPanel.removeAll();
                paramPanel = null;
                isActive = false;
            }
        });
        xButton.setFocusable(false);
        xButton.setToolTipText("Remove this parameter");
        type = Integer.parseInt(args[1]);
        switch (type) {
            case 0:
                int min = Integer.parseInt(args[2].split(",")[0]);
                int initial = Integer.parseInt(args[2].split(",")[1]);
                int max = Integer.parseInt(args[2].split(",")[2]);
                NUD.setModel(new SpinnerNumberModel(
                        initial,
                        min,
                        max,
                        1.0));
                NUD.setValue(initial);
                NUD.setToolTipText("Select a number between " + min + " and " + max);
                info = ":" + min + "," + initial + "," + max + ":";
                component = NUD;
                break;
            case 1:
                boolean checked = Boolean.parseBoolean(args[2].split(",")[0]);
                trueText = args[2].split(",")[1];
                falseText = args[2].split(",")[2];
                toggleButton.setSelected(checked);
                if (checked) {
                    toggleButton.setText(trueText);
                } else {
                    toggleButton.setText(falseText);
                }
                info = ":" + Boolean.toString(checked) + "," + trueText + "," + falseText + ":";
                component = toggleButton;
                break;
            case 2:
                component = textField;
                info = ":null:";
                break;
            case 3:
                int minSlider = Integer.parseInt(args[2].split(",")[0]);
                int initialSlider = Integer.parseInt(args[2].split(",")[1]);
                int maxSlider = Integer.parseInt(args[2].split(",")[2]);
                slider.setMaximum(maxSlider);
                slider.setMinimum(minSlider);
                slider.setValue(initialSlider);
                slider.setMinorTickSpacing(1);
                info = ":" + minSlider + "," + initialSlider + "," + maxSlider + ":";
                slider.setToolTipText("Slider Values: (min=" + slider.getMinimum() + ", current=" + slider.getValue() + ", max=" + slider.getMaximum() + ")");
                component = slider;
                break;
        }
        if (enabled) {
            //Initialize ParamPanel
            paramPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), title));
            paramPanel.setFont(new java.awt.Font("Tahoma", 0, 12));
            GroupLayout paramPanelLayout = new GroupLayout(paramPanel);
            paramPanel.setLayout(paramPanelLayout);
            paramPanelLayout.setHorizontalGroup(
                    paramPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, paramPanelLayout.createSequentialGroup().addComponent(component, GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(xButton, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)));
            paramPanelLayout.setVerticalGroup(
                    paramPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(paramPanelLayout.createSequentialGroup().addGroup(paramPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(xButton, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE).addComponent(component, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        } else {
            component.setEnabled(false);
            //Initialize ParamPanel
            paramPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), title));
            paramPanel.setFont(new java.awt.Font("Tahoma", 0, 12));
            GroupLayout paramPanelLayout = new GroupLayout(paramPanel);
            paramPanel.setLayout(paramPanelLayout);
            paramPanelLayout.setHorizontalGroup(
                    paramPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, paramPanelLayout.createSequentialGroup().addComponent(component, GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)));
            paramPanelLayout.setVerticalGroup(
                    paramPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(paramPanelLayout.createSequentialGroup().addGroup(paramPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(component, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        }
    }

    public JPanel getParameterPanel() {
        return paramPanel;
    }

    public String getTitle() {
        return title;
    }

    public String getParameterInfo() {
        String result = "";
        switch (type) {
            case 0:
                result = "number" + info + title;
                break;
            case 1:
                result = "boolean" + info + title;
                break;
            case 2:
                result = "string" + info + title;
                break;
            case 3:
                result = "slider" + info + title;
        }
        return result;
    }

    public void setInfo(String s) {
        switch (type) {
            case 0:
                NUD.setValue((int) Double.parseDouble(s));
                break;
            case 1:
                toggleButton.setSelected(Boolean.parseBoolean(s));
                toggleButton.setText(toggleButton.isSelected() ? trueText : falseText);
                break;
            case 2:
                textField.setText(s);
                break;
            case 3:
                slider.setValue((int) Double.parseDouble(s));
                break;
        }
    }

    public String getCollectedData() {
        switch (type) {
            case 0:
                return Integer.toString(Integer.parseInt(NUD.getValue().toString()));
            case 1:
                return toggleButton.isSelected() + "";
            case 2:
                return textField.getText();
            default:
                return slider.getValue() + "";
        }
    }

    public void kill() {
        isActive = false;
    }

    public void setParameter(String s) {
        paramPanel.setBorder(BorderFactory.createTitledBorder(s));
    }

    public boolean isActive() {
        return isActive;
    }

    public String getInfo() {
        return info;
    }

    public String getType() {
        switch (type) {
            case 0:
                return "number";
            case 1:
                return "boolean";
            case 2:
                return "string";
            default:
                return "slider";
        }
    }

    public int getRawType() {
        return type;
    }

    public void resetFilter() {
        filterInfo = defaultFilterInfo;
    }
}
