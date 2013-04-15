package nrgscoutserver;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.InetAddress;
import javax.swing.*;
import java.util.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

public class MainGUI extends JFrame {

    private LinkedList<Team> teams = new LinkedList<Team>();
    public static boolean running = true;
    public final int STRING = 2;
    public final int SLIDER = 3;
    public final int NUMBER = 0;
    public final int BOOLEAN = 1;
    private LinkedList<ParameterPanel> params = new LinkedList<ParameterPanel>();
    private LinkedList<ParameterPanel> previewParams = new LinkedList<ParameterPanel>();
    private LinkedList<ParameterPanel> matchParams = new LinkedList<ParameterPanel>();
    private LinkedList<Match> matches = new LinkedList<Match>();
    private LinkedList<String> teamData = new LinkedList<String>();
    public static boolean override = false;
    private static String[] overridenData;
    AllianceSwitch as;

    public MainGUI() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName()) || "GTK+".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                }
            }
        } catch (Exception e) {
        }
        initComponents();
        setIPAddress();
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/nrgscoutserver/icon.png")).getImage());
        matchTabbedPane.remove(1);
        TCPServer server = new TCPServer();
        server.start();
        tabbedPane.setSelectedIndex(1);
        setCenterScreen();
        //AllianceSwitch
        as = new AllianceSwitch();
        as.setSize(this.allianceSwitchPanel.getSize());
        as.setAlliance(Alliance.ALLIANCE_RED);
        this.allianceSwitchPanel.add(as);
        this.setMinimumSize(this.getSize());
        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(250);
                } catch (Exception e) {
                }
                dumbScrollPane.setMinimumSize(new Dimension(0, 0));
                while (true) {
                    try {
                        Thread.sleep(100);
                        for (int i = 0; i < previewParams.size(); i++) {
                            if (!previewParams.get(i).isActive()) {
                                previewParams.remove(i);
                                refresh(previewPanel, previewParams);
                            }
                        }
                        for (int i = 0; i < matchParams.size(); i++) {
                            if (!matchParams.get(i).isActive()) {
                                matchParams.remove(i);
                                refresh(matchPreviewPanel, matchParams);
                            }
                        }
                        if (matchTable.getSelectedRow() == -1 && matchTabbedPane.getTabCount() > 1) {
                            matchTabbedPane.remove(1);
                        }
                        if (override) {
                            serverOverride();
                            override = false;
                        }
                    } catch (Exception e) {
                        System.err.println("Error");
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(r).start();
    }

    private void refreshViewTab() {
        try {
            int index = matchTable.getSelectedRow();
            int matchNumber = Integer.parseInt(matchTable.getModel().getValueAt(index, 0).toString());
            String redAlliance = matchTable.getModel().getValueAt(index, 1).toString();
            int red1 = Integer.parseInt(redAlliance.split(",")[0]);
            int red2 = Integer.parseInt(redAlliance.split(",")[1]);
            int red3 = Integer.parseInt(redAlliance.split(",")[2]);
            String blueAlliance = matchTable.getModel().getValueAt(index, 2).toString();
            int blue1 = Integer.parseInt(blueAlliance.split(",")[0]);
            int blue2 = Integer.parseInt(blueAlliance.split(",")[1]);
            int blue3 = Integer.parseInt(blueAlliance.split(",")[2]);
            int redScore = Integer.parseInt(matchTable.getModel().getValueAt(index, 3).toString());
            int blueScore = Integer.parseInt(matchTable.getModel().getValueAt(index, 4).toString());
            matchNumberNUD.setValue(matchNumber);
            redAllianceNUD1.setValue(red1);
            redAllianceNUD2.setValue(red2);
            redAllianceNUD3.setValue(red3);
            blueAllianceNUD1.setValue(blue1);
            blueAllianceNUD2.setValue(blue2);
            blueAllianceNUD3.setValue(blue3);
            redAllianceScoreNUD.setValue(redScore);
            blueAllianceScoreNUD.setValue(blueScore);
            redButton1.setText(red1 + "");
            redButton2.setText(red2 + "");
            redButton3.setText(red3 + "");
            blueButton1.setText(blue1 + "");
            blueButton2.setText(blue2 + "");
            blueButton3.setText(blue3 + "");
            redButton1.setEnabled(!(findMatch(matchNumber).getTeam(Alliance.ALLIANCE_RED, red1).getData() == null));
            redButton2.setEnabled(!(findMatch(matchNumber).getTeam(Alliance.ALLIANCE_RED, red2).getData() == null));
            redButton3.setEnabled(!(findMatch(matchNumber).getTeam(Alliance.ALLIANCE_RED, red3).getData() == null));
            blueButton1.setEnabled(!(findMatch(matchNumber).getTeam(Alliance.ALLIANCE_BLUE, blue1).getData() == null));
            blueButton2.setEnabled(!(findMatch(matchNumber).getTeam(Alliance.ALLIANCE_BLUE, blue2).getData() == null));
            blueButton3.setEnabled(!(findMatch(matchNumber).getTeam(Alliance.ALLIANCE_BLUE, blue3).getData() == null));
            matchParams.clear();
            matchViewPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Match " + matchNumber));
            refresh(matchPreviewPanel, matchParams);
            if (matchTabbedPane.getTabCount() < 2) {
                matchTabbedPane.addTab("View", viewTab);
            }
        } catch (Exception e) {
        }
    }

    private void refresh(JPanel previewPanel, LinkedList<ParameterPanel> parameters) {
        updatePreviewParamLists();
        try {
            GroupLayout previewPanelLayout = new GroupLayout(previewPanel);
            previewPanel.removeAll();
            previewPanel.setLayout(previewPanelLayout);
            GroupLayout.SequentialGroup group1 = previewPanelLayout.createSequentialGroup();
            GroupLayout.ParallelGroup group = previewPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(group1);
            for (ParameterPanel j : parameters) {
                group1.addComponent(j.getParameterPanel(), GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE);
                group1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
            }
            group1.addContainerGap(271, Short.MAX_VALUE);
            previewPanelLayout.setVerticalGroup(group);

            GroupLayout.ParallelGroup group2 = previewPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
            for (ParameterPanel j : parameters) {
                group2.addComponent(j.getParameterPanel(), GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
            }
            previewPanelLayout.setHorizontalGroup(group2);
        } catch (Exception e) {
        }
        packScrollView();
    }

    private void parseMatchParams(String[] teamData) {
        matchParams.clear();
        for (int i = 0; i < teamData.length; i++) {
            String data = teamData[i];
            String stringType = data.split(":")[0];
            ParameterPanel p = null;
            if (stringType.equals("number")) {
                String[] config = {
                    data.split(":")[2],
                    "0",
                    data.split(":")[1]
                };
                p = new ParameterPanel(config);
            } else if (stringType.equals("boolean")) {
                String[] config = {
                    data.split(":")[2],
                    "1",
                    data.split(":")[1]
                };
                p = new ParameterPanel(config);
            } else if (stringType.equals("string")) {
                String[] config = {
                    data.split(":")[2],
                    "2",
                    data.split(":")[1]
                };
                p = new ParameterPanel(config);
            } else if (stringType.equals("slider")) {
                String[] config = {
                    data.split(":")[2],
                    "3",
                    data.split(":")[1]
                };
                p = new ParameterPanel(config);
            }
            i++;
            String info = teamData[i];
            p.setInfo(info);
            matchParams.add(p);
        }
        refresh(matchPreviewPanel, matchParams);
    }

    private void packScrollView() {
        previewPanel.setPreferredSize(new Dimension(0, 51 * previewParams.size()));
        matchPreviewPanel.setPreferredSize(new Dimension(0, 53 * matchParams.size()));
    }

    private void openScoutFile(boolean showError) {
        int choice = 1;
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
                    previewParams.clear();
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("team=")) {
                            teamNumNUD.setValue(Integer.parseInt(line.split("=")[1]));
                        } else if (line.startsWith("match=")) {
                            matchNUD.setValue(Integer.parseInt(line.split("=")[1]));
                        } else if (line.startsWith("alliance=")) {
                            as.setAlliance(Integer.parseInt(line.split("=")[1]));
                        } else {
                            while (line != null) {
                                if (line.startsWith("team=")) {
                                    teamNumNUD.setValue(Integer.parseInt(line.split("=")[1]));
                                } else {
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
                                    } else if (stringType.equals("boolean")) {
                                        String[] config = {
                                            line.split(":")[2],
                                            "1",
                                            line.split(":")[1]
                                        };
                                        p = new ParameterPanel(config);
                                    } else if (stringType.equals("string")) {
                                        String[] config = {
                                            line.split(":")[2],
                                            "2",
                                            line.split(":")[1]
                                        };
                                        p = new ParameterPanel(config);
                                    } else if (stringType.equals("slider")) {
                                        String[] config = {
                                            line.split(":")[2],
                                            "3",
                                            line.split(":")[1]
                                        };
                                        p = new ParameterPanel(config);
                                    }
                                    String info = br.readLine();
                                    p.setInfo(info);
                                    previewParams.add(p);
                                }
                                line = br.readLine();
                            }
                        }
                    }
                    in.close();
                    updatePreviewParamLists();
                    refresh(previewPanel, previewParams);
                    System.out.println("\nREAD DATA:");
                    for (String s : teamData) {
                        System.out.println(s);
                    }
                } catch (Exception e) {
                    if (showError) {
                        JOptionPane.showMessageDialog(rootPane, "Failed to load the parameters from the file.\nThe file may be corrupt, invalid, or nonexistent.", "Error: " + e.toString(), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void updatePreviewParamLists() {
        LinkedList<String> list = new LinkedList<String>(), known = new LinkedList<String>(), unknown = new LinkedList<String>();
        for (ParameterPanel param : previewParams) {
            list.add(param.getTitle());
            if (exactParamExists(param.getTitle(), param.getRawType(), params)) {
                known.add(param.getTitle());
            } else {
                unknown.add(param.getTitle());
            }
        }
        final String[] model = list.toArray(new String[list.size()]);
        previewParamList.setModel(
                new AbstractListModel() {

                    @Override
                    public int getSize() {
                        return model.length;
                    }

                    @Override
                    public Object getElementAt(int i) {
                        return model[i];
                    }
                });
        final String[] knownModel = known.toArray(new String[list.size()]);
        knownList.setModel(
                new AbstractListModel() {

                    @Override
                    public int getSize() {
                        return knownModel.length;
                    }

                    @Override
                    public Object getElementAt(int i) {
                        return knownModel[i];
                    }
                });
        final String[] unknownModel = unknown.toArray(new String[list.size()]);
        unknownList.setModel(
                new AbstractListModel() {

                    @Override
                    public int getSize() {
                        return unknownModel.length;
                    }

                    @Override
                    public Object getElementAt(int i) {
                        return unknownModel[i];
                    }
                });
    }

    private LinkedList<ParameterPanel> getUnknownParams() {
        LinkedList<ParameterPanel> list = new LinkedList<ParameterPanel>();
        for (ParameterPanel param : previewParams) {
            if (!exactParamExists(param.getTitle(), param.getRawType(), params)) {
                list.add(param);
            }
        }
        return list;
    }

    private boolean paramExists(String name, LinkedList<ParameterPanel> parameters) {
        for (ParameterPanel param : parameters) {
            if (name.equals(param.getTitle())) {
                return true;
            }
        }
        return false;
    }

    private boolean exactParamExists(String name, int rawType, LinkedList<ParameterPanel> parameters) {
        for (ParameterPanel param : parameters) {
            if (name.equals(param.getTitle()) && param.getRawType() == rawType) {
                return true;
            }
        }
        return false;
    }

    private Team findTeam(int number) {
        for (Team team : teams) {
            if (team.getNumber() == number) {
                return team;
            }
        }
        return null;
    }

    private Match findMatch(int number) {
        for (Match match : matches) {
            if (match.getNumber() == number) {
                return match;
            }
        }
        return null;
    }

    private ParameterPanel findParameter(String paramName) {
        for (ParameterPanel panel : params) {
            if (panel.getTitle().equals(paramName)) {
                return panel;
            }
        }
        return null;
    }

    private void updateMatchTable() {
        int[] matchNums = new int[matches.size()];
        for (int i = 0; i < matches.size(); i++) {
            matchNums[i] = matches.get(i).getNumber();
        }
        //Sort numerically
        Arrays.sort(matchNums);
        LinkedList<Match> matches2 = new LinkedList<Match>();
        for (int number : matchNums) {
            matches2.add(findMatch(number));
        }
        matches.clear();
        matches = matches2;
        ((DefaultTableModel) matchTable.getModel()).setRowCount(0);
        for (int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            ((DefaultTableModel) matchTable.getModel()).insertRow(matchTable.getRowCount(),
                    new String[]{match.getNumber() + "",
                        match.getTeam(Alliance.ALLIANCE_RED, match.getAlliance(Alliance.ALLIANCE_RED).getTeam(0).getNumber()).getNumber()
                        + "," + match.getTeam(Alliance.ALLIANCE_RED, match.getAlliance(Alliance.ALLIANCE_RED).getTeam(1).getNumber()).getNumber()
                        + "," + match.getTeam(Alliance.ALLIANCE_RED, match.getAlliance(Alliance.ALLIANCE_RED).getTeam(2).getNumber()).getNumber(),
                        match.getTeam(Alliance.ALLIANCE_BLUE, match.getAlliance(Alliance.ALLIANCE_BLUE).getTeam(0).getNumber()).getNumber()
                        + "," + match.getTeam(Alliance.ALLIANCE_BLUE, match.getAlliance(Alliance.ALLIANCE_BLUE).getTeam(1).getNumber()).getNumber()
                        + "," + match.getTeam(Alliance.ALLIANCE_BLUE, match.getAlliance(Alliance.ALLIANCE_BLUE).getTeam(2).getNumber()).getNumber(),
                        match.getScore(Alliance.ALLIANCE_RED)
                        + "", match.getScore(Alliance.ALLIANCE_BLUE)
                        + ""
                    });
        }
        totalMatchesLabel.setText(matches.size() + "");
        updateAnalysisTable();
    }

    private void setCenterScreen() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int locX = dim.width / 2 - this.getWidth() / 2;
        int locY = dim.height / 2 - this.getHeight() / 2;
        this.setLocation(locX, locY);
    }

    private void updateTeamList() {
        int[] teamNums = new int[teams.size()];
        for (int i = 0; i < teams.size(); i++) {
            teamNums[i] = teams.get(i).getNumber();
        }
        Arrays.sort(teamNums);
        LinkedList<Team> teams2 = new LinkedList<Team>();
        for (int teamNum : teamNums) {
            teams2.add(findTeam(teamNum));
        }
        teams.clear();
        teams = teams2;
        String[] list = new String[teams.size()];
        for (int i = 0; i < teams.size(); i++) {
            list[i] = "Team " + teams.get(i).getNumber() + " - " + teams.get(i).getName();
        }
        final String[] model = list;
        teamList.setModel(new AbstractListModel() {

            @Override
            public int getSize() {
                return model.length;
            }

            @Override
            public Object getElementAt(int i) {
                return model[i];
            }
        });
        totalTeamsLabel.setText(teams.size() + "");
    }

    private void updateParameterList() {
        String[] list = new String[params.size()];
        LinkedList<ParameterPanel> numberSliderParams = new LinkedList<ParameterPanel>();
        LinkedList<ParameterPanel> trueFalseParams = new LinkedList<ParameterPanel>();
        LinkedList<ParameterPanel> textParams = new LinkedList<ParameterPanel>();
        for (int i = 0; i < params.size(); i++) {
            list[i] = params.get(i).getTitle();
            if (params.get(i).getRawType() == 0 || params.get(i).getRawType() == 3) {
                numberSliderParams.add(params.get(i));
            } else if (params.get(i).getRawType() == 1) {
                trueFalseParams.add(params.get(i));
            } else if (params.get(i).getRawType() == 2) {
                textParams.add(params.get(i));
            }
        }
        try {
            ///SET THE COMBO BOX MODELS
            LinkedList<String> names = new LinkedList<String>();
            for (ParameterPanel panel : numberSliderParams) {
                names.add(panel.getTitle());
            }
            this.numberParamCB.setModel(new javax.swing.DefaultComboBoxModel(names.toArray()));
            names.clear();
            for (ParameterPanel panel : trueFalseParams) {
                names.add(panel.getTitle());
            }
            this.tfParamCB.setModel(new javax.swing.DefaultComboBoxModel(names.toArray()));
            names.clear();
            for (ParameterPanel panel : textParams) {
                names.add(panel.getTitle());
            }
            this.textParamCB.setModel(new javax.swing.DefaultComboBoxModel(names.toArray()));
            names.clear();
            //
            final String[] model = list;
            paramList.setModel(new AbstractListModel() {

                @Override
                public int getSize() {
                    return model.length;
                }

                @Override
                public Object getElementAt(int i) {
                    return model[i];
                }
            });
            if (params.size() < 1) {
                paramTitle.setText("");
            }
            totalParametersLabel.setText(params.size() + "");

            /**
             * UPDATE ALL COMBO BOXES
             */
            //Number Slider Combo Box:
            String info = findParameter(numberParamCB.getSelectedItem().toString()).getFilterInfo();
            int min = Integer.parseInt(info.split(",")[0]);
            int max = Integer.parseInt(info.split(",")[1]);
            filterMin.setValue(min);
            filterMax.setValue(max);
        } catch (Exception e) {
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
            dir = s.replace('/', File.separatorChar);
        } catch (Exception e) {
        }
        return dir;
    }

    private void openConfig(boolean showError) {
        int choice = 1;
        if (choice == 1) {
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
                        } else if (stringType.equals("boolean")) {
                            String[] config = {
                                line.split(":")[2],
                                "1",
                                line.split(":")[1]
                            };
                            params.add(new ParameterPanel(config));
                        } else if (stringType.equals("string")) {
                            String[] config = {
                                line.split(":")[2],
                                "2",
                                line.split(":")[1]
                            };
                            params.add(new ParameterPanel(config));
                        } else if (stringType.equals("slider")) {
                            String[] config = {
                                line.split(":")[2],
                                "3",
                                line.split(":")[1]
                            };
                            params.add(new ParameterPanel(config));
                        }
                    }
                    in.close();
                } catch (Exception e) {
                    if (showError) {
                        JOptionPane.showMessageDialog(rootPane, "Failed to read the parameters from the config file.\nThe file may be corrupt, invalid, or nonexistent.", "Error: " + e.toString(), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void saveConfig() {
        boolean save = true;
        if (save) {
            JFileChooser chooser = new JFileChooser(getDir());
            chooser.setDialogTitle("Save config");
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
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(rootPane, "Failed to save file " + chooser.getSelectedFile().getName() + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showTeamInfo(int alliance, int teamIndex) {
        try {
            int matchNumber = Integer.parseInt(matchTable.getModel().getValueAt(matchTable.getSelectedRow(), 0).toString());
            String[] data = findMatch(matchNumber).getAlliance(alliance).getTeam(teamIndex).getData();
            parseMatchParams(data);
            matchPreviewPanel.setToolTipText("Team " + findMatch(matchNumber).getAlliance(alliance).getTeam(teamIndex).getNumber());
            matchPreviewPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Team " + findMatch(matchNumber).getAlliance(alliance).getTeam(teamIndex).getNumber()));
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(rootPane, "Failed to fetch data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 
     * 
     * RANKING CODE
     * 
     * 
     */
    private boolean filter(Match match, int teamNumber) {
        boolean countThisMatch = true;
        String[] data = match.getTeam(teamNumber).getData();
        for (String s : data) {
            System.out.println(s);
        }
        for (ParameterPanel panel : params) {
            int type = panel.getRawType();
            String info = panel.getFilterInfo();
            switch (type) {
                case 0:
                case 3:
                    int min = Integer.parseInt(info.split(",")[0]);
                    int max = Integer.parseInt(info.split(",")[1]);
                    int current = Integer.parseInt(data[indexOf(data, panel.getTitle()) + 1]);
                    System.out.println("min = " + min + ", max = " + max + ", current = " + current);
                    if (current < min || current > max) {
                        countThisMatch = false;
                    }
                    break;
                case 1:
                    boolean b = Boolean.parseBoolean(data[indexOf(data, panel.getTitle()) + 1]);
                    if (info.toLowerCase().equals("true")) {
                        if (!b) {
                            countThisMatch = false;
                        }
                    } else if (info.toLowerCase().equals("false")) {
                        if (b) {
                            countThisMatch = false;
                        }
                    }
                    break;
                case 2:
                    String s = data[indexOf(data, panel.getTitle()) + 1];
                    if (!s.contains(info) && info.length() > 0) {
                        countThisMatch = false;
                    }
                    break;
            }
        }
        return countThisMatch;
    }

    private int getCumulativeScore(int teamNumber) {
        int score = 0;
        for (Match match : matches) {
            if (match.checkTeamExists(Alliance.ALLIANCE_BLUE, teamNumber)) {
                score += match.getScore(Alliance.ALLIANCE_BLUE);
            } else if (match.checkTeamExists(Alliance.ALLIANCE_RED, teamNumber)) {
                score += match.getScore(Alliance.ALLIANCE_RED);
            }
        }
        return score;
    }

    private int getFilteredCumulativeScore(int teamNumber) throws Exception {
        int score = 0;
        for (Match match : matches) {
            if (match.checkTeamExists(Alliance.ALLIANCE_BLUE, teamNumber)) {
                if (filter(match, teamNumber)) {
                    score += match.getScore(Alliance.ALLIANCE_BLUE);
                } else {
                    throw new Exception();
                }
            } else if (match.checkTeamExists(Alliance.ALLIANCE_RED, teamNumber)) {
                if (filter(match, teamNumber)) {
                    score += match.getScore(Alliance.ALLIANCE_RED);
                } else {
                    throw new Exception();
                }
            }
        }
        return score;
    }

    private int getHighestScore(int teamNumber) {
        int score = 0;
        for (Match match : matches) {
            if (match.checkTeamExists(Alliance.ALLIANCE_BLUE, teamNumber)) {
                score = Math.max(score, match.getScore(Alliance.ALLIANCE_BLUE));
            } else if (match.checkTeamExists(Alliance.ALLIANCE_RED, teamNumber)) {
                score = Math.max(score, match.getScore(Alliance.ALLIANCE_RED));
            }
        }
        return score;
    }

    private int getFilteredHighScore(int teamNumber) throws Exception {
        int score = 0;
        for (Match match : matches) {
            if (match.checkTeamExists(Alliance.ALLIANCE_BLUE, teamNumber)) {
                if (filter(match, teamNumber)) {
                    score = Math.max(score, match.getScore(Alliance.ALLIANCE_BLUE));
                } else {
                    throw new Exception();
                }
            } else if (match.checkTeamExists(Alliance.ALLIANCE_RED, teamNumber)) {
                if (filter(match, teamNumber)) {
                    score = Math.max(score, match.getScore(Alliance.ALLIANCE_RED));
                } else {
                    throw new Exception();
                }
            }
        }
        return score;
    }

    private int indexOf(String[] data, String lookingFor) {
        int index = -1;
        for (int i = 0; i < data.length; i++) {
            if (data[i].split(":")[data[i].split(":").length - 1].equals(lookingFor)) {
                return i;
            }
        }
        return index;
    }

    private int getLowestScore(int teamNumber) {
        int score = Integer.MAX_VALUE;
        for (Match match : matches) {
            if (match.checkTeamExists(Alliance.ALLIANCE_BLUE, teamNumber)) {
                score = Math.min(score, match.getScore(Alliance.ALLIANCE_BLUE));
            } else if (match.checkTeamExists(Alliance.ALLIANCE_RED, teamNumber)) {
                score = Math.min(score, match.getScore(Alliance.ALLIANCE_RED));
            }
        }
        return (score == Integer.MAX_VALUE) ? 0 : score;
    }

    private int getFilteredLowestScore(int teamNumber) throws Exception {
        int score = Integer.MAX_VALUE;
        for (Match match : matches) {
            if (match.checkTeamExists(Alliance.ALLIANCE_BLUE, teamNumber)) {
                if (filter(match, teamNumber)) {
                    score = Math.min(score, match.getScore(Alliance.ALLIANCE_BLUE));
                } else {
                    throw new Exception();
                }
            } else if (match.checkTeamExists(Alliance.ALLIANCE_RED, teamNumber)) {
                if (filter(match, teamNumber)) {
                    score = Math.min(score, match.getScore(Alliance.ALLIANCE_RED));
                } else {
                    throw new Exception();
                }
            }
        }
        return (score == Integer.MAX_VALUE) ? 0 : score;
    }

    private void updateAnalysisTable() {
        LinkedList<Integer> cumScores = new LinkedList<Integer>();
        LinkedList<Integer> highScores = new LinkedList<Integer>();
        LinkedList<Integer> lowScores = new LinkedList<Integer>();
        ((DefaultTableModel) analysisTable.getModel()).setRowCount(0);
        int count = 0;
        for (Team team : teams) {
            count++;
            int cumulative = getCumulativeScore(team.getNumber());
            try {
                cumulative = getFilteredCumulativeScore(team.getNumber());
            } catch (Exception e) {
                if (cumFilterCB.isSelected()) {
                    continue;
                }
            }
            int highest = getHighestScore(team.getNumber());
            try {
                highest = getFilteredHighScore(team.getNumber());
            } catch (Exception e) {
                if (highFilterCB.isSelected()) {
                    continue;
                }
            }
            int lowest = getLowestScore(team.getNumber());
            try {
                lowest = getFilteredLowestScore(team.getNumber());
            } catch (Exception e) {
                if (lowFilterCB.isSelected()) {
                    continue;
                }
            }
            Integer[] data = lowScores.toArray(new Integer[0]);
            if (cumFilterCB.isSelected()) {
                data = cumScores.toArray(new Integer[0]);
            } else if (highFilterCB.isSelected()) {
                data = highScores.toArray(new Integer[0]);
            }
            Arrays.sort(data, Collections.reverseOrder());
            if (reverseCB.isSelected()) {
                Arrays.sort(data);
            }
            int index = findIndex(data, lowest);
            if (cumFilterCB.isSelected()) {
                index = findIndex(data, cumulative);
            } else if (highFilterCB.isSelected()) {
                index = findIndex(data, highest);
            }
            cumScores.add(cumulative);
            lowScores.add(lowest);
            highScores.add(highest);
            ((DefaultTableModel) analysisTable.getModel()).insertRow(index,
                    new String[]{
                        "",
                        team.getNumber() + " - " + team.getName(),
                        cumulative + "",
                        highest + "",
                        lowest + "",});
        }
        for (int i = 0; i < analysisTable.getRowCount(); i++) {
            ((DefaultTableModel) analysisTable.getModel()).setValueAt(i + 1, i, 0);
        }
    }

    private int findIndex(Integer[] data, int i) {
        for (int x = 0; x < data.length; x++) {
            if (!reverseCB.isSelected()) {
                if (i > data[x]) {
                    return x;
                }
            } else {
                if (i < data[x]) {
                    return x;
                }
            }
        }
        return data.length;
    }

    public static void setOverridenData(String[] data) {
        overridenData = data;
    }

    private void serverOverride() {
        int choice = JOptionPane.showConfirmDialog(rootPane, "A remote client has sent data over the network.\nAttempt to import the data into the database?", "Client Data", JOptionPane.YES_NO_OPTION);
        if (choice == 0) {
            for (int i = 0; i < overridenData.length; i++) {
                String line = overridenData[i];
                if (line.startsWith("team=")) {
                    teamNumNUD.setValue(Integer.parseInt(line.split("=")[1]));
                } else if (line.startsWith("match=")) {
                    matchNUD.setValue(Integer.parseInt(line.split("=")[1]));
                } else if (line.startsWith("alliance=")) {
                    as.setAlliance(Integer.parseInt(line.split("=")[1]));
                } else {
                    while (line != null) {
                        if (line.startsWith("team=")) {
                            teamNumNUD.setValue(Integer.parseInt(line.split("=")[1]));
                        } else {
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
                            } else if (stringType.equals("boolean")) {
                                String[] config = {
                                    line.split(":")[2],
                                    "1",
                                    line.split(":")[1]
                                };
                                p = new ParameterPanel(config);
                            } else if (stringType.equals("string")) {
                                String[] config = {
                                    line.split(":")[2],
                                    "2",
                                    line.split(":")[1]
                                };
                                p = new ParameterPanel(config);
                            } else if (stringType.equals("slider")) {
                                String[] config = {
                                    line.split(":")[2],
                                    "3",
                                    line.split(":")[1]
                                };
                                p = new ParameterPanel(config);
                            }
                            String info = overridenData[++i];
                            p.setInfo(info);
                            previewParams.add(p);
                        }
                    }
                }
            }
        }
        updatePreviewParamLists();
        refresh(previewPanel, previewParams);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel24 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel34 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        analysisTable = new javax.swing.JTable();
        jPanel27 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        cumFilterCB = new javax.swing.JRadioButton();
        highFilterCB = new javax.swing.JRadioButton();
        lowFilterCB = new javax.swing.JRadioButton();
        reverseCB = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        paramFilterTabbedPane = new javax.swing.JTabbedPane();
        jPanel31 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        numberParamCB = new javax.swing.JComboBox();
        filterMin = new javax.swing.JSpinner();
        jLabel22 = new javax.swing.JLabel();
        filterMax = new javax.swing.JSpinner();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        tfParamCB = new javax.swing.JComboBox();
        filterTrue = new javax.swing.JRadioButton();
        filterFalse = new javax.swing.JRadioButton();
        filterTF = new javax.swing.JCheckBox();
        jPanel33 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        textParamCB = new javax.swing.JComboBox();
        filterTextTB = new javax.swing.JTextField();
        mustContainCB = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        totalTeamsLabel = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        totalMatchesLabel = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        totalParametersLabel = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        totalDataEntriesLabel = new javax.swing.JLabel();
        resetFiltersButton = new javax.swing.JButton();
        saveFilterButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel19 = new javax.swing.JPanel();
        importDataButton = new javax.swing.JButton();
        cancelDataButton = new javax.swing.JButton();
        addDataButton = new javax.swing.JButton();
        unknownCB = new javax.swing.JCheckBox();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        previewParamList = new javax.swing.JList();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        knownList = new javax.swing.JList();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        unknownList = new javax.swing.JList();
        jPanel20 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        dumbScrollPane = new javax.swing.JScrollPane();
        jPanel25 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        teamNumNUD = new javax.swing.JSpinner();
        jLabel34 = new javax.swing.JLabel();
        matchNUD = new javax.swing.JSpinner();
        jLabel35 = new javax.swing.JLabel();
        allianceSwitchPanel = new javax.swing.JPanel();
        previewScrollPane = new javax.swing.JScrollPane();
        previewPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        teamList = new javax.swing.JList();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        addTeamNUD = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        teamName = new javax.swing.JTextField();
        addTeamButton = new javax.swing.JButton();
        deleteTeamButton = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        matchTabbedPane = new javax.swing.JTabbedPane();
        jPanel23 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        redAllianceNUD1 = new javax.swing.JSpinner();
        redAllianceNUD2 = new javax.swing.JSpinner();
        redAllianceNUD3 = new javax.swing.JSpinner();
        jPanel4 = new javax.swing.JPanel();
        blueAllianceNUD1 = new javax.swing.JSpinner();
        blueAllianceNUD2 = new javax.swing.JSpinner();
        blueAllianceNUD3 = new javax.swing.JSpinner();
        jPanel7 = new javax.swing.JPanel();
        redAllianceScoreNUD = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        blueAllianceScoreNUD = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        matchNumberNUD = new javax.swing.JSpinner();
        saveMatchButton = new javax.swing.JButton();
        deleteMatchButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        matchTable = new javax.swing.JTable();
        viewTab = new javax.swing.JPanel();
        jSplitPane5 = new javax.swing.JSplitPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        matchViewPanel = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        redButton1 = new javax.swing.JButton();
        redButton2 = new javax.swing.JButton();
        redButton3 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        blueButton1 = new javax.swing.JButton();
        blueButton2 = new javax.swing.JButton();
        blueButton3 = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        matchPreviewPanel = new javax.swing.JPanel();
        viewSaveButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        paramList = new javax.swing.JList();
        jLabel11 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        paramTabPane = new javax.swing.JTabbedPane();
        numberTab = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        minNUD = new javax.swing.JSpinner();
        initialNUD = new javax.swing.JSpinner();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        maxNUD = new javax.swing.JSpinner();
        booleanTab = new javax.swing.JPanel();
        initiallyChecked = new javax.swing.JRadioButton();
        initiallyUnchecked = new javax.swing.JRadioButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        trueTB = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        falseTB = new javax.swing.JTextField();
        stringTab = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        sliderTab = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        minNUD1 = new javax.swing.JSpinner();
        initialNUD1 = new javax.swing.JSpinner();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        maxNUD1 = new javax.swing.JSpinner();
        paramTitle = new javax.swing.JTextField();
        addParamButton = new javax.swing.JButton();
        deleteParamButton = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        importConfigButton = new javax.swing.JButton();
        exportConfigButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("NRGScout Server");
        setName("jframe"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closing(evt);
            }
        });

        tabbedPane.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTabbedPane3.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        analysisTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Overall rank", "Team number", "Cumulative score", "Highest score", "Lowest score"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane11.setViewportView(analysisTable);

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Rankings", jPanel34);

        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder("Ranking Settings"));

        cumFilterCB.setSelected(true);
        cumFilterCB.setText("Rank based on cumulative score");
        cumFilterCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cumFilterCBActionPerformed(evt);
            }
        });

        highFilterCB.setText("Rank based on highest score");
        highFilterCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highFilterCBActionPerformed(evt);
            }
        });

        lowFilterCB.setText("Rank based on lowest score");
        lowFilterCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lowFilterCBActionPerformed(evt);
            }
        });

        reverseCB.setText("Reverse order");
        reverseCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reverseCBActionPerformed(evt);
            }
        });

        jLabel3.setText("Matches will ultimately be ranked by score:");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(cumFilterCB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lowFilterCB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(highFilterCB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(reverseCB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(5, 5, 5)
                .addComponent(cumFilterCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(highFilterCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lowFilterCB)
                .addGap(6, 6, 6)
                .addComponent(reverseCB))
        );

        paramFilterTabbedPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameter Filters"));

        jLabel26.setText("Parameter:");

        numberParamCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberParamCBActionPerformed(evt);
            }
        });

        filterMin.setModel(new javax.swing.SpinnerNumberModel());

        jLabel22.setText("Filter out a certain range of numbers:");

        filterMax.setModel(new javax.swing.SpinnerNumberModel());

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Min:");

        jLabel29.setText("Max:");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(numberParamCB, 0, 144, Short.MAX_VALUE))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel22))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel28)
                        .addGap(8, 8, 8)
                        .addComponent(filterMin, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filterMax, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(numberParamCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(filterMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        paramFilterTabbedPane.addTab("Number and Slider", jPanel31);

        jLabel30.setText("Parameter:");

        tfParamCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfParamCBActionPerformed(evt);
            }
        });

        filterTrue.setSelected(true);
        filterTrue.setText("Filter only True");
        filterTrue.setIconTextGap(8);
        filterTrue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterTrueActionPerformed(evt);
            }
        });

        filterFalse.setText("Filter only False");
        filterFalse.setIconTextGap(8);
        filterFalse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterFalseActionPerformed(evt);
            }
        });

        filterTF.setText("Filter out either True or False values");
        filterTF.setIconTextGap(8);

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filterTF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tfParamCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addComponent(filterTrue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filterFalse)))
                .addContainerGap())
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(tfParamCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filterTF)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterTrue)
                    .addComponent(filterFalse))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        paramFilterTabbedPane.addTab("True/False", jPanel32);

        jLabel31.setText("Parameter:");

        textParamCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textParamCBActionPerformed(evt);
            }
        });

        filterTextTB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterTextTBKeyReleased(evt);
            }
        });

        mustContainCB.setText("Must contain a certain string");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filterTextTB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textParamCB, 0, 144, Short.MAX_VALUE))
                    .addComponent(mustContainCB, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(textParamCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mustContainCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterTextTB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        paramFilterTabbedPane.addTab("Text", jPanel33);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Usage Statistics"));

        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Teams", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        totalTeamsLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalTeamsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalTeamsLabel.setText("0");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalTeamsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(totalTeamsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel37.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Matches", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        totalMatchesLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalMatchesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalMatchesLabel.setText("0");

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalMatchesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(totalMatchesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel38.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Parameters", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        totalParametersLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalParametersLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalParametersLabel.setText("0");

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalParametersLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(totalParametersLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel39.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data Entries", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        totalDataEntriesLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalDataEntriesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalDataEntriesLabel.setText("0");

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalDataEntriesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(totalDataEntriesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel37, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        resetFiltersButton.setText("Reset filters");
        resetFiltersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetFiltersButtonActionPerformed(evt);
            }
        });

        saveFilterButton.setText("Save filter");
        saveFilterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFilterButtonActionPerformed(evt);
            }
        });

        jButton2.setText("Refresh rankings");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshRankingActionPerfomed(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel27Layout.createSequentialGroup()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(saveFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(resetFiltersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(paramFilterTabbedPane))))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paramFilterTabbedPane, 0, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveFilterButton)
                    .addComponent(resetFiltersButton)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Control Panel", jPanel27);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3))
        );

        tabbedPane.addTab("Analysis", jPanel2);

        jPanel18.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        importDataButton.setText("Import Data");
        importDataButton.setToolTipText("Import local scouting data");
        importDataButton.setIconTextGap(14);
        importDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importDataButtonActionPerformed(evt);
            }
        });

        cancelDataButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nrgscoutserver/removeButton.png"))); // NOI18N
        cancelDataButton.setToolTipText("Cancel addendum of data");
        cancelDataButton.setEnabled(false);
        cancelDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelDataButtonActionPerformed(evt);
            }
        });

        addDataButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nrgscoutserver/addButton.png"))); // NOI18N
        addDataButton.setToolTipText("Add this data to the server");
        addDataButton.setEnabled(false);
        addDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDataButtonActionPerformed(evt);
            }
        });

        unknownCB.setSelected(true);
        unknownCB.setText("Add all unknown parameters");
        unknownCB.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jTabbedPane2.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        previewParamList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(previewParamList);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Parameters", jPanel9);

        knownList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane7.setViewportView(knownList);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Known", jPanel11);

        unknownList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane12.setViewportView(unknownList);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Unknown", jPanel12);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unknownCB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(importDataButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addDataButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelDataButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addDataButton, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(cancelDataButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(importDataButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(unknownCB)
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(jPanel19);

        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        dumbScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        dumbScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        dumbScrollPane.setMaximumSize(new java.awt.Dimension(32767, 98));
        dumbScrollPane.setMinimumSize(new java.awt.Dimension(0, 98));

        jPanel25.setMaximumSize(new java.awt.Dimension(32767, 92));
        jPanel25.setPreferredSize(new java.awt.Dimension(0, 0));

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Team Number:");

        teamNumNUD.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        teamNumNUD.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        teamNumNUD.setToolTipText("The team number of the team being watched (i.e 948)");
        teamNumNUD.setDoubleBuffered(true);
        teamNumNUD.setEditor(new javax.swing.JSpinner.NumberEditor(teamNumNUD, ""));

        jLabel34.setText("Match Number:");

        matchNUD.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        matchNUD.setToolTipText("The match number");

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Alliance:");

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

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(matchNUD, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(allianceSwitchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(teamNumNUD, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(teamNumNUD)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(allianceSwitchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel34)
                        .addComponent(matchNUD)
                        .addComponent(jLabel35)))
                .addGap(106, 106, 106))
        );

        dumbScrollPane.setViewportView(jPanel25);

        jSplitPane3.setTopComponent(dumbScrollPane);

        previewScrollPane.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        previewScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout previewPanelLayout = new javax.swing.GroupLayout(previewPanel);
        previewPanel.setLayout(previewPanelLayout);
        previewPanelLayout.setHorizontalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 386, Short.MAX_VALUE)
        );
        previewPanelLayout.setVerticalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 245, Short.MAX_VALUE)
        );

        previewScrollPane.setViewportView(previewPanel);

        jSplitPane3.setRightComponent(previewScrollPane);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane3)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(jPanel20);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Add Data", jPanel17);

        teamList.setBorder(javax.swing.BorderFactory.createTitledBorder("Team List"));
        teamList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        teamList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listSelect(evt);
            }
        });
        jScrollPane3.setViewportView(teamList);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Edit Team"));

        jLabel6.setText("Team #");

        addTeamNUD.setModel(new javax.swing.SpinnerNumberModel());

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Description:");

        addTeamButton.setText("Save");
        addTeamButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTeamButtonActionPerformed(evt);
            }
        });

        deleteTeamButton.setText("Delete");
        deleteTeamButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteTeamButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addTeamNUD, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teamName, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addTeamButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteTeamButton)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(addTeamNUD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(teamName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addTeamButton)
                    .addComponent(deleteTeamButton)
                    .addComponent(jLabel7))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedPane.addTab("Teams", jPanel3);

        matchTabbedPane.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Red Alliance", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), new java.awt.Color(255, 0, 0))); // NOI18N

        redAllianceNUD1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        redAllianceNUD2.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        redAllianceNUD3.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(redAllianceNUD2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(redAllianceNUD3, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(redAllianceNUD1, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(redAllianceNUD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(redAllianceNUD2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(redAllianceNUD3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Blue Alliance", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), new java.awt.Color(0, 0, 255))); // NOI18N

        blueAllianceNUD1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        blueAllianceNUD2.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        blueAllianceNUD3.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(blueAllianceNUD2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(blueAllianceNUD3, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(blueAllianceNUD1, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(blueAllianceNUD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blueAllianceNUD2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blueAllianceNUD3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Final Scores", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        redAllianceScoreNUD.setModel(new javax.swing.SpinnerNumberModel());

        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Red Alliance:");

        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Blue Alliance:");

        blueAllianceScoreNUD.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(redAllianceScoreNUD, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blueAllianceScoreNUD, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(redAllianceScoreNUD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(blueAllianceScoreNUD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Match Number:");

        matchNumberNUD.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        saveMatchButton.setText("Save");
        saveMatchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMatchButtonActionPerformed(evt);
            }
        });

        deleteMatchButton.setText("Delete");
        deleteMatchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMatchButtonActionPerformed(evt);
            }
        });

        matchTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Match Number", "Red Alliance", "Blue Alliance", "Red Score", "Blue Score"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        matchTable.setColumnSelectionAllowed(true);
        matchTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        matchTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tableMouseDown(evt);
            }
        });
        jScrollPane4.setViewportView(matchTable);
        matchTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(matchNumberNUD, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(saveMatchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteMatchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(matchNumberNUD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(saveMatchButton)
                            .addComponent(deleteMatchButton))))
                .addContainerGap())
        );

        matchTabbedPane.addTab("Manage", jPanel23);

        jScrollPane5.setToolTipText("");
        jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane5.setMinimumSize(new java.awt.Dimension(250, 100));

        matchViewPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Match"));

        jPanel14.setBackground(new java.awt.Color(255, 0, 0));
        jPanel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel14.setRequestFocusEnabled(false);

        redButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        redButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redButton1ActionPerformed(evt);
            }
        });

        redButton2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        redButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redButton2ActionPerformed(evt);
            }
        });

        redButton3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        redButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(redButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(redButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(redButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(redButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(redButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(redButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel21.setBackground(new java.awt.Color(0, 0, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        blueButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        blueButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blueButton1ActionPerformed(evt);
            }
        });

        blueButton2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        blueButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blueButton2ActionPerformed(evt);
            }
        });

        blueButton3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        blueButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blueButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(blueButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(blueButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(blueButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(blueButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blueButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blueButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout matchViewPanelLayout = new javax.swing.GroupLayout(matchViewPanel);
        matchViewPanel.setLayout(matchViewPanelLayout);
        matchViewPanelLayout.setHorizontalGroup(
            matchViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, matchViewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        matchViewPanelLayout.setVerticalGroup(
            matchViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(matchViewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(matchViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jScrollPane5.setViewportView(matchViewPanel);

        jSplitPane5.setLeftComponent(jScrollPane5);

        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        matchPreviewPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Team"));

        javax.swing.GroupLayout matchPreviewPanelLayout = new javax.swing.GroupLayout(matchPreviewPanel);
        matchPreviewPanel.setLayout(matchPreviewPanelLayout);
        matchPreviewPanelLayout.setHorizontalGroup(
            matchPreviewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 348, Short.MAX_VALUE)
        );
        matchPreviewPanelLayout.setVerticalGroup(
            matchPreviewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 283, Short.MAX_VALUE)
        );

        jScrollPane6.setViewportView(matchPreviewPanel);

        viewSaveButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        viewSaveButton.setText("Save Changes");
        viewSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(viewSaveButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(viewSaveButton))
        );

        jSplitPane5.setRightComponent(jPanel28);

        javax.swing.GroupLayout viewTabLayout = new javax.swing.GroupLayout(viewTab);
        viewTab.setLayout(viewTabLayout);
        viewTabLayout.setHorizontalGroup(
            viewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane5)
        );
        viewTabLayout.setVerticalGroup(
            viewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
        );

        matchTabbedPane.addTab("View", viewTab);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(matchTabbedPane)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(matchTabbedPane)
        );

        tabbedPane.addTab("Matches", jPanel22);

        paramList.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));
        paramList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        paramList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                paramListSelected(evt);
            }
        });
        jScrollPane10.setViewportView(paramList);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Title:");

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameter Type"));

        paramTabPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        paramTabPane.setToolTipText("");

        jLabel12.setText("Min:");

        jLabel13.setText("Initial:");

        jLabel14.setText("Max:");

        javax.swing.GroupLayout numberTabLayout = new javax.swing.GroupLayout(numberTab);
        numberTab.setLayout(numberTabLayout);
        numberTabLayout.setHorizontalGroup(
            numberTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(numberTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(numberTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(numberTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(maxNUD, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                    .addComponent(minNUD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(initialNUD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE))
                .addContainerGap())
        );
        numberTabLayout.setVerticalGroup(
            numberTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(numberTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(numberTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(minNUD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(numberTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(initialNUD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(numberTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(maxNUD))
                .addContainerGap())
        );

        paramTabPane.addTab("Number", numberTab);

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

        jLabel15.setText("Initial value:");

        jLabel16.setText("True text:");

        trueTB.setText("This Parameter is True");

        jLabel17.setText("False text:");

        falseTB.setText("This Parameter is False");

        javax.swing.GroupLayout booleanTabLayout = new javax.swing.GroupLayout(booleanTab);
        booleanTab.setLayout(booleanTabLayout);
        booleanTabLayout.setHorizontalGroup(
            booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(booleanTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(booleanTabLayout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(initiallyChecked)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(initiallyUnchecked)
                        .addGap(0, 51, Short.MAX_VALUE))
                    .addGroup(booleanTabLayout.createSequentialGroup()
                        .addGroup(booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(trueTB, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addComponent(falseTB, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))))
                .addContainerGap())
        );
        booleanTabLayout.setVerticalGroup(
            booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(booleanTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(initiallyChecked, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(initiallyUnchecked, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(trueTB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(booleanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(falseTB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        paramTabPane.addTab("True/False", booleanTab);

        jLabel18.setForeground(new java.awt.Color(153, 153, 153));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("No configuration required.");

        javax.swing.GroupLayout stringTabLayout = new javax.swing.GroupLayout(stringTab);
        stringTab.setLayout(stringTabLayout);
        stringTabLayout.setHorizontalGroup(
            stringTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stringTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                .addContainerGap())
        );
        stringTabLayout.setVerticalGroup(
            stringTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stringTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addContainerGap())
        );

        paramTabPane.addTab("Text", stringTab);

        jLabel19.setText("Min:");

        minNUD1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        initialNUD1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel20.setText("Initial:");

        jLabel21.setText("Max:");

        maxNUD1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(10), Integer.valueOf(0), null, Integer.valueOf(1)));

        javax.swing.GroupLayout sliderTabLayout = new javax.swing.GroupLayout(sliderTab);
        sliderTab.setLayout(sliderTabLayout);
        sliderTabLayout.setHorizontalGroup(
            sliderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sliderTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sliderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(jLabel19)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sliderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(minNUD1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                    .addComponent(initialNUD1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(maxNUD1, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE))
                .addContainerGap())
        );
        sliderTabLayout.setVerticalGroup(
            sliderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sliderTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sliderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(minNUD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(sliderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(initialNUD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(sliderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(maxNUD1)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        paramTabPane.addTab("Slider", sliderTab);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paramTabPane)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paramTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
        );

        paramTitle.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        paramTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paramTitleKeyReleased(evt);
            }
        });

        addParamButton.setText("Save");
        addParamButton.setEnabled(false);
        addParamButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addParamButtonActionPerformed(evt);
            }
        });

        deleteParamButton.setText("Delete");
        deleteParamButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteParamButtonActionPerformed(evt);
            }
        });

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Additonal Options"));

        importConfigButton.setText("Import configuration from file");
        importConfigButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importConfigButtonActionPerformed(evt);
            }
        });

        exportConfigButton.setText("Export current configuration");
        exportConfigButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportConfigButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(importConfigButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(exportConfigButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(importConfigButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportConfigButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(paramTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addParamButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteParamButton))
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(paramTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(deleteParamButton)
                            .addComponent(addParamButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        tabbedPane.addTab("Parameters", jPanel5);

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("© Developed by Mohammad Adib 2012");

        jMenu1.setText("File");

        jMenu3.setText("Export");

        jMenuItem1.setText("Team List");
        jMenu3.add(jMenuItem1);

        jMenuItem2.setText("Match List");
        jMenu3.add(jMenuItem2);

        jMenu1.add(jMenu3);

        jMenu4.setText("Import");

        jMenuItem3.setText("Team List");
        jMenu4.add(jMenuItem3);

        jMenuItem4.setText("Match List");
        jMenu4.add(jMenuItem4);

        jMenu1.add(jMenu4);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");

        jMenuItem5.setText("Help");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabbedPane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exportConfigButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportConfigButtonActionPerformed
        saveConfig();
        }//GEN-LAST:event_exportConfigButtonActionPerformed

    private void importConfigButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importConfigButtonActionPerformed

        openConfig(true);
        updateParameterList();
        }//GEN-LAST:event_importConfigButtonActionPerformed

    private void deleteParamButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteParamButtonActionPerformed
        params.remove(paramList.getSelectedIndex());
        updateParameterList();
        }//GEN-LAST:event_deleteParamButtonActionPerformed

    private void addParamButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addParamButtonActionPerformed
        int type = paramTabPane.getSelectedIndex();
        int[] numParams = {0, 0, 0}, sliderParams = {0, 0, 0};
        String name = paramTitle.getText();
        String info = "";
        switch (type) {
            case NUMBER:
                numParams = new int[]{Integer.parseInt(minNUD.getValue().toString()), Integer.parseInt(initialNUD.getValue().toString()), Integer.parseInt(maxNUD.getValue().toString())};
                if (numParams[1] >= numParams[0] && numParams[1] <= numParams[2]) {
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Initial, Minimum and Maximum values are incorrect!\nPlease try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                info = minNUD.getValue().toString() + "," + initialNUD.getValue().toString() + "," + maxNUD.getValue().toString();
                break;
            case BOOLEAN:
                info = initiallyChecked.isSelected() + "," + trueTB.getText() + "," + falseTB.getText();
                break;
            case STRING:
                info = "";
                break;
            case SLIDER:
                sliderParams = new int[]{Integer.parseInt(minNUD1.getValue().toString()), Integer.parseInt(initialNUD1.getValue().toString()), Integer.parseInt(maxNUD1.getValue().toString())};
                if (sliderParams[1] >= sliderParams[0] && sliderParams[0] < sliderParams[2] && sliderParams[1] <= sliderParams[2]) {
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Initial, Minimum and Maximum values are incorrect!\nPlease try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                info = minNUD1.getValue().toString() + "," + initialNUD1.getValue().toString() + "," + maxNUD1.getValue().toString();
                break;
        }
        String[] args = new String[]{name, type + "", info};
        if (!paramExists(name, params) && paramList.getSelectedIndex() == -1) {
            params.add(new ParameterPanel(args));
            updateParameterList();
        } else {
            try {
                if (paramList.getSelectedIndex() > -1) {
                    if (paramExists(name, params) && !params.get(paramList.getSelectedIndex()).getTitle().equals(name)) {
                        throw new Exception();
                    }
                    params.set(paramList.getSelectedIndex(), new ParameterPanel(args));
                    updateParameterList();
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, "Parameter \"" + name + "\" already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        updatePreviewParamLists();
        }//GEN-LAST:event_addParamButtonActionPerformed

    private void paramTitleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paramTitleKeyReleased
        addParamButton.setEnabled(paramTitle.getText().length() > 0);
        }//GEN-LAST:event_paramTitleKeyReleased

    private void initiallyUncheckedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initiallyUncheckedActionPerformed
        this.initiallyChecked.setSelected(!this.initiallyUnchecked.isSelected());
        }//GEN-LAST:event_initiallyUncheckedActionPerformed

    private void initiallyCheckedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initiallyCheckedActionPerformed
        this.initiallyUnchecked.setSelected(!this.initiallyChecked.isSelected());
        }//GEN-LAST:event_initiallyCheckedActionPerformed

    private void paramListSelected(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_paramListSelected

        try {
            ParameterPanel param = params.get(paramList.getSelectedIndex());
            paramTabPane.setSelectedIndex(param.getRawType());
            paramTitle.setText(param.getTitle());
            String info = param.getInfo().replaceAll(":", "");
            switch (param.getRawType()) {
                case NUMBER:
                    minNUD.setValue(Integer.parseInt(info.split(",")[0]));
                    initialNUD.setValue(Integer.parseInt(info.split(",")[1]));
                    maxNUD.setValue(Integer.parseInt(info.split(",")[2]));
                    break;
                case BOOLEAN:
                    initiallyChecked.setSelected(Boolean.parseBoolean(info.split(",")[0]));
                    initiallyUnchecked.setSelected(!Boolean.parseBoolean(info.split(",")[0]));
                    trueTB.setText(info.split(",")[1]);
                    falseTB.setText(info.split(",")[2]);
                    break;
                case STRING:
                    break;
                case SLIDER:
                    minNUD1.setValue(Integer.parseInt(info.split(",")[0]));
                    initialNUD1.setValue(Integer.parseInt(info.split(",")[1]));
                    maxNUD1.setValue(Integer.parseInt(info.split(",")[2]));
                    break;
            }
        } catch (Exception e) {
        }
        }//GEN-LAST:event_paramListSelected

    private void deleteTeamButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteTeamButtonActionPerformed

        int num = Integer.parseInt(addTeamNUD.getValue().toString());
        boolean foundIt = false;
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getNumber() == num) {
                teams.remove(i);
                foundIt = true;
                break;
            }
        }
        if (!foundIt) {
            JOptionPane.showMessageDialog(rootPane, "An entry for team " + num + " was not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        updateTeamList();
        }//GEN-LAST:event_deleteTeamButtonActionPerformed

    private void addTeamButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTeamButtonActionPerformed
        int number = Integer.parseInt(addTeamNUD.getValue().toString());
        String name = teamName.getText();
        if (name.length() < 1) {
            name = "No Description";
        }
        boolean exists = false;
        for (Team team : teams) {
            if (team.getNumber() == number) {
                exists = true;
            }
        }
        if (!exists) {
            teams.add(new Team(number, name));
            updateTeamList();
        } else {
            try {
                if (teams.get(teamList.getSelectedIndex()).getNumber() == number) {
                    teams.set(teamList.getSelectedIndex(), new Team(number, name));
                    updateTeamList();
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, "Team " + number + " already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        teamName.setText("");
        addTeamNUD.setValue(0);
        }//GEN-LAST:event_addTeamButtonActionPerformed

    private void listSelect(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listSelect

        try {
            addTeamNUD.setValue(teams.get(teamList.getSelectedIndex()).getNumber());
            teamName.setText(teams.get(teamList.getSelectedIndex()).getName());
        } catch (Exception e) {
        }
        }//GEN-LAST:event_listSelect

    private void importDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importDataButtonActionPerformed
        this.openScoutFile(true);
        if (previewParams.size() > 0) {
            addDataButton.setEnabled(true);
            cancelDataButton.setEnabled(true);
            importDataButton.setEnabled(false);
        }
        }//GEN-LAST:event_importDataButtonActionPerformed

    private void cancelDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelDataButtonActionPerformed
        previewParams.clear();
        refreshViewTab();
        teamNumNUD.setValue(0);
        matchNUD.setValue(1);
        as.setAlliance(Alliance.ALLIANCE_RED);
        refresh(previewPanel, previewParams);
        addDataButton.setEnabled(false);
        cancelDataButton.setEnabled(false);
        importDataButton.setEnabled(true);
    }//GEN-LAST:event_cancelDataButtonActionPerformed

    private void addDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDataButtonActionPerformed
        int matchNumber = Integer.parseInt(this.matchNUD.getValue().toString());
        int teamNumber = Integer.parseInt(teamNumNUD.getValue().toString());
        if (findMatch(matchNumber) == null) {
            int i = JOptionPane.showConfirmDialog(rootPane, "Match " + matchNumber + " has not been added yet.\nWould you like to add it now?", "Error", JOptionPane.YES_NO_OPTION);
            if (i == 0) {
                this.matchNumberNUD.setValue(matchNumber);
                if (as.getAlliance() == Alliance.ALLIANCE_RED) {
                    this.redAllianceNUD1.setValue(teamNumber);
                } else {
                    this.blueAllianceNUD1.setValue(teamNumber);
                }
                this.tabbedPane.setSelectedIndex(3);
            }
        } else {
            if (findMatch(matchNumber).checkTeamExists(as.getAlliance(), teamNumber)) {
                boolean approved = true;
                if (findMatch(matchNumber).getTeam(as.getAlliance(), teamNumber).getData() != null) {
                    int i = JOptionPane.showConfirmDialog(rootPane, "Data for team " + teamNumber + " in match " + matchNumber + " already exists.\nWould you like to overwrite the data?", "Warning", JOptionPane.YES_NO_OPTION);
                    if (i == 0) {
                        approved = true;
                    } else {
                        approved = false;
                    }
                }
                if (approved) {
                    if (unknownCB.isSelected()) {
                        LinkedList<ParameterPanel> parameters = getUnknownParams();
                        for (ParameterPanel param : parameters) {
                            params.add(param);
                            updateParameterList();
                        }
                    }
                    boolean exists = false;
                    for (Team team : teams) {
                        if (teamNumber == team.getNumber()) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        String teamDesc = JOptionPane.showInputDialog(rootPane, "Team " + teamNumber + " has been added. Give it a description:", "Add team", JOptionPane.PLAIN_MESSAGE);
                        teams.add(new Team(teamNumber, (teamDesc != null) ? teamDesc : "No Description"));
                        updateTeamList();
                    }
                    //Set data
                    teamData.clear();
                    for (ParameterPanel panel : previewParams) {
                        teamData.add(panel.getType() + panel.getInfo() + panel.getTitle());
                        teamData.add(panel.getCollectedData());
                    }
                    findMatch(matchNumber).getTeam(as.getAlliance(), teamNumber).setData(teamData.toArray(new String[teamData.size()]));
                    totalDataEntriesLabel.setText((Integer.parseInt(totalDataEntriesLabel.getText()) + 1) + "");
                    cancelDataButtonActionPerformed(evt);
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Team " + teamNumber + " is not participating in match " + matchNumber + " on the " + (as.getAlliance() == Alliance.ALLIANCE_RED ? "red" : "blue") + " alliance.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_addDataButtonActionPerformed

    private void saveMatchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMatchButtonActionPerformed
        try {
            int matchNumber, redTeam1, redTeam2, redTeam3, blueTeam1, blueTeam2, blueTeam3, redScore, blueScore;
            matchNumber = Integer.parseInt(matchNumberNUD.getValue().toString());
            redTeam1 = Integer.parseInt(redAllianceNUD1.getValue().toString());
            redTeam2 = Integer.parseInt(redAllianceNUD2.getValue().toString());
            redTeam3 = Integer.parseInt(redAllianceNUD3.getValue().toString());
            blueTeam1 = Integer.parseInt(blueAllianceNUD1.getValue().toString());
            blueTeam2 = Integer.parseInt(blueAllianceNUD2.getValue().toString());
            blueTeam3 = Integer.parseInt(blueAllianceNUD3.getValue().toString());
            redScore = Integer.parseInt(redAllianceScoreNUD.getValue().toString());
            blueScore = Integer.parseInt(blueAllianceScoreNUD.getValue().toString());
            boolean updating = false, updatingTeams = false;
            for (Match match : matches) {
                if (match.getNumber() == matchNumber && matchTable.getSelectedRow() != -1) {
                    if (matchTable.getModel().getValueAt(matchTable.getSelectedRow(), 0).toString().equals("" + match.getNumber())) {
                        updating = true;
                        if (findMatch(matchNumber).getTeamList()[0] != redTeam1
                                || findMatch(matchNumber).getTeamList()[1] != redTeam2
                                || findMatch(matchNumber).getTeamList()[2] != redTeam3
                                || findMatch(matchNumber).getTeamList()[3] != blueTeam1
                                || findMatch(matchNumber).getTeamList()[4] != blueTeam2
                                || findMatch(matchNumber).getTeamList()[5] != blueTeam3) {
                            int choice = JOptionPane.showConfirmDialog(rootPane, "Updating match information will delete all\ncollected data for that match. Continue?", "Warning", JOptionPane.YES_NO_OPTION);
                            if (choice == 0) {
                                updating = true;
                                updatingTeams = true;
                            } else {
                                updating = false;
                            }
                        }
                    } else {
                        throw new IllegalArgumentException("Match " + matchNumber + " already exists.");
                    }
                    break;
                } else if (match.getNumber() == matchNumber) {
                    throw new IllegalArgumentException("Match " + matchNumber + " already exists.");
                }
            }
            //Add teams to alliances
            Team red1, red2, red3, blue1, blue2, blue3;
            //RED ALLIANCE
            if (findTeam(redTeam1) == null) {
                String teamDesc = JOptionPane.showInputDialog(rootPane, "Team " + redTeam1 + " has been added. Give it a description:", "Add team", JOptionPane.PLAIN_MESSAGE);
                red1 = new Team(redTeam1, (teamDesc != null && teamDesc.length() > 0) ? teamDesc : "No Description");
                teams.add(new Team(red1.getNumber(), red1.getName()));
                updateTeamList();
            }
            if (findTeam(redTeam2) == null) {
                String teamDesc = JOptionPane.showInputDialog(rootPane, "Team " + redTeam2 + " has been added. Give it a description:", "Add team", JOptionPane.PLAIN_MESSAGE);
                red2 = new Team(redTeam2, (teamDesc != null && teamDesc.length() > 0) ? teamDesc : "No Description");
                teams.add(new Team(red2.getNumber(), red2.getName()));
                updateTeamList();
            }
            if (findTeam(redTeam3) == null) {
                String teamDesc = JOptionPane.showInputDialog(rootPane, "Team " + redTeam3 + " has been added. Give it a description:", "Add team", JOptionPane.PLAIN_MESSAGE);
                red3 = new Team(redTeam3, (teamDesc != null && teamDesc.length() > 0) ? teamDesc : "No Description");
                teams.add(new Team(red3.getNumber(), red3.getName()));
                updateTeamList();
            }
            //BLUE ALLIANCE
            if (findTeam(blueTeam1) == null) {
                String teamDesc = JOptionPane.showInputDialog(rootPane, "Team " + blueTeam1 + " has been added. Give it a description:", "Add team", JOptionPane.PLAIN_MESSAGE);
                blue1 = new Team(blueTeam1, (teamDesc != null && teamDesc.length() > 0) ? teamDesc : "No Description");
                teams.add(new Team(blue1.getNumber(), blue1.getName()));
                updateTeamList();
            }
            if (findTeam(blueTeam2) == null) {
                String teamDesc = JOptionPane.showInputDialog(rootPane, "Team " + blueTeam2 + " has been added. Give it a description:", "Add team", JOptionPane.PLAIN_MESSAGE);
                blue2 = new Team(blueTeam2, (teamDesc != null && teamDesc.length() > 0) ? teamDesc : "No Description");
                teams.add(new Team(blue2.getNumber(), blue2.getName()));
                updateTeamList();
            }
            if (findTeam(blueTeam3) == null) {
                String teamDesc = JOptionPane.showInputDialog(rootPane, "Team " + blueTeam3 + " has been added. Give it a description:", "Add team", JOptionPane.PLAIN_MESSAGE);
                blue3 = new Team(blueTeam3, (teamDesc != null && teamDesc.length() > 0) ? teamDesc : "No Description");
                teams.add(new Team(blue3.getNumber(), blue3.getName()));
                updateTeamList();
            }
            red1 = new Team(findTeam(redTeam1).getNumber(), findTeam(redTeam1).getName());
            red2 = new Team(findTeam(redTeam2).getNumber(), findTeam(redTeam2).getName());
            red3 = new Team(findTeam(redTeam3).getNumber(), findTeam(redTeam3).getName());
            blue1 = new Team(findTeam(blueTeam1).getNumber(), findTeam(blueTeam1).getName());
            blue2 = new Team(findTeam(blueTeam2).getNumber(), findTeam(blueTeam2).getName());
            blue3 = new Team(findTeam(blueTeam3).getNumber(), findTeam(blueTeam3).getName());
            if (redTeam1 == blueTeam1 || redTeam1 == blueTeam2 || redTeam1 == blueTeam3
                    || redTeam2 == blueTeam1 || redTeam2 == blueTeam2 || redTeam2 == blueTeam3
                    || redTeam3 == blueTeam1 || redTeam3 == blueTeam2 || redTeam3 == blueTeam3
                    || redTeam1 == redTeam2 || redTeam1 == redTeam3 || redTeam2 == redTeam3) {
                throw new IllegalArgumentException("Cannot use the same team more than once in the same match.");
            }
            if (blueTeam1 == redTeam1 || blueTeam1 == redTeam2 || blueTeam1 == redTeam3
                    || blueTeam2 == redTeam1 || blueTeam2 == redTeam2 || blueTeam2 == redTeam3
                    || blueTeam3 == redTeam1 || blueTeam3 == redTeam2 || blueTeam3 == redTeam3
                    || blueTeam1 == blueTeam2 || blueTeam1 == blueTeam3 || blueTeam2 == blueTeam3) {
                throw new IllegalArgumentException("Cannot use the same team more than once in the same match.");
            }

            Alliance redAlliance = new Alliance(red1, red2, red3, Alliance.ALLIANCE_RED);

            Alliance blueAlliance = new Alliance(blue1, blue2, blue3, Alliance.ALLIANCE_BLUE);
            //Add/Update a match
            Match match = new Match(matchNumber, redAlliance, blueAlliance, redScore, blueScore);
            if (!updating) {
                matches.add(match);
            } else {
                if (!updatingTeams) {
                    for (int i = 0; i < matches.size(); i++) {
                        if (matches.get(i).getNumber() == matchNumber) {
                            matches.get(i).setBlueScore(blueScore);
                            matches.get(i).setRedScore(redScore);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < matches.size(); i++) {
                        if (matches.get(i).getNumber() == matchNumber) {
                            matches.set(i, match);
                            break;
                        }
                    }
                }
            }
            updateMatchTable();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_saveMatchButtonActionPerformed

    private void tableMouseDown(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseDown
        refreshViewTab();
    }//GEN-LAST:event_tableMouseDown

    private void redButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redButton1ActionPerformed
        showTeamInfo(Alliance.ALLIANCE_RED, 0);
    }//GEN-LAST:event_redButton1ActionPerformed

    private void redButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redButton2ActionPerformed
        showTeamInfo(Alliance.ALLIANCE_RED, 1);
    }//GEN-LAST:event_redButton2ActionPerformed

    private void redButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redButton3ActionPerformed
        showTeamInfo(Alliance.ALLIANCE_RED, 2);
    }//GEN-LAST:event_redButton3ActionPerformed

    private void blueButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blueButton1ActionPerformed
        showTeamInfo(Alliance.ALLIANCE_BLUE, 0);
    }//GEN-LAST:event_blueButton1ActionPerformed

    private void blueButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blueButton2ActionPerformed
        showTeamInfo(Alliance.ALLIANCE_BLUE, 1);
    }//GEN-LAST:event_blueButton2ActionPerformed

    private void blueButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blueButton3ActionPerformed
        showTeamInfo(Alliance.ALLIANCE_BLUE, 2);
    }//GEN-LAST:event_blueButton3ActionPerformed

    private void viewSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSaveButtonActionPerformed
        int teamNumber = Integer.parseInt(matchPreviewPanel.getToolTipText().replaceAll("Team", "").trim());
        int matchNumber = Integer.parseInt(matchTable.getModel().getValueAt(matchTable.getSelectedRow(), 0).toString());
        LinkedList<String> data = new LinkedList<String>();
        for (ParameterPanel panel : matchParams) {
            data.add(panel.getType() + panel.getInfo() + panel.getTitle());
            data.add(panel.getCollectedData());
        }
        findMatch(matchNumber).getTeam(teamNumber).setData(data.toArray(new String[data.size()]));
    }//GEN-LAST:event_viewSaveButtonActionPerformed

    private void deleteMatchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMatchButtonActionPerformed
        matches.remove(findMatch(Integer.parseInt(matchTable.getModel().getValueAt(matchTable.getSelectedRow(), 0).toString())));
        updateMatchTable();
    }//GEN-LAST:event_deleteMatchButtonActionPerformed

    private void numberParamCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numberParamCBActionPerformed
        try {
            String info = findParameter(numberParamCB.getSelectedItem().toString()).getFilterInfo();
            int min = Integer.parseInt(info.split(",")[0]);
            int max = Integer.parseInt(info.split(",")[1]);
            filterMin.setValue(min);
            filterMax.setValue(max);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_numberParamCBActionPerformed

    private void tfParamCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfParamCBActionPerformed
        try {
            String info = findParameter(tfParamCB.getSelectedItem().toString()).getFilterInfo().toLowerCase();
            System.out.println("read filter info: " + info);
            if (info.equals("none")) {
                filterTF.setSelected(false);
                filterTrue.setSelected(true);
                filterFalse.setSelected(false);
            }
            if (info.equals("true")) {
                filterTF.setSelected(true);
                filterTrue.setSelected(true);
                filterFalse.setSelected(false);
            }
            if (info.equals("false")) {
                filterTF.setSelected(true);
                filterTrue.setSelected(false);
                filterFalse.setSelected(true);
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_tfParamCBActionPerformed

    private void saveFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFilterButtonActionPerformed
        try {
            int tab = paramFilterTabbedPane.getSelectedIndex();
            ParameterPanel panel;
            switch (tab) {
                case 0:
                    //Sentinel
                    int min = Integer.parseInt(filterMin.getValue().toString());
                    int max = Integer.parseInt(filterMax.getValue().toString());
                    if (min >= max) {
                        JOptionPane.showMessageDialog(rootPane, "Invalid minimum and maximum values.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        //Number/Slider filter
                        panel = findParameter(numberParamCB.getSelectedItem().toString());
                        panel.setFilterInfo(filterMin.getValue() + "," + filterMax.getValue());
                    }
                    break;
                case 1:
                    panel = findParameter(tfParamCB.getSelectedItem().toString());
                    panel.setFilterInfo(!filterTF.isSelected() ? "none" : filterTrue.isSelected() + "");
                    break;
                case 2:
                    //Text filter
                    panel = findParameter(textParamCB.getSelectedItem().toString());
                    if (mustContainCB.isSelected()) {
                        panel.setFilterInfo(filterTextTB.getText());
                    } else {
                        panel.setFilterInfo("");
                    }
                    break;
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_saveFilterButtonActionPerformed

    private void cumFilterCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cumFilterCBActionPerformed
        cumFilterCB.setSelected(true);
        highFilterCB.setSelected(false);
        lowFilterCB.setSelected(false);
        updateAnalysisTable();
    }//GEN-LAST:event_cumFilterCBActionPerformed

    private void highFilterCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highFilterCBActionPerformed
        highFilterCB.setSelected(true);
        cumFilterCB.setSelected(false);
        lowFilterCB.setSelected(false);
        updateAnalysisTable();
    }//GEN-LAST:event_highFilterCBActionPerformed

    private void lowFilterCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lowFilterCBActionPerformed
        lowFilterCB.setSelected(true);
        cumFilterCB.setSelected(false);
        highFilterCB.setSelected(false);
        updateAnalysisTable();
    }//GEN-LAST:event_lowFilterCBActionPerformed

    private void reverseCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reverseCBActionPerformed
        updateAnalysisTable();
    }//GEN-LAST:event_reverseCBActionPerformed

    private void textParamCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textParamCBActionPerformed
        try {
            String info = findParameter(textParamCB.getSelectedItem().toString()).getFilterInfo();
            if (info.length() > 0) {
                mustContainCB.setSelected(true);
                filterTextTB.setText(info);
            } else {
                mustContainCB.setSelected(false);
                filterTextTB.setText("");
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_textParamCBActionPerformed

    private void refreshRankingActionPerfomed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshRankingActionPerfomed
        updateAnalysisTable();
    }//GEN-LAST:event_refreshRankingActionPerfomed

    private void filterTrueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterTrueActionPerformed
        filterFalse.setSelected(!filterTrue.isSelected());
        filterTF.setSelected(true);
    }//GEN-LAST:event_filterTrueActionPerformed

    private void filterFalseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterFalseActionPerformed
        filterTrue.setSelected(!filterFalse.isSelected());
        filterTF.setSelected(true);
    }//GEN-LAST:event_filterFalseActionPerformed

    private void filterTextTBKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filterTextTBKeyReleased
        mustContainCB.setSelected(filterTextTB.getText().length() > 0);
    }//GEN-LAST:event_filterTextTBKeyReleased

    private void resetFiltersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetFiltersButtonActionPerformed
        for (ParameterPanel panel : params) {
            panel.resetFilter();
        }
        textParamCBActionPerformed(evt);
        tfParamCBActionPerformed(evt);
        numberParamCBActionPerformed(evt);
    }//GEN-LAST:event_resetFiltersButtonActionPerformed

    private void closing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closing
        int i = JOptionPane.showConfirmDialog(rootPane, "Closing the program will result in the loss\nof all compiled scouting data. Continue?", "Exit", JOptionPane.YES_NO_OPTION);
        System.out.println("NRG Out");
        if (i == 0) {
            System.exit(0);
        }
    }//GEN-LAST:event_closing

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDataButton;
    private javax.swing.JButton addParamButton;
    private javax.swing.JButton addTeamButton;
    private javax.swing.JSpinner addTeamNUD;
    private javax.swing.JPanel allianceSwitchPanel;
    private javax.swing.JTable analysisTable;
    private javax.swing.JSpinner blueAllianceNUD1;
    private javax.swing.JSpinner blueAllianceNUD2;
    private javax.swing.JSpinner blueAllianceNUD3;
    private javax.swing.JSpinner blueAllianceScoreNUD;
    private javax.swing.JButton blueButton1;
    private javax.swing.JButton blueButton2;
    private javax.swing.JButton blueButton3;
    private javax.swing.JPanel booleanTab;
    private javax.swing.JButton cancelDataButton;
    private javax.swing.JRadioButton cumFilterCB;
    private javax.swing.JButton deleteMatchButton;
    private javax.swing.JButton deleteParamButton;
    private javax.swing.JButton deleteTeamButton;
    private javax.swing.JScrollPane dumbScrollPane;
    private javax.swing.JButton exportConfigButton;
    private javax.swing.JTextField falseTB;
    private javax.swing.JRadioButton filterFalse;
    private javax.swing.JSpinner filterMax;
    private javax.swing.JSpinner filterMin;
    private javax.swing.JCheckBox filterTF;
    private javax.swing.JTextField filterTextTB;
    private javax.swing.JRadioButton filterTrue;
    private javax.swing.JRadioButton highFilterCB;
    private javax.swing.JButton importConfigButton;
    private javax.swing.JButton importDataButton;
    private javax.swing.JSpinner initialNUD;
    private javax.swing.JSpinner initialNUD1;
    private javax.swing.JRadioButton initiallyChecked;
    private javax.swing.JRadioButton initiallyUnchecked;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JList knownList;
    private javax.swing.JRadioButton lowFilterCB;
    private javax.swing.JSpinner matchNUD;
    private javax.swing.JSpinner matchNumberNUD;
    private javax.swing.JPanel matchPreviewPanel;
    private javax.swing.JTabbedPane matchTabbedPane;
    private javax.swing.JTable matchTable;
    private javax.swing.JPanel matchViewPanel;
    private javax.swing.JSpinner maxNUD;
    private javax.swing.JSpinner maxNUD1;
    private javax.swing.JSpinner minNUD;
    private javax.swing.JSpinner minNUD1;
    private javax.swing.JCheckBox mustContainCB;
    private javax.swing.JComboBox numberParamCB;
    private javax.swing.JPanel numberTab;
    private javax.swing.JTabbedPane paramFilterTabbedPane;
    private javax.swing.JList paramList;
    private javax.swing.JTabbedPane paramTabPane;
    private javax.swing.JTextField paramTitle;
    private javax.swing.JPanel previewPanel;
    private javax.swing.JList previewParamList;
    private javax.swing.JScrollPane previewScrollPane;
    private javax.swing.JSpinner redAllianceNUD1;
    private javax.swing.JSpinner redAllianceNUD2;
    private javax.swing.JSpinner redAllianceNUD3;
    private javax.swing.JSpinner redAllianceScoreNUD;
    private javax.swing.JButton redButton1;
    private javax.swing.JButton redButton2;
    private javax.swing.JButton redButton3;
    private javax.swing.JButton resetFiltersButton;
    private javax.swing.JCheckBox reverseCB;
    private javax.swing.JButton saveFilterButton;
    private javax.swing.JButton saveMatchButton;
    private javax.swing.JPanel sliderTab;
    private javax.swing.JPanel stringTab;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JList teamList;
    private javax.swing.JTextField teamName;
    private javax.swing.JSpinner teamNumNUD;
    private javax.swing.JComboBox textParamCB;
    private javax.swing.JComboBox tfParamCB;
    private javax.swing.JLabel totalDataEntriesLabel;
    private javax.swing.JLabel totalMatchesLabel;
    private javax.swing.JLabel totalParametersLabel;
    private javax.swing.JLabel totalTeamsLabel;
    private javax.swing.JTextField trueTB;
    private javax.swing.JCheckBox unknownCB;
    private javax.swing.JList unknownList;
    private javax.swing.JButton viewSaveButton;
    private javax.swing.JPanel viewTab;
    // End of variables declaration//GEN-END:variables

    private void setIPAddress() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            LinkedList<String> ips = new LinkedList<String>();
            InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getHostName());
            if (allMyIps != null && allMyIps.length > 1) {
                for (int i = 0; i < allMyIps.length; i++) {
                    if (!allMyIps[i].toString().contains(":")) {
                        ips.add(allMyIps[i].toString().substring(allMyIps[i].toString().indexOf("/") + 1));
                        //ipCB.setModel(new javax.swing.DefaultComboBoxModel(ips.toArray()));
                    }
                }
            }
            ips.add("Listen on all");
            //ipCB.setModel(new javax.swing.DefaultComboBoxModel(ips.toArray()));
        } catch (Exception e) {
        }
    }
}
