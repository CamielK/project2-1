package Library.AI.MLR;

import Jama.Matrix;
import Jama.QRDecomposition;
import Library.AI.MLR.Data.Reader.NothanksReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultipleLinearRegression extends Thread {

    //class variables
    private boolean useIntercept;
    private String[][] detailsArray; //used to save calculation results

    /**
     * Data params:
     *
     * Format of data: bPickedCard, CardNumber, NumChipsOnCard, ScoreDiff, bFitsInSeries, bFitsInSeriesAlmost, NumHighCardsLeft, NumMediumCardsLeft, NumLowCardsLeft, NumOwnedChips
     * numParameters = 10
     */
    private static int numParameters = 4;
    private final int trainingDataRows = 147; // Number of rows in the training set
//    private final int trainingDataRows = 393; // Number of rows in the training set
    private List<Double> MLRdata = new ArrayList<>();

    /**
     * Run a test prediction with predefined game status
     * @param args
     */
    public static void main(String[] args) {
        // Input data for regression
        List<Double> inputData = new ArrayList<>();

        inputData.add(null);                              // bPickedCard [SPECIAL: ### = to be predicted]
        inputData.add(Double.parseDouble("12"));        // CardNumber
//        inputData.add(Double.parseDouble("0"));         // NumChipsOnCard
//        inputData.add(Double.parseDouble("30"));        // ScoreDiff
        inputData.add(Double.parseDouble("0"));         // bFitsInSeries
        inputData.add(Double.parseDouble("1"));         // bFitsInSeriesAlmost
//        inputData.add(Double.parseDouble("6"));         // NumHighCardsLeft
//        inputData.add(Double.parseDouble("6"));         // NumMediumCardsLeft
//        inputData.add(Double.parseDouble("6"));         // NumLowCardsLeft
//        inputData.add(Double.parseDouble("10"));        // NumOwnedChips

        new Library.AI.MLR.MultipleLinearRegression(inputData, true).start();
    }

    //constructor
    public MultipleLinearRegression(List<Double> inputData, boolean useIntercept) {
        this.useIntercept = useIntercept;
        this.MLRdata = inputData;
    }

    // The actual prediction is run in another thread
    // TODO: seperate thread not really needed as game freezes anyway while AI is picking a move
    public void run() {
        float predictedOutput = 0;
        try {

            // debugging: create detailsArray and fill it with initial info
            detailsArray = new String[this.numParameters+7][2];
            detailsArray[0] = new String[]{"Output Variable: ", "bPickedCard"};
            detailsArray[1] = new String[]{"Used predictors: ", Integer.toString(this.numParameters)};
            detailsArray[2] = new String[]{"Using intercept: ", Boolean.toString(useIntercept)};
            detailsArray[3] = new String[]{"Intercept value: ", "Unused"};
            detailsArray[4] = new String[]{"R2 value of model: ","..."};
            detailsArray[5] = new String[]{"",""};
            detailsArray[6] = new String[]{"Beta values: ",""};

            //add intercept to predictorCount
            int usedPredictorCount = numParameters;
            if (useIntercept) { usedPredictorCount++; }

            //create predictorData Matrix and outputData Matrix based on training data
            ResultSet trainingData = getTrainingData();
            double[][] predictorData = new double[trainingDataRows][usedPredictorCount];
            double[][] outputData = new double[trainingDataRows][1];
            int i = 0;

            while (trainingData.next()) {
                if (i < trainingDataRows) {
                    double[] dataRow = new double[usedPredictorCount];
                    int f = 0;
                    int t = 0;

                    //add a 1 as the first record of the row to calculate intercept
                    if (useIntercept) {
                        dataRow[f] = Double.parseDouble("1");
                        f++;
                    }

                    //loop trough MLR data. fill predictorData array with training data if the predictor is used in the MLRdata
                    List<String> dbColNames = getDatabaseColumnNames();
                    for (int x = 0; x < numParameters; x++) {
                        if (MLRdata.get(x) != null) {
                            dataRow[f] = Double.parseDouble(trainingData.getString(dbColNames.get(x)));
                            detailsArray[t+7][0] = dbColNames.get(x);
                            f++; t++;
                        }
                    }

                    //add training data output variable to output array
                    outputData[i] = new double[]{Double.parseDouble(trainingData.getString("bPickedCard"))};

                    predictorData[i] = dataRow;
                    i++;
                }
            }

            //get beta values for all used predictors
            final Matrix beta = getBetas(predictorData, outputData);

            //add betas to details array
            int u = 0;
            int y = 0;
            if (useIntercept) detailsArray[3][1] = Double.toString(beta.get(0,0)); u++;
            while (u < beta.getRowDimension()) {
                detailsArray[y+7][1] = "beta:   "+Double.toString(beta.get(u,0));
                u++; y++;
            }


            //calculate predicted output based on betas
            double predictedOutputDouble = 0;
            int n = 0;
            if (useIntercept) { predictedOutputDouble += beta.get(0,0); n++; }
            while (n < beta.getRowDimension()) {
                double predictor = 0;
                int foundRows = 0; if (useIntercept) {foundRows++;}

                for (int k = 0; k < MLRdata.size(); k++) {
                    if (MLRdata.get(k) != null && foundRows == n ) {
                        predictor = MLRdata.get(k);
                        break;
                    } else if (MLRdata.get(k) != null) {
                        foundRows++;
                    }
                }

                predictedOutputDouble += beta.get(n, 0) * predictor;
                n++;
            }

            //calculate R2 value
            detailsArray[4][1] = Double.toString(getR2(predictorData,outputData,beta));

            //save prediction result
            predictedOutput = (float) predictedOutputDouble;

        } catch (Exception e) {
            System.out.println("Exception while predicting");
            e.printStackTrace();
        }

        // Results
        System.out.println("Predicted bPickedCard: " + predictedOutput);
        System.out.println("MLR model info: ");
        System.out.println(Arrays.deepToString(detailsArray));
    }

    // Calls Jama library to calculate beta values
    private Matrix getBetas(double[][] predictorData, double[][] outputData) throws Exception {
        QRDecomposition qr = new QRDecomposition(new Matrix(predictorData));
        Matrix beta = qr.solve(new Matrix(outputData));

        return beta;
    }

    // Get training data from database
    private ResultSet getTrainingData() {
        ResultSet trainingData = null;

        //query
        try {
            //load driver
            Class.forName("org.relique.jdbc.csv.CsvDriver");

            //configure database connection
            Connection conn = null;

            //get execution path to detect jar execution
            String executionPath = this.getClass().getResource("/Library/AI/MLR/Data/testDatabase.csv").toExternalForm();

            //jar data connection
            if (executionPath.startsWith("jar:")) {
                conn = DriverManager.getConnection("jdbc:relique:csv:class:" + NothanksReader.class.getName());
            }

            //ide data connection
            else if (executionPath.startsWith("file:")) {
                String path = this.getClass().getResource("/Library/AI/MLR/Data").toExternalForm();
                path = path.substring("file:".length());
                conn = DriverManager.getConnection("jdbc:relique:csv:" + path);
            }

            //do the query
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM nothanksTraining");
            trainingData = stmt.executeQuery();

        } catch (Exception e) {
            System.out.println("Exception retrieving training data");
            e.printStackTrace();
        }
        return trainingData;
    }


    //returns a list of all main database column names that are used for the MLR methods
    private List<String> getDatabaseColumnNames() {
        List<String> dbColNames = new ArrayList<>();
        dbColNames.add("bPickedCard");
        dbColNames.add("CardNumber");
//        dbColNames.add("NumChipsOnCard");
//        dbColNames.add("ScoreDiff");
        dbColNames.add("bFitsInSeries");
        dbColNames.add("bFitsInSeriesAlmost");
//        dbColNames.add("NumHighCardsLeft");
//        dbColNames.add("NumMediumCardsLeft");
//        dbColNames.add("NumLowCardsLeft");
//        dbColNames.add("NumOwnedChips");
        return dbColNames;
    }


    //calculates the r2 value of the model and returns it
    private double getR2(double[][] predictorData, double[][] outputData, Matrix betas) {

        // mean of output values (training data)
        double total = 0;
        for (int o = 0; o < trainingDataRows; o++) {
            total += outputData[o][0];
        }
        double mean = total / trainingDataRows;

        // total variation
        double SST = 0;
        for (int o = 0; o < trainingDataRows; o++) {
            double dev = outputData[o][0] - mean;
            SST += dev*dev;
        }

        //calculate R2
        Matrix residuals = new Matrix(predictorData).times(betas).minus(new Matrix(outputData));
        double SSE = residuals.norm2() * residuals.norm2();
        double r2 = 1.0 - (SSE/SST);

        return r2;
    }

}

