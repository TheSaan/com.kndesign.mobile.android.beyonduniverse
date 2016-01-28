package com.thesaan.gameengine.android.handler;

import android.support.annotation.Nullable;

import java.math.*;


public class MathHandler {
    /**
     *
     */
    final static int MULTIPLICATION = 1;
    /**
     *
     */
    final static int DIVISION = 2;
    /**
     *
     */
    final static int SUBTRACTION = 3;
    /**
     *
     */
    final static int ADDITION = 4;
    /**
     *
     */
    final static int FRAC = 4;

    /**
     * float null comparing precision
     */
    public static float precision = 0.00000001f;
    /**
     *
     */
    public final static int FACTORIAL_ARGUMENT_ZERO = -1;
    /**
     *
     */
    public final static int FACTORIAL_ARGUMENT_TOO_BIG = -2;
    /**
     *
     */
    public final static int FACTORIAL_REPETITION_MAX = 20;
    /**
     *
     */
    public final static int ALTERNATIVE_DOUBLE_SET = -3;
    /**
     *
     */
    public final static int ALTERNATIVE_LONG_SET = -4;

    /**
     * identifiers to get a decimal value
     */
    private final static int BIG_DECIMAL_REQUIRED = -5;


    /**
     * alternative Values if a BigInteger is set instead of long
     */

    private BigInteger alternativeLong;
    /**
     * alternative Values if a BigDecimal is set instead of double
     */
    private BigDecimal alternativeDouble;

    /**
     * Proofs if a float is really zero.
     *
     * @param f
     * @return
     */
    public static boolean isFloatZero(float f) {
        float d = 1f;
        if (f < 0) {
            f *= -1;
        }
        if (Math.abs(f * d - 0) < precision) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Binomialcoefficent
     *
     * @param n amount of repetitions
     * @param k amount of successes
     */
    public long getBinCE(long n, long k) {

        if (getFactorial(n) != FACTORIAL_ARGUMENT_TOO_BIG) {
            long part1 = getFactorial(n);
            long part2 = getFactorial(k);
            long part3 = getFactorial(n - k);

            if (k == n)
                part3 = 1;
            if (k == 0)
                part2 = 1;

            long bce = part1 / (part2 * part3);

//                    System.out.println("   "+n+"!\n------------  =\t"+bce+"\n"+k+"! *("+n+"-"+k+")!");
//                    System.out.println("   "+getFactorial(n)+"\n----------------------------  =\t"+bce+"\n"+getFactorial(k)+" * "+getFactorial(n-k));
            return bce;
        } else {
            //System.err.println("Use getHugeBinCE(long,long)[returns BigInteger]. The current values are too big.");
            return FACTORIAL_ARGUMENT_TOO_BIG;
        }
    }

    /**
     * @param n
     * @param success
     * @param possibilities
     * @param expectationValue
     * @return
     */
    public double getSigmaOfBinCE(int n, int success, int possibilities, double expectationValue) {
        double sigma = 0.0;

        for (int i = 0; i < n; i++) {
            sigma += power((i + 1) - expectationValue, 2) * getProbability(n, success, possibilities, 6);
        }
        sigma = Math.sqrt(sigma);

//		System.out.println("Sigma:\t"+sigma);
        return sigma;
    }

    /**
     * @param n
     * @param k
     * @return
     */
    public BigInteger getHugeBinCE(long n, long k) {
        BigInteger bce = getHugeFactorial(n).divide((getHugeFactorial(k).multiply(getHugeFactorial(n - k))));
//                    System.out.println("   "+n+"!\n------------  =\t"+bce+"\n"+k+"! *("+n+"-"+k+")!");
        //System.out.println("   "+getHugeFactorial(n)+"\n----------------------------  =\t"+bce+"\n"+getHugeFactorial(k)+" * "+getHugeFactorial(n-k));
        return bce;
    }

    /**
     * @param n
     * @param k
     * @param identifier
     * @return
     */
    public BigDecimal getHugeBinCE(long n, long k, int identifier) {
        BigDecimal bce = getHugeFactorial(n, BIG_DECIMAL_REQUIRED).divide((getHugeFactorial(k, BIG_DECIMAL_REQUIRED).multiply(getHugeFactorial(n - k, BIG_DECIMAL_REQUIRED))));
//                    System.out.println("   "+n+"!\n------------  =\t"+bce+"\n"+k+"! *("+n+"-"+k+")!");
        //System.out.println("   "+getHugeFactorial(n,BIG_DECIMAL_REQUIRED)+"\n----------------------------  =\t"+bce+"\n"+getHugeFactorial(k,BIG_DECIMAL_REQUIRED)+" * "+getHugeFactorial(n-k,BIG_DECIMAL_REQUIRED));
        return bce;
    }

    /**
     * Returns -1 if the value is less than zero.
     *
     * @param n
     * @return
     */
    public long getFactorial(long n) {
        if (n >= 0) {
            if (n <= 20) {
                if (n == 0 || n == 1)
                    return n;
                long tmp = n;
                for (int i = 1; i < tmp - 1; i++) {

                    n *= tmp - i;
                }
                return n;
            } else {
                //System.err.println("For factorial calculation is set with a maximum of 20.");
                return FACTORIAL_ARGUMENT_TOO_BIG;
            }
        } else {
            //System.err.println("For factorial calculation is an positive value required!");
            return FACTORIAL_ARGUMENT_ZERO;
        }
    }

    /**
     * Round to certain number of decimals
     *
     * @param d
     * @param decimalPlace
     * @return
     */
    public static double round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    /**
     * @param n
     * @return
     */
    public BigInteger getHugeFactorial(long n) {

        BigInteger result = BigInteger.ONE;

        for (int i = 1; i <= n; i++) {

            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    /**
     * @param n
     * @param identifier
     * @return
     */
    public BigDecimal getHugeFactorial(long n, int identifier) {

        BigDecimal result = BigDecimal.ONE;

        for (int i = 1; i <= n; i++) {

            result = result.multiply(BigDecimal.valueOf(i));
        }
        return result;
    }

    /**
     * @param n
     * @param k
     * @param possibilities
     * @param accuracy
     * @return
     */
    public synchronized double getProbability(int n, int k, int possibilities, int accuracy) {
        alternativeDouble = null;
        //the win percentage
        double percent;

        BigDecimal qBig = BigDecimal.valueOf(1);
        BigDecimal pBig = BigDecimal.valueOf(1);

        BigDecimal poss = BigDecimal.valueOf(possibilities);

        if (n <= FACTORIAL_REPETITION_MAX) {
            double p = 1.0 / (double) possibilities;
            double q = 1.0 - p;
            percent = getBinCE(n, k) * power(p, k) * power(q, n - k);

//                    System.out.println(percent+"= ("+n+" nCr "+0+") * "+p+"^"+0+" * "+q+"^("+(n-0)+")");
//                    System.out.println(percent+"= ("+getBinCE(n,0) +" * "+power(p,0)+" * "+power(q,n-0));

            return percent;
        } else {
//                    System.out.println("poss: "+poss);
            pBig = pBig.divide(poss, accuracy, RoundingMode.UP);
//                    System.out.println("pBig: "+pBig);
            qBig = qBig.subtract(pBig);
//                    System.out.println("qBig: "+qBig);
//                    System.out.println("success(k): "+success);
            pBig = pBig.pow(k);
            qBig = qBig.pow(n - k);

            pBig = pBig.multiply(qBig);

//                    System.out.println("pBig after multiplying: "+pBig);

            //MathContext mc = new MathContext(2, RoundingMode.UP);

//                    System.out.println("Alternative value before setting: "+alternativeDouble);

            System.out.println("nCr= " + getHugeBinCE(n, k, BIG_DECIMAL_REQUIRED) + " * " + pBig);
            alternativeDouble = getHugeBinCE(n, k, BIG_DECIMAL_REQUIRED).multiply(pBig);

//                    System.out.println("Alternative value after setting: "+alternativeDouble);

//                    System.out.println("Alt.Double: "+pBig);
            return ALTERNATIVE_DOUBLE_SET;
        }
    }

    /**
     * Please use always repetitions+1 because the zero value is counted
     * in the array and the calculation. So if you want to get 25 repetitions
     * take 26 as argument.
     *
     * @param n
     * @param p
     * @return
     */
    public BigDecimal[] getProbabilities(int n, int p) {
        BigDecimal[] probabilities = new BigDecimal[n + 1];

        for (int i = 0; i < n + 1; i++) {
            if (getProbability(n, i, p, 6) == ALTERNATIVE_DOUBLE_SET) {
                if (i == 0)
                    System.out.println("[alt] " + alternativeDouble);
                probabilities[i] = alternativeDouble;
            } else {
                if (i == 0)
                    System.out.println("[alt] " + BigDecimal.valueOf(getProbability(n, i, p, 6)));
                ;

                probabilities[i] = BigDecimal.valueOf(getProbability(n, i, p, 6));
            }
        }
        return probabilities;
    }

    /**
     * @param values
     * @param start
     * @param end
     * @return
     */
    public BigDecimal getProbabilitiesFromRange(BigDecimal[] values, int start, int end) {
        BigDecimal range = BigDecimal.valueOf(0);

        for (int i = start; i < end; i++) {
            range = range.add(values[i]);
        }
        return range;
    }

    /**
     * @param n
     * @param p
     * @return
     */
    public double getExpValueOfBinCE(int n, int p) {
        double my = 0.0;

        for (int i = 0; i < n; i++) {
            my += (i) * getProbability(n, i, p, 4);
//        		System.out.println("µ = "+my);
        }

        return my;
    }

    /**
     * @param x
     * @param y
     * @return
     */
    public static double getAngle(double x, double y) {
        double sin = x / y;

        Math.pow(sin, -1);

        return sin;
    }

    /**
     * Different Potentiation Possibilities
     */
    public static double power(double basis, double exponent) {
        double tmp = basis;
        if (exponent == 0)
            return 1;
        if (exponent == 1)
            return basis;

        for (int i = 1; i < exponent; i++) {
            basis *= tmp;
            //System.out.println(basis/tmp+"*"+tmp+"="+basis);

        }
        return basis;
    }

    /**
     * @param basis
     * @return
     */
    public static double squareRoot(double basis) {
        return power(basis, 0.5);
    }

    /**
     * @param basis
     * @param n
     * @return
     */
    public static double squareRootX(double basis, double n) {
        try {
            double r = power(basis, (1.0 / n));
            return r;
        } catch (Exception e) {
            System.err.println("SqrRootX Exception " + e);
            return 0;
        }
    }

    /**
     * @return
     */
    public double getPrimitiveIntegral() {
        //TODO Integrieren
        double surface = 0;

        return surface;
    }

    /**
     * @param n          Repetitions.
     * @param k          Success value.
     * @param p1         Possibility value. gets devided via 1/p. So if p = 100, the function
     *                   calculates with 0,01.
     * @param rangeStart Start of summary of probability calculation
     * @param rangeEnd   End of summary of probability calculation
     * @param accuracy   The amount of digits behind the comma.
     */
    public void printAllValues(int n, int k, int p1, int rangeStart, int rangeEnd, int accuracy) {
        BigDecimal oneTest = new BigDecimal(0);
        double p = 1.0 / p1;
        double q = 1.0 - p;
        double µ = getExpValueOfBinCE(n, p1);
        double percent;
        BigDecimal[] values = getProbabilities(n, p1);
        BigDecimal currVal = BigDecimal.valueOf(0);


        percent = getBinCE(n, k) * power(p, (double) k) * power(q, (double) n - k);

        System.out.println(percent + "= (" + n + " nCr " + k + ") * " + (1.0 / p) + "^" + k + " * " + q + "^(" + (n - k) + ")");
        System.out.println("n: " + n + "\tk: " + k + "\tp: " + p);
        System.out.println("------------------------------------");
        System.out.println("n ^ k =" + power(n, k));
        System.out.println("------------------------------------");
        System.out.println("BinCE: " + getBinCE(n, k));
        System.out.println("------------------------------------");
        System.out.println("BinCE(if n > 20): " + getHugeBinCE(n, k));
        System.out.println("------------------------------------");
        System.out.println("Binomial CE µ: " + µ);
        System.out.println("------------------------------------");
        System.out.println("Binomial CE sigma: " + getSigmaOfBinCE(n, k, p1, µ));
        System.out.println("------------------------------------");
        System.out.println("!k = !" + k + "= " + getFactorial(k));
        System.out.println("------------------------------------");
        System.out.println("!n = !" + n + "= " + getHugeFactorial(n) + " [for bigger factorials]");
        System.out.println("------------------------------------");
        System.out.println("Die Wahrscheinlichkeit bei:");
        //System.out.println("P(X=xi)=P(X="+0+")\t= "+getProbability(n, 0, p1, 6)+"\t\tin Prozent: "+(getProbability(n, 0, p1, 6)*100)+" %");
        for (int i = 0; i < n + 1; i++) {
            currVal = values[i];
            System.out.println("P(X=xi)=P(X=" + i + ")\t= " + currVal + "\t\tin Prozent: " + values[i].multiply(BigDecimal.valueOf(100)) + " %");
            oneTest = oneTest.add(currVal);
        }

        System.out.println("Summe\t\t= " + oneTest);
        System.out.println("------------------------------------");
        if (getProbability(n, k, p1, accuracy) == ALTERNATIVE_DOUBLE_SET) {
            System.out.println("[GROß]Die  Wahrscheinlichkeit dass bei\n" + n + " Versuchen und " + p1 + " Möglichkeiten " + k + " Erfolge\nwahrscheinlich sind liegt bei " + alternativeDouble.multiply(BigDecimal.valueOf(100)) + " %.");
        } else {
            System.out.println("Die Wahrscheinlichkeit dass bei\n" + n + " Versuchen und " + p1 + " Möglichkeiten " + k + " Erfolge\nwahrscheinlich sind liegt bei " + (getProbability(n, k, p1, accuracy) * 100) + " %.");
        }
        System.out.println("------------------------------------");
        System.out.println("\n\nDie  Wahrscheinlichkeit dass bei\n" + (n) + " Versuchen und " + p1 + " Möglichkeiten die Erfolge\nzwischen " + rangeStart + " und " + rangeEnd + " wahrscheinlich sind liegt bei " + (getProbabilitiesFromRange(values, rangeStart, rangeEnd).multiply(BigDecimal.valueOf(100))) + " %.");

    }

    /**
     * @param lambda
     * @return
     */
    public double getDecreaseValue(double lambda) {
        double decr = 0.0;
        return decr;
    }


    public static class Matrix {
        /*----------------------------------------CONSTRUCTORS-----------------------------------*/
        /**
         * Pimary Matrix object
         */
        float[][] mMatrix;

        /**
         * Right Top Matrix
         */
        float[][] rMatrix;

        /**
         * Left Bottom Matrix
         */
        float[][] lMatrix;

        /**
         * Saves the switch changes for "LR-Zerlegung"
         */
        private float[][] pMatrix;


        int columns, rows;

        public int numberOfElements;
        /**
         * Simulates real x axis
         */
        public final static int X_AXIS = 0;
        /**
         * Simulates real y axis
         */
        public final static int Y_AXIS = 1;
        /**
         * Simulates real Z-Axis (Space Depth)
         */
        public final static int Z_AXIS = 2;


        /**
         * for direct rotating.
         * Translates first, than rotates on the axis
         */
        int screenWidth, screenHeight;

        /**
         * Creates a matrix with a "nxm" two dimensional array
         *
         * @param n
         * @param m
         */
        public Matrix(int n, int m) {
            mMatrix = new float[n][m];
            columns = n;
            rows = m;

            numberOfElements = n * m;
        }

        /**
         * Gets a single array and forms it into a two dimensional array
         * to make it useable inside this class. Later you call call {@link Matrix#toOpenGLESMatrix()}
         * to turn it back into a one dimensional array. For e.g. using it for OpenGL ES.
         *
         * @param matrix
         * @param splitAt
         */
        public Matrix(float[] matrix, int splitAt) {
            numberOfElements = matrix.length;
            columns = splitAt;
            rows = matrix.length / splitAt;

            mMatrix = new float[columns][rows];
            //fill local matrix as 2d float
            for (float f : matrix) {
                for (int i = 0; i < splitAt; i++) {
                    for (int k = 0; k < splitAt; k++) {
                        mMatrix[i][k] = f;
                    }
                }
            }

        }

        /**
         * Creates a {@link MathHandler.Matrix} out of the nm array
         *
         * @param nm
         */
        public Matrix(float[][] nm) {
            columns = nm.length;
            rows = nm[0].length;
            numberOfElements = rows * columns;
        }

        public Matrix() {

        }


        /**
         * Prints the matrix in a mathamatical form on the console
         */
        public static void printMatrix(float[][] matrix) {
            String matrixPlotter = "";
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    matrixPlotter += matrix[i][j] + " | ";
                }
                matrixPlotter += "\n";
            }

            System.out.println(matrixPlotter);
        }

        /**
         * Prints the matrix in a mathamatical form on the console
         */
        public static void printMatrix(String[][] matrix) {
            String matrixPlotter = "";
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    matrixPlotter += matrix[i][j] + " | ";
                }
                matrixPlotter += "\n";
            }

            System.out.println(matrixPlotter);
        }



        /*----------------------------------------HANDLERS-----------------------------------*/

        /**
         * Switches the columns into rows and opposite.
         *
         * @param matrix
         * @return
         */
        public static float[][] transpositionMatrix(float[][] matrix) {
            int rowLength = matrix.length;
            int columnLength = matrix[0].length;

            float[][] nMatrix = new float[columnLength][rowLength];

            //column
            int n;
            //row
            int m;

            for (n = 0; n < rowLength; n++) {
                for (m = 0; m < columnLength; m++) {
                    nMatrix[m][n] = matrix[n][m];
                }
            }
            return nMatrix;
        }

        /**
         * Reset the matrix to the new form
         */
        public void setMatrix(float[][] mMatrix) {
            this.mMatrix = mMatrix;
            columns = mMatrix.length;
            rows = mMatrix[0].length;
            numberOfElements = rows * columns;
        }

        /**
         * Multiplies an two dimensional array with a {@link MathHandler.Vector}.
         * The size will get proofed.
         *
         * @param matrixArray
         * @param vector
         * @return
         */
        public static Vector multiplyMatrixByVector(float[][] matrixArray, Vector vector) {

//            printMatrix(matrixArray);

            float[] v = vector.getmFloatVec();

            float[] nV = new float[v.length];

            int m = 0;
            //check column amount
            if (v.length == matrixArray.length) {

                //column
                for (int n = 0; n < matrixArray.length; n++) {

                    for (int k = 0; k < matrixArray.length; k++) {
                        nV[n] += matrixArray[k][m] * v[k];
                    }
                    //row
                    m++;
                }

                vector.setmFloatVec(nV);
            } else {
                System.err.println("Number of matrix columns is not equal to the number of vector data!");
                return null;
            }
            return vector;
        }

        /**
         * Multiplies two matrices in the argument order
         *
         * @param m1
         * @param m2
         * @return
         */
        public static Matrix multiplyMatrixByMatrix(Matrix m1, Matrix m2) {

            int m = 0;
            //check column amount
            if (m1.numberOfElements == m2.numberOfElements) {

                //column
                for (int n = 0; n < m1.columns; n++) {

                    for (int k = 0; k < m1.rows; k++) {
                        m1.getArray()[n][k] += m1.getArray()[k][m] * m2.getArray()[n][k];
                    }
                    //row
                    m++;
                }

            } else {
                System.err.println("Number of matrix columns is not equal to the number of second matrix data!");
                return null;
            }
            return m1;
        }

        /**
         * Multiplies two matrices in the argument order
         *
         * @param m1
         * @param m2
         * @return
         */
        public static float[][] multiplyMatrixByMatrix(float[][] m1, float[][] m2) {

            int m = 0;
            //check column amount
            if (m1.length == m2.length) {
                int length = m1.length;
                //column
                for (int n = 0; n < length; n++) {

                    for (int k = 0; k < length; k++) {
                        m1[n][k] += m1[k][m] * m2[n][k];
                    }
                    //row
                    m++;
                }

            } else {
                System.err.println("Number of matrix columns is not equal to the number of second matrix data!");
                return null;
            }
            return m1;
        }

        /**
         * Forms the 2 dimensional array into a one dimensional form
         * to make it accessable to the opengl calculations
         *
         * @return
         */
        public float[] toOpenGLESMatrix() {
            float[] floats = new float[numberOfElements];


            int i = 0;
            for (float[] n : mMatrix) {
                for (float m : n) {
                    floats[i] = m;
                    i++;
                }
            }

            return floats;
        }


        /*----------------------------------------SETTERS-----------------------------------*/

        /**
         * Is required to get a correct translating behavior.
         *
         * @param width
         * @param height
         */
        public void setScreenDimension(int width, int height) {
            screenWidth = width;
            screenHeight = height;
        }

        /**
         * Uses this matrix as x rotation matrix
         *
         * @param angle
         */
        public void setAsXRotationMatrix(float angle) {
            mMatrix = getXRotationMatrix(angle, true);
        }

        /**
         * Uses this matrix as y rotation matrix
         *
         * @param angle
         */
        public void setAsYRotationMatrix(float angle) {
            mMatrix = getYRotationMatrix(angle, true);
        }

        /**
         * Uses this matrix as z rotation matrix
         *
         * @param angle
         */
        public void setAsZRotationMatrix(float angle) {
            mMatrix = getZRotationMatrix(angle, true);
        }
        /*----------------------------------------GETTERS-----------------------------------*/

        /**
         * @param angle
         * @param x
         * @param y
         * @param z
         * @param origin
         * @return
         */
        public static float[][] getRotationMatrix(float angle, int x, int y, int z, @Nullable Vector origin) {
            if (x == 1 && y == 0 && z == 0) {

                return getXRotationMatrix(angle, true);
            } else if ((x == 0 && y == 1 && z == 0)) {

                return getYRotationMatrix(angle, true);
            } else if ((x == 0 && y == 0 && z == 1)) {
                return getZRotationMatrix(angle, true);


            } else {

                if (origin != null) {
                    return getNRotationMatrix(angle, origin);
                } else {
                    System.err.println("WARNING: n-rotation started without origin vector.");
                    return new float[4][4];
                }

            }
        }

        /**
         * Returns a matrix to rotate on the x axis
         *
         * @param angle
         * @param isCoordinateSystemChanged
         * @return
         */
        public static float[][] getXRotationMatrix(float angle, boolean isCoordinateSystemChanged) {
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            if (isCoordinateSystemChanged)
                sin *= -1;

            float[][] m = {
                    new float[]{1, 0, 0, 0},
                    new float[]{0, cos, sin, 0},
                    new float[]{0, -sin, cos, 0},
                    new float[]{0, 0, 0, 1},
            };

            return m;
        }

        /**
         * Returns a matrix to rotate on the y axis
         *
         * @param angle
         * @param isCoordinateSystemChanged
         * @return
         */
        public static float[][] getYRotationMatrix(float angle, boolean isCoordinateSystemChanged) {
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            if (isCoordinateSystemChanged)
                sin *= -1;

            float[][] m = {
                    new float[]{cos, 0, -sin, 0},
                    new float[]{0, 1, 0, 0},
                    new float[]{sin, 0, cos, 0},
                    new float[]{0, 0, 0, 1}
            };

            return m;
        }

        /**
         * Returns a matrix to rotate on the z axis
         *
         * @param angle
         * @param isCoordinateSystemChanged
         * @return
         */
        public static float[][] getZRotationMatrix(float angle, boolean isCoordinateSystemChanged) {
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            if (isCoordinateSystemChanged)
                sin *= -1;

            float[][] m = {
                    new float[]{cos, sin, 0, 0},
                    new float[]{-sin, cos, 0, 0},
                    new float[]{0, 0, 1, 0},
                    new float[]{0, 0, 0, 1},
            };

            return m;
        }

        /**
         * Returns a matrix to rotate quaternion based
         *
         * @param angle
         * @param vec
         * @return
         */
        public static double[][] getQuaternionRotationMatrix(float angle, Vector vec) {
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);

            double[] pos = vec.getmDoubleVec();

            double x = pos[0];
            double y = pos[1];
            double z = pos[2];

            double halfAngle = angle / 2;

            double xSq = Math.pow(x, 2);
            double ySq = Math.pow(y, 2);
            double zSq = Math.pow(z, 2);

            double a = Math.cos(halfAngle);
            double b = x * Math.sin(halfAngle);
            double c = y * Math.sin(halfAngle);
            double d = z * Math.sin(halfAngle);
            double f = 1 - Math.cos(angle);

            double[][] m = {
                    new double[]{cos + xSq * f, z * sin + x * y * f, -y * sin + x * z * f},
                    new double[]{-z * sin + x * y * f, cos + ySq * f, x * sin + y * z * f},
                    new double[]{y * sin + x * z * f, -x * sin + y * z * f, cos + zSq * f}
            };

            return m;
        }

        /**
         * Rotation around the origin
         *
         * @param angle
         * @param origin
         * @return
         */
        public static float[][] getNRotationMatrix(float angle, Vector origin) {
            float x, y, z;
            x = origin.getXf();
            y = origin.getYf();
            z = origin.getZf();

            float len = (float) origin.getLength();
            if (1.0f != len) {
                float recipLen = 1.0f / len;
                x *= recipLen;
                y *= recipLen;
                z *= recipLen;
            }

            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);


            float[][] m = {
                    new float[]{
                            x * x * (1 - cos) + cos,
                            y * x * (1 - cos) + z * sin,
                            z * x * (1 - cos) - y * sin,
                            0
                    },
                    new float[]{
                            x * y * (1 - cos) - z * sin,
                            y * y * (1 - cos) + cos,
                            z * y * (1 - cos) + x * sin,
                            0
                    },
                    new float[]{
                            x * z * (1 - cos) + y * sin,
                            y * z * (1 - cos) - x * sin,
                            z * z * (1 - cos) + cos,
                            0
                    },
                    new float[]{
                            0, 0, 0, 1
                    }

            };

            String[][] s = {
                    new String[]{
                            "x * x * (1 - cos) + cos",
                            "y * x * (1 - cos) + z * sin",
                            "z * x * (1 - cos) - y * sin",
                            "0"
                    },
                    new String[]{
                            "x * y * (1 - cos) - z * sin",
                            "y * y * (1 - cos) + cos",
                            "z * y * (1 - cos) + x * sin",
                            "0"
                    },
                    new String[]{
                            "x * z * (1 - cos) + y * sin",
                            "y * z * (1 - cos) - x * sin",
                            "z * z * (1 - cos) + cos",
                            "0"
                    },
                    new String[]{
                            "0", "0", "0", "1"
                    }

            };


//        Matrix.transpositionMatrix(m);


//        printMatrix(s);
            return m;
        }

        /**
         * Returns a matrix to scale
         *
         * @param scaleFactor
         * @return
         */
        public static float[][] getScaleMatrix(float scaleFactor) {

            return
                    new float[][]{
                            new float[]{scaleFactor, 0, 0, 0},
                            new float[]{0, scaleFactor, 0, 0},
                            new float[]{0, 0, scaleFactor, 0},
                            new float[]{0, 0, 0, 1}
                    };
        }

        //*******************************************************************

        /**
         * @param size
         * @return
         */
        public float[][] getNullMatrix(int size) {
            float[][] e = new float[size][size];

            for (int i = 0; i < size; i++) {
                for (int s = 0; s < size; s++) {
                    e[i][s] = 0;
                }
            }

            return e;
        }

        /**
         * @param size
         * @return
         */
        public float[][] getGeneralMatrix(int size) {

            float[][] e = new float[size][size];

            for (int i = 0; i < size; i++) {
                for (int s = 0; s < size; s++) {
                    if (s == i) {
                        e[i][s] = 1;
                    } else {
                        e[i][s] = 0;
                    }
                }
            }

            return e;
        }

        /**
         * @return
         */
        public float[][] getLMatrix() {

            return null;
        }

        /**
         * @param mMatrix
         * @return
         */
        public float[][] getRMatrix(float[][] mMatrix) {

            if (mMatrix != null) {

                int length = mMatrix.length;

                pMatrix = getGeneralMatrix(length);
                lMatrix = getGeneralMatrix(length);

                //standard matrix
                float[][] e = getGeneralMatrix(length);


                boolean isDone = false;

                while (!isDone) {
//                    optimizeRowOrder(mMatrix);
                    //check if r is finished
                    //go through columns
                    for (int n = 0; n < length; n++) {
                        boolean correct = true;
                        //go through current row
                        for (int m = 0; e[n][m] != 1; m++) {

                            if (mMatrix[n][m] != e[n][m]) {
                                correct = false;
                            }
                        }

                        //stop testing
                        if (!correct) {
                            break;
                        }

                        //if bottom R Matrix is correct calculated
                        if (correct && n == length - 1) {
                            //just to be sure^^
                            isDone = true;
                            finishLMatrix();
                            return mMatrix;
                        }
                    }

                    //calculate

                    //row
                    for (int i = 1; i < length; i++) {
                        //column
                        for (int k = i - 1; k < i; k++) {
                            /*
                              * Now compare each row with each other
                              * to check if a calculation can
                              * a set row value to zero
                             */

                            //first row for calculation check
                            float current = mMatrix[i - 1][k];
                            //every following row after the first row
                            //and check each possible calculation
                            for (int u = i; u < length; u++) {
                                //try subtraction or addition
                                float next = mMatrix[u][k];

                                //if next != 0 && current != 0
                                if (!isFloatZero(current)) {

                                    double factor = (next / current);

                                    if (factor < 0) {
                                        factor *= -1;
                                    }
                                    //store changes in left matrix
                                    lMatrix[u][k] = (float) factor;

                                    int cFactor = 1;

                                    //set factor negative or positive
                                    if (current < 0 && next < 0) {
                                        cFactor *= -1;
                                    }
                                    if (current > 0 && next > 0) {
                                        cFactor *= -1;
                                    }
//                                    if (current > 0 && next < 0) {
//                                        cFactor *= -1;
//                                    }
                                    float[] temp = copyFloat(mMatrix[i - 1], k);

                                    calculateRow(factor * cFactor, temp, MULTIPLICATION);

                                    calculateRows(temp, mMatrix[u], ADDITION);


                                    printMatrix(mMatrix);
                                } else {
                                    /*
                                    if the "current" row has more zeros than
                                    the next, switch them
                                     */
                                    optimizeRowOrder(mMatrix);
                                }
                            }
                        }
//
                    }
                }//while
            }
            return mMatrix;
        }

        /**
         * @param matrix
         */
        public void optimizeRowOrder(float[][] matrix) {

            int length = matrix.length;
            //saves the number of zeros in the rows
            int[] switchStatus = new int[length];
            //saves the position of the zero
            //by setting the index to on or off with 0 or 1
            float[][] zeroColumnPosition = getNullMatrix(length);

            //check if some rows can get changed first
            int zeroCounter = 0; //counts how many zeros should stand in this row
            int currentZeros = 0; //counts the zeros at the beginning of this row
//
//            System.out.println("before switch:\n");
//            printMatrix(matrix);

            for (int i = 1; i < length; i++) {
                for (int k = 0; k <= i; k++) {
                    if (k <= zeroCounter && isFloatZero(matrix[i][k])) {
                        currentZeros++;
                        zeroColumnPosition[i][k] = 1f;
                    }
                }

                //save the zeros in this row
                switchStatus[i] = currentZeros;
                currentZeros = 0;
                zeroCounter++;
            }

//            System.out.println("zero Storage:\n");
//            printMatrix(zeroColumnPosition);

            //change the rows if possible and save the changes for the P-Matrix
            for (int i = 0; i < length - 1; i++) {
                if (switchStatus[i] >= switchStatus[i + 1]) {
                    //switch this row with the next
                    switchRows(matrix, i, i + 1);
                    //save changes in P-Matrix
                    switchRows(pMatrix, i, i + 1);
                }
            }

//            System.out.println("after switch:\n");
//            printMatrix(matrix);

        }

        /**
         * Applies the row switches which
         * were saved in pMatrix
         */
        public void finishLMatrix() {

            if (pMatrix != null) {
                int length = pMatrix.length;
                int[] changes = new int[length];

                for (int i = 1; i < length + 1; i++) {
                    for (int k = 0; k < length; k++) {
                        if (k == i - 1 && pMatrix[i - 1][k] == 1f) {
                            changes[i - 1] = i - 1;
                        }
                        if (k != i - 1 && pMatrix[i - 1][k] == 1f) {
                            //if not standard, count columns until row element is 1
                            //this counter shows the position of the origin row
                            changes[i - 1] = k;
                        }
                    }
                }

                //now create a simple matrix where only the changed
                //values of the L-Matrix are stored
                float[][] lValues = getNullMatrix(length);

                for (int i = 1; i < length; i++) {
                    for (int k = 0; k < i; k++) {
                        lValues[i][k] = lMatrix[i][k];
                    }
                }

                //switch the rows which were changed
                for (int i = 0; i < length - 1; i++) {
                    if (changes[i] > changes[i + 1]) {
                        //switch this row with the next
                        switchRows(lValues, i, i + 1);
                    }
                }

                //restore the changes to the original L-Matrix
                for (int i = 1; i < length; i++) {
                    for (int k = 0; k < i; k++) {
                        lMatrix[i][k] = lValues[i][k];
                    }
                }

            } else {

            }
        }


        /**
         * Eliminate elements with the standard gauß elimination
         *
         * @return
         */
        public float[][] gaussElimination() {

            float precision = 0.00000001f;
            if (pMatrix != null) {
                /*
                Maximum tries to find a multiplicator(or divider)
                of a value
                 */

                printMatrix(mMatrix);
                System.out.println("----------------\n");
                int multiplicator_max = 50;

                int length = mMatrix.length;

                //standard matrix
                float[][] e = getGeneralMatrix(length);

                boolean isDone = false;

                int loop = 1;
                while (!isDone) {
                    //check if r is finished
                    //go through columns
                    for (int n = 0; n < length; n++) {
                        boolean correct = true;
                        //go through current row
                        for (int m = 0; e[n][m] != 1; m++) {

                            if (mMatrix[n][m] != e[n][m]) {
                                correct = false;
                            }
                        }

                        //stop testing
                        if (!correct) {
                            break;
                        }

                        //if bottom R Matrix is correct calculated
                        if (correct && n == length - 1) {
                            //just to be sure^^
                            isDone = true;
                            printMatrix(mMatrix);
                            return mMatrix;
                        }
                    }

                    //calculate

                    //row
                    for (int i = 1; i < length; i++) {
                        //column
                        for (int k = i - 1; k < i; k++) {
                            /*
                              * Now compare each row with each other
                              * to check if a calculation can
                              * a set row value to zero
                             */

                            //first row for calculation check
                            float current = mMatrix[i - 1][k];
                            //every following row after the first row
                            //and check each possible calculation
                            for (int u = i; u < length; u++) {
                                //try subtraction or addition
                                float next = mMatrix[u][k];
//                                    mMatrix[i + u][k] = 10.11f;

                                //if next != 0 && current != 0
                                if (Math.abs(current) >= precision) {
                                    double factor = (next / current);

                                    int cFactor = 1;

                                    if (factor < 0) {
                                        factor *= -1;
                                    }
                                    //set factor negative or positive
                                    if (current < 0 && next < 0) {
                                        cFactor *= -1;
                                    }
                                    if (current > 0 && next > 0) {
                                        cFactor *= -1;
                                    }
                                    float[] temp = copyFloat(mMatrix[i - 1], k);

                                    calculateRow(factor * cFactor, temp, MULTIPLICATION);

                                    calculateRows(temp, mMatrix[u], ADDITION);

                                    printMatrix(mMatrix);
                                }
                            }
                        }
//
                    }
                }//while

            }

            return null;
        }

        /**
         * @param f
         * @param offset
         * @return
         */
        public float[] copyFloat(float[] f, int offset) {
            float[] f2 = new float[f.length];

            for (int i = 0; i < f.length; i++) {
                if (i == offset - 1) {
                    f2[i] = 0f;
                } else {
                    f2[i] = f[i];
                }
            }
            return f2;
        }

        /**
         * @return
         */
        public float[][] gaussElimation() {

            float precision = 0.00000001f;
            if (pMatrix != null) {
                /*
                Maximum tries to find a multiplicator(or divider)
                of a value
                 */

                printMatrix(mMatrix);
                System.out.println("----------------\n");
                int multiplicator_max = 50;

                int length = mMatrix.length;

                //standard matrix
                float[][] e = getGeneralMatrix(length);

                boolean isDone = false;

                int loop = 1;
                while (!isDone) {
                    //check if r is finished
                    //go through columns
                    for (int n = 0; n < length; n++) {
                        boolean correct = true;
                        //go through current row
                        for (int m = 0; e[n][m] != 1; m++) {

                            if (mMatrix[n][m] != e[n][m]) {
                                correct = false;
                            }
                        }

                        //stop testing
                        if (!correct) {
                            break;
                        }

                        //if bottom R Matrix is correct calculated
                        if (correct && n == length - 1) {
                            //just to be sure^^
                            isDone = true;
                            return mMatrix;
                        }
                    }


                    //calculate
                    for (int i = 0; i < length; i++) {
                        for (int k = 0; k < length; k++) {
                            if (mMatrix[i][i] == 1//diagonal line
                                    && mMatrix[i][k] == 0) {//current position in row

                                continue;//move on if current position is correct
                            } else {
                                /*
                                  * Now compare each row with each other
                                  * to check if a calculation can
                                  * a set row value to zero
                                 */

                                //first row for calculation check
                                for (int l = 0; l < (length - 1) - i; l++) {
                                    float current = mMatrix[i + l][k];
                                    //every following row after the first row
                                    //and check each possible calculation
                                    for (int u = l + 1; u < length - 1; u++) {
                                        //try subtraction or addition
                                        float next = mMatrix[i + u][k];
                                        if (current <= next) {
                                            /*
                                            If an subtraction would create zero
                                             */
                                            if (Math.abs(next - current - 0) < precision) {
                                                System.out.println(next + " - " + current + " = " +
                                                        (next - current));
                                                calculateRows(
                                                        mMatrix[i + l],
                                                        mMatrix[i + u],
                                                        SUBTRACTION);
                                                break;
                                            }
                                        } else if (current > next) {
                                            /*
                                            If an addition would create zero
                                             */
                                            if (Math.abs(current + next - 0) < precision) {

                                                System.out.println(next + " + " + current + " = " +
                                                        (next - current));
                                                calculateRows(
                                                        mMatrix[i + u],
                                                        mMatrix[i + l],
                                                        ADDITION);
                                                break;
                                            } else {
                                                /*
                                                Check if a multiplicator of the second value
                                                could set the first value to zero
                                                 */
                                                for (int h = 1; h < multiplicator_max; h++) {
                                                    //check for multiplication
//                                                float box_test = Math.abs((current + (next * h)) - 0);
//                                                System.err.println("Mult: " + box_test);
//                                                box_test = Math.abs((current + (next / h)) - 0);
//                                                System.err.println("Div: " + box_test);

                                                    if (Math.abs(current) >= precision) {
                                                        if (Math.abs((current + (next * h)) - 0) < precision) {
//                                                        System.out.println("[Multiplicator *] " + current + " + " + (next * h) + " = " +
//                                                                (current + (next * h)));
                                                            calculateRow(
                                                                    h,
                                                                    (mMatrix[i + u]),
                                                                    MULTIPLICATION);
                                                            calculateRows(
                                                                    mMatrix[i + u],
                                                                    mMatrix[i + l],
                                                                    ADDITION);
                                                            break;
                                                        } else
                                                            //check for division

                                                            if (Math.abs(current + (next / h) - 0) < precision) {
//                                                            System.out.println("[Multiplicator /]" + current + " + " + (next * h) + " = " +
//                                                                    (current + (next / h)));
                                                                calculateRow(
                                                                        h,
                                                                        (mMatrix[i + u]),
                                                                        DIVISION);
                                                                calculateRows(
                                                                        mMatrix[i + u],
                                                                        mMatrix[i + l],
                                                                        ADDITION);
                                                                break;
                                                            }
                                                    }
                                                }

                                                /*
                                                Check if a fraction could set the row to zero
                                                 */
//                                                System.out.println("Try fractions:\n\n");
                                                for (double z = 1; z < multiplicator_max; z++) {
                                                    for (double y = 1; y < z + 1; y++) {
                                                        if (y != z) {
                                                            double frac = y / z;
                                                            if (Math.abs(current + frac - 0) < precision) {
                                                                System.out.println("Addition: " + current + " + " + frac + " = 0");
                                                                calculateRow(frac, mMatrix[i + l], ADDITION);
                                                            }
                                                            if (Math.abs(current - frac - 0) < precision) {
                                                                System.out.println("Subtraction: " + current + " - " + frac + " = 0");
                                                                calculateRow(frac, mMatrix[i + l], SUBTRACTION);
                                                            }
                                                            if (Math.abs(frac - 0) < precision && Math.abs(current / frac - 0) < precision) {
                                                                System.out.println("Division: " + current + " / " + frac + " = 0");
                                                                calculateRow(frac, mMatrix[i + l], DIVISION);
                                                            }
                                                            if (Math.abs(frac - 0) < precision && Math.abs(current * frac - 0) < precision) {
                                                                System.out.println("Multiplication: " + current + " * " + frac + " = 0");
                                                                calculateRow(frac, mMatrix[i + l], MULTIPLICATION);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }

//                    System.out.println("Runs("+(loop++)+")");
                }


            }
            return null;
        }


        /**
         * Switch rows for Gauß' Elimination
         *
         * @param row1
         * @param row2
         */

        public void switchRows(float[][] mMatrix, int row1, int row2) {
            if (mMatrix != null) {
                int columns = mMatrix.length;
                int first = row1;
                int second = row2;

                float[] firstRow = copyFloat(mMatrix[first], 0);
                float[] secondRow = copyFloat(mMatrix[second], 0);

                for (int i = 0; i < columns; i++) {
                    mMatrix[first][i] = secondRow[i];
                    mMatrix[second][i] = firstRow[i];
                }
            }
        }

        /**
         * @param calculation_value
         * @param target
         * @param calculation
         */
        public void calculateRows(float[] calculation_value, float[] target, int calculation) {
            if (calculation_value.length == target.length) {
                float[] temp = new float[target.length];

                switch (calculation) {
                    case MathHandler.SUBTRACTION:
                        for (int i = 0; i < target.length; i++) {
                            temp[i] = target[i] - calculation_value[i];
                            target[i] = temp[i];
                        }
                        break;
                    case MathHandler.ADDITION:
                        for (int i = 0; i < target.length; i++) {
                            temp[i] = target[i] + calculation_value[i];
                            target[i] = temp[i];
                        }
                        break;
                    case MathHandler.MULTIPLICATION:
                        for (int i = 0; i < target.length; i++) {
                            temp[i] = target[i] * calculation_value[i];
                            target[i] = temp[i];
                        }
                        break;
                    case MathHandler.DIVISION:
                        for (int i = 0; i < target.length; i++) {
                            temp[i] = target[i] / calculation_value[i];
                            target[i] = temp[i];
                        }
                        break;

                }

            } else {
                System.err.println("Length unequal in 'substractRow'!");
            }
        }

        /**
         * @param calculation_value
         * @param target
         * @param calculation
         */
        public void calculateRow(double calculation_value, float[] target, int calculation) {
            double[] temp = new double[target.length];

            switch (calculation) {
                case MathHandler.SUBTRACTION:
                    for (int i = 0; i < target.length; i++) {
                        temp[i] = target[i] - calculation_value;
                        target[i] = (float) temp[i];
                    }
                    break;
                case MathHandler.ADDITION:
                    for (int i = 0; i < target.length; i++) {
                        temp[i] = target[i] + calculation_value;
                        target[i] = (float) temp[i];
                    }
                    break;
                case MathHandler.MULTIPLICATION:
                    for (int i = 0; i < target.length; i++) {
                        temp[i] = target[i] * calculation_value;
                        target[i] = (float) temp[i];
                    }
                    break;
                case MathHandler.DIVISION:
                    for (int i = 0; i < target.length; i++) {
                        temp[i] = target[i] / calculation_value;
                        target[i] = (float) temp[i];
                    }
                    break;
            }
        }
        //*******************************************************************

        /**
         * @param w current value
         * @param g whole amount
         * @return
         */
        public double getWidthPercentage(double w, double g) {

            if (g == 1) {
                g = screenWidth;
            }
            double p = w / g * 100;
            return p;
        }

        /**
         * @param w current value
         * @param g whole amount
         * @return
         */
        public double getHeightPercentage(double w, double g) {

            if (g == 1) {
                g = screenHeight;
            }
            double p = w / g * 100;
            return p;
        }

        /**
         * Returns the amount of columns
         *
         * @return
         */
        public int getColumns() {
            return columns;
        }

        /**
         * Returns the amount of rows
         *
         * @return
         */
        public int getRows() {
            return rows;
        }

        /**
         * Returns the amount of elements in the matrix
         *
         * @return
         */
        public int getNumberOfElements() {
            return numberOfElements;
        }

        /**
         * Returns the two dimensional array form of the class
         *
         * @return
         */
        public float[][] getArray() {
            return mMatrix;
        }

        /**
         * @param t
         */
        public void test(float[][] t) {
            t[0][1] += 2;
        }
    }


    public static class Vector {
        /*----------------------------------------CONSTRUCTORS-----------------------------------*/

        private int[] mIntVec;
        private float[] mFloatVec;
        private double[] mDoubleVec;


        private boolean isInt = false;
        private boolean isFloat = false;
        private boolean isDouble = false;

        private float[] screenCenter;
        private int screenWidth, screenHeight;

        public Vector() {

        }

        /**
         * @param vector
         */
        public Vector(float[] vector) {
            mFloatVec = vector;
            isFloat = true;
        }

        /**
         * @param vector
         */
        public Vector(double[] vector) {
            mDoubleVec = vector;
            isDouble = true;
        }

        /**
         * @param vector
         */
        public Vector(int[] vector) {
            mIntVec = vector;
            isInt = true;
        }

        /**
         * @param x
         * @param y
         * @param z
         */
        public Vector(int x, int y, int z) {
            mIntVec = new int[]{x, y, z, 1};
            isInt = true;
        }

        /**
         * @param x
         * @param y
         * @param z
         */
        public Vector(float x, float y, float z) {
            mFloatVec = new float[]{x, y, z, 1};
            isFloat = true;
        }

        /**
         * @param x
         * @param y
         * @param z
         */
        public Vector(double x, double y, double z) {
            mDoubleVec = new double[]{x, y, z, 1};
            isDouble = true;
        }

        /**
         * @param angle
         * @return
         */
        private Matrix get2DRotationMatrix(float angle) {
            Matrix m = new Matrix(2, 2);
            float[][] rotMatrix = m.getArray();

            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            rotMatrix[0][0] = cos;
            rotMatrix[0][1] = sin;
            rotMatrix[1][0] = -sin;
            rotMatrix[0][1] = cos;

            return m;
        }

        /**
         * @param angle
         * @return
         */
        public void rotate2D(float angle) {
            //creates the 2d rotation matrix
            Matrix m = get2DRotationMatrix(angle);

            //now get the actual matrix array for 2d rotation
            float[][] rotation;

            //temp
            Vector vec;

            if (m != null) {
                rotation = m.getArray();

                vec = m.multiplyMatrixByVector(rotation, this);

                //set values
                if (isFloat()) {
                    setX(vec.getXf());
                    setY(vec.getYf());
                    setZ(vec.getZf());
                }
                if (isDouble()) {
                    setX(vec.getXd());
                    setY(vec.getYd());
                    setZ(vec.getZd());
                }
                if (isInt()) {
                    setX(vec.getXi());
                    setY(vec.getYi());
                    setZ(vec.getZi());
                }
            }
        }

        /**
         * @param angle
         * @param xAxis
         * @param yAxis
         * @param zAxis
         * @param projCamView
         * @param origin
         */
        public void rotate3D(float angle, int xAxis, int yAxis, int zAxis, @Nullable Matrix projCamView, @Nullable Vector origin) {
            Matrix m = new Matrix(4, 4);

            //temp
            Vector vec;

            if (isInt()) {
                vec = new Vector(getmIntVec());
            } else if (isFloat()) {
                vec = new Vector(getmFloatVec());
            } else if (isDouble()) {
                vec = new Vector(getmDoubleVec());
            } else {
                vec = null;
            }

            boolean isPhysical = true;

            setToScreenOrigin();

            m.setMatrix(Matrix.getRotationMatrix(angle, xAxis, yAxis, zAxis, origin));
            if (projCamView != null) {
                m = m.multiplyMatrixByMatrix(m, projCamView);
            }
            vec = m.multiplyMatrixByVector(m.getArray(), vec);


            setToObjectOrigin();

            //set values
            if (isFloat()) {
                setmFloatVec(vec.getmFloatVec());
            }
            if (isDouble()) {
                setmDoubleVec(vec.getmDoubleVec());
            }
            if (isInt()) {
                setmIntVec(vec.getmIntVec());
            }
        }

        /**
         * @param scaleFactor
         */
        public void scale(float scaleFactor) {

            Vector vec = Matrix.multiplyMatrixByVector(new Matrix().getScaleMatrix(scaleFactor), this);
            //set values
            if (isFloat()) {
                setX(vec.getXf());
                setY(vec.getYf());
                setZ(vec.getZf());
            }
            if (isDouble()) {
                setX(vec.getXd());
                setY(vec.getYd());
                setZ(vec.getZd());
            }
            if (isInt()) {
                setX(vec.getXi());
                setY(vec.getYi());
                setZ(vec.getZi());
            }
        }

        /**
         * @param axis
         * @return
         */
        public Vector mirror(int axis) {

            return null;
        }

        /**
         *
         */
        public void definePositionAsScreenPercentage() {
            setX(((screenWidth / 100) * getXf()) / screenWidth);
            setY(((screenHeight / 100) * getYf()) / screenHeight);
            setZ((((screenWidth / 2) / 100) * getZf()) / (screenWidth / 2));
        }

        /**
         * @return
         */
        public int[] getmIntVec() {
            return mIntVec;
        }

        /**
         * @return
         */
        public float[] getmFloatVec() {
            return mFloatVec;
        }

        /**
         * @return
         */
        public double[] getmDoubleVec() {
            return mDoubleVec;
        }

        /**
         * @return
         */
        public double getLength() {
            if (isInt) {
                return Math.sqrt((Math.pow(mIntVec[0], 2) + Math.pow(mIntVec[1], 2) + Math.pow(mIntVec[2], 2)));
            } else if (isFloat) {
                return Math.sqrt((Math.pow(mFloatVec[0], 2) + Math.pow(mFloatVec[1], 2) + Math.pow(mFloatVec[2], 2)));
            } else if (isDouble) {
                return Math.sqrt((Math.pow(mDoubleVec[0], 2) + Math.pow(mDoubleVec[1], 2) + Math.pow(mDoubleVec[2], 2)));
            } else
                return -1;
        }

        /**
         * @return
         */
        public boolean isDouble() {
            return isDouble;
        }

        /**
         * @return
         */
        public boolean isFloat() {
            return isFloat;
        }

        /**
         * @return
         */
        public boolean isInt() {
            return isInt;
        }

        //calculation

        /**
         * @param vector
         */
        public void subtractWith(float[] vector) {
            if (isFloat) {
                float[] i = this.getmFloatVec();
                float[] i2 = vector;
                i[0] -= i2[0];
                i[1] -= i2[1];
                i[2] -= i2[2];

                setmFloatVec(i);
            }
        }

        /**
         * @param vector
         * @return
         */
        public Vector subtractWith(Vector vector) {
            if (isInt) {
                int[] i = this.getmIntVec();
                int[] i2 = vector.getmIntVec();
                i[0] -= i2[0];
                i[1] -= i2[1];
                i[2] -= i2[2];

                setmIntVec(i);
                return this;
            } else if (isFloat) {
                float[] i = this.getmFloatVec();
                float[] i2 = vector.getmFloatVec();
                i[0] -= i2[0];
                i[1] -= i2[1];
                i[2] -= i2[2];

                setmFloatVec(i);
                return this;
            } else if (isDouble) {
                double[] i = this.getmDoubleVec();
                double[] i2 = vector.getmDoubleVec();
                i[0] -= i2[0];
                i[1] -= i2[1];
                i[2] -= i2[2];

                setmDoubleVec(i);
                return this;
            } else
                return null;
        }

        /**
         * @param vector
         */
        public void addWith(float[] vector) {
            if (isFloat) {
                float[] i = this.getmFloatVec();
                float[] i2 = vector;
                i[0] += i2[0];
                i[1] += i2[1];
                i[2] += i2[2];

                setmFloatVec(i);
            }
        }

        /**
         * @param vector
         * @return
         */
        public Vector addWith(Vector vector) {
            if (isInt) {
                int[] i = this.getmIntVec();
                int[] i2 = vector.getmIntVec();
                i[0] += i2[0];
                i[1] += i2[1];
                i[2] += i2[2];

                setmIntVec(i);
                return this;
            } else if (isFloat) {
                float[] i = this.getmFloatVec();
                float[] i2 = vector.getmFloatVec();
                i[0] += i2[0];
                i[1] += i2[1];
                i[2] += i2[2];

                setmFloatVec(i);
                return this;
            } else if (isDouble) {
                double[] i = this.getmDoubleVec();
                double[] i2 = vector.getmDoubleVec();
                i[0] += i2[0];
                i[1] += i2[1];
                i[2] += i2[2];

                setmDoubleVec(i);
                return this;
            } else
                return null;
        }


        /**
         * @param mulitplier
         */
        public void multiplyBy(float mulitplier) {
            if (isInt) {
                setX((int) (mulitplier * getXi()));
                setY((int) (mulitplier * getYi()));
                setZ((int) (mulitplier * getZi()));
            } else if (isFloat) {
                setX((float) (mulitplier * getXf()));
                setY((float) (mulitplier * getYf()));
                setZ((float) (mulitplier * getZf()));
            } else if (isDouble) {
                setX((double) (mulitplier * getXd()));
                setY((double) (mulitplier * getYd()));
                setZ((double) (mulitplier * getZd()));
            }
        }

        /**
         * @param i
         */
        public void setmIntVec(int[] i) {
            this.mIntVec = i;
        }

        /**
         * @param f
         */
        public void setmFloatVec(float[] f) {
            this.mFloatVec = f;
        }

        /**
         * @param d
         */
        public void setmDoubleVec(double[] d) {
            this.mDoubleVec = d;
        }

        /**
         * @param x
         */
        public void setX(int x) {
            if (mIntVec != null) {
                mIntVec[0] = x;
            } else {
                System.err.println("[Vector.setX] mIntVec is null");
            }
        }

        /**
         * @param x
         */
        public void setX(float x) {
            if (mFloatVec != null) {
                mFloatVec[0] = x;
            } else {
                System.err.println("[Vector.setX] mFloatVec is null");
            }
        }

        /**
         * @param x
         */
        public void setX(double x) {
            if (mDoubleVec != null) {
                mDoubleVec[0] = x;
            } else {
                System.err.println("[Vector.setX] mDoubleVec is null");
            }
        }

        /**
         * @param y
         */
        public void setY(int y) {
            if (mIntVec != null) {
                mIntVec[1] = y;
            } else {
                System.err.println("[Vector.setY] mIntVec is null");
            }
        }

        /**
         * @param y
         */
        public void setY(float y) {
            if (mFloatVec != null) {
                mFloatVec[1] = y;
            } else {
                System.err.println("[Vector.setY] mFloatVec is null");
            }
        }

        /**
         * @param y
         */
        public void setY(double y) {
            if (mDoubleVec != null) {
                mDoubleVec[1] = y;
            } else {
                System.err.println("[Vector.setY] mDoubleVec is null");
            }
        }

        /**
         * @param z
         */
        public void setZ(int z) {
            if (mIntVec != null) {
                mIntVec[2] = z;
            } else {
                System.err.println("[Vector.setZ] mIntVec is null");
            }
        }

        /**
         * @param z
         */
        public void setZ(float z) {
            if (mFloatVec != null) {
                mFloatVec[2] = z;
            } else {
                System.err.println("[Vector.setZ] mFloatVec is null");
            }
        }

        /**
         * @param z
         */
        public void setZ(double z) {
            if (mDoubleVec != null) {
                mDoubleVec[2] = z;
            } else {
                System.err.println("[Vector.setZ] mDoubleVec is null");
            }
        }

        /**
         * @param vector1
         * @param vector2
         * @return
         */
        public static float[] getDistance(float[] vector1, float[] vector2) {
            float[] distance = new float[4];
            for (int i = 0; i < distance.length - 1; i++) {
                distance[i] = vector1[i] - vector2[i];
            }

            return distance;
        }

        /**
         *
         */
        public void setToScreenOrigin() {
            subtractWith(Vector.getDistance(mFloatVec, screenCenter));
        }

        public void setScreenCenter(int screenWidth, int screenHeight) {
            if (mFloatVec != null) {
                screenCenter = new float[]{screenWidth / 2, screenHeight / 2, 0};
            }
        }

        /**
         *
         */
        public void setToObjectOrigin() {
            addWith(Vector.getDistance(mFloatVec, screenCenter));
        }

        /**
         * @param width
         * @param height
         */
        public void setScreenSize(int width, int height) {
            screenWidth = width;
            screenHeight = height;

            screenCenter = new float[]{screenWidth / 2, screenHeight / 2, 0};

            System.out.println();
        }

        /**
         * @return
         */
        public int getXi() {
            if (isInt()) {
                return mIntVec[0];
            } else {
                String is = "NO VALUE";
                if (isDouble()) is = "DOUBLE";
                if (isFloat()) is = "FLOAT";

                System.err.println("Vector is no Integer! It's a " + is);
                return -1;
            }

        }

        /**
         * @return
         */
        public float getXf() {
            if (isFloat()) {
                return mFloatVec[0];
            } else {
                String is = "NO VALUE";
                if (isDouble()) is = "DOUBLE";
                if (isInt()) is = "INT";

                System.err.println("Vector is no Integer! It's a " + is);
                return -1;
            }
        }

        /**
         * @return
         */
        public double getXd() {
            if (isDouble()) {
                return mDoubleVec[1];
            } else {
                String is = "NO VALUE";
                if (isInt()) is = "INT";
                if (isFloat()) is = "FLOAT";

                System.err.println("Vector is no Integer! It's a " + is);
                return -1;
            }
        }

        /**
         * @return
         */
        public int getYi() {
            if (isInt()) {
                return mIntVec[1];
            } else {
                String is = "NO VALUE";
                if (isDouble()) is = "DOUBLE";
                if (isFloat()) is = "FLOAT";

                System.err.println("Vector is no Integer! It's a " + is);
                return -1;
            }

        }

        /**
         * @return
         */
        public float getYf() {
            if (isFloat()) {
                return mFloatVec[1];
            } else {
                String is = "NO VALUE";
                if (isDouble()) is = "DOUBLE";
                if (isInt()) is = "INT";

                System.err.println("Vector is no Integer! It's a " + is);
                return -1;
            }
        }

        /**
         * @return
         */
        public double getYd() {
            if (isDouble()) {
                return mDoubleVec[1];
            } else {
                String is = "NO VALUE";
                if (isInt()) is = "INT";
                if (isFloat()) is = "FLOAT";

                System.err.println("Vector is no Integer! It's a " + is);
                return -1;
            }
        }

        /**
         * @return
         */
        public int getZi() {
            if (isInt()) {
                return mIntVec[2];
            } else {
                String is = "NO VALUE";
                if (isDouble()) is = "DOUBLE";
                if (isFloat()) is = "FLOAT";

                System.err.println("Vector is no Integer! It's a " + is);
                return -1;
            }

        }

        /**
         * @return
         */
        public float getZf() {
            if (isFloat()) {
                return mFloatVec[2];
            } else {
                String is = "NO VALUE";
                if (isDouble()) is = "DOUBLE";
                if (isInt()) is = "INT";

                System.err.println("Vector is no Integer! It's a " + is);
                return -1;
            }
        }

        /**
         * @return
         */
        public double getZd() {
            if (isDouble()) {
                return mDoubleVec[2];
            } else {
                String is = "NO VALUE";
                if (isInt()) is = "INT";
                if (isFloat()) is = "FLOAT";

                System.err.println("Vector is no Integer! It's a " + is);
                return -1;
            }
        }

        /**
         * @param isDouble
         */
        public void setIsDouble(boolean isDouble) {
            this.isDouble = isDouble;
        }

        /**
         * @param isFloat
         */
        public void setIsFloat(boolean isFloat) {
            this.isFloat = isFloat;
        }

        /**
         * @param isInt
         */
        public void setIsInt(boolean isInt) {
            this.isInt = isInt;
        }

        /**
         *
         */
        public void print() {
            if (isInt) {
                System.out.print("{ ");
                for (int i : mIntVec) {
                    System.out.print(" " + i + ",");
                }
                System.out.print(" }\n");
            }
            if (isFloat) {
                System.out.print("{ ");
                for (float i : mFloatVec) {
                    System.out.print(" " + i + ",");
                }
                System.out.print(" }\n");
            }
            if (isDouble) {
                System.out.print("{ ");
                for (double i : mDoubleVec) {
                    System.out.print(" " + i + ",");
                }
                System.out.print(" }\n");
            }
        }
    }
//
//    public static void main(String[] args) {
//        MathHandler m = new MathHandler();
//        //LR Matrix Calculation box_test
////        Matrix matrix = new Matrix();
////
////        float[][] f1 = new float[][]{
////                {2f, -3f, 1f},
////                {6f, -9f, 2f},
////                {4f, 0f, 2f}
////        };
////
////        float[][] f2 = new float[][]{
////                {2f, 1f, 3f},
////                {4f, -1f, 3f},
////                {-2f, 5f, 5f}
////        };
////        matrix.setMatrix(new float[][]{
////                {2f, 1f, 3f},
////                {4f, -1f, 3f},
////                {-2f, 5f, 5f}
////        });
////
////        matrix.rMatrix = matrix.getRMatrix(f2);
////        System.out.println("A-Matrix:\n");
////        matrix.printMatrix(matrix.getArray());
////        System.out.println("R-Matrix:\n");
////        matrix.printMatrix(matrix.rMatrix);
////        System.out.println("L-Matrix:\n");
////        matrix.printMatrix(matrix.lMatrix);
////        System.out.println("P Matrix:\n");
////        matrix.printMatrix(matrix.pMatrix);
////
////        Matrix L = new Matrix(matrix.lMatrix);
////        Matrix R = new Matrix(matrix.rMatrix);
////        Matrix P = new Matrix(matrix.pMatrix);
////        Matrix A = new Matrix(matrix.mMatrix);
////        f2 = new float[][]{
////                {2f, 1f, 3f},
////                {4f, -1f, 3f},
////                {-2f, 5f, 5f}
////        };
////        float[][] LR = Matrix.multiplyMatrixByMatrix(matrix.lMatrix, matrix.rMatrix);
////        float[][] PA = Matrix.multiplyMatrixByMatrix(matrix.pMatrix, matrix.mMatrix);
////
////        System.out.println("L * R:\n");
////        Matrix.printMatrix(LR);
////        System.out.println("P * A:\n");
////        Matrix.printMatrix(PA);
//    }


}
