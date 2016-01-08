package com.thesaan.gameengine.android.handler;

import android.support.annotation.Nullable;

import java.math.*;


public class MathHandler {
    public final static int FACTORIAL_ARGUMENT_ZERO = -1;
    public final static int FACTORIAL_ARGUMENT_TOO_BIG = -2;
    public final static int FACTORIAL_REPETITION_MAX = 20;

    public final static int ALTERNATIVE_DOUBLE_SET = -3;
    public final static int ALTERNATIVE_LONG_SET = -4;

    //identifiers to get a decimal value
    private final static int BIG_DECIMAL_REQUIRED = -5;


    //alternative Values if a BigInteger is set instead of long 
    private BigInteger alternativeLong;
    //alternative Values if a BigDecimal is set instead of double 
    private BigDecimal alternativeDouble;

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

    public double getSigmaOfBinCE(int n, int success, int possibilities, double expectationValue) {
        double sigma = 0.0;

        for (int i = 0; i < n; i++) {
            sigma += power((i + 1) - expectationValue, 2) * getProbability(n, success, possibilities, 6);
        }
        sigma = Math.sqrt(sigma);

//		System.out.println("Sigma:\t"+sigma);
        return sigma;
    }

    public BigInteger getHugeBinCE(long n, long k) {
        BigInteger bce = getHugeFactorial(n).divide((getHugeFactorial(k).multiply(getHugeFactorial(n - k))));
//                    System.out.println("   "+n+"!\n------------  =\t"+bce+"\n"+k+"! *("+n+"-"+k+")!");
        //System.out.println("   "+getHugeFactorial(n)+"\n----------------------------  =\t"+bce+"\n"+getHugeFactorial(k)+" * "+getHugeFactorial(n-k));
        return bce;
    }

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

    public BigInteger getHugeFactorial(long n) {

        BigInteger result = BigInteger.ONE;

        for (int i = 1; i <= n; i++) {

            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    public BigDecimal getHugeFactorial(long n, int identifier) {

        BigDecimal result = BigDecimal.ONE;

        for (int i = 1; i <= n; i++) {

            result = result.multiply(BigDecimal.valueOf(i));
        }
        return result;
    }

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

    public BigDecimal getProbabilitiesFromRange(BigDecimal[] values, int start, int end) {
        BigDecimal range = BigDecimal.valueOf(0);

        for (int i = start; i < end; i++) {
            range = range.add(values[i]);
        }
        return range;
    }


    public double getExpValueOfBinCE(int n, int p) {
        double my = 0.0;

        for (int i = 0; i < n; i++) {
            my += (i) * getProbability(n, i, p, 4);
//        		System.out.println("µ = "+my);
        }

        return my;
    }

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

    public static double squareRoot(double basis) {
        return power(basis, 0.5);
    }

    public static double squareRootX(double basis, double n) {
        try {
            double r = power(basis, (1.0 / n));
            return r;
        } catch (Exception e) {
            System.err.println("SqrRootX Exception " + e);
            return 0;
        }
    }

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

    public double getDecreaseValue(double lambda) {
        double decr = 0.0;
        return decr;
    }


    public static class Matrix {
        /*----------------------------------------CONSTRUCTORS-----------------------------------*/
        float[][] mMatrix;

        int columns, rows;

        public int numberOfElements;

        public final static int X_AXIS = 0;
        /**
         * Simulates real
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
         * Creates a {@link com.thesaan.gameengine.android.handler.MathHandler.Matrix} out of the nm array
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
         * Multiplies an two dimensional array with a {@link com.thesaan.gameengine.android.handler.MathHandler.Vector}.
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

        public Vector(float[] vector) {
            mFloatVec = vector;
            isFloat = true;
        }

        public Vector(double[] vector) {
            mDoubleVec = vector;
            isDouble = true;
        }

        public Vector(int[] vector) {
            mIntVec = vector;
            isInt = true;
        }

        public Vector(int x, int y, int z) {
            mIntVec = new int[]{x, y, z, 1};
            isInt = true;
        }

        public Vector(float x, float y, float z) {
            mFloatVec = new float[]{x, y, z, 1};
            isFloat = true;
        }

        public Vector(double x, double y, double z) {
            mDoubleVec = new double[]{x, y, z, 1};
            isDouble = true;
        }

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

        public Vector mirror(int axis) {

            return null;
        }

        public void definePositionAsScreenPercentage() {
            setX(((screenWidth / 100) * getXf()) / screenWidth);
            setY(((screenHeight / 100) * getYf()) / screenHeight);
            setZ((((screenWidth / 2) / 100) * getZf()) / (screenWidth / 2));
        }

        public int[] getmIntVec() {
            return mIntVec;
        }

        public float[] getmFloatVec() {
            return mFloatVec;
        }

        public double[] getmDoubleVec() {
            return mDoubleVec;
        }

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

        public boolean isDouble() {
            return isDouble;
        }

        public boolean isFloat() {
            return isFloat;
        }

        public boolean isInt() {
            return isInt;
        }

        //calculation

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

        public void multiplyBy(float mulitplier) {
            if (isInt) {
                setX((int) (mulitplier * getXi()));
                setY((int) (mulitplier * getYi()));
                setZ((int) (mulitplier * getZi()));
            } else if (isFloat) {
                setX((int) (mulitplier * getXf()));
                setY((int) (mulitplier * getYf()));
                setZ((int) (mulitplier * getZf()));
            } else if (isDouble) {
                setX((int) (mulitplier * getXd()));
                setY((int) (mulitplier * getYd()));
                setZ((int) (mulitplier * getZd()));
            }
        }

        public void setmIntVec(int[] i) {
            this.mIntVec = i;
        }

        public void setmFloatVec(float[] f) {
            this.mFloatVec = f;
        }

        public void setmDoubleVec(double[] d) {
            this.mDoubleVec = d;
        }

        public void setX(int x) {
            if (mIntVec != null) {
                mIntVec[0] = x;
            } else {
                System.err.println("[Vector.setX] mIntVec is null");
            }
        }

        public void setX(float x) {
            if (mFloatVec != null) {
                mFloatVec[0] = x;
            } else {
                System.err.println("[Vector.setX] mFloatVec is null");
            }
        }

        public void setX(double x) {
            if (mDoubleVec != null) {
                mDoubleVec[0] = x;
            } else {
                System.err.println("[Vector.setX] mDoubleVec is null");
            }
        }

        public void setY(int y) {
            if (mIntVec != null) {
                mIntVec[1] = y;
            } else {
                System.err.println("[Vector.setY] mIntVec is null");
            }
        }

        public void setY(float y) {
            if (mFloatVec != null) {
                mFloatVec[1] = y;
            } else {
                System.err.println("[Vector.setY] mFloatVec is null");
            }
        }

        public void setY(double y) {
            if (mDoubleVec != null) {
                mDoubleVec[1] = y;
            } else {
                System.err.println("[Vector.setY] mDoubleVec is null");
            }
        }

        public void setZ(int z) {
            if (mIntVec != null) {
                mIntVec[2] = z;
            } else {
                System.err.println("[Vector.setZ] mIntVec is null");
            }
        }

        public void setZ(float z) {
            if (mFloatVec != null) {
                mFloatVec[2] = z;
            } else {
                System.err.println("[Vector.setZ] mFloatVec is null");
            }
        }

        public void setZ(double z) {
            if (mDoubleVec != null) {
                mDoubleVec[2] = z;
            } else {
                System.err.println("[Vector.setZ] mDoubleVec is null");
            }
        }


        public static float[] getDistance(float[] vector1, float[] vector2) {
            float[] distance = new float[4];
            for (int i = 0; i < distance.length - 1; i++) {
                distance[i] = vector1[i] - vector2[i];
            }

            return distance;
        }

        public void setToScreenOrigin() {
            subtractWith(Vector.getDistance(mFloatVec, screenCenter));
        }

        public void setToObjectOrigin() {
            addWith(Vector.getDistance(mFloatVec, screenCenter));
        }

        public void setScreenSize(int width, int height) {
            screenWidth = width;
            screenHeight = height;

            screenCenter = new float[]{screenWidth / 2, screenHeight / 2, 0};

            System.out.println();
        }

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


    }

    public static void main(String[] args) {
        MathHandler m = new MathHandler();
        
       /*
        int n = 20;
        int k = 3;
        int p = 100;
        
        int rangeStart = 0;
        int rangeEnd = 1;
        int accuracy = 6;
        */

        /* Matrix multiply test
        double [] values;
        double[][] mValues = {
                new double[]{10,10,0,0.5},
                new double[]{30,30,25,0.2},
                new double[]{20,2,12,0.1}
        };
        double a = 70;
        double b = 80;
        double c = 100;
        Vector vector = new Vector(a,b,c);
        values = vector.getmDoubleVec();
        System.out.println("V -> {"+values[0]+","+values[1]+","+values[2]+"}");

        vector = RotationMatrix.multiplyMatrixByVector(mValues,vector);

        values = vector.getmDoubleVec();
        System.out.println("V -> {"+values[0]+","+values[1]+","+values[2]+"}");
        */

/*
        double[][] matrix = {
                new double[]{8,5,2},
                new double[]{17,23,15}
        };

        double[][] trans = RotationMatrix.transpositionMatrix(matrix);


        Matrix.printMatrix(matrix);
        System.out.println("New:\n");
        Matrix.printMatrix(trans);*/
        //////////////////////////////////////////////////////////
//        int n = 2000;
//
//        int sol = 0;
//        int j = 0;
//
//        boolean is = false;
//
//        for( int k = 0; is = true && j != 1000000; k++){
//            j++;
//            sol += (int)m.getBinCE(n,k);
////            System.out.println("Solution "+(k+1)+" :"+sol);
//            if( Math.pow(2, n) == sol){
//                System.out.println("Stimmt bei l="+k);
//                is = true;
//            }
//
//            if(j == 1000000){
//                System.err.println("Fertig");
//                is = true;
//            }
//        }
        /////////////////////////////////////////////////////////

    }


}
