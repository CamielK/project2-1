package Library.AI.MLR;

import Jama.Matrix;
import Jama.QRDecomposition;
import Library.AI.MLR.Data.Reader.DbReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnotherTest extends Thread {

    //class variables
    private final int trainingDataRows = 3693; //rows in mainDatabase.csv
    private boolean useIntercept;
    private String outputVar; //the variable to be predicted. can be either 'rating' or 'revenue'
    private String[][] detailsArray; //used to save calculation results

    //input data set
    //format: (releaseYear, runtime, mpaaRating, budget, genre, director, writer, cast, language, country) all are strings
    private List<String> inputData = new ArrayList<>();
    private List<Float> inputDataFloat = new ArrayList<>(); //avgDirectorRating, avgWriterRating, avgCastRating


    //MLR data set (only numerical variables)
    //format: YearSinceRelease,Runtime,AvgDirectorRating,AvgWriterRating,AvgCastRating,imdbRating,Budget,Revenue,budget_norm_E8,revenue_norm_E8,mpaaRating_G,mpaaRating_NC-17,mpaaRating_PG,mpaaRating_PG-13,mpaaRating_R,mpaaRating_UNRATED,Genre_Action,Genre_Adventure,Genre_Animation,Genre_Comedy,Genre_Biography,Genre_Crime,Genre_Drama,Genre_Documentary,Genre_Family,Genre_Fantasy,Genre_History,Genre_Horror,Genre_Mystery,Genre_Romance,Genre_Sci-Fi,Genre_Thriller,Genre_Western,Genre_Sport,Genre_Music,Genre_Musical,Genre_War,Language_English,Language_Spanish,Language_European,Language_Asian,Language_Arabic,Language_Other,Countrys_English,Countrys_Spanish,Countrys_European,Countrys_Asian,Countrys_Arabic,Countrys_Other
    //47 columns in total. initiated at null
    private List<Double> MLRdata = new ArrayList<>();

    public static void main(String[] args) {
        //add input data to a list. if input is disabled "Unused predictor" is added to this list. format: (releaseYear, runtime, mpaaRating, budget, genre, director, writer, cast, language, country)
        List<String> inputData = new ArrayList<>();
        List<Float> inputDataFloat = new ArrayList<>();

        inputData.add("2005"); // release year
        inputData.add("120"); // runtime
        inputData.add("Unused predictor"); // mpaa rating
        inputData.add("10000000"); // budget
        inputData.add("comedy, action"); // genre
        inputData.add("Unused predictor"); // director (avg rating of directors)
        inputData.add("Unused predictor"); // writer (avg rating of writers)
        inputData.add("Unused predictor"); // cast (avg rating of cast)
//        inputData.add("7.0"); // cast (avg rating of cast)
//        inputDataFloat.add((float)7.0);
        inputData.add("Unused predictor"); // language
        inputData.add("Unused predictor"); // country

        new AnotherTest(inputData, inputDataFloat, true, "rating").start();
//        new AnotherTest(inputData, inputDataFloat, true, "revenue");
    }

    //constructor
    public AnotherTest(List<String> inputData, List<Float> inputDataFloat, boolean useIntercept, String outputVar) {
        this.inputData = inputData;
        this.inputDataFloat = inputDataFloat;
        this.useIntercept = useIntercept;
        this.outputVar = outputVar;

        //init MLRdata list with 47 null columns
        for (int i = 0; i < 47; i++) {
            MLRdata.add(null);
        }
    }

    //main method for this thread. calculates prediction for output variable
    public void run() {
        float predictedOutput = 0;
        try {

            //put input data into MLRdata array at correct index and creating dummys
            generateMlrData();

            //count used predictor variables-
            int usedPredictorCount = 0;
            for (int i = 0; i < 47; i++) {
                if (MLRdata.get(i) != null) {
                    usedPredictorCount++;
                }
            }

            //create detailsArray and fill it with initial info
            detailsArray = new String[usedPredictorCount+7][2];
            detailsArray[0] = new String[]{"Output Variable: ", outputVar};
            detailsArray[1] = new String[]{"Used predictors: ", Integer.toString(usedPredictorCount)};
            detailsArray[2] = new String[]{"Using intercept: ", Boolean.toString(useIntercept)};
            detailsArray[3] = new String[]{"Intercept value: ","Unused"};
            detailsArray[4] = new String[]{"R2 value of model: ","..."};
            detailsArray[5] = new String[]{"",""};
            detailsArray[6] = new String[]{"Beta values: ",""};

            //add intercept to predictorCount
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
                    for (int x = 0; x < 47; x++) {
                        if (MLRdata.get(x) != null) {
                            dataRow[f] = Double.parseDouble(trainingData.getString(dbColNames.get(x)));
                            detailsArray[t+7][0] = dbColNames.get(x);
                            f++; t++;
                        }
                    }

                    //add training data output variable to output array
                    if (outputVar.equals("rating")) {
                        outputData[i] = new double[]{Double.parseDouble(trainingData.getString("imdbRating"))};
                    } else if (outputVar.equals("revenue")) {
                        outputData[i] = new double[]{Double.parseDouble(trainingData.getString("revenue_norm_E8"))};
                    }

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

        //send result to GUI
        System.out.println("Predicted " + outputVar + ": " + predictedOutput);
        System.out.println("MLR model info: ");
        System.out.println(Arrays.deepToString(detailsArray));
//        if (outputVar.equals("rating")) { guiController.setPredictedRating(predictedOutput); guiController.setDetailsArray(detailsArray, "rating"); }
//        else if (outputVar.equals("revenue")) { guiController.setPredictedRevenue(predictedOutput); guiController.setDetailsArray(detailsArray, "revenue"); }
    }


    //generate MLR data list including dummy variables. based on input data list
    //input list format: (releaseYear, runtime, mpaaRating, budget, genre, director, writer, cast, language, country) all are strings
    private void generateMlrData() {
        //imdbRating and revenue default to null because they are not part of input
        MLRdata.set(5,null);
        MLRdata.set(7,null);

        //check years since release
        if (!inputData.get(0).equals("Unused predictor")) {
            double yearsSinceRelease = 2016 - Integer.parseInt(inputData.get(0));
            if (yearsSinceRelease < 0) {yearsSinceRelease=0;}
            MLRdata.set(0,yearsSinceRelease);
        } else {MLRdata.set(0,null);}

        //check runtime
        if (!inputData.get(1).equals("Unused predictor")) {
            double runtime = Integer.parseInt(inputData.get(1));
            MLRdata.set(1,runtime);
        } else {MLRdata.set(1,null);}

        //check mpaa rating
        if (!inputData.get(2).contains("Unused predictor")) {
            String mpaaRating = inputData.get(2);
            if (mpaaRating.contains("G (all ages)")) {MLRdata.set(8,Double.parseDouble("1"));} else {{MLRdata.set(8,Double.parseDouble("0"));}}
            if (mpaaRating.contains("PG (parental guidance advised)")) {MLRdata.set(10,Double.parseDouble("1"));} else {{MLRdata.set(10,Double.parseDouble("0"));}}
            if (mpaaRating.contains("PG-13 (13+)")) {MLRdata.set(11,Double.parseDouble("1"));} else {{MLRdata.set(11,Double.parseDouble("0"));}}
            if (mpaaRating.contains("NC-17 (17+)")) {MLRdata.set(9,Double.parseDouble("1"));} else {{MLRdata.set(9,Double.parseDouble("0"));}}
            if (mpaaRating.contains("R (mature audiences)")) {MLRdata.set(12,Double.parseDouble("1"));} else {{MLRdata.set(12,Double.parseDouble("0"));}}
            if (mpaaRating.contains("UNRATED (no rating)")) {MLRdata.set(13,Double.parseDouble("1"));} else {{MLRdata.set(13,Double.parseDouble("0"));}}
        } else {MLRdata.set(8,null);MLRdata.set(9,null);MLRdata.set(10,null);MLRdata.set(11,null);MLRdata.set(12,null);MLRdata.set(13,null);}

        //check budget (normalized)
        if (!inputData.get(3).equals("Unused predictor")) {
            double budget = Double.parseDouble(inputData.get(3));
            double budget_norm_E8 = budget / 100000000;
            MLRdata.set(6,budget_norm_E8);
        } else {MLRdata.set(6,null);}

        //check genre
        if (!inputData.get(4).contains("Unused predictor")) {
            String genreField = inputData.get(4);
            if (genreField.contains("Action")) {MLRdata.set(14,Double.parseDouble("1"));} else {{MLRdata.set(14,Double.parseDouble("0"));}}
            if (genreField.contains("Adventure")) {MLRdata.set(15,Double.parseDouble("1"));} else {{MLRdata.set(15,Double.parseDouble("0"));}}
            if (genreField.contains("Animation")) {MLRdata.set(16,Double.parseDouble("1"));} else {{MLRdata.set(16,Double.parseDouble("0"));}}
            if (genreField.contains("Comedy")) {MLRdata.set(17,Double.parseDouble("1"));} else {{MLRdata.set(17,Double.parseDouble("0"));}}
            if (genreField.contains("Biography")) {MLRdata.set(18,Double.parseDouble("1"));} else {{MLRdata.set(18,Double.parseDouble("0"));}}
            if (genreField.contains("Crime")) {MLRdata.set(19,Double.parseDouble("1"));} else {{MLRdata.set(19,Double.parseDouble("0"));}}
            if (genreField.contains("Drama")) {MLRdata.set(20,Double.parseDouble("1"));} else {{MLRdata.set(20,Double.parseDouble("0"));}}
            if (genreField.contains("Documentary")) {MLRdata.set(21,Double.parseDouble("1"));} else {{MLRdata.set(21,Double.parseDouble("0"));}}
            if (genreField.contains("Family")) {MLRdata.set(22,Double.parseDouble("1"));} else {{MLRdata.set(22,Double.parseDouble("0"));}}
            if (genreField.contains("Fantasy")) {MLRdata.set(23,Double.parseDouble("1"));} else {{MLRdata.set(23,Double.parseDouble("0"));}}
            if (genreField.contains("History")) {MLRdata.set(24,Double.parseDouble("1"));} else {{MLRdata.set(24,Double.parseDouble("0"));}}
            if (genreField.contains("Horror")) {MLRdata.set(25,Double.parseDouble("1"));} else {{MLRdata.set(25,Double.parseDouble("0"));}}
            if (genreField.contains("Mystery")) {MLRdata.set(26,Double.parseDouble("1"));} else {{MLRdata.set(26,Double.parseDouble("0"));}}
            if (genreField.contains("Romance")) {MLRdata.set(27,Double.parseDouble("1"));} else {{MLRdata.set(27,Double.parseDouble("0"));}}
            if (genreField.contains("Sci-Fi")) {MLRdata.set(28,Double.parseDouble("1"));} else {{MLRdata.set(28,Double.parseDouble("0"));}}
            if (genreField.contains("Thriller")) {MLRdata.set(29,Double.parseDouble("1"));} else {{MLRdata.set(29,Double.parseDouble("0"));}}
            if (genreField.contains("Western")) {MLRdata.set(30,Double.parseDouble("1"));} else {{MLRdata.set(30,Double.parseDouble("0"));}}
            if (genreField.contains("Sport")) {MLRdata.set(31,Double.parseDouble("1"));} else {{MLRdata.set(31,Double.parseDouble("0"));}}
            if (genreField.contains("Music")) {MLRdata.set(32,Double.parseDouble("1"));} else {{MLRdata.set(32,Double.parseDouble("0"));}}
            if (genreField.contains("Musical")) {MLRdata.set(33,Double.parseDouble("1"));} else {{MLRdata.set(33,Double.parseDouble("0"));}}
            if (genreField.contains("War")) {MLRdata.set(34,Double.parseDouble("1"));} else {{MLRdata.set(34,Double.parseDouble("0"));}}
        } else {MLRdata.set(14,null);MLRdata.set(15,null);MLRdata.set(16,null);MLRdata.set(17,null);MLRdata.set(18,null);MLRdata.set(19,null);
            MLRdata.set(20,null);MLRdata.set(21,null);MLRdata.set(22,null);MLRdata.set(23,null);MLRdata.set(24,null);MLRdata.set(25,null);
            MLRdata.set(26,null);MLRdata.set(27,null);MLRdata.set(28,null);MLRdata.set(29,null);MLRdata.set(30,null);MLRdata.set(31,null);
            MLRdata.set(32,null);MLRdata.set(33,null);MLRdata.set(34,null);}

        //check director
        if (!inputData.get(5).equals("Unused predictor")) {
            double averageDirectorRating = inputDataFloat.get(0);
            MLRdata.set(2,averageDirectorRating);
        } else {MLRdata.set(2,null);}

        //check writer
        if (!inputData.get(6).equals("Unused predictor")) {
            double averageWriterRating = inputDataFloat.get(1);
            MLRdata.set(3,averageWriterRating);
        } else {MLRdata.set(3,null);}

        //check cast
        if (!inputData.get(7).equals("Unused predictor")) {
            double averageCastRating = inputDataFloat.get(2);
            MLRdata.set(4,averageCastRating);
        } else {MLRdata.set(4,null);}

        //check language
        if (!inputData.get(8).contains("Unused predictor")) {
            String languages = inputData.get(8);
            if (languages.contains("English")) {MLRdata.set(35,Double.parseDouble("1"));} else {{MLRdata.set(35,Double.parseDouble("0"));}}
            if (languages.contains("Spanish")) {MLRdata.set(36,Double.parseDouble("1"));} else {{MLRdata.set(36,Double.parseDouble("0"));}}
            if (languages.contains("European")) {MLRdata.set(37,Double.parseDouble("1"));} else {{MLRdata.set(37,Double.parseDouble("0"));}}
            if (languages.contains("Asian")) {MLRdata.set(38,Double.parseDouble("1"));} else {{MLRdata.set(38,Double.parseDouble("0"));}}
            if (languages.contains("Arabic")) {MLRdata.set(39,Double.parseDouble("1"));} else {{MLRdata.set(39,Double.parseDouble("0"));}}
            if (languages.contains("Other")) {MLRdata.set(40,Double.parseDouble("1"));} else {{MLRdata.set(40,Double.parseDouble("0"));}}
        } else {MLRdata.set(35,null);MLRdata.set(36,null);MLRdata.set(37,null);MLRdata.set(38,null);MLRdata.set(39,null);MLRdata.set(40,null);}

        //check country
        if (!inputData.get(9).contains("Unused predictor")) {
            String languages = inputData.get(9);
            if (languages.contains("English")) {MLRdata.set(41,Double.parseDouble("1"));} else {{MLRdata.set(41,Double.parseDouble("0"));}}
            if (languages.contains("Spanish")) {MLRdata.set(42,Double.parseDouble("1"));} else {{MLRdata.set(42,Double.parseDouble("0"));}}
            if (languages.contains("European")) {MLRdata.set(43,Double.parseDouble("1"));} else {{MLRdata.set(43,Double.parseDouble("0"));}}
            if (languages.contains("Asian")) {MLRdata.set(44,Double.parseDouble("1"));} else {{MLRdata.set(44,Double.parseDouble("0"));}}
            if (languages.contains("Arabic")) {MLRdata.set(45,Double.parseDouble("1"));} else {{MLRdata.set(45,Double.parseDouble("0"));}}
            if (languages.contains("Other")) {MLRdata.set(46,Double.parseDouble("1"));} else {{MLRdata.set(46,Double.parseDouble("0"));}}
        } else {MLRdata.set(41,null);MLRdata.set(42,null);MLRdata.set(43,null);MLRdata.set(44,null);MLRdata.set(45,null);MLRdata.set(46,null);}


    }



    //calls Jama library to calculate beta values
    private Matrix getBetas(double[][] predictorData, double[][] outputData) throws Exception {
        QRDecomposition qr = new QRDecomposition(new Matrix(predictorData));
        Matrix beta = qr.solve(new Matrix(outputData));

        return beta;
    }





    //get all records from the mainDatabase
    private ResultSet getTrainingData() {
        ResultSet trainingData = null;

        //query
        try {
            //load driver
            Class.forName("org.relique.jdbc.csv.CsvDriver");

            //configure database connection
            Connection conn = null;

            //get execution path to detect jar execution
            String executionPath = this.getClass().getResource("/Library/AI/MLR/Data/mainDatabase.csv").toExternalForm();

            //jar data connection
            if (executionPath.startsWith("jar:")) {
                conn = DriverManager.getConnection("jdbc:relique:csv:class:" + DbReader.class.getName());
            }

            //ide data connection
            else if (executionPath.startsWith("file:")) {
                String path = this.getClass().getResource("/Library/AI/MLR/Data").toExternalForm();
                path = path.substring("file:".length());
                conn = DriverManager.getConnection("jdbc:relique:csv:" + path);
            }

            //do the query
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM mainDatabase");
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
        dbColNames.add("YearSinceRelease"); dbColNames.add("Runtime"); dbColNames.add("AvgDirectorRating"); dbColNames.add("AvgWriterRating"); dbColNames.add("AvgCastRating");
        dbColNames.add("imdbRating"); dbColNames.add("budget_norm_E8"); dbColNames.add("revenue_norm_E8"); dbColNames.add("mpaaRating_G"); dbColNames.add("mpaaRating_NC-17");
        dbColNames.add("mpaaRating_PG"); dbColNames.add("mpaaRating_PG-13"); dbColNames.add("mpaaRating_R"); dbColNames.add("mpaaRating_UNRATED"); dbColNames.add("Genre_Action");
        dbColNames.add("Genre_Adventure"); dbColNames.add("Genre_Animation"); dbColNames.add("Genre_Comedy"); dbColNames.add("Genre_Biography"); dbColNames.add("Genre_Crime");
        dbColNames.add("Genre_Drama"); dbColNames.add("Genre_Documentary"); dbColNames.add("Genre_Family"); dbColNames.add("Genre_Fantasy"); dbColNames.add("Genre_History");
        dbColNames.add("Genre_Horror"); dbColNames.add("Genre_Mystery"); dbColNames.add("Genre_Romance"); dbColNames.add("Genre_Sci-Fi"); dbColNames.add("Genre_Thriller");
        dbColNames.add("Genre_Western"); dbColNames.add("Genre_Sport"); dbColNames.add("Genre_Music"); dbColNames.add("Genre_Musical"); dbColNames.add("Genre_War");
        dbColNames.add("Language_English"); dbColNames.add("Language_Spanish"); dbColNames.add("Language_European"); dbColNames.add("Language_Asian"); dbColNames.add("Language_Arabic");
        dbColNames.add("Language_Other"); dbColNames.add("Countrys_English"); dbColNames.add("Countrys_Spanish"); dbColNames.add("Countrys_European"); dbColNames.add("Countrys_Asian");
        dbColNames.add("Countrys_Arabic"); dbColNames.add("Countrys_Other");
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
