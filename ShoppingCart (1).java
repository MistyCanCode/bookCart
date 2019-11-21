import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import java.util.Scanner;


/**
 * Chapter 12
 * Programming Challenge 5: Shopping Cart System
 */

public class ShoppingCart extends JFrame
{
   private String[] bookTitles ;     // To hold the book titles
   private String[] cartTitles;     // Titles in the shopping cart
   private double[] prices;         // To hold the book prices

   private JLabel banner;           // To display a banner
   private JPanel bannerPanel;      // To hold the banner
   private JPanel bookPanel;        // To hold the book list
   private JPanel cartPanel;        // To hold the shopping cart
   private JPanel buttonPanel;      // To hold the buttons

   private JList bookList;          // To show a list of books
   private JList cartList;          // Shopping cart list

   private JButton addButton;       // To add an item to the cart
   private JButton removeButton;    // To remove an item from the cart
   private JButton checkOutButton;  // To check out

   private double subtotal = 0.0;   // Selection subtotal
   private double tax = 0.0;        // Sales tax
   private double total = 0.0;      // Sale total

   // Constants
   private final int LIST_ROWS = 5; // Number of rows to display in lists
   private final double TAX_RATE = 0.06;  // Sales tax rate
   private final int WINDOW_WIDTH = 500;
   private final int WINDOW_HEIGHT= 1000;

   public ShoppingCart() throws IOException
   {
      // Display a title.
      super("Book Store Shopping Cart");

        Toolkit tk  = Toolkit.getDefaultToolkit();
            Dimension d = tk.getScreenSize();
            int width   = 300;
            int height  = 500;
            setLocation((d.width - width)/2, (d.height - height)/2);
            setSize(WINDOW_WIDTH, WINDOW_HEIGHT);


      // Specify what happens when the close button is clicked.
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // Read the book titles and prices from the file.
      readBookFile();

      // Create the banner on a panel and add it to the North region.
      buildBannerPanel();
      add(bannerPanel, BorderLayout.NORTH);

      // Create a list component to show the books and add it
      // to the West region.
      buildBookPanel();
      add(bookPanel, BorderLayout.WEST);

      // Create a list component for the shopping cart and
      // add it to the East region.
      buildCartPanel();
      add(cartPanel, BorderLayout.EAST);

      // Build the button panel and add it to the center region.
      buildButtonPanel();
      add(buttonPanel, BorderLayout.CENTER);

      // Pack and display the window.
      pack();
      setVisible(true);
   }

   /**
    * readBookFile method
    * call the countBooks method to get a count for creating  the arrays, bookTitles and prices
    * a StringTokenizer  object is set up to tokenize the input string  read from the file . The text line  is set up as title, price
    */

    private void readBookFile() throws IOException
    {
		int count = countBooks();
		bookTitles = new String[count];
		prices = new double[count];
		File file = new File("BookPrices.txt");
		Scanner inputFile = new Scanner(file);
		String line;
		int i = 0;

		//Read lines from file until no more are left
		while(inputFile.hasNext())
		{
			line =  inputFile.nextLine();
			StringTokenizer strTokenizer = new StringTokenizer(line, ",");

			bookTitles[i] = strTokenizer.nextToken();
			prices[i] = Double.parseDouble(strTokenizer.nextToken());
			i++;
		}


		inputFile.close();

	}

   /**
    * countBooks method
    * This method returns the number of books in the file.
    */

   private int countBooks() throws IOException
   {
      int count = 0;

      // Open the file.
      FileReader fr = new FileReader("BookPrices.txt");
      BufferedReader inFile = new BufferedReader(fr);

      while (inFile.readLine() != null)
         count++;

      inFile.close();
      return count;
   }

   /**
    * buildBannerPanel method
    */
	private void buildBannerPanel()
	{
		//create panel
		bannerPanel = new JPanel();
		//set border layout
		bannerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		//labbel welcome text
		String labelText = ("Welcome to the Bookstore!");
		JLabel banner = new JLabel(labelText);
		banner.setFont(new Font("Serif", Font.BOLD + Font.ITALIC, 26));

		bannerPanel.add(banner);
	}


   /**
    * buildBookPanel method
    */

   private void buildBookPanel()
   {
      JLabel bookMsg = new JLabel("Available Books");
      bookPanel = new JPanel();
      bookPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      bookList = new JList(bookTitles);
      bookList.setVisibleRowCount(LIST_ROWS);
      JScrollPane bookScrollPane = new JScrollPane(bookList);
      bookPanel.setLayout(new BorderLayout());
      bookPanel.add(bookMsg, BorderLayout.NORTH);
      bookPanel.add(bookScrollPane, BorderLayout.CENTER);
   }

   /**
    * buildCartPanel method
    */

	private void buildCartPanel()
	{
		//holds shoping cart
		//create panel
		cartPanel = new JPanel();
		//setBorderLayout
		cartPanel.setLayout(new BorderLayout());
		//create List
		cartList = new JList();
		//sreate shopping cart message
		JLabel cartMsg = new JLabel("Shopping Cart");
		cartPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		JScrollPane cartScrollPane = new JScrollPane(cartList);
		//make visible
		cartList.setVisibleRowCount(LIST_ROWS);
		cartPanel.add(cartMsg, BorderLayout.NORTH);
		cartPanel.add(cartScrollPane, BorderLayout.CENTER);

	}

   /**
    * buildButtonPanel method
    */

   private void buildButtonPanel()
   {
      // Create a button to add items to the shopping cart.
      addButton = new JButton("Add Selected Item");

      // Add an action listener to the button.
      addButton.addActionListener(new AddButtonListener());

      // Create a button to remove an item from the shopping cart.
      removeButton = new JButton("Remove Selected Item");

      // Add an action listener to the button.
      removeButton.addActionListener(new RemoveButtonListener());

      // Create a button to check out.
      checkOutButton = new JButton("Check Out");

      // Add an action listener to the button.
      checkOutButton.addActionListener(new CheckOutButtonListener());

      // Put the buttons in their own panel.
      buttonPanel = new JPanel();
      buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
      buttonPanel.setLayout(new GridLayout(3, 1));
      buttonPanel.add(addButton);
      buttonPanel.add(removeButton);
      buttonPanel.add(checkOutButton);
   }

   /**
    * addToCart method
    */

   private void addToCart(String str)
   {
      String[] temp;

      if (cartTitles == null)
         temp = new String[1];
      else
      {
         // Make a copy of the cartTitles array.
         temp = new String[cartTitles.length + 1];
         for (int i = 0; i < cartTitles.length; i++)
            temp[i] = cartTitles[i];
      }

      // Add the argument to the end of the temp array.
      temp[temp.length - 1] = str;

      // Replace the cartTitles array with the temp array.
      cartTitles = temp;

      // Update the cartList component.
      cartList.setListData(cartTitles);

      // Find the book's price and update the subtotal.
      boolean found = false;
      int index = 0;
      while (!found && index < bookTitles.length)
      {
         if (str.equals(bookTitles[index]))
         {
            subtotal += prices[index];
            found = true;
         }
         index++;
      }
   }


   /**
    * removeFromCart method
    */
	private void removeFromCart(int index, String str)
	{
		String[] temp2 = null;
		/*if(cartTitles != null)
		{
			temp2 = new String[1];
		}
		else*/
		if (cartTitles != null && cartTitles.length > 0)
		{
			//make copy of cartTitles
			temp2 = new String[cartTitles.length - 1];
			for(int i = 0, c = 0; i < temp2.length; c++)
			{
				if( c != index)
				{
					temp2[i] = cartTitles[c];
					i++;
				}
			}
		}
		//temp2[temp2.length -1] = str;
		//replace the cartTitles array with temp
		cartTitles = temp2;
		//update the cartList
		cartList.setListData(cartTitles);
		boolean found = false;
		int i = 0;
		while(!found && i < bookTitles.length)
		{
			if(str.equals(bookTitles[i]))
			{
			subtotal -= prices[i];
			found = true;
			}
			i++;
		}
	}

   /**
    * AddButtonListener is an action listener class for the
    * addButton component.
    */

   private class AddButtonListener implements ActionListener
   {
      /**
       * actionPerformed method
       */

      public void actionPerformed(ActionEvent e)
      {
         if (bookList.getSelectedValue() != null)
         {
            String selected = (String) bookList.getSelectedValue();
            addToCart(selected);
         }
      }
   } // End of inner class

   /**
    * RemoveButtonListener is an action listener class for the
    * removeButton component.
    */

   private class RemoveButtonListener implements ActionListener
   {
      /**
       * actionPerformed method
       */

      public void actionPerformed(ActionEvent e)
      {
         int selectedIndex = cartList.getSelectedIndex();
         String str = (String) cartList.getSelectedValue();
         if (selectedIndex > -1)
         {
            removeFromCart(selectedIndex, str);
         }
      }
   } // End of inner class

   /**
    * CheckOutButtonListener is an action listener class for the
    * checkOutButton component.
    */
	private class CheckOutButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{

			tax = subtotal * TAX_RATE;
			total = subtotal + tax;

			//decimal format object
			DecimalFormat dollar = new DecimalFormat("##0.00");

			//display total
			JOptionPane.showMessageDialog(null, "Subtotal: $" +
										dollar.format(subtotal) + "\n" +
										"Tax: $" + dollar.format(tax) + "\n" +
										"Total: $" + dollar.format(total));
		}
	}


   /**
    * The main method instantiates the ShoppingCart class.
    */

   public static void main(String[] args) throws IOException
   {
      ShoppingCart sc = new ShoppingCart();
   }
}
