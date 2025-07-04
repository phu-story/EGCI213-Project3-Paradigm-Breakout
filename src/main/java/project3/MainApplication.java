package project3;
/*
    Made with ♥ by, 

    Veerapat Leepiboonsawat 6580969
    Yoswaris Lawpaiboon,    6681170
    Pasin Piyavej           6681187
    Nathan Tanabotiboon     6681224 
    Praepilai Phetsamsri    6681374

*/

/*
    Requirement:
    • JTextField, JPasswordField, or JTextArea 
    • JCheckBox or JRadioButton : at least 5 items 
    • JComboBox : at least 5 items 
    • JList : at least 5 items 
    • JButton : one frame/dialog must have a button that opens another frame/dialog 
    • Names and IDs of everyone in your group, as any component
*/

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import java.awt.event.*;

public class MainApplication extends JFrame{
    public static void main(String[] args) {
        new MainApplication();
    }
    
    private JPanel contentPane;
    private MainApplication currentFrame;
    private static final String PATH = "src/main/java/project3/";
    private static final String FILE_LOGO = PATH + "Logo.png";
    private static int volumeLevel = 0;
    private static int difficultyLevel = 1;

    // Create frame
    public MainApplication() {
        setTitle("A random ball bouncing game");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        // For dev phase, ESC to quick exit program, Enter to quick start
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                System.err.println();
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                } else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    contentPane.removeAll();
                    contentPane.add(gameRender.renderPlayable(0, 1, 1, currentFrame));
                    
                    contentPane.revalidate();
                    contentPane.repaint();
                }
            }
        });

        currentFrame = this;
        contentPane = (JPanel) getContentPane();
        contentPane.setBackground(new Color(20, 16, 24));
        contentPane.setLayout(null); // null = Absolute position
        
        // Method to add all main menu components
        constructMainMenu(contentPane);

        setVisible(true);
    }

    // Using in gameRender to come back to main menu
    public JPanel getMainMenu() {
        contentPane.revalidate();
        contentPane.repaint();
        setTitle("A random ball bouncing game");
        return constructMainMenu(contentPane);
    } // End of getMainMenu

    // Root
    public JPanel constructMainMenu(JPanel contentPane) {
        // Logo
        JLabel logoLabel = constructLogo();
        logoLabel.setBounds(140, -50, 500, 500);
        contentPane.add(logoLabel);

        // Start button
        JButton startBtn = constructStartBtn();
        startBtn.setBounds(350, 300, 80, 30);
        contentPane.add(startBtn);

        // Settings button
        JButton configBtn = constructSettingsBtn();
        configBtn.setBounds(475, 300, 90, 30);
        contentPane.add(configBtn);

        // Difficulty Box
        JComboBox<String> diffSelect = constructDiffiBtn();
        diffSelect.setBounds(210, 300, 80, 30);
        contentPane.add(diffSelect);

        return contentPane;
    } // End of constructMainMenu

    // Helper method to add logo, subset of main menu
    public JLabel constructLogo() {
        ImageIcon imageIcon = new ImageIcon(FILE_LOGO);
        Image scaledImage = imageIcon.getImage().getScaledInstance(500, 500, java.awt.Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel logoLabel = new JLabel(scaledIcon);
        return logoLabel;
    } // End of constructLogo

        // Helper method to add start button, subset of main menu
    public JButton constructStartBtn() {
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if (startButton.isEnabled()) {
                    int winPoint = 1;
                    // If difficulty isn't Endless
                    if (difficultyLevel != 0) {
                        // Inquire user by pop-up box
                        String input = JOptionPane.showInputDialog(null, "How many points to win?", "Winning Score select", JOptionPane.INFORMATION_MESSAGE);
                        while (true) {
                            try {
                                if (input == null) return;    // if user pressed cancelled
                                // Try interpret to int
                                winPoint = Integer.parseInt(input);
                                if (winPoint <= 0) throw new Exception();
                                break;
                            } catch (Exception e) {
                                input = JOptionPane.showInputDialog(null, "Invalid Input\nHow many points to win?", "Winning Score select", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }

                    // Remove main menu Ui & Render gameplay
                    contentPane.removeAll();
                    contentPane.add(gameRender.renderPlayable(volumeLevel, difficultyLevel, winPoint, currentFrame));
                    
                    contentPane.revalidate();
                    contentPane.repaint();
                }
             } 
        });
        return startButton;
    } // End of constructStartBtn

    // Helper method to add difficulty selector button, subset of main menu
    public JComboBox<String> constructDiffiBtn() {
        String[] difficulty = {"Endless", "Easy", "Medium", "Hard", "Random"};
        JComboBox<String> diffSelect = new JComboBox<String>(difficulty);
        
        // Set sefault difficulty to easy
        diffSelect.setSelectedIndex(1);
        diffSelect.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent ev) {
                 switch(diffSelect.getSelectedIndex()) {
                    case 0:     // Endless
                        difficultyLevel = 0;
                       break;    
                    case 1:     // Easy
                        difficultyLevel = 1;
                       break;
                    case 2:     // Medium
                        difficultyLevel = 2;
                       break;
                    case 3:     // Hard
                        difficultyLevel = 3;
                       break;
                    case 4:     // Random
                        difficultyLevel = (int) (Math.random() * 3);
                       break;
                    default:
                       break;
                 }
               }
        });
        return diffSelect;
    } // End of constructDiffiBtn

    // Helper method to add settings button, subset of main menu
    public JButton constructSettingsBtn() {
        JButton settingsButton = new JButton("Settings");
        // settingsButton.setLocationRelativeTo(null);
        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                JFrame settingsPanel = new JFrame("Settings");
                settingsPanel.setSize(500, 300);
                settingsPanel.setLocationRelativeTo(null);
                settingsPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Left: JList in a scroll pane
                String[] configOption = {"How to play", "I don't know what this should be", "Sound", "Background Image", "Credits"};
                JList<String> displayList = new JList<>(configOption);
                JScrollPane listScrollPane = new JScrollPane(displayList);

                // Right: Config area (e.g. text fields or labels)
                JPanel configArea = new JPanel();
                configArea.setLayout(new BoxLayout(configArea, BoxLayout.Y_AXIS));
                configArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                // Create the split pane
                JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, configArea);
                splitPane.setDividerLocation(150);
                splitPane.setEnabled(false);
                settingsPanel.add(splitPane);

                // Listen to selected config option 
                displayList.addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent listEvent) {
                        int selectedIndex = displayList.getSelectedIndex();
                        configArea.removeAll();
                        switch (selectedIndex) {
                            case 0:     // How to play
                                configArea.add(howToPlayPanel());
                                break;
                            case 1:     // Gameplay
                                configArea.add(gameplayPanel());
                                break;
                            case 2:     // Sound
                                configArea.add(soundPanel());
                                break;
                            case 3:     // BG Image
                                configArea.add(selectBGPanel());
                                break;
                            case 4:     // Credits
                                configArea.add(creditsPanel());
                                break;
                            default:
                                break;
                        }
                        configArea.validate();
                        configArea.repaint();
                    }
                });
                settingsPanel.setVisible(true);
            }
        });
        return settingsButton;
    } // End of constructSettingsBtn

    // *************************************************************************************
    // From here, the rest going to be helper method to support JList within settings button
    // *************************************************************************************

    // Helper method to handle JList in option pane, subset of settings
    public JPanel howToPlayPanel() {
        String message = "Use your mouse to glide the slider around \nPress ESC to return to main menu";
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(textArea);
        return panel;
    } // End of howToPlayPanel

    // Helper method to handle JList in option pane, subset of settings
    public JPanel gameplayPanel() {
        String message = "I want to kill myself";
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(textArea);
        return panel;
    } // End of gameplayPanel

    // Helper method to handle JList in option pane, subset of settings
    public JPanel soundPanel() {
        String message = "Sound volume";
        JTextArea textArea = new JTextArea(message);
        JToggleButton[] toggleButton = new JToggleButton[5];
        ButtonGroup btnGroup = new ButtonGroup();

        toggleButton[0] = new JRadioButton("0");           
        toggleButton[1] = new JRadioButton("25");  
        toggleButton[2] = new JRadioButton("50");  
        toggleButton[3] = new JRadioButton("75");  
        toggleButton[4] = new JRadioButton("100");  
	    toggleButton[volumeLevel/25].setSelected(true);
        btnGroup.add(toggleButton[0]);                                  
        btnGroup.add(toggleButton[1]);
        btnGroup.add(toggleButton[2]);
        btnGroup.add(toggleButton[3]);
        btnGroup.add(toggleButton[3]);
        btnGroup.add(toggleButton[4]);

        for (int i = 0; i <= 4; i++) {
            final int level = i * 25; // 0, 25, 50, 75, 100
            toggleButton[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    volumeLevel = level;
                }
            });
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(textArea, BorderLayout.NORTH);

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        radioPanel.add(toggleButton[0]);
        radioPanel.add(toggleButton[1]);
        radioPanel.add(toggleButton[2]);
        radioPanel.add(toggleButton[3]);
        radioPanel.add(toggleButton[4]);
        panel.add(radioPanel, BorderLayout.CENTER);

        return panel;
    } // End of soundPanel

    // Helper method to handle JList in option pane, subset of settings
    public JPanel selectBGPanel() {
        String message = "Why did I even chose STEM";
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JToggleButton[] toggleButton = new JToggleButton[5];
        ButtonGroup btnGroup = new ButtonGroup();

        toggleButton[0] = new JRadioButton("");           
        toggleButton[1] = new JRadioButton("");  
        toggleButton[2] = new JRadioButton("");  
        toggleButton[3] = new JRadioButton("");  
        toggleButton[4] = new JRadioButton("");  
        toggleButton[0].setSelected(true);
        btnGroup.add(toggleButton[0]);                                  
        btnGroup.add(toggleButton[1]);
        btnGroup.add(toggleButton[2]);
        btnGroup.add(toggleButton[3]);
        btnGroup.add(toggleButton[4]);

        for (int i = 0; i <= 4; i++) {
            toggleButton[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    // BG Image 1-4
                }
            });
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(textArea, BorderLayout.NORTH);

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        radioPanel.add(toggleButton[0]);
        radioPanel.add(toggleButton[1]);
        radioPanel.add(toggleButton[2]);
        radioPanel.add(toggleButton[3]);
        radioPanel.add(toggleButton[4]);
        panel.add(radioPanel, BorderLayout.CENTER);

        return panel;
    } // End of selectBGPanel

    // Helper method to handle JList in option pane, subset of settings
    public JPanel creditsPanel() {
        String message = "    Made with ♥ by these fellows, \n\n" + //
                        "    Veerapat Leepiboonsawat 6580969\n" + //
                        "    Yoswaris Lawpaiboon,    6681170\n" + //
                        "    Pasin Piyavej           6681187\n" + //
                        "    Nathan Tanabotiboon     6681224 \n" + //
                        "    Praepilai Phetsamsri    6681374";
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(textArea);
        return panel;
    } // End of creditsPanel
}
