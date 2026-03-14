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
			int y_true=Integer.parseInt(row[0]);
			float y_predicted=Float.parseFloat(row[1]);
			System.out.print(y_true + "  \t  "+y_predicted); 
			System.out.println(); 
			count++;
			if (count==10){
				break;
			}
		} 

		String[] models = {"model_1.csv","model_2.csv","model_3.csv"};

		float bestBCE = Float.MAX_VALUE;
		float bestAccuracy = 0;
		float bestPrecision = 0;
		float bestRecall = 0;
		float bestF1 = 0;
		float bestAUC = 0;

		String bestBCEModel="";
		String bestAccuracyModel="";
		String bestPrecisionModel="";
		String bestRecallModel="";
		String bestF1Model="";
		String bestAUCModel="";

		for(String model : models){

			try{
				FileReader reader = new FileReader(model);
				CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
				List<String[]> data = csvReader.readAll();

				float bce = 0;

				int TP=0;
				int TN=0;
				int FP=0;
				int FN=0;

				int P=0;
				int N=0;

				for(String[] row : data){

					int y_true = Integer.parseInt(row[0]);
					float y_predicted = Float.parseFloat(row[1]);

					float p = Math.min(Math.max(y_predicted,0.000001f),0.999999f);

					bce += -(y_true*Math.log(p) + (1-y_true)*Math.log(1-p));

					int y_hat = (y_predicted>=0.5)?1:0;

					if(y_true==1) P++;
					if(y_true==0) N++;

					if(y_hat==1 && y_true==1) TP++;
					if(y_hat==0 && y_true==0) TN++;
					if(y_hat==1 && y_true==0) FP++;
					if(y_hat==0 && y_true==1) FN++;
				}

				int total = data.size();
				bce = bce/total;

				float accuracy = (float)(TP+TN)/total;
				float precision = (float)TP/(TP+FP);
				float recall = (float)TP/(TP+FN);
				float f1 = (2*precision*recall)/(precision+recall);

				float TPR = recall;
				float TNR = (float)TN/(TN+FP);
				float auc = (TPR+TNR)/2;

				System.out.println("for "+model);
				System.out.println("\tBCE ="+bce);
				System.out.println("\tConfusion matrix");
				System.out.println("\t\t\t\ty=1\t\ty=0");
				System.out.println("\t\t y^=1\t"+TP+"\t"+FP);
				System.out.println("\t\t y^=0\t"+FN+"\t"+TN);
				System.out.println("\tAccuracy ="+accuracy);
				System.out.println("\tPrecision ="+precision);
				System.out.println("\tRecall ="+recall);
				System.out.println("\tf1 score ="+f1);
				System.out.println("\tauc roc ="+auc);

				if(bce < bestBCE){
					bestBCE = bce;
					bestBCEModel = model;
				}

				if(accuracy > bestAccuracy){
					bestAccuracy = accuracy;
					bestAccuracyModel = model;
				}

				if(precision > bestPrecision){
					bestPrecision = precision;
					bestPrecisionModel = model;
				}

				if(recall > bestRecall){
					bestRecall = recall;
					bestRecallModel = model;
				}

				if(f1 > bestF1){
					bestF1 = f1;
					bestF1Model = model;
				}

				if(auc > bestAUC){
					bestAUC = auc;
					bestAUCModel = model;
				}

			}
			catch(Exception e){
				System.out.println("Error reading file "+model);
			}
		}

		System.out.println("According to BCE, The best model is "+bestBCEModel);
		System.out.println("According to Accuracy, The best model is "+bestAccuracyModel);
		System.out.println("According to Precision, The best model is "+bestPrecisionModel);
		System.out.println("According to Recall, The best model is "+bestRecallModel);
		System.out.println("According to F1 score, The best model is "+bestF1Model);
		System.out.println("According to AUC ROC, The best model is "+bestAUCModel);

	}
	
}
