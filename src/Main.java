import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.Timer;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class Main {
    private JFrame frame;
    private JPanel mapPanel;
    private JLabel infoLabel;
    private JLabel pointsLabel;
    private JButton upgradeButton;
    private Map<String, JButton> countryButtons;
    private Timer animationTimer;
    private Timer gameTimer;
    private Map<String, Country> countries;
    private DefaultListModel<String> highScoresModel;
    private JList<String> highScoresList;
    private int points;
    private Random random;
    private int elapsedTime = 0;



    public Main() {
        points = 0;
        random = new Random();
        countries = new HashMap<>();
        highScoresModel = new DefaultListModel<>();
        loadHighScores();
        initializeMainMenu();

        setupKeyBindings(frame.getRootPane());


    }
    private void reduceInfection() {
        for (Country country : countries.values()) {
            int reducedInfected = (int) (country.getInfected() * 0.7);
            country.setInfected(reducedInfected);
        }
    }

    private void fasterCure() {
        int currentDelay = gameTimer.getDelay();
        gameTimer.setDelay((int) (currentDelay * 0.8));
    }


    private void increaseImmunity() {
        List<String> countryNames = new ArrayList<>(countries.keySet());
        String immuneCountry = countryNames.get(random.nextInt(countryNames.size()));
        countries.get(immuneCountry).lockDown();
    }

    private void improveTravelRestrictions() {
        for (Country country : countries.values()) {
            country.reduceConnections(1);
        }
    }
    private void quarantineHighRisk() {
        for (Country country : countries.values()) {
            if (country.getInfected() > country.getPopulation() * 0.5) {
                country.lockDown();
            }
        }
    }
    private void boostVaccineDistribution() {
        int curedPoints = points / 10;
        for (Country country : countries.values()) {
            int reducedInfected = Math.max(country.getInfected() - curedPoints, 0);
            country.setInfected(reducedInfected);
        }
    }
    private void sanitationCampaign() {
        for (Country country : countries.values()) {
            int reducedInfected = (int) (country.getInfected() * 0.9);
            country.setInfected(reducedInfected);
        }
    }
    private void globalAwarenessCampaign() {
        for (Country country : countries.values()) {
            country.increaseAwareness(10);
        }
    }
    private void medicalAid() {
        for (Country country : countries.values()) {
            int reducedInfected = Math.max(country.getInfected() - 1000, 0);
            country.setInfected(reducedInfected);
        }
    }
    private void extraLife() {
        points += 5000;
    }

    private void applyMedicalAid(int percentage) {
        for (Country country : countries.values()) {
            int reducedInfected = country.getInfected() * percentage / 100;
            country.setInfected(country.getInfected() - reducedInfected);
        }
        JOptionPane.showMessageDialog(frame, "Medical Aid applied! Infection reduced by " + percentage + "%.");
    }
    private void reduceSpreadRate(int percentage) {
        for (Country country : countries.values()) {
            country.reduceConnections(percentage / 10);
        }
        JOptionPane.showMessageDialog(frame, "Spread rate reduced by " + percentage + "%.");
    }
    private void increaseAwareness(int percentage) {
        for (Country country : countries.values()) {
            country.increaseAwareness(percentage);
        }
        JOptionPane.showMessageDialog(frame, "Awareness increased by " + percentage + "%.");
    }
    private void applyTravelRestrictions(int percentage) {
        for (Country country : countries.values()) {
            country.reduceConnections(percentage / 10);
        }
        JOptionPane.showMessageDialog(frame, "Travel restrictions applied! Connections reduced by " + percentage + "%.");
    }
    private void stopSpreadInRandomCountry() {
        List<Country> countryList = new ArrayList<>(countries.values());
        Country randomCountry = countryList.get(random.nextInt(countryList.size()));
        randomCountry.setInfected(0);
        JOptionPane.showMessageDialog(frame, "Spread stopped in " + randomCountry.getName() + "!");
    }
    private void applyGlobalLockdown(int seconds) {
        Timer lockdownTimer = new Timer(1000, null);
        lockdownTimer.addActionListener(new ActionListener() {
            int remainingTime = seconds;

            @Override
            public void actionPerformed(ActionEvent e) {
                remainingTime--;
                if (remainingTime <= 0) {
                    lockdownTimer.stop();
                    JOptionPane.showMessageDialog(frame, "Global lockdown ended!");
                }
            }
        });
        lockdownTimer.start();
        JOptionPane.showMessageDialog(frame, "Global lockdown applied for " + seconds + " seconds!");
    }
    private void increaseRecoverySpeed(int percentage) {
        for (Country country : countries.values()) {
            int reducedInfected = country.getInfected() * percentage / 100;
            country.setInfected(country.getInfected() - reducedInfected);
        }
        JOptionPane.showMessageDialog(frame, "Emergency Response activated! Recovery speed increased by " + percentage + "%.");
    }



    private void initializeMainMenu() {
        frame = new JFrame("AntiPlague Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setLayout(new BorderLayout());


        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/menufoto.jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);
        frame.add(backgroundPanel);


        JLabel titleLabel = new JLabel("AntiPlague Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE); // Beyaz renk başlık
        titleLabel.setBounds(500, 100, 800, 100);
        backgroundPanel.add(titleLabel);


        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.PLAIN, 24));
        newGameButton.setBounds(800, 300, 200, 50);
        newGameButton.addActionListener(e -> initializeGame());
        backgroundPanel.add(newGameButton);


        JButton highScoresButton = new JButton("High Scores");
        highScoresButton.setFont(new Font("Arial", Font.PLAIN, 24));
        highScoresButton.setBounds(800, 400, 200, 50);
        highScoresButton.addActionListener(e -> showHighScores());
        backgroundPanel.add(highScoresButton);


        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 24));
        exitButton.setBounds(800, 500, 200, 50);
        exitButton.addActionListener(e -> System.exit(0));
        backgroundPanel.add(exitButton);

        frame.setVisible(true);
    }


    private void initializeGame() {
        String[] difficultyOptions = {"Easy", "Medium", "Hard"};
        String selectedDifficulty = (String) JOptionPane.showInputDialog(
                frame,
                "Select Difficulty Level:",
                "Difficulty Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                difficultyOptions,
                difficultyOptions[0]
        );

        if (selectedDifficulty == null) {
            initializeMainMenu();
            return;
        }

        int spreadDelay;
        switch (selectedDifficulty) {
            case "Easy":
                spreadDelay = 3000;
                break;
            case "Medium":
                spreadDelay = 2000;
                break;
            case "Hard":
                spreadDelay = 1000;
                break;
            default:
                spreadDelay = 2000;
        }

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        infoLabel = new JLabel("Game Information", SwingConstants.CENTER);
        frame.add(infoLabel, BorderLayout.NORTH);

        pointsLabel = new JLabel("Points: 0", SwingConstants.CENTER);
        frame.add(pointsLabel, BorderLayout.SOUTH);


        pointsLabel = new JLabel("Points: 0 | Time: 0s", SwingConstants.CENTER);
        frame.add(pointsLabel, BorderLayout.SOUTH);





        upgradeButton = new JButton("Upgrades");
        upgradeButton.setBounds(650, 20, 120, 30);
        upgradeButton.addActionListener(e -> handleUpgrade());
        frame.add(upgradeButton, BorderLayout.EAST);


        mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/dunya-haritasi-2.jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        mapPanel.setLayout(null);
        frame.add(mapPanel, BorderLayout.CENTER);

        countryButtons = new HashMap<>();
        initializeCountries();
        setupCountryButtons();

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Q) {
                    endGame();
                }
            }
        });




        frame.setFocusable(true);
        frame.requestFocusInWindow();

        frame.revalidate();
        frame.repaint();

        startInfection();
        setupSpreadTimer(spreadDelay);

        startTravelAnimationLoop("Canada", "USA", "car");
        startTravelAnimationLoop("Canada", "Norway", "Ship");
        startTravelAnimationLoop("Canada", "France", "plane");

        startTravelAnimationLoop("USA", "Canada", "plane");
        startTravelAnimationLoop("USA", "Brazil", "plane");
        startTravelAnimationLoop("USA", "France", "plane");
        startTravelAnimationLoop("USA", "Norway", "plane");

        startTravelAnimationLoop("Brazil", "South Africa", "ship");
        startTravelAnimationLoop("Brazil", "USA", "ship");
        startTravelAnimationLoop("Brazil", "France", "plane");
        startTravelAnimationLoop("Brazil", "Norway", "plane");



        startTravelAnimationLoop("France", "Norway", "ship");
        startTravelAnimationLoop("France", "Poland", "car");
        startTravelAnimationLoop("France", "Turkey", "plane");
        startTravelAnimationLoop("France", "Canada", "ship");
        startTravelAnimationLoop("France", "USA", "ship");
        startTravelAnimationLoop("France", "Brazil", "ship");



        startTravelAnimationLoop("Norway", "France", "plane");
        startTravelAnimationLoop("Norway", "Poland", "ship");
        startTravelAnimationLoop("Norway", "Canada", "plane");
        startTravelAnimationLoop("Norway", "USA", "ship");
        startTravelAnimationLoop("Norway", "Brazil", "ship");



        startTravelAnimationLoop("Poland", "France", "plane");
        startTravelAnimationLoop("Poland", "Norway", "plane");
        startTravelAnimationLoop("Poland", "Turkey", "car");
        startTravelAnimationLoop("Poland", "China", "plane");



        startTravelAnimationLoop("Turkey", "China", "plane");
        startTravelAnimationLoop("Turkey", "Poland", "plane");
        startTravelAnimationLoop("Turkey", "France", "car");
        startTravelAnimationLoop("Turkey", "South Africa", "plane");



        startTravelAnimationLoop("South Africa", "Brazil", "plane");
        startTravelAnimationLoop("South Africa", "Australia", "ship");
        startTravelAnimationLoop("South Africa", "China", "ship");
        startTravelAnimationLoop("South Africa", "Turkey", "ship");



        startTravelAnimationLoop("China", "Australia", "plane");
        startTravelAnimationLoop("China", "South Africa", "plane");
        startTravelAnimationLoop("China", "Turkey", "car");
        startTravelAnimationLoop("China", "Poland", "car");

        startTravelAnimationLoop("Australia", "China", "ship");
        startTravelAnimationLoop("Australia", "South Africa", "plane");


        gameTimer = new Timer(1000, e -> {
            elapsedTime++;
            pointsLabel.setText("Points: " + points + " | Time: " + elapsedTime + "s");
        });
        gameTimer.start();


        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Q) {
                    endGame();
                }
            }
        });

    }
    private void setupKeyBindings(JComponent component) {
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();


        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), "quitGame");
        actionMap.put("quitGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endGame(); // Oyunu bitir
            }
        });
    }



    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }




    private void startTravelAnimationLoop(String from, String to, String mode) {
        JButton fromButton = countryButtons.get(from);
        JButton toButton = countryButtons.get(to);

        if (fromButton == null || toButton == null) {
            System.err.println("Error: Missing button for animation. Check 'from' or 'to' country.");
            return;
        }


        ImageIcon icon;
        switch (mode.toLowerCase()) {
            case "car":
                icon = resizeIcon(new ImageIcon("resources//carr.png"), 30, 30);
                break;
            case "plane":
                icon = resizeIcon(new ImageIcon("resources/plane.png"), 30, 30);
                break;
            case "ship":
                icon = resizeIcon(new ImageIcon("resources/ship.png"), 30, 30);
                break;
            default:
                throw new IllegalArgumentException("Invalid travel mode: " + mode);
        }


        JLabel animationLabel = new JLabel(icon);
        int startX = fromButton.getX() + fromButton.getWidth() / 2;
        int startY = fromButton.getY() + fromButton.getHeight() / 2;
        int endX = toButton.getX() + toButton.getWidth() / 2;
        int endY = toButton.getY() + toButton.getHeight() / 2;

        animationLabel.setBounds(startX, startY, 50, 50);
        mapPanel.add(animationLabel);
        mapPanel.revalidate();
        mapPanel.repaint();

        Timer animationTimer = new Timer(10, new ActionListener() {
            boolean forward = true;
            int currentX = startX;
            int currentY = startY;

            @Override
            public void actionPerformed(ActionEvent e) {
                int targetX = forward ? endX : startX;
                int targetY = forward ? endY : startY;

                int dx = (targetX - currentX) / 20;
                int dy = (targetY - currentY) / 20;

                currentX += dx;
                currentY += dy;

                animationLabel.setLocation(currentX, currentY);


                if (Math.abs(currentX - targetX) < 5 && Math.abs(currentY - targetY) < 5) {
                    forward = !forward;
                }
            }
        });

        animationTimer.start();
    }



    private void endGame() {
        if (gameTimer != null) gameTimer.stop(); 


        String playerName = JOptionPane.showInputDialog(frame, "Enter your name:", "Save Score", JOptionPane.PLAIN_MESSAGE);


        if (playerName != null && !playerName.trim().isEmpty()) {
            String scoreEntry = playerName + " - " + points + " Points | Time: " + elapsedTime + "s";
            highScoresModel.addElement(scoreEntry);
            saveHighScores();
        }

        JOptionPane.showMessageDialog(frame, "Returning to Main Menu...", "Game Over", JOptionPane.INFORMATION_MESSAGE);


        initializeMainMenu();
    }



    private void saveHighScores() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("high_scores.txt"))) {
            for (int i = 0; i < highScoresModel.size(); i++) {
                writer.println(highScoresModel.get(i));
            }
        } catch (IOException e) {
            System.err.println("Failed to save high scores: " + e.getMessage());
        }
    }




    private void showHighScores() {
        JFrame highScoresFrame = new JFrame("High Scores");
        highScoresFrame.setSize(400, 600);
        highScoresFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        highScoresList = new JList<>(highScoresModel);
        highScoresList.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(highScoresList);

        highScoresFrame.add(scrollPane);
        highScoresFrame.setVisible(true);
    }

    private void loadHighScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader("high_scores.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                highScoresModel.addElement(line);
            }
        } catch (IOException e) {
            System.err.println("Failed to load high scores: " + e.getMessage());
        }
    }



    private void initializeCountries() {
        countries.put("Canada", new Country("Canada", 40000000));
        countries.put("USA", new Country("USA", 334000000));
        countries.put("Turkey", new Country("Turkey", 85000000));
        countries.put("Brazil", new Country("Brazil", 216000000));
        countries.put("Poland", new Country("Poland", 37000000));
        countries.put("France", new Country("France", 68000000));
        countries.put("Australia", new Country("Australia", 25600000));
        countries.put("China", new Country("China", 1412000000));
        countries.put("Norway", new Country("Norway", 5400000));
        countries.put("South Africa", new Country("South Africa", 60000000));

        countries.get("Canada").addConnection("USA");
        countries.get("Canada").addConnection("Norway");
        countries.get("Canada").addConnection("France");

        countries.get("USA").addConnection("Canada");
        countries.get("USA").addConnection("Brazil");
        countries.get("USA").addConnection("France");
        countries.get("USA").addConnection("Norway");

        countries.get("Brazil").addConnection("South Africa");
        countries.get("Brazil").addConnection("France");
        countries.get("Brazil").addConnection("USA");

        countries.get("France").addConnection("Norway");
        countries.get("France").addConnection("Poland");
        countries.get("France").addConnection("Turkey");
        countries.get("France").addConnection("Canada");
        countries.get("France").addConnection("USA");
        countries.get("France").addConnection("Brazil");

        countries.get("Norway").addConnection("France");
        countries.get("Norway").addConnection("Poland");
        countries.get("Norway").addConnection("Canada");
        countries.get("Norway").addConnection("USA");
        countries.get("Norway").addConnection("Brazil");

        countries.get("Poland").addConnection("France");
        countries.get("Poland").addConnection("Norway");
        countries.get("Poland").addConnection("Turkey");
        countries.get("Poland").addConnection("China");

        countries.get("Turkey").addConnection("China");
        countries.get("Turkey").addConnection("Poland");
        countries.get("Turkey").addConnection("France");
        countries.get("Turkey").addConnection("South Africa");

        countries.get("South Africa").addConnection("Brazil");
        countries.get("South Africa").addConnection("Australia");
        countries.get("South Africa").addConnection("China");
        countries.get("South Africa").addConnection("Turkey");

        countries.get("China").addConnection("Australia");
        countries.get("China").addConnection("South Africa");
        countries.get("China").addConnection("Turkey");
        countries.get("China").addConnection("Poland");

        countries.get("Australia").addConnection("China");
        countries.get("Australia").addConnection("South Africa");
    }

    private void setupCountryButtons() {
        addCountryButton("Canada", 300, 280);
        addCountryButton("USA", 300, 450);
        addCountryButton("Turkey", 980, 465);
        addCountryButton("Brazil", 550, 700);
        addCountryButton("Poland", 930, 380);
        addCountryButton("France", 770, 400);
        addCountryButton("Australia", 1500, 750);
        addCountryButton("China", 1300, 470);
        addCountryButton("Norway", 850, 280);
        addCountryButton("South Africa", 900, 800);

        for (String countryName : countries.keySet()) {
            JButton button = countryButtons.get(countryName);
            button.addActionListener(e -> handleCountryClick(countryName));
        }
    }

    private void addCountryButton(String countryName, int x, int y) {
        JButton button = new JButton(countryName);
        button.setBounds(x, y, 100, 50);
        mapPanel.add(button);
        mapPanel.revalidate();
        mapPanel.repaint();
        countryButtons.put(countryName, button);
    }

    private void handleCountryClick(String countryName) {
        Country country = countries.get(countryName);
        infoLabel.setText(countryName + " - Population: " + country.getPopulation() + ", Infected: " + country.getInfected());
    }
    private void handleUpgrade() {
        String[] upgrades = {
                "1. Medical Aid (1000 points): Reduce infections by 30%",
                "2. Faster Spread Control (800 points): Reduce spread rate by 20%",
                "3. Increased Awareness (500 points): Increase population awareness by 10%",
                "4. Vaccination Program (1500 points): Reduce infections by 50%",
                "5. Travel Restrictions (700 points): Reduce connections by 10%",
                "6. Emergency Response (1200 points): Increase recovery speed by 20%",
                "7. Healthcare Funding (900 points): Reduce infection spread rate by 15%",
                "8. Media Campaign (600 points): Increase awareness by 20%",
                "9. Research Funding (2000 points): Stop spread in 1 random country",
                "10. Lockdown (2500 points): Stop spread globally for 30 seconds"
        };

        String upgrade = (String) JOptionPane.showInputDialog(
                frame,
                "Choose an upgrade:",
                "Upgrades",
                JOptionPane.PLAIN_MESSAGE,
                null,
                upgrades,
                upgrades[0]
        );

        if (upgrade != null) {
            if (upgrade.startsWith("1")) { // Medical Aid
                if (points >= 1000) {
                    points -= 1000;
                    applyMedicalAid(30);
                    pointsLabel.setText("Points: " + points);
                } else {
                    JOptionPane.showMessageDialog(frame, "Not enough points for Medical Aid!");
                }
            } else if (upgrade.startsWith("2")) { // Faster Spread Control
                if (points >= 800) {
                    points -= 800;
                    reduceSpreadRate(20);
                    pointsLabel.setText("Points: " + points);
                } else {
                    JOptionPane.showMessageDialog(frame, "Not enough points for Faster Spread Control!");
                }
            } else if (upgrade.startsWith("3")) { // Increased Awareness
                if (points >= 500) {
                    points -= 500;
                    increaseAwareness(10);
                    pointsLabel.setText("Points: " + points);
                } else {
                    JOptionPane.showMessageDialog(frame, "Not enough points for Increased Awareness!");
                }
            } else if (upgrade.startsWith("4")) { // Vaccination Program
                if (points >= 1500) {
                    points -= 1500;
                    applyMedicalAid(50);
                    pointsLabel.setText("Points: " + points);
                } else {
                    JOptionPane.showMessageDialog(frame, "Not enough points for Vaccination Program!");
                }
            } else if (upgrade.startsWith("5")) { // Travel Restrictions
                if (points >= 700) {
                    points -= 700;
                    applyTravelRestrictions(10);
                    pointsLabel.setText("Points: " + points);
                } else {
                    JOptionPane.showMessageDialog(frame, "Not enough points for Travel Restrictions!");
                }
            } else if (upgrade.startsWith("6")) { // Emergency Response
                if (points >= 1200) {
                    points -= 1200;
                    increaseRecoverySpeed(20);
                    pointsLabel.setText("Points: " + points);
                } else {
                    JOptionPane.showMessageDialog(frame, "Not enough points for Emergency Response!");
                }
            } else if (upgrade.startsWith("7")) { // Healthcare Funding
                if (points >= 900) {
                    points -= 900;
                    reduceSpreadRate(15);
                    pointsLabel.setText("Points: " + points);
                } else {
                    JOptionPane.showMessageDialog(frame, "Not enough points for Healthcare Funding!");
                }
            } else if (upgrade.startsWith("8")) { // Media Campaign
                if (points >= 600) {
                    points -= 600;
                    increaseAwareness(20);
                    pointsLabel.setText("Points: " + points);
                } else {
                    JOptionPane.showMessageDialog(frame, "Not enough points for Media Campaign!");
                }
            } else if (upgrade.startsWith("9")) { // Research Funding
                if (points >= 2000) {
                    points -= 2000;
                    stopSpreadInRandomCountry();
                    pointsLabel.setText("Points: " + points);
                } else {
                    JOptionPane.showMessageDialog(frame, "Not enough points for Research Funding!");
                }
            } else if (upgrade.startsWith("10")) { // Lockdown
                if (points >= 2500) {
                    points -= 2500;
                    applyGlobalLockdown(30);
                    pointsLabel.setText("Points: " + points);
                } else {
                    JOptionPane.showMessageDialog(frame, "Not enough points for Lockdown!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid choice!");
            }
        }
    }

    private void applyUpgrade(boolean canApply, int cost, Runnable upgradeAction, String successMessage) {
        if (canApply) {
            points -= cost;
            upgradeAction.run();
            JOptionPane.showMessageDialog(frame, successMessage, "Upgrade Applied", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Not enough points for this upgrade.", "Insufficient Points", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void startInfection() {
        String initialCountry = "Canada";
        infectCountry(initialCountry, 10);
    }

    private void infectCountry(String countryName, int count) {
        Country country = countries.get(countryName);

        if (country != null && !country.isLocked()) {
            // Infect the country
            country.infect(count);

            // Update points
            points += count;


            pointsLabel.setText("Points: " + points + " | Time: " + elapsedTime + "s");
            infoLabel.setText(countryName + " - Population: " + country.getPopulation()
                    + ", Infected: " + country.getInfected());

            checkGameOver();
        }
    }


    private void setupSpreadTimer(int spreadDelay) {
        Timer spreadTimer = new Timer(spreadDelay, e -> {
            for (String countryName : countries.keySet()) {
                Country country = countries.get(countryName);

                if (country.getInfected() > 0) {
                    for (String neighbor : country.getConnections()) {

                        int newInfections = random.nextInt(5) + 1;
                        infectCountry(neighbor, newInfections);

                        // Reduce animation frequency only
                        if (random.nextInt(100) < 10) {
                            String[] modes = {"car", "plane", "ship"};
                            String mode = modes[random.nextInt(modes.length)];
                            startTravelAnimationLoop(countryName, neighbor, mode);
                        }
                    }
                }
            }
        });
        spreadTimer.start();
    }




    private void checkGameOver() {
        boolean allInfected = countries.values().stream().allMatch(Country::isFullyInfected);
        if (allInfected) {
            JOptionPane.showMessageDialog(frame, "Game Over: All countries infected.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        boolean allHealthy = countries.values().stream().allMatch(country -> country.getInfected() == 0);
        if (allHealthy) {
            JOptionPane.showMessageDialog(frame, "Congratulations! All countries are saved.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}

class Country implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int population;
    private int infected;
    private boolean isLocked;
    private List<String> connections;
    private Random random;

    public Country(String name, int population) {
        this.name = name;
        this.population = population;
        this.infected = 0;
        this.isLocked = false;
        this.connections = new ArrayList<>();
        this.random = new Random();
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public int getInfected() {
        return infected;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void infect(int count) {
        infected = Math.min(infected + count, population);
    }

    public List<String> getConnections() {
        return connections;
    }

    public void addConnection(String countryName) {
        connections.add(countryName);
    }

    public boolean isFullyInfected() {
        return infected >= population;
    }
    public void lockDown() {
        this.isLocked = true;
    }


    public void reduceConnections(int count) {
        for (int i = 0; i < count && !connections.isEmpty(); i++) {
            connections.remove(random.nextInt(connections.size()));
        }
    }
    public void increaseAwareness(int percentage) {
        this.population += this.population * percentage / 100; // Awareness artırma
    }

    public void setInfected(int infected) {
        this.infected = Math.max(0, Math.min(infected, population));
    }


}
