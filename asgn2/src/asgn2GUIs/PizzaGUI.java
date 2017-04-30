package asgn2GUIs;

import java.awt.event.ActionEvent;


import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.text.DefaultCaret;

import asgn2Customers.Customer;
import asgn2Exceptions.CustomerException;
import asgn2Exceptions.LogHandlerException;
import asgn2Exceptions.PizzaException;
import asgn2Pizzas.Pizza;
import asgn2Restaurant.LogHandler;
import asgn2Restaurant.PizzaRestaurant;

import java.awt.*;
import javax.swing.*;


/**
 * This class is the graphical user interface for the rest of the system. 
 * Currently it is a summary class which extends JFrame and implements Runnable and ActionLister. 
 * It should contain an instance of an asgn2Restaurant.PizzaRestaurant object which you can use to 
 * interact with the rest of the system. You may choose to implement this class as you like, including changing 
 * its class signature as long as it  maintains its core responsibility of acting as a GUI for the rest of the system. 
 * You can also use this class and asgn2Wizards.PizzaWizard to test your system as a whole
 * 
 * 
 * @author Person A and Person B
 *
 */
public class PizzaGUI extends javax.swing.JFrame implements Runnable, ActionListener {
	
	private static final long serialVersionUID = -7031008862559936404L;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private PizzaRestaurant restaurant;
	//Jfram elements
	private JPanel pnlBtn;
	private JPanel pnlTotals;
	private JLabel labDist;
	private JLabel labProf;
	private JButton btnLoad;
	private JButton btnReset;
	private JButton btnTotals;
	private JTextArea areDisplay;
	
	//temp variable for loading using loghandler instead of pizza restuarant
    private ArrayList<Customer> customerArr = new ArrayList<Customer>();



	
	/**
	 * Creates a new Pizza GUI with the specified title 
	 * @param title - The title for the supertype JFrame
	 */
	public PizzaGUI(String title) {
		super(title);
		createGUI();
	}

	
	@Override
	public void run() {
		// TO DO
	}
	
	private void createGUI() { 
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		areDisplay = displayTextArea();

		pnlBtn = createPanel(Color.LIGHT_GRAY); 
		pnlTotals = createPanel(Color.LIGHT_GRAY);
		
		this.getContentPane().add(pnlBtn,BorderLayout.NORTH);
		this.getContentPane().add(pnlTotals,BorderLayout.SOUTH);
		this.getContentPane().add(areDisplay,BorderLayout.CENTER);

		btnLoad = createButton("Load"); 
		btnReset = createButton("Reset");
		btnTotals = createButton("Totals");
		
		labDist = createLabel("Total Distance:     ");
		labProf = createLabel("Profit:     ");
		
		layoutButtonPanel();
		totalsLabelPanel();
		repaint();
		this.setVisible(true);
	}
	
	/*
	 * Create panel with specified colour
	 */
	private JPanel createPanel(Color c) {
		//Create a JPanel object and store it in a local var
		JPanel temp = new JPanel();
		//set the background colour to that passed in c
		temp.setBackground(c);
		//Return the JPanel object
		return temp;
	}
	
	/*
	 * Create Label
	 */
	private JLabel createLabel(String str){
		JLabel notifyline1 = new JLabel();
        notifyline1.setText(str);
        return notifyline1;
	}
	
	/*
	 * Create Button
	 */
	private JButton createButton(String str) {
		//Create a JButton object and store it in a local var
		JButton tempButton = new JButton();
		//Set the button text to that passed in str
		tempButton.setText(str);
		//Add the frame as an actionListener
		tempButton.addActionListener(this);
		//Return the JButton object
		return tempButton;
	}
	
	/**
	 * Add buttons to button panel
	 * Refactor to call method for creating the grid
	 */
	private void layoutButtonPanel() {
		GridBagLayout layout = new GridBagLayout();
		pnlBtn.setLayout(layout);
		//Lots of layout code here
		//add components to grid
		GridBagConstraints constraints = new GridBagConstraints();
		//Defaults
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 100;
		constraints.weighty = 100;
		addToPanel(pnlBtn, btnLoad,constraints,0,0,1,1);
		addToPanel(pnlBtn, btnReset,constraints,1,0,1,1);
		addToPanel(pnlBtn, btnTotals,constraints,2,0,1,1);
	} 
	
	
	private void totalsLabelPanel(){
		GridBagLayout layout = new GridBagLayout();
		pnlTotals.setLayout(layout);
		//Lots of layout code here
		//add components to grid
		GridBagConstraints constraints = new GridBagConstraints();
		//Defaults
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 100;
		constraints.weighty = 100;
		addToPanel(pnlTotals, labDist,constraints,0,0,1,1);
		addToPanel(pnlTotals, labProf,constraints,1,0,1,1);
	}

	
	/**
	*
	* A convenience method to add a component to given grid bag
	* layout locations. Code due to Cay Horstmann
	*
	* @param c the component to add
	* @param constraints the grid bag constraints to use
	* @param x the x grid position
	* @param y the y grid position
	* @param w the grid width of the component
	* @param h the grid height of the component
	*/
	private void addToPanel(JPanel jp,Component c, GridBagConstraints
		constraints,int x, int y, int w, int h) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		jp.add(c, constraints);
	}



	
	private JTextArea displayTextArea(){

		JTextArea tempDisplay = new JTextArea();
		tempDisplay.setEditable(false);
		tempDisplay.setLineWrap(true);
		tempDisplay.setFont(new Font("Arial",Font.BOLD,24));
		tempDisplay.setBorder(BorderFactory.createEtchedBorder());
		return tempDisplay;
	}	
	
	@Override
	public void actionPerformed(ActionEvent e){
		//Get event source
				Object src = e.getSource();
				//Consider the alternatives - not all active at once.
				if (src==btnLoad) {
					JFileChooser fc = new JFileChooser();
					
					int returnVal = fc.showOpenDialog(this);
					
					if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            
			            
						//restaurant.processLog(file.getPath());
			            
			            //Temporary way of loading from log file
			            try {
							customerArr = LogHandler.populateCustomerDataset(file.getPath());
						} catch (LogHandlerException | CustomerException e1) {
							// TODO Auto-generated catch block	
						}
			            
			            //Display Each Customer as line of text
			            DisplayText();
			            
					}
					
					//Total Distance (need to implement restaurant correctly)
					if (src==labDist) {
//						labDist.setText("Total Distance: " + restaurant.getTotalDeliveryDistance())	;
						//"Wiring Class: Warning",JOptionPane.WARNING_MESSAGE); 

					}
				}		
	}
	
	/*
	 * Display Each Customer as line in text box
	 */
	private void DisplayText(){
		//areDisplay.setText(customerArr.get(0).toString());

		for (int i = 0; i < customerArr.size(); i++){
			areDisplay.append("\n"+customerArr.get(i).getName());
		}

	}
	
	/*
	 * Display Restaurant data in jtable. 
	 */
	private void DisplayDaysData(){
		//create two dimensional string array for restaurant data
		String data[][] = customers2dArr();
		//create column string array for column labels
		String column[] = customerFieldsArr();
		//create jtable
		JTable jt = new JTable(data, column);
		//jt.setBounds(30,40,200,300);
		//make scrollable 
	    JScrollPane sp=new JScrollPane(jt);    
	    //add to jFrame
	    this.getContentPane().add(sp, BorderLayout.CENTER);
	    //this.add(sp);
	}
	
	private String[][] customers2dArr(){
		String data[][] = null;
		
		return data;
	}
	
	private String[] customerFieldsArr(){
		String columns[] = null;
		return columns;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(false);
		SwingUtilities.invokeLater(new PizzaGUI("Pizza GUI"));

	}
}
