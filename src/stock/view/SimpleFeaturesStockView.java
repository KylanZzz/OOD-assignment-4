package stock.view;

import java.awt.*;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import stock.controller.PortfolioStockFeatures;
import stock.view.FeaturesStockView;

public class SimpleFeaturesStockView implements FeaturesStockView {
  private JButton commandButton;
  private JFrame portfolioFrame;
  private JLabel portfolioLabel;
  private Border portfolioBorder;
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
    init(title);
  }

  private void init(String title) {
    // Set up main frame
    JFrame mainFrame = new JFrame();
    mainFrame.setTitle(title);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setMinimumSize(new Dimension(300,100));
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
    mainPanel.add(createPortfolioPanel);

    //Set up load portfolio save file input
    JPanel loadPortfolioPanel = new JPanel();
    getPortfolioSaveButton = new JButton("Choose File");
    getPortfolioSaveButton.addActionListener(it -> createNewGetPortfolioSaveFrame());
    loadPortfolioSaveButton = new JButton("Load Save");
    loadPortfolioPanel.add(getPortfolioSaveButton);
    loadPortfolioPanel.add(loadPortfolioSaveButton);
    mainPanel.add(loadPortfolioPanel);

    //Create display save label
    loadPortfolioLabel = new JLabel(" ");
    loadPortfolioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    mainPanel.add(loadPortfolioLabel);

    //Create display results label
    createPortfolioLabel = new JLabel(" ");
    createPortfolioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    mainPanel.add(createPortfolioLabel);

    //Portfolio Dropdown
    JPanel portfolioSelectorPanel = new JPanel();
    portfolioDropdown = new JComboBox<>();
    portfolioSelectorPanel.add(new JLabel("Select Portfolio:"));
    portfolioSelectorPanel.add(portfolioDropdown);
    editPortfolioButton = new JButton("Edit");
    portfolioSelectorPanel.add(editPortfolioButton);
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
    portfolioFrame = new JFrame();
    portfolioLabel = new JLabel();
    portfolioPanel = new JPanel();
    JLabel sharesLabel = new JLabel("Shares: ");
    JTextField sharesText = new JTextField("Enter your text here");


    portfolioFrame.setTitle(portfolioName);
    portfolioFrame.setSize(900, 1300);
    portfolioFrame.setMinimumSize(new Dimension(300, 600));
    portfolioFrame.setLayout(new FlowLayout());

    sharesLabel.setBounds(5, 20, 30, 60);
    sharesLabel.setFont(new Font("MV Boli", Font.PLAIN, 20));

    sharesText.setBounds(5, 10, 30, 60);

    JLabel tickerLabel = new JLabel("Ticker: ");
    tickerLabel.setFont(new Font("MV Boli", Font.PLAIN, 20));

    JTextField tickerText = new JTextField("Enter Here");

    tickerLabel.setBounds(5, 20, 50, 60);
    tickerText.setBounds(5, 10, 50, 60);


    portfolioFrame.add(sharesLabel);
    portfolioFrame.add(sharesText);
    portfolioFrame.add(tickerLabel);
    portfolioFrame.add(tickerText);

    JLabel DateLabel = new JLabel("Date: ");
    JTextField monthText = new JTextField("MM:__");
    JTextField dayText = new JTextField("DD:__");
    JTextField yearText = new JTextField("YYYY:____");


    portfolioLabel = new JLabel();


    portfolioFrame.pack();
    portfolioFrame.setVisible(true);
  }

  @Override
  public void addFeatures(PortfolioStockFeatures features) {
    loadPortfolioSaveButton.addActionListener(it -> features.loadPortfolio(loadPortfolioLabel.getText().replace("Selected: ", "")));
    newPortfolioButton.addActionListener(it -> features.createPortfolio(createPortfolioInput.getText()));
    editPortfolioButton.addActionListener(it -> features.choosePortfolio(
            (String) portfolioDropdown.getSelectedItem()));
  }

  @Override
  public void displayComposition(Map<String, Double> composition) {

  }

  @Override
  public void displayValue(double value) {

  }

  @Override
  public void displayBoughtStock(String ticker, double shares) {

  }

  @Override
  public void displaySoldStock(String ticker, double shares) {

  }

  @Override
  public void displayPortfolios(List<String> names) {
    portfolioDropdown.removeAllItems();
    for (String name: names) {
      portfolioDropdown.addItem(name);
    }
  }

  @Override
  public void displayCreatedSave(String name, String filepath) {

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
    //TODO: Create a new window to display error message (temporarily printing)
    System.out.println(message);
  }

}
