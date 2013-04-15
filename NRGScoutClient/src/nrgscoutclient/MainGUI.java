package nrgscoutclient;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.*;
import java.util.LinkedList;
import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle;
import javax.swing.filechooser.FileFilter;
import org.omg.PortableServer.POAManagerPackage.State;

public class MainGUI extends JFrame {

    private LinkedList<ParameterPanel> params = new LinkedList<ParameterPanel>();
    static boolean saved = true;
    ClientGUI clientGUI = new ClientGUI();
    public static int alliance = 0;

    public MainGUI() {
        initComponents();
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/nrgscoutclient/icon.png")).getImage());
        setCenterScreen();
        //Initialize switch
        final AllianceSwitch as = new AllianceSwitch();
        allianceSwitchPanel.add(as);
        as.setSize(allianceSwitchPanel.getSize());
        //Initialize Client        
        clientGUI.setLoc(this.getLocation().x + this.getSize().width, this.getLocation().y);
        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                }
                catch (Exception e) {
                }
                while (true) {
                    ParameterPanel panel = null;
                    try {
                        Thread.sleep(100);
                        as.setSize(allianceSwitchPanel.getSize());
                        as.repaint();
                        for (int i = 0; i < params.size(); i++) {
                            if (!params.get(i).isActive()) {
                                params.remove(i);
                                refresh();
                                if (params.size() < 1) {
                                    saved = true;
                                }
                            }
                            int spinnerValue = params.get(i).getSpinnerValue();
                            panel = params.get(i);
                            if (spinnerValue > 100 && params.size() > 1) {
                                params.set(i, params.get(i + 1));
                                params.set(i + 1, panel);
                                refresh();
                            }
                            if (spinnerValue < 100 && params.size() > 1) {
                                params.set(i, params.get(i - 1));
                                params.set(i - 1, panel);
                                refresh();
                            }
                            panel.resetSpinnerValue();
                        }
                    }
                    catch (Exception e) {
                        System.err.println("Error");
                        if (panel != null) {
                            panel.resetSpinnerValue();
                        }
                    }
                }
            }
        };
        new Thread(r).start();
        this.setMinimumSize(this.getSize());
        if (new File(this.getDir() + "\\params.config").exists()) {
            Runnable startUp = new Runnable() {

                @Override
                public void run() {
                    int i = JOptionPane.showConfirmDialog(rootPane, "A local params.config file has been found.\nWould you like to import the parameters?", "Startup", JOptionPane.YES_NO_OPTION);
                    if (i == 0) {
                        openConfig(false, true);
                    }
                }
            };
            new Thread(startUp).start();
        }
    }

    private void setCenterScreen() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int locX = dim.width / 2 - this.getWidth() / 2;
        int locY = dim.height / 2 - this.getHeight() / 2;
        this.setLocation(locX, locY);
    }

    private void openConfig(boolean showError, boolean localDir) {
        int choice = 1;
        if (!saved) {
            choice = JOptionPane.showConfirmDialog(rootPane, "All collected scouting data will be cleared.\nSave before loading a new configuration?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            if (choice == 0) {
                save();
                choice = 1;
            }
            else if (choice == 2) {
                return;
            }
        }
        if (!localDir && choice == 1) {
            JFileChooser chooser = new JFileChooser(getDir());
            chooser.setDialogTitle("Open config");
            FileFilter type1 = (FileFilter) new ExtensionFilter("Configuration file (*.config)", ".config");
            chooser.addChoosableFileFilter(type1);
            chooser.setFileFilter(type1);
            int i = chooser.showOpenDialog(rootPane);
            if (i == 0) {
                String file = chooser.getSelectedFile().getAbsolutePath();
                try {
                    FileInputStream fstream = new FileInputStream(file);
                    // Get the object of DataInputStream
                    DataInputStream in = new DataInputStream(fstream);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String line;
                    params.clear();
                    while ((line = br.readLine()) != null) {
                        String stringType = line.split(":")[0];
                        if (stringType.equals("number")) {
                            String[] config = {
                                line.split(":")[2],
                                "0",
                                line.split(":")[1]
                            };
                            params.add(new ParameterPanel(config));
                        }
                        else if (stringType.equals("boolean")) {
                            String[] config = {
                                line.split(":")[2],
                                "1",
                                line.split(":")[1]
                            };
                            params.add(new ParameterPanel(config));
                        }
                        else if (stringType.equals("string")) {
                            String[] config = {
                                line.split(":")[2],
                                "2",
                                line.split(":")[1]
                            };
                            params.add(new ParameterPanel(config));
                        }
                        else if (stringType.equals("slider")) {
                            String[] config = {
                                line.split(":")[2],
                                "3",
                                line.split(":")[1]
                            };
                            params.add(new ParameterPanel(config));
                        }
                    }
                    saved = true;
                    in.close();
                    refresh();
                }
                catch (Exception e) {
                    if (showError) {
                        JOptionPane.showMessageDialog(rootPane, "Failed to read the parameters from the config file.\nThe file may be corrupt, invalid, or nonexistent.", "Error: " + e.toString(), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        else {
            try {
                params.clear();
                FileInputStream fstream = new FileInputStream("params.config");
                // Get the object of DataInputStream
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    String stringType = line.split(":")[0];
                    if (stringType.equals("number")) {
                        String[] config = {
                            line.split(":")[2],
                            "0",
                            line.split(":")[1]
                        };
                        params.add(new ParameterPanel(config));
                    }
                    else if (stringType.equals("boolean")) {
                        String[] config = {
                            line.split(":")[2],
                            "1",
                            line.split(":")[1]
                        };
                        params.add(new ParameterPanel(config));
                    }
                    else if (stringType.equals("string")) {
                        String[] config = {
                            line.split(":")[2],
                            "2",
                            line.split(":")[1]
                        };
                        params.add(new ParameterPanel(config));
                    }
                    else if (stringType.equals("slider")) {
                        String[] config = {
                            line.split(":")[2],
                            "3",
                            line.split(":")[1]
                        };
                        params.add(new ParameterPanel(config));
                    }
                }
                saved = true;
                in.close();
                refresh();
            }
            catch (Exception e) {
                if (showError) {
                    JOptionPane.showMessageDialog(rootPane, "Failed to load parameters from params.config file." + "\nPlease make sure the params.config file is in the same\ndirectory as the application.", "Error: " + e.toString(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void openScoutFile(boolean showError) {
        int choice = 1;
        if (!saved) {
            choice = JOptionPane.showConfirmDialog(rootPane, "All collected scouting data will be cleared.\nSave before opening another file?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            if (choice == 0) {
                save();
                choice = 1;
            }
            else if (choice == 2) {
                return;
            }
        }
        if (choice == 1) {
            JFileChooser chooser = new JFileChooser(getDir());
            chooser.setDialogTitle("Open scouting data");
            FileFilter type1 = (FileFilter) new ExtensionFilter("Scouting data (*.scout)", ".scout");
            chooser.addChoosableFileFilter(type1);
            chooser.setFileFilter(type1);
            int i = chooser.showOpenDialog(rootPane);
            if (i == 0) {
                String file = chooser.getSelectedFile().getAbsolutePath();
                try {
                    FileInputStream fstream = new FileInputStream(file);
                    // Get the object of DataInputStream
                    DataInputStream in = new DataInputStream(fstream);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String line;
                    params.clear();
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("team=")) {
                            teamNumNUD.setValue(Integer.parseInt(line.split("=")[1]));
                        }
                        else if (line.startsWith("match=")) {
                            matchNUD.setValue(Integer.parseInt(line.split("=")[1]));
                        }
                        else if (line.startsWith("alliance=")) {
                            alliance = (Integer.parseInt(line.split("=")[1]));
                        }
                        else {
                            while (line != null) {
                                if (line.startsWith("team=")) {
                                    teamNumNUD.setValue(Integer.parseInt(line.split("=")[1]));
                                }
                                else {
                                    //String info = br.readLine();
                                    String stringType = line.split(":")[0];
                                    ParameterPanel p = null;
                                    if (stringType.equals("number")) {
                                        String[] config = {
                                            line.split(":")[2],
                                            "0",
                                            line.split(":")[1]
                                        };
                                        p = new ParameterPanel(config);
                                    }
                                    else if (stringType.equals("boolean")) {
                                        String[] config = {
                                            line.split(":")[2],
                                            "1",
                                            line.split(":")[1]
                                        };
                                        p = new ParameterPanel(config);
                                    }
                                    else if (stringType.equals("string")) {
                                        String[] config = {
                                            line.split(":")[2],
                                            "2",
                                            line.split(":")[1]
                                        };
                                        p = new ParameterPanel(config);
                                    }
                                    else if (stringType.equals("slider")) {
                                        String[] config = {
                                            line.split(":")[2],
                                            "3",
                                            line.split(":")[1]
                                        };
                                        p = new ParameterPanel(config);
                                    }
                                    p.setInfo(br.readLine());
                                    params.add(p);
                                }
                                line = br.readLine();
                            }
                        }
                    }
                    saved = true;
                    in.close();
                    refresh();
                }
                catch (Exception e) {
                    if (showError) {
                        JOptionPane.showMessageDialog(rootPane, "Failed to load the parameters from the file.\nThe file may be corrupt, invalid, or nonexistent.", "Error: " + e.toString(), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private String getDir() {
        String dir = "";
        try {
            // get name and path
            String name = getClass().getName().replace('.', '/');
            name = getClass().getResource("/" + name + ".class").toString();
            // remove junk
            name = name.substring(0, name.indexOf(".jar"));
            name = name.substring(name.lastIndexOf(':') - 1, name.lastIndexOf('/') + 1).replace('%', ' ');
            // remove escape characters
            String s = "";
            for (int k = 0; k < name.length(); k++) {
                s += name.charAt(k);
                if (name.charAt(k) == ' ') {
                    k += 2;
                }
            }
            // replace '/' with system separator char
            System.out.println(s.replace('/', File.separatorChar));
            dir = s.replace('/', File.separatorChar);
        }
        catch (Exception e) {
        }
        return dir;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        layoutMain = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        teamNumNUD = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        matchNUD = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        allianceSwitchPanel = new javax.swing.JPanel();
        scrollView = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        saveLocalButton = new javax.swing.JButton();
        sendServerButton = new javax.swing.JButton();
        menu = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMB = new javax.swing.JMenuItem();
        openMB = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        exitMB = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        reloadLocalMB = new javax.swing.JMenuItem();
        serverConMB = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        uiMB = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("NRGScout");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closing(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                moved(evt);
            }
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resize(evt);
            }
        });

        layoutMain.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        layoutMain.setMinimumSize(new java.awt.Dimension(301, 448));

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Team Number:");

        teamNumNUD.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        teamNumNUD.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        teamNumNUD.setToolTipText("The team number of the team being watched (i.e 948)");
        teamNumNUD.setDoubleBuffered(true);
        teamNumNUD.setEditor(new javax.swing.JSpinner.NumberEditor(teamNumNUD, ""));
        teamNumNUD.setNextFocusableComponent(addButton);

        jLabel2.setText("Match Number:");

        matchNUD.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        matchNUD.setToolTipText("The match number");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Alliance:");

        allianceSwitchPanel.setToolTipText("Drag the slider to change the alliance");

        javax.swing.GroupLayout allianceSwitchPanelLayout = new javax.swing.GroupLayout(allianceSwitchPanel);
        allianceSwitchPanel.setLayout(allianceSwitchPanelLayout);
        allianceSwitchPanelLayout.setHorizontalGroup(
            allianceSwitchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 93, Short.MAX_VALUE)
        );
        allianceSwitchPanelLayout.setVerticalGroup(
            allianceSwitchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(matchNUD, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(allianceSwitchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(teamNumNUD)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(teamNumNUD)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(allianceSwitchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(matchNUD)
                        .addComponent(jLabel3)))
                .addContainerGap())
        );

        scrollView.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        scrollView.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollView.setToolTipText("");
        scrollView.setAutoscrolls(true);
        scrollView.setDoubleBuffered(true);

        mainPanel.setToolTipText("Add parameters here");

        addButton.setText("Add Parameter");
        addButton.setToolTipText("Adds a new parameter (i.e Score)");
        addButton.setPreferredSize(new java.awt.Dimension(0, 23));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(270, Short.MAX_VALUE))
        );

        scrollView.setViewportView(mainPanel);

        saveLocalButton.setText("Save data (local)");
        saveLocalButton.setToolTipText("Saves the collected scouting data to a directory on the local hard drive");
        saveLocalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveLocalButtonActionPerformed(evt);
            }
        });

        sendServerButton.setText("Send to server");
        sendServerButton.setToolTipText("Sends the collected scouting data to a remote server");
        sendServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendServerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layoutMainLayout = new javax.swing.GroupLayout(layoutMain);
        layoutMain.setLayout(layoutMainLayout);
        layoutMainLayout.setHorizontalGroup(
            layoutMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layoutMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layoutMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrollView, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layoutMainLayout.createSequentialGroup()
                        .addComponent(saveLocalButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendServerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layoutMainLayout.setVerticalGroup(
            layoutMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layoutMainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollView, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layoutMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveLocalButton)
                    .addComponent(sendServerButton))
                .addContainerGap())
        );

        fileMenu.setText("File");

        newMB.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMB.setText("New");
        newMB.setToolTipText("Clears all collected scouting data");
        newMB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMBActionPerformed(evt);
            }
        });
        fileMenu.add(newMB);

        openMB.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMB.setText("Open");
        openMB.setToolTipText("Opens a .scout file containing previously collected scouting data");
        openMB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMBActionPerformed(evt);
            }
        });
        fileMenu.add(openMB);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Save");
        jMenuItem1.setToolTipText("Saves the collected scouting data to a directory on the local hard drive");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        exitMB.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitMB.setText("Exit");
        exitMB.setToolTipText("Exits the application");
        exitMB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMBActionPerformed(evt);
            }
        });
        fileMenu.add(exitMB);

        menu.add(fileMenu);

        editMenu.setText("Edit");

        reloadLocalMB.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        reloadLocalMB.setText("Import configuration");
        reloadLocalMB.setToolTipText("Load the parameters set forth in a params.config on the local hard drive");
        reloadLocalMB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadLocalMBActionPerformed(evt);
            }
        });
        editMenu.add(reloadLocalMB);

        serverConMB.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));
        serverConMB.setText("Server connection");
        serverConMB.setToolTipText("Alter server connectivity settings");
        serverConMB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverConMBActionPerformed(evt);
            }
        });
        editMenu.add(serverConMB);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem3.setText("Export configuration");
        jMenuItem3.setToolTipText("Saves the parameters in a .config file");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3saveConfigMBActionPerformed(evt);
            }
        });
        editMenu.add(jMenuItem3);

        menu.add(editMenu);

        helpMenu.setText("Help");

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem5.setText("About");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        helpMenu.add(jMenuItem5);

        uiMB.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        uiMB.setText("UI Options");
        uiMB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uiMBActionPerformed(evt);
            }
        });
        helpMenu.add(uiMB);

        menu.add(helpMenu);

        setJMenuBar(menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layoutMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layoutMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        addParameter();
    }//GEN-LAST:event_addButtonActionPerformed

    private void saveLocalButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveLocalButtonActionPerformed
        save();
    }//GEN-LAST:event_saveLocalButtonActionPerformed

    private void sendServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendServerButtonActionPerformed
        //Send data to server
        if (clientGUI.getClient().isConnected()) {
            //Send data to server
            sendToServer();
        }
        else {
            int result = JOptionPane.showConfirmDialog(rootPane, "Server communication has not been established.\nConfigure server settings to establish connection?", "Server communication unavailable", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                clientGUI.attach();
            }
        }
    }//GEN-LAST:event_sendServerButtonActionPerformed

    private void newMBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMBActionPerformed
        if (params.size() > 0) {
            int i = 0;
            if (!saved) {
                i = JOptionPane.showConfirmDialog(rootPane, "All collected scouting data will be discarded.\n Save before clearing all parameters?", "New", JOptionPane.YES_NO_CANCEL_OPTION);
                if (i == 0) {
                    save();
                    if (saved) {
                        i = 1;
                    }
                    else {
                        i = 2;
                    }
                }
            }
            if (i != 2) {
                params.clear();
                refresh();
                teamNumNUD.setValue(0);
                matchNUD.setValue(1);
                alliance = 0;
                saved = true;
            }
        }
    }//GEN-LAST:event_newMBActionPerformed

    private void openMBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMBActionPerformed
        openScoutFile(true);
    }//GEN-LAST:event_openMBActionPerformed

    private void jMenuItem3saveConfigMBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3saveConfigMBActionPerformed
        saveConfig();
    }//GEN-LAST:event_jMenuItem3saveConfigMBActionPerformed

    private void exitMBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMBActionPerformed
        exit();
    }//GEN-LAST:event_exitMBActionPerformed

    private void reloadLocalMBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadLocalMBActionPerformed
        openConfig(true, false);
    }//GEN-LAST:event_reloadLocalMBActionPerformed

    private void serverConMBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serverConMBActionPerformed
        clientGUI.setVisible(true);
        clientGUI.attach();
    }//GEN-LAST:event_serverConMBActionPerformed

    private void uiMBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uiMBActionPerformed
        UserInterfaceGUI gui = new UserInterfaceGUI(this, true, rootPane);
        gui.setVisible(true);
        if (gui.result == gui.APPLY) {
            clientGUI.refresh();
        }
    }//GEN-LAST:event_uiMBActionPerformed

    private void closing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closing
        exit();
    }//GEN-LAST:event_closing

    private void moved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_moved
        clientGUI.setLoc(this.getLocation().x + this.getSize().width, this.getLocation().y);
    }//GEN-LAST:event_moved

    private void resize(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_resize
        clientGUI.setLoc(this.getLocation().x + this.getSize().width, this.getLocation().y);
    }//GEN-LAST:event_resize

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

        save();     }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        new AboutDialog(this, true).setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void addParameter() {
        String title = "Parameter";
        AddParamDialog gui = new AddParamDialog(this, true);
        gui.setVisible(true);
        if (gui.getResult() == gui.ADD) {
            try {
                if (title != null && title.trim().length() > 0) {
                    boolean exists = false;
                    for (ParameterPanel p : params) {
                        if (p.getTitle().equals(title)) {
                            exists = true;
                            JOptionPane.showMessageDialog(rootPane, "Parameter \"" + title + "\" is already in use.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    if (!exists) {
                        String[] args = {
                            gui.getParameter(),
                            Integer.toString(gui.getParameterType()),
                            gui.getInfo(),};
                        params.add(new ParameterPanel(args));
                        saved = false;
                        refresh();
                    }
                }
                else if (title != null && title.trim().length() < 1) {
                    JOptionPane.showMessageDialog(rootPane, "Could not add a new parameter because the name was blank.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch (Exception e) {
            }
        }
    }

    private void exit() {
        if (!saved) {
            int i = JOptionPane.showConfirmDialog(rootPane, "All collected scouting data will be cleared.\nSave before exiting?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION);
            if (i == 0) {
                save();
                System.exit(0);
            }
            else if (i == 1) {
                clientGUI.getClient().stop();
                System.exit(0);
            }
            else {
                this.setState(State._HOLDING);
            }
        }
        else {
            clientGUI.getClient().stop();
            System.exit(0);
        }
    }

    private void save() {
        boolean save = true;
        if (params.size() < 1) {
            int c = JOptionPane.showConfirmDialog(rootPane, "No data has been collected. Save anyway?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
            if (c == 0) {
                save = true;
            }
            else if (c == 1) {
                save = false;
            }
            else {
                return;
            }
        }
        boolean b = false;
        for (ParameterPanel p : params) {
            if (p.getInfo().length() < 1 && !b && p.getParameterInfo().split(":")[0].equals("string")) {
                save = false;
                int i = JOptionPane.showConfirmDialog(rootPane, "One or more parameters are blank. Save anyway?", "Error", JOptionPane.YES_NO_CANCEL_OPTION);
                b = true;
                if (i == 0) {
                    save = true;
                }
                else if (i == 2) {
                    return;
                }
            }
        }
        if (save) {
            JFileChooser chooser = new JFileChooser(getDir());
            chooser.setDialogTitle("Save scouting data");
            chooser.setSelectedFile(new File("team" + teamNumNUD.getValue() + "match" + matchNUD.getValue() + ".scout"));
            FileFilter type1 = (FileFilter) new ExtensionFilter("Scouting data (*.scout)", ".scout");
            chooser.addChoosableFileFilter(type1);
            chooser.setFileFilter(type1);
            int i = chooser.showSaveDialog(rootPane);
            if (i == 0) {
                try {
                    if (chooser.getSelectedFile().exists()) {
                        int choice = JOptionPane.showConfirmDialog(rootPane, "The file " + chooser.getSelectedFile().getName() + " already exists!\nWould you like to overwrite the file?", "Save", JOptionPane.OK_CANCEL_OPTION);
                        if (choice == 0) {
                            String extension = "";
                            if (!chooser.getSelectedFile().getName().endsWith(".scout")) {
                                extension = ".scout";
                            }
                            FileWriter writer = new FileWriter(chooser.getSelectedFile().getAbsolutePath() + extension);
                            writer.write("team=" + teamNumNUD.getValue() + "\r\n" + "match=" + Integer.parseInt(matchNUD.getValue().toString()) + "\r\n" + "alliance=" + alliance + "\r\n");
                            for (ParameterPanel p : params) {
                                writer.write(p.getParameterInfo() + "\r\n" + p.getInfo() + "\r\n");
                            }
                            writer.flush();
                            writer.close();
                            saved = true;
                        }
                    }
                    else {
                        String extension = "";
                        if (!chooser.getSelectedFile().getName().endsWith(".scout")) {
                            extension = ".scout";
                        }
                        FileWriter writer = new FileWriter(chooser.getSelectedFile().getAbsolutePath() + extension);
                        writer.write("team=" + teamNumNUD.getValue() + "\r\n" + "match=" + Integer.parseInt(matchNUD.getValue().toString()) + "\r\n" + "alliance=" + alliance + "\r\n");
                        for (ParameterPanel p : params) {
                            writer.write(p.getParameterInfo() + "\r\n" + p.getInfo() + "\r\n");
                        }
                        writer.flush();
                        writer.close();
                        saved = true;
                    }
                }
                catch (IOException e) {
                    JOptionPane.showMessageDialog(rootPane, "Failed to save file " + chooser.getSelectedFile().getName() + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void saveConfig() {
        boolean save = true;
        if (params.size() < 1) {
            int c = JOptionPane.showConfirmDialog(rootPane, "No parameters have been added. Save anyway?", "Save", JOptionPane.YES_NO_OPTION);
            if (c == 0) {
                save = true;
            }
            else {
                save = false;
            }
        }
        if (save) {
            JFileChooser chooser = new JFileChooser(getDir());
            chooser.setDialogTitle("Save config");
            chooser.setSelectedFile(new File("params.config"));
            FileFilter type1 = (FileFilter) new ExtensionFilter("Configuration file (*.config)", ".config");
            chooser.addChoosableFileFilter(type1);
            chooser.setFileFilter(type1);
            int i = chooser.showSaveDialog(rootPane);
            if (i == 0) {
                try {
                    if (chooser.getSelectedFile().exists()) {
                        int choice = JOptionPane.showConfirmDialog(rootPane, chooser.getSelectedFile().getName() + " already exists!\nWould you like to overwrite the file?", "Save", JOptionPane.YES_NO_OPTION);
                        if (choice == 0) {
                            String extension = "";
                            if (!chooser.getSelectedFile().getName().endsWith(".config")) {
                                extension = ".config";
                            }
                            FileWriter writer = new FileWriter(chooser.getSelectedFile().getAbsolutePath() + extension);
                            for (ParameterPanel p : params) {
                                writer.write(p.getParameterInfo() + "\r\n");
                            }
                            writer.flush();
                            writer.close();
                        }
                    }
                    else {
                        String extension = "";
                        if (!chooser.getSelectedFile().getName().endsWith(".config")) {
                            extension = ".config";
                        }
                        FileWriter writer = new FileWriter(chooser.getSelectedFile().getAbsolutePath() + extension);
                        for (ParameterPanel p : params) {
                            writer.write(p.getParameterInfo() + "\r\n");
                        }
                        writer.flush();
                        writer.close();
                    }
                }
                catch (IOException e) {
                    JOptionPane.showMessageDialog(rootPane, "Failed to save file " + chooser.getSelectedFile().getName() + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void refresh() {
        try {
            GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
            mainPanel.removeAll();
            mainPanel.setLayout(mainPanelLayout);
            GroupLayout.SequentialGroup group1 = mainPanelLayout.createSequentialGroup();
            GroupLayout.ParallelGroup group = mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(group1);
            for (ParameterPanel j : params) {
                group1.addComponent(j.getParameterPanel(), GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE);
                group1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
            }
            group1.addComponent(addButton);
            group1.addContainerGap(271, Short.MAX_VALUE);
            mainPanelLayout.setVerticalGroup(group);

            GroupLayout.ParallelGroup group2 = mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
            for (ParameterPanel j : params) {
                group2.addComponent(j.getParameterPanel(), GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
            }
            group2.addComponent(addButton, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE);
            mainPanelLayout.setHorizontalGroup(group2);
        }
        catch (Exception e) {
        }
        packScrollView();
    }

    private void packScrollView() {
        mainPanel.setPreferredSize(new Dimension(0, addButton.getHeight() + (51 * params.size())));
    }

    private void sendToServer() {
        String result = "";
        result += ("team=" + teamNumNUD.getValue().toString() + "\r\n" + "match=" + Integer.parseInt(matchNUD.getValue().toString()) + "\r\n" + "alliance=" + alliance + "\r\n");
        for (ParameterPanel p : params) {
            result += (p.getParameterInfo() + "\r\n" + p.getInfo() + "\r\n");
        }
        clientGUI.forwardToClient(result);
        clientGUI.append("\nSent scouting data to server...\n" + result);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel allianceSwitchPanel;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMB;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel layoutMain;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JSpinner matchNUD;
    private javax.swing.JMenuBar menu;
    private javax.swing.JMenuItem newMB;
    private javax.swing.JMenuItem openMB;
    private javax.swing.JMenuItem reloadLocalMB;
    private javax.swing.JButton saveLocalButton;
    private javax.swing.JScrollPane scrollView;
    private javax.swing.JButton sendServerButton;
    private javax.swing.JMenuItem serverConMB;
    private javax.swing.JSpinner teamNumNUD;
    private javax.swing.JMenuItem uiMB;
    // End of variables declaration//GEN-END:variables
}
