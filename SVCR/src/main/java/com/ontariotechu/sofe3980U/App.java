package com.ontariotechu.sofe3980U;


import java.io.FileReader; 
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * Evaluate Single Variable Continuous Regression
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		String filePath="model_1.csv";
		FileReader filereader;
		List<String[]> allData;
		try{
			filereader = new FileReader(filePath); 
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build(); 
			allData = csvReader.readAll();
		}
		catch(Exception e){
			System.out.println( "Error reading the CSV file" );
			return;
		}
		
		int count=0;
		for (String[] row : allData) { 
			float y_true=Float.parseFloat(row[0]);
			float y_predicted=Float.parseFloat(row[1]);
			System.out.print(y_true + "  \t  "+y_predicted); 
			System.out.println(); 
			count++;
			if (count==10){
				break;
			}
		} 

		String[] models = {"model_1.csv","model_2.csv","model_3.csv"};

		float bestMSE = Float.MAX_VALUE;
		float bestMAE = Float.MAX_VALUE;
		float bestMARE = Float.MAX_VALUE;

		String bestMSEModel="";
		String bestMAEModel="";
		String bestMAREModel="";

		for(String model : models){
			try{
				FileReader reader = new FileReader(model);
				CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
				List<String[]> data = csvReader.readAll();

				float mse = 0;
				float mae = 0;
				float mare = 0;
				int n = data.size();

				for(String[] row : data){
					float y_true = Float.parseFloat(row[0]);
					float y_predicted = Float.parseFloat(row[1]);

					float error = y_true - y_predicted;

					mse += error * error;
					mae += Math.abs(error);
					mare += Math.abs(error) / Math.abs(y_true);
				}

				mse = mse / n;
				mae = mae / n;
				mare = mare / n;

				System.out.println("for " + model);
				System.out.println("\tMSE =" + mse);
				System.out.println("\tMAE =" + mae);
				System.out.println("\tMARE =" + mare);

				if(mse < bestMSE){
					bestMSE = mse;
					bestMSEModel = model;
				}

				if(mae < bestMAE){
					bestMAE = mae;
					bestMAEModel = model;
				}

				if(mare < bestMARE){
					bestMARE = mare;
					bestMAREModel = model;
				}

			}
			catch(Exception e){
				System.out.println("Error reading file " + model);
			}
		}

		System.out.println("According to MSE, The best model is " + bestMSEModel);
		System.out.println("According to MAE, The best model is " + bestMAEModel);
		System.out.println("According to MARE, The best model is " + bestMAREModel);

    }
}
