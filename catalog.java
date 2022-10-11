package com.wolverine.catalog;

import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.LinkedList;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class catalog {
    public static String csvFile = null;
    
    public static int recordNumber = 0;
    //Catelog Record Set
    public static int partNumber = 0;
    public static String productDesc = null;
    public static String productCategory = null;
    public static String unitOfMeasure =null;
    public static double productPrice = 0;
    public static double priceAfterDisc = 0;
    public static String priceDiscount = null;
    public static double absDiscount = 0;
    
	//This is optional. It can be used for initializing methods or other tasks. I like having it out of being consistant.
    public catalog()
	{
		//Instantiate
	}
	
	static public void main(String[] args) throws Exception {
		for (int i = 0; i < args.length; i++) {
		      if (i == 0) {
		    	  //Used to pass in the CVS file name in the event they are timestamped or any other variation in naming.
		    	  csvFile = args[i];
		      } else if (i == 1) {
		    	  //This is an optional parameter for specifying a specific product type. Not used in this scenario.
		    	  productCategory = args[i];
		      }
		}
		
		if (!(csvFile == null) && !csvFile.isEmpty() && !csvFile.trim().isEmpty()) {
		    readFile();
	    } else {
	    	System.out.println("There is no CSV file to import. Please supply appropriate CSV file.");    	
	    }
	}
		
	public static void readFile() throws Exception {
		Collection<String> sortedCatalog = new LinkedList<String>();
		
		//Using OpPen Source OpenCSV. It has built in contructs/methods for reading CSV files. Such as skipping the header row.
		try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFile)).withSkipLines(1).build()) {
		      String[] csvRecord;
		      
		      while ((csvRecord = reader.readNext()) != null) {
		    	  /*
		    	     The intent here would be to create an ArrayList/Collection, then sort and process the records in the collection as desired.
		    	     It was intentionally left out. The CSV was processed as-is due to personal family issues and a need to get the project returned. 
		    	  */
		    	  
		    	  recordNumber += 1;
		    	  
		    	  try {
			    	  if (csvRecord[0] == null || csvRecord[0].isEmpty() || csvRecord[0].trim().isEmpty()) {
			    		  csvRecord[0] = "0";	  
			    	  }
			  		
			    	  partNumber =  Integer.parseInt(csvRecord[0]);
			    	  
			    	  if (partNumber != 0 ) {
			    		  productDesc = csvRecord[1];
				    	  productCategory = csvRecord[2];
				    	  unitOfMeasure = csvRecord[3];
				    	  productPrice =Double.parseDouble(csvRecord[4]);
				    	  priceAfterDisc = productPrice;
				    	  
				    	  priceDiscount = csvRecord[5];
				    	  
				    	  if (!(priceDiscount == null) && !priceDiscount.isEmpty() && !priceDiscount.trim().isEmpty()) {
				    		  absDiscount = Integer.parseInt(priceDiscount.replaceAll("[^0-9]", ""));
				    		  
				    		  //Need to take reverse logic to get actual disount to use.
				    		  absDiscount = 100 - absDiscount;
				    	  } else {
				    		  //Let's default to 100 so the subsequent math can always fire.
				    		  priceDiscount = "N/A";
				    		  absDiscount = 100;
				    	  }
				    	 
				    	  priceAfterDisc =  productPrice * (Double.valueOf(absDiscount / 100));
				    	  
				    	  //Round to 2 places
				    	  DecimalFormat df = new DecimalFormat("####0.00");
				    	  String holdValue =  df.format(priceAfterDisc);
				    	  priceAfterDisc = Double.parseDouble(holdValue);
				    	  
				    	  System.out.println("Catalog Item:");
				    	  System.out.println("Record Number: " + recordNumber);
				    	  System.out.println("Part Number: " + partNumber);
				    	  System.out.println("Description:");
				    	  System.out.println(productDesc);
				    	  System.out.println("Category:" + productCategory);
				    	  System.out.println("UOM: " + unitOfMeasure);
				    	  System.out.println("Price: " + productPrice);
				    	  System.out.println("Discount: " + priceDiscount);
				    	  System.out.println("Discounted Price: " + priceAfterDisc);
				    	  System.out.println("\n=====================\n");
				    	  
			    	  }	else {
						  System.out.println("Record Number " + recordNumber + " has a missing part number.");	  
					  }
		    	  }	catch(Exception e) {
					  System.out.println("Record Number " + recordNumber + " has encountered an error with part number [" +  partNumber + "].");	  
				  }
		      }
		  }	catch(Exception e) {
			  System.out.println("\n\nAn error has occurred in proicessing the file.");	  
		  }
	}
}
