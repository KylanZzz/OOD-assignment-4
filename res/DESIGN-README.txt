***Design***

--Overview--
The Stock Program is designed using the Model-View-Controller (MVC) architecture. This design
separates the application into three independent components which allows for modularity and
extensibility.

--Model--
At the highest level, a stock model performs tasks such as retrieving stock data, calculating
gain/loss, moving averages, and crossovers, which is defined by the PortfolioStockModel interface.

The PortfolioStockModelImpl class implements the PortfolioStockModel interface and provides simple
implementations for these functionalities. It interacts with data sources to fetch stock data and
perform calculations.

The PortfolioStockModelImpl constructor takes in a DataSource (and a filepath to store portfolio
saves), which is our representation of a data streamfor stock data. Currently, there are two data
sources implemented:
- AlphaVantageDataSource: This implementation uses the AlphaVantage API to retrieve real-time and
historical stock data. It requires an API key for access and retrieves data over the internet.
- CSVDataSource: This implementation reads stock data from CSV files stored locally on the user's
computer. The CSV files must be placed in the res/CSVData directory and named according to the
stock ticker symbol (e.g., AAPL.csv for Apple Inc.). There is no example CSV data shown; the user
must download and get this data on their own.

The PortfolioStockModel is also in charge of managing all file portfolio operations. This includes
creating new portfolios, adding stocks to them, performing calculations on them, and also creating/
loading saves. This is done through an external Portfolio class, where composition is used instead
of inheritance.

--Controller--

The controller performs two main tasks: handling user input and orchestrating the flow of
information and logic between the model and view.

The BasicStockController and PortfolioStockController both implement the StockController interface
and have a single function: run(). In addition, PortfolioStockController extends
BasicStockController, which allows the program to retain the original functionalities.
This setup is the starting point of the program and must be called for the program to begin.
It also utilizes the Command design pattern, which defines specific commands that the controller
invokes upon receiving user input. The Command class is an abstract class that defines the apply()
method, which is implemented by various concrete command classes. Each concrete command class (e.g.,
CalculateGain, CalculateAverage, CalculateCrossover, ViewPortfolios) handles a specific user
request. An abstract class was used instead of an interface because there are several helper methods
that all commands can use (mainly for parsing input) that are placed in the parent Command class.
Building on the original structure we had defined, StockPortfolioCommand adds new functionalities as
the program calls on PortfolioStockController. For all new features, they all extend
StockPortfolioCommand. As StockPortfolioCommand plays a similar role to PortfolioCommand,
they both extend the abstract Command class.

Several commands also act as "menus," which continue looping until a specific user input is
detected ("0" in this case).

The FeaturesStockController is a new controller that is compatible with both TUI and GUI views. It
implements a "Features" interface (PortfolioStockFeatures) that has several functions for handling
user input that can be passed down from the view. This is the controller that we used for linking
our existing PortfolioStockModel with our new GUI view. Since it implements the 'Features' interface,
it works with any view that implements a function in the view named "addFeatures" which links the
controller with user input from the view.

We decided to not use this controller with our old views because it would result in a higher coupling
between the view and controller (Since the view has to pass the input to the controller now), thus,
we opted for TWO separate controllers for our TUI and GUI. Also, this would require us to change our
existing views because they would also need to implement the 'addFeatures' method.


--View--
The view is responsible for displaying data to the user.

The BasicStockView implements the StockView interface and displays data by printing text to
the terminal. It simply receives input from the controller and then displays that information in a
visually appealing format.

To introduce new text-based features to the user, BasicPortfolioView extends BasicStockView
and implements the PortfolioStockView to present new information to users as BasicStockView did.

With GUI, we made a new interface called FeaturesStockView, which doesn't extend to any other
interface. SimpleFeaturesStockView implements this new interface. This interface essentially changes
the first point of contact with user input from the controller to the view. It has a new method called
"addFeatures" which 'links' the controller with the view. In this class, we made the view more
interactive so that the user can manage it with buttons and text fields.

--Testing--
The Model was tested using a combination of unit tests and mock data sources. We implemented a
MockDataSource to real data fetches, which allowed us to directly test whether our model calculations
were correct or not. It also allowed us to test how our model handled scenarios where a data
fetching error occurs (IE: If a file is missing, there is a network error, or API limit).
Additionally, we also unit tested each data source we implemented, including the CSVDataSource and
AlphaVantage API DataSource. These tests ensured that each data source correctly retrieved and
processed stock data. For the CSVDataSource, we verified that it could correctly parse CSV files
and return the correct information. For the AlphaVantage API Data Source, we checked that calls to
the API worked properly, and correct errors were thrown in the case that the API did not work. The
functions for portfolios such as adding/selling stock, rebalancing, saving, and loading were all
tested through the portfolio test class. This isolates the functionality of a portfolio transactions
which ensures encapsulation.

The Controller was unit tested by mocking both the model and view simultaneously.
MockModel simulates the behavior of the StockModel interface and logs method calls, which allows
us to verify interactions without needing actual stock data. MockView simulates the StockView
interface and logs messages sent to the view. These mocks help isolate the controller's behavior,
making sure that it handles user input properly, relays information from the model to the view
correctly, and handles exceptions from the model accordingly.

The View was tested straightforwardly by measuring it's actual output with the expected output.

Finally, we wrote an integration test for our Stock Program at
test/stock/controller/BasicStockControllerIntegrationTest
This test simulates user interactions with the stock program, including user inputs and navigation
through the application. It tests the entire MVC architecture, making real API calls and generating
actual output, ensuring there the user experience is smooth.












