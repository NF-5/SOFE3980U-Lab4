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
		String filePath="model.csv";
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
		float[] y_predicted=new float[5];
		for (String[] row : allData) { 
			int y_true=Integer.parseInt(row[0]);
			System.out.print(y_true);
			for(int i=0;i<5;i++){
				y_predicted[i]=Float.parseFloat(row[i+1]);
				System.out.print("  \t  "+y_predicted[i]); 
			}
			System.out.println(); 
			count++;
			if (count==10){
				break;
			}
		} 

		float CE = 0;
		int[][] confusion = new int[5][5];
		int total = allData.size();

		for(String[] row : allData){

			int y_true = Integer.parseInt(row[0]);  // class 1..5
			float[] probs = new float[5];

			for(int i=0;i<5;i++){
				probs[i] = Float.parseFloat(row[i+1]);
			}

			// Cross Entropy calculation
			float p = probs[y_true-1];
			if(p <= 0) p = 0.000001f;
			CE += -Math.log(p);

			// predicted class (argmax)
			int y_hat = 0;
			float max = probs[0];
			for(int i=1;i<5;i++){
				if(probs[i] > max){
					max = probs[i];
					y_hat = i;
				}
			}

			confusion[y_hat][y_true-1]++;

		}

		CE = CE / total;

		System.out.println("CE =" + CE);
		System.out.println("Confusion matrix");
		System.out.println("\t\ty=1\t\ty=2\t\ty=3\t\ty=4\t\ty=5");

		for(int i=0;i<5;i++){
			System.out.print("\ty^="+(i+1));
			for(int j=0;j<5;j++){
				System.out.print("\t"+confusion[i][j]);
			}
			System.out.println();
		}

	}
	
}
