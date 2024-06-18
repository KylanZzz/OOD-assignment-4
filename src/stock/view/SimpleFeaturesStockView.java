package stock.view;

import java.awt.*;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import stock.controller.PortfolioStockFeatures;
import stock.view.FeaturesStockView;

public class SimpleFeaturesStockView implements FeaturesStockView {
  private JPanel savePanel;
  private JButton saveButton;

  private JTable table;
  private JScrollPane tablePanel;
  private DefaultTableModel tableModel;
  private JPanel DisplayingPanel;
  private CardLayout cardLayout;

  private JLabel displayLabel;
  private JButton buyStockButton;
  private JTextField sharesText;
  private JTextField monthText;
  private JTextField dayText;
  private JTextField yearText;
  private JTextField tickerText;
  private JButton sellStockButton;
  private JButton compositionButton;
  private JButton ValueButton;
  private JFrame mainFrame;
  private JFrame portfolioFrame;
  private JLabel portfolioLabel;
  private JPanel portfolioPanel;
  private JPanel mainPanel;
  private JLabel loadPortfolioLabel;
  private JTextField createPortfolioInput;
  private JButton getPortfolioSaveButton;
  private JButton newPortfolioButton;
  private JButton loadPortfolioSaveButton;
  private JLabel createPortfolioLabel;
  private JButton editPortfolioButton;
  private JComboBox<String> portfolioDropdown;

  public SimpleFeaturesStockView(String title) {
    initMainWindow(title);
    initPortfolioWindow();
  }

  private void initMainWindow(String title) {
    // Set up main frame
    mainFrame = new JFrame();
    mainFrame.setTitle(title);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setMinimumSize(new Dimension(400,300));
    this.mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    // Set up the createPortfolio panel
    JPanel createPortfolioPanel = new JPanel();
    newPortfolioButton = new JButton("Create New Portfolio");
    createPortfolioInput = new JTextField();
    createPortfolioInput.setPreferredSize(new Dimension(100,
            createPortfolioInput.getPreferredSize().height));
    createPortfolioPanel.add(new JLabel("Name:"));
    createPortfolioPanel.add(createPortfolioInput);
    createPortfolioPanel.add(newPortfolioButton);
    createPortfolioPanel.add(Box.createVerticalStrut(50));
    mainPanel.add(createPortfolioPanel);

    //Set up load portfolio save file input
    JPanel loadPortfolioPanel = new JPanel();
    getPortfolioSaveButton = new JButton("Choose File");
    getPortfolioSaveButton.addActionListener(it -> createNewGetPortfolioSaveFrame());
    loadPortfolioSaveButton = new JButton("Load Save");
    loadPortfolioPanel.add(getPortfolioSaveButton);
    loadPortfolioPanel.add(loadPortfolioSaveButton);
    loadPortfolioPanel.add(Box.createVerticalStrut(50));
    mainPanel.add(loadPortfolioPanel);

    //Create display save label
    loadPortfolioLabel = new JLabel(" ");
    loadPortfolioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    mainPanel.add(Box.createVerticalStrut(25));
    mainPanel.add(loadPortfolioLabel);

    //Create display results label
    createPortfolioLabel = new JLabel(" ");
    createPortfolioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    mainPanel.add(Box.createVerticalStrut(25));
    mainPanel.add(createPortfolioLabel);

    //Portfolio Dropdown
    JPanel portfolioSelectorPanel = new JPanel();
    portfolioDropdown = new JComboBox<>();
    portfolioSelectorPanel.add(new JLabel("Select Portfolio:"));
    portfolioSelectorPanel.add(portfolioDropdown);
    editPortfolioButton = new JButton("Edit");
    portfolioSelectorPanel.add(editPortfolioButton);
    portfolioSelectorPanel.add(Box.createVerticalStrut(50));
    mainPanel.add(portfolioSelectorPanel);

    mainFrame.getContentPane().add(mainPanel);
    mainFrame.pack();
    mainFrame.setLocationRelativeTo(null);
    mainFrame.setVisible(true);
  }

  private void createNewGetPortfolioSaveFrame() {
    JFileChooser portfolioSaveInput = new JFileChooser("./res/portfolio");
    portfolioSaveInput.setDialogTitle("Please pick the save file");
    portfolioSaveInput.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
    int result = portfolioSaveInput.showOpenDialog(null);

    if (result == JFileChooser.APPROVE_OPTION) {
      java.io.File selectedFile = portfolioSaveInput.getSelectedFile();
      loadPortfolioLabel.setText("Selected: " + selectedFile.getName());
    }
  }

  private void createManagePortfolioFrame(String portfolioName) {
    portfolioFrame.setTitle(portfolioName);
    portfolioFrame.setVisible(true);
  }

  private void initPortfolioWindow() {
    portfolioFrame = new JFrame();
    portfolioLabel = new JLabel();
    portfolioPanel = new JPanel(new GridBagLayout());  // Use GridBagLayout

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(20, 5, 15, 5);  // Increased top and bottom margin

    // Share
    JLabel sharesLabel = new JLabel("Shares: ");
    sharesText = new JTextField(10);
    sharesLabel.setFont(new Font("MV Boli", Font.PLAIN, 20));

    // Ticker
    JLabel tickerLabel = new JLabel("Ticker: ");
    tickerText = new JTextField(10);
    tickerLabel.setFont(new Font("MV Boli", Font.PLAIN, 20));

    // Date
    JLabel dateLabel = new JLabel("Date: ");
    dateLabel.setFont(new Font("MV Boli", Font.PLAIN, 20));
    JLabel monthLabel = new JLabel("Month ");
    monthText = new JTextField(5);
    JLabel dayLabel = new JLabel("Day ");
    dayText = new JTextField(5);
    JLabel yearLabel = new JLabel("Year ");
    yearText = new JTextField(5);

    // All the functions of the portfolio
    JPanel FunctionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

    buyStockButton = new JButton("Buy Stock");
    buyStockButton.setFont(new Font("MV Boli", Font.BOLD, 16));
    buyStockButton.setForeground(Color.BLACK);

    sellStockButton = new JButton("Sell Stock");
    sellStockButton.setFont(new Font("MV Boli", Font.BOLD, 16));
    sellStockButton.setForeground(Color.BLACK);

    compositionButton = new JButton("Get Composition");
    compositionButton.setFont(new Font("MV Boli", Font.BOLD, 16));
    compositionButton.setForeground(Color.BLACK);

    ValueButton = new JButton("Get Value");
    ValueButton.setFont(new Font("MV Boli", Font.BOLD, 16));
    ValueButton.setForeground(Color.BLACK);

    FunctionPanel.add(buyStockButton);
    FunctionPanel.add(sellStockButton);
    FunctionPanel.add(compositionButton);
    FunctionPanel.add(ValueButton);

    DisplayingPanel = new JPanel();
    cardLayout = new CardLayout();
    DisplayingPanel.setLayout(cardLayout);

    displayLabel = new JLabel(" ");
    displayLabel.setHorizontalAlignment(SwingConstants.CENTER);

    JPanel labelPanel = new JPanel(new BorderLayout());
    labelPanel.add(displayLabel, BorderLayout.CENTER);

    String[] columnNames = {"Tickers", "Shares"};
    tableModel = new DefaultTableModel(columnNames, 0);
    table = new JTable(tableModel);
    tablePanel = new JScrollPane(table);

    DisplayingPanel.add(labelPanel, "Label");
    DisplayingPanel.add(tablePanel, "Table");

    // save portfolio
    savePanel = new JPanel(new BorderLayout());
    saveButton = new JButton("Save Portfolio");
    saveButton.setFont(new Font("MV Boli", Font.BOLD, 16));

    savePanel.add(saveButton, BorderLayout.CENTER);

    portfolioFrame.setSize(700, 1300);
    portfolioFrame.setMinimumSize(new Dimension(300, 600));
    portfolioFrame.setLayout(new BorderLayout());

    portfolioPanel.add(sharesLabel);
    portfolioPanel.add(sharesText, gbc);
    portfolioPanel.add(tickerLabel);
    portfolioPanel.add(tickerText, gbc);
    portfolioPanel.add(dateLabel);
    portfolioPanel.add(monthLabel);
    portfolioPanel.add(monthText);
    portfolioPanel.add(dayLabel);
    portfolioPanel.add(dayText);
    portfolioPanel.add(yearLabel);
    portfolioPanel.add(yearText, gbc);
    portfolioPanel.add(FunctionPanel, gbc);

    JPanel topMargin = new JPanel();
    topMargin.setPreferredSize(new Dimension(0, 5));
    portfolioFrame.add(topMargin, BorderLayout.CENTER);
    portfolioFrame.add(portfolioPanel, BorderLayout.NORTH);
    portfolioFrame.add(DisplayingPanel, BorderLayout.CENTER);
    portfolioFrame.add(savePanel, BorderLayout.SOUTH);

    portfolioFrame.pack();
  }

  @Override
  public void addFeatures(PortfolioStockFeatures features) {
    loadPortfolioSaveButton.addActionListener(it ->
            features.loadPortfolio(loadPortfolioLabel.getText().replace("Selected: ", "")));

    newPortfolioButton.addActionListener(it ->
            features.createPortfolio(createPortfolioInput.getText()));

    editPortfolioButton.addActionListener(it ->
    features.choosePortfolio((String) portfolioDropdown.getSelectedItem()));

    compositionButton.addActionListener(it -> {
      cardLayout.show(DisplayingPanel, "Table");
      features.getComposition(
              portfolioFrame.getTitle(),
              monthText.getText(),
              dayText.getText(),
              yearText.getText(),
              sharesText.getText(),
              tickerText.getText());
    });

    ValueButton.addActionListener(it -> {
      cardLayout.show(DisplayingPanel, "Label");
      features.getValue(
              portfolioFrame.getTitle(),
              monthText.getText(),
              dayText.getText(),
              yearText.getText(),
              sharesText.getText(),
              tickerText.getText());
    });

    buyStockButton.addActionListener(it -> {
      cardLayout.show(DisplayingPanel, "Label");
      features.buyStock(
              portfolioFrame.getTitle(),
              tickerText.getText(),
              sharesText.getText(),
              monthText.getText(),
              dayText.getText(),
              yearText.getText());
    });

    sellStockButton.addActionListener(it -> {
      cardLayout.show(DisplayingPanel, "Label");
      features.sellStock(
              portfolioFrame.getTitle(),
              tickerText.getText(),
              sharesText.getText(),
              monthText.getText(),
              dayText.getText(),
              yearText.getText());
    });

    saveButton.addActionListener(it -> {
      cardLayout.show(DisplayingPanel, "Label");
      features.savePortfolio(
              portfolioFrame.getTitle());
    });
  }

  @Override
  public void displayComposition(Map<String, Double> composition) {
    tableModel.setRowCount(0);

    for (Map.Entry<String, Double> entry : composition.entrySet()) {
      tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
    }
  }

  @Override
  public void displayValue(double value) {
    displayLabel.setText("The value of this portfolio on this date is: " + "$" + value);
  }

  @Override
  public void displayBoughtStock(String ticker, double shares) {
    displayLabel.setText("You have successfully purchased " + ticker + " by " + shares + " shares");
  }

  @Override
  public void displaySoldStock(String ticker, double shares) {
    displayLabel.setText("You have successfully sold " + ticker + " by " + shares + " shares");
  }

  @Override
  public void displayPortfolios(List<String> names) {
    portfolioDropdown.removeAllItems();
    for (String name: names) {
      portfolioDropdown.addItem(name);
    }
  }

  @Override
  public void displayCreatedSave(String portfolioName) {
    displayLabel.setText("Successfully saved: " + portfolioName);
  }

  @Override
  public void displayCreatedPortfolio(String portfolio) {
    createPortfolioLabel.setText("Successfully created: " + portfolio);
  }

  @Override
  public void displayLoadedPortfolio(String portfolio) {
    createPortfolioLabel.setText("Successfully loaded: " + portfolio);
  }

  @Override
  public void displayEditPortfolio(String portfolio) {
    createManagePortfolioFrame(portfolio);
  }

  @Override
  public void displayErrorMessage(String message) {
    JOptionPane.showMessageDialog(mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
  }
}
