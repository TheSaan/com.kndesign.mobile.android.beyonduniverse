package com.thesaan.gameengine.android.handler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;


public class MathHandler
{
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
        
	/**Binomialcoefficent
	*
	*@param n
	*	amount of repetitions
	*@param k
	*	amount of successes
	*
	*/
	public long getBinCE(long n, long k){

                if(getFactorial(n) != FACTORIAL_ARGUMENT_TOO_BIG){
                    long part1 = getFactorial(n);
                    long part2 = getFactorial(k);
                    long part3 = getFactorial(n-k);
                    
                    if(k==n)
                        part3 = 1;
                    if(k==0)
                        part2 = 1;
                    
                    long bce = part1/(part2*part3);
                    
//                    System.out.println("   "+n+"!\n------------  =\t"+bce+"\n"+k+"! *("+n+"-"+k+")!");
//                    System.out.println("   "+getFactorial(n)+"\n----------------------------  =\t"+bce+"\n"+getFactorial(k)+" * "+getFactorial(n-k));
                    return bce;
                }else{
                    //System.err.println("Use getHugeBinCE(long,long)[returns BigInteger]. The current values are too big.");
                    return FACTORIAL_ARGUMENT_TOO_BIG;
                }
	}
    
	public double getSigmaOfBinCE(int n,int success,int possibilities, double expectationValue){
		double sigma = 0.0;
		
		for(int i = 0;i < n;i++){
			sigma += power((i+1)-expectationValue, 2)*getProbability(n, success, possibilities, 6);
		}
		sigma = Math.sqrt(sigma);
		
//		System.out.println("Sigma:\t"+sigma);
		return sigma;
	}
	
	public BigInteger getHugeBinCE(long n, long k){
                    BigInteger bce = getHugeFactorial(n).divide((getHugeFactorial(k).multiply(getHugeFactorial(n-k))));
//                    System.out.println("   "+n+"!\n------------  =\t"+bce+"\n"+k+"! *("+n+"-"+k+")!");
                    //System.out.println("   "+getHugeFactorial(n)+"\n----------------------------  =\t"+bce+"\n"+getHugeFactorial(k)+" * "+getHugeFactorial(n-k));
                    return bce;
        }
        
    public BigDecimal getHugeBinCE(long n, long k,int identifier){
                    BigDecimal bce = getHugeFactorial(n,BIG_DECIMAL_REQUIRED).divide((getHugeFactorial(k,BIG_DECIMAL_REQUIRED).multiply(getHugeFactorial(n-k,BIG_DECIMAL_REQUIRED))));
//                    System.out.println("   "+n+"!\n------------  =\t"+bce+"\n"+k+"! *("+n+"-"+k+")!");
                    //System.out.println("   "+getHugeFactorial(n,BIG_DECIMAL_REQUIRED)+"\n----------------------------  =\t"+bce+"\n"+getHugeFactorial(k,BIG_DECIMAL_REQUIRED)+" * "+getHugeFactorial(n-k,BIG_DECIMAL_REQUIRED));
                    return bce;
        }
	/**
     * Returns -1 if the value is less than zero.
     * @param n
     * @return 
     */
    public long getFactorial(long n){
        if(n >=0){
            if(n<= 20){
        if(n==0 || n==1)
            return n;
        long tmp = n;
	for(int i = 1;i < tmp-1;i++){
                
                n *= tmp-i;
	}
	return n;
            }else{
                //System.err.println("For factorial calculation is set with a maximum of 20.");
            return FACTORIAL_ARGUMENT_TOO_BIG;
            }
        }else{
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
    public BigInteger getHugeFactorial(long n){
        
        BigInteger result = BigInteger.ONE;
                    
        for(int i = 1;i <= n;i++){

            result = result.multiply( BigInteger.valueOf(i));
        }            
        return result;		
}
    
    public BigDecimal getHugeFactorial(long n, int identifier){
            
            BigDecimal result = BigDecimal.ONE;
                        
            for(int i = 1;i <= n;i++){

                result = result.multiply( BigDecimal.valueOf(i));
            }            
            return result;		
	}
        
	public synchronized double getProbability(int n, int k, int possibilities,int accuracy){
		alternativeDouble = null;
		//the win percentage
		double percent;
                
		BigDecimal qBig = BigDecimal.valueOf(1);
                BigDecimal pBig = BigDecimal.valueOf(1);
                
                BigDecimal poss = BigDecimal.valueOf(possibilities);
                
                if(n<=FACTORIAL_REPETITION_MAX){
                    double p =1.0/(double)possibilities;
                    double q = 1.0-p;
                    percent = getBinCE(n,k) * power(p,k) * power(q,n-k);
                    
//                    System.out.println(percent+"= ("+n+" nCr "+0+") * "+p+"^"+0+" * "+q+"^("+(n-0)+")");
//                    System.out.println(percent+"= ("+getBinCE(n,0) +" * "+power(p,0)+" * "+power(q,n-0));
                   
                    return percent;
                }else{
//                    System.out.println("poss: "+poss);
                    pBig = pBig.divide(poss,accuracy, RoundingMode.UP);
//                    System.out.println("pBig: "+pBig);
                    qBig = qBig.subtract(pBig);
//                    System.out.println("qBig: "+qBig);
//                    System.out.println("success(k): "+success);
                    pBig = pBig.pow(k);
                    qBig = qBig.pow(n-k);
                    
                    pBig = pBig.multiply(qBig);
                    
//                    System.out.println("pBig after multiplying: "+pBig);
                    
                    //MathContext mc = new MathContext(2, RoundingMode.UP);
                    
//                    System.out.println("Alternative value before setting: "+alternativeDouble);
                    
                    System.out.println("nCr= "+ getHugeBinCE(n,k,BIG_DECIMAL_REQUIRED) +" * "+pBig);
                    alternativeDouble = getHugeBinCE(n,k,BIG_DECIMAL_REQUIRED).multiply(pBig);	

//                    System.out.println("Alternative value after setting: "+alternativeDouble);
                    
//                    System.out.println("Alt.Double: "+pBig);
                    return ALTERNATIVE_DOUBLE_SET;
                }
	}
        /**
     * Please use always repetitions+1 because the zero value is counted
     * in the array and the calculation. So if you want to get 25 repetitions
     * take 26 as argument.
     * @param n
     * @param p
     * @return 
     */
    public BigDecimal[] getProbabilities(int n, int p){
        BigDecimal[] probabilities = new BigDecimal[n+1];
        
        for(int i = 0;i < n+1; i++){
            if(getProbability(n, i, p, 6) == ALTERNATIVE_DOUBLE_SET){ 
            	if(i==0)
            		System.out.println("[alt] "+alternativeDouble);
                probabilities[i] = alternativeDouble;
            }else{ 
            	if(i==0)
            		System.out.println("[alt] "+ BigDecimal.valueOf(getProbability(n, i, p, 6)));;
            		
                probabilities[i] = BigDecimal.valueOf(getProbability(n, i, p, 6));
            }
        }
        return probabilities;
    }
    
    public BigDecimal getProbabilitiesFromRange(BigDecimal[] values,int start,int end){
        BigDecimal range = BigDecimal.valueOf(0);
        
        for(int i = start; i < end; i++){
            range = range.add(values[i]);                
        }
        return range;
    }
    
    
    public double getExpValueOfBinCE(int n,int p){
        	double my = 0.0;
        	
        	for(int i = 0;i<n;i++){
        		my += (i) * getProbability(n, i, p, 4);
//        		System.out.println("µ = "+my);
        	}
        	
            return my;
        }
        
	/**
	*Different Potentiation Possibilities
	*/
	public static double power(double basis, double exponent){
		double tmp = basis;
		if(exponent==0)
			return 1;
		if(exponent==1)
			return basis;
		
		for(int i=1;i<exponent;i++){
			basis *= tmp;
                        //System.out.println(basis/tmp+"*"+tmp+"="+basis);
                        
		}
		return basis;
	}
	public static double squareRoot(double basis){
		return power(basis,0.5);
	}
    public static double squareRootX(double basis, double n){
        try {
            double r = power(basis, (1.0 / n));
            return r;
        }catch (Exception e){
            System.err.println("SqrRootX Exception "+e);
            return 0;
        }
    }

    public double getPrimitiveIntegral(){
        //TODO Integrieren
        double surface = 0;
        
        return surface;
    }
    /**
     * 
     * @param n
     * 	Repetitions.
     * @param k
     * 	Success value.
     * @param p1
     * 	Possibility value. gets devided via 1/p. So if p = 100, the function 
     * 	calculates with 0,01.
     * @param rangeStart
     * 	Start of summary of probability calculation
     * @param rangeEnd
     * 	End of summary of probability calculation
     * @param accuracy
     * 	The amount of digits behind the comma.
     */
    public void printAllValues(int n,int k,int p1,int rangeStart, int rangeEnd,int accuracy){
    	BigDecimal oneTest = new BigDecimal(0);
    	double p = 1.0/p1;
    	double q = 1.0-p;
    	double µ = getExpValueOfBinCE(n, p1);
    	double percent;
        BigDecimal[] values = getProbabilities(n, p1);
        BigDecimal currVal = BigDecimal.valueOf(0);
        

        percent = getBinCE(n,k) * power(p,(double)k) * power(q,(double)n-k);
        
        System.out.println(percent+"= ("+n+" nCr "+k+") * "+(1.0/p)+"^"+k+" * "+q+"^("+(n-k)+")");
    	System.out.println("n: "+n+"\tk: "+k+"\tp: "+p);
    	System.out.println("------------------------------------");
    	System.out.println("n ^ k ="+power(n,k));
    	System.out.println("------------------------------------");
    	System.out.println("BinCE: "+getBinCE(n, k));
    	System.out.println("------------------------------------");
    	System.out.println("BinCE(if n > 20): "+getHugeBinCE(n, k));
    	System.out.println("------------------------------------");
    	System.out.println("Binomial CE µ: "+µ);
    	System.out.println("------------------------------------");
    	System.out.println("Binomial CE sigma: "+getSigmaOfBinCE(n,k,p1, µ));
    	System.out.println("------------------------------------");
    	System.out.println("!k = !"+k+"= "+getFactorial(k));
    	System.out.println("------------------------------------");
    	System.out.println("!n = !"+n+"= "+getHugeFactorial(n)+" [for bigger factorials]");
    	System.out.println("------------------------------------");
    	System.out.println("Die Wahrscheinlichkeit bei:");
    	//System.out.println("P(X=xi)=P(X="+0+")\t= "+getProbability(n, 0, p1, 6)+"\t\tin Prozent: "+(getProbability(n, 0, p1, 6)*100)+" %");
    	 for(int i = 0; i < n+1;i++){
    		 currVal = values[i];
           System.out.println("P(X=xi)=P(X="+i+")\t= "+currVal+"\t\tin Prozent: "+values[i].multiply(BigDecimal.valueOf(100))+" %");
           oneTest = oneTest.add(currVal);               
       }
    	
    	System.out.println("Summe\t\t= "+oneTest);
    	System.out.println("------------------------------------");
    	if(getProbability(n, k, p1,accuracy) == ALTERNATIVE_DOUBLE_SET){
          System.out.println("[GROß]Die  Wahrscheinlichkeit dass bei\n"+n+" Versuchen und "+p1+" Möglichkeiten "+k+" Erfolge\nwahrscheinlich sind liegt bei "+ alternativeDouble.multiply(BigDecimal.valueOf(100))+" %.");
      }else{
          System.out.println("Die Wahrscheinlichkeit dass bei\n"+n+" Versuchen und "+p1+" Möglichkeiten "+k+" Erfolge\nwahrscheinlich sind liegt bei "+(getProbability(n, k, p1,accuracy)*100)+" %.");
      }
    	System.out.println("------------------------------------");
    	System.out.println("\n\nDie  Wahrscheinlichkeit dass bei\n"+(n)+" Versuchen und "+p1+" Möglichkeiten die Erfolge\nzwischen "+rangeStart+" und "+rangeEnd+" wahrscheinlich sind liegt bei "+(getProbabilitiesFromRange(values,rangeStart,rangeEnd).multiply(BigDecimal.valueOf(100)))+" %.");
    	
    }
    public double getDecreaseValue(double lambda){
    	double decr = 0.0;
    	return decr;
    }
    public static TranslationMatrix getTranslationMatrix(){
        return new TranslationMatrix();
    }
    public static class Matrix{
        /*----------------------------------------CONSTRUCTORS-----------------------------------*/
        float[][] mMatrix;
        public Matrix(int n,int m){
            mMatrix = new float[n][m];
        }
        public Matrix(float[] n, int m){
            
        }
        public Matrix(float[] n,float[] m){
            
        }
        public Matrix(float[][] nm){
            
        }
        public Matrix(){
            
        }
        public static void printMatrix(double[][] matrix){
            String matrixPlotter = "";
            for(int i = 0;i< matrix.length;i++){
                for(int j = 0;j<matrix[i].length;j++){
                    matrixPlotter += matrix[i][j]+" | ";
                }
                matrixPlotter += "\n";
            }

            System.out.println(matrixPlotter);
        }
        public float[][] getmMatrix() {
            return mMatrix;
        }

        public static Vector multiplyByMatrix(float[][] matrixArray, Vector vector){

            float[] v = vector.getmFloatVec();

            float[] nV = new float[v.length];

            int m = 0;
            //check column amount
            if(v.length == matrixArray.length) {

                    //column
                    for (int n = 0; n < matrixArray.length; n++) {

                        for(int k = 0; k< matrixArray.length;k++){
                            nV[n] += matrixArray[k][m]*v[k];
                        }
                        //row
                        m++;
                    }

                vector.setmFloatVec(nV);
            }else{
                System.err.println("Number of matrix columns is not equal to the number of vector data!");
                return null;
            }
            return vector;
        }
        /*----------------------------------------SETTERS-----------------------------------*/
    }
    public static class TranslationMatrix extends Matrix{
        
        
        private Matrix _2DRotationMatrix;
        private Matrix _3DRotationMatrixX;
        private Matrix _3DRotationMatrixY;
        private Matrix _3DRotationMatrixZ;

        private Matrix _ScaleMatrix;

        public final static int X_AXIS = 0;
        public final static int Y_AXIS = 1;
        public final static int Z_AXIS = 2;

        /*----------------------------------------HANDLERS-----------------------------------*/
        public static float[][] transpositionMatrix(float[][] matrix){
            int rowLength = matrix.length;
            int columnLength = matrix[0].length;

            float[][] nMatrix = new float[columnLength][rowLength];

            //column
            int n;
            //row
            int m;

            for(n = 0;n < rowLength;n++){
                for(m = 0;m < columnLength;m++) {
                    nMatrix[m][n] = matrix[n][m];
                }
            }
            return nMatrix;
        }
        /**
         *
         * @param vec
         * @param angle
         * @return
         */
        public Vector rotateVector2D(Vector vec, float angle) {
            //creates the 2d rotation matrix
            set_2DRotationMatrix(angle);

            //now get the actual matrix array for 2d rotation
            float[][] rotation;

            if (_2DRotationMatrix != null){
                rotation = _2DRotationMatrix.getmMatrix();

                vec = multiplyByMatrix(rotation, vec);
                return vec;
            }

            return vec;
        }
        public Vector rotateVector3D(Vector vec, float angle,int axis){
            float[][] matrix;
            String whichAxis;
            boolean isPhysical = true;
            switch (axis){
                case X_AXIS:
                    matrix = getXRotationMatrix(angle, isPhysical);
                    whichAxis = "x-axis";
                    break;
                case Y_AXIS:
                    matrix = getYRotationMatrix(angle,isPhysical);
                    whichAxis = "y-axis";
                    break;
                case Z_AXIS:
                    matrix = getZRotationMatrix(angle,isPhysical);
                    whichAxis = "z-axis";
                    break;
                default:
                    System.err.println("Wrong axis assignment value!");

                    whichAxis = "no-axis";
                    return null;
            }
//            System.out.println(whichAxis+"| Vector before multiplying: "+"|v|{"+vec.getmDoubleVec()[0]+","+vec.getmDoubleVec()[1]+","+vec.getmDoubleVec()[2]+"}\n<<<");
            vec = multiplyByMatrix(matrix,vec);
//            System.out.println(whichAxis+"| Vector after multiplying: "+"|v|{"+vec.getmDoubleVec()[0]+","+vec.getmDoubleVec()[1]+","+vec.getmDoubleVec()[2]+"}\n<<<");
            return vec;
        }
        public Vector scaleVector(Vector vec,float scaleFactor){
            vec = multiplyByMatrix(getScaleMatrix(scaleFactor),vec);
            return vec;
        }
        public Vector mirrorVector(Vector vec,int axis){

            return null;
        }
        /*----------------------------------------SETTERS-----------------------------------*/
        private void set_2DRotationMatrix(float angle){
            _2DRotationMatrix = new Matrix(2,2);
            float[][] rotMatrix = _2DRotationMatrix.getmMatrix();

            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            rotMatrix[0][0] = cos;
            rotMatrix[0][1] = sin;
            rotMatrix[1][0] = -sin;
            rotMatrix[0][1] = cos;
        }
        /*----------------------------------------GETTERS-----------------------------------*/
        private float[][] getXRotationMatrix(float angle,boolean isCoordinateSystemChanged){
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            if(isCoordinateSystemChanged)
                sin *= -1;

            float[][] m = {
                    new float[]{1,0,0},
                    new float[]{0,cos,sin},
                    new float[]{0,-sin,cos}
            };

            return m;
        }
        private float[][] getYRotationMatrix(float angle,boolean isCoordinateSystemChanged){
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            if(isCoordinateSystemChanged)
                sin *= -1;

            float[][] m = {
                    new float[]{cos,0,-sin},
                    new float[]{0,1,0},
                    new float[]{sin,0,cos}
            };

            return m;
        }
        private float[][] getZRotationMatrix(float angle,boolean isCoordinateSystemChanged) {
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            if (isCoordinateSystemChanged)
                sin *= -1;

            float[][] m = {
                    new float[]{cos, sin, 0},
                    new float[]{-sin, cos, 0},
                    new float[]{0, 0, 1}
            };

            return m;
        }
        private double[][] getQuaternionRotationMatrix(float angle, Vector vec){
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);

            double[] pos = vec.getmDoubleVec();

            double x = pos[0];
            double y = pos[1];
            double z = pos[2];

            double halfAngle = angle/2;

            double xSq = Math.pow(x, 2);
            double ySq = Math.pow(y, 2);
            double zSq = Math.pow(z, 2);

            double a = Math.cos(halfAngle);
            double b = x* Math.sin(halfAngle);
            double c = y* Math.sin(halfAngle);
            double d = z* Math.sin(halfAngle);
            double f = 1- Math.cos(angle);

            double[][] m = {
                    new double[]{cos+xSq*f,z*sin+x*y*f,-y*sin+x*z*f},
                    new double[]{-z*sin+x*y*f,cos+ySq*f,x*sin+y*z*f},
                    new double[]{y*sin+x*z*f,-x*sin+y*z*f,cos+zSq*f}
            };

            return m;
        }

        /**
         * Rotation around the origin
         * @param angle
         * @param origin
         * @return
         */
        private float[][] getNRotationMatrix(float angle, Vector origin){
            float n1,n2,n3;
            n1 = origin.getXf();
            n2 = origin.getYf();
            n3 = origin.getZf();


            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);


            float[][] m = {
                    new float[]{
                            (float) Math.pow(n1, 2)*(1-cos)+cos,
                            n2*n1*(1-cos)+n3*sin,
                            n3*n1*(1-cos)-n2*sin
                    },
                    new float[]{
                            n1*n2*(1-cos)-n3*sin,
                            (float) Math.pow(n2, 2)*(1-cos)+cos,
                            n3*n2*(1-cos)+n1*sin
                    },
                    new float[]{
                            n1*n3*(1-cos)+n2*sin,
                            n2*n3*(1-cos)-n1*sin,
                            (float) Math.pow(n3, 2)*(1-cos)+cos
                    }
            };
            return m;
        }

        private float[][] getScaleMatrix(float scaleFactor){

            return
                    new float[][]{
                            new float[]{scaleFactor,0,0},
                            new float[]{0,scaleFactor,0},
                            new float[]{0,0,scaleFactor}
                     };
        }

    }
    public static class Vector{
        /*----------------------------------------CONSTRUCTORS-----------------------------------*/
        private int[] mIntVec;
        private float[] mFloatVec;
        private double[] mDoubleVec;

        private boolean isInt = false;
        private boolean isFloat = false;
        private boolean isDouble = false;

        private Vector screenCenter;

        public Vector(int x, int y, int z){
            mIntVec = new int[3];
            mIntVec[0] = x;
            mIntVec[1] = y;
            mIntVec[2] = z;
            isInt = true;
        }
        public Vector(float x, float y, float z){
            mFloatVec = new float[3];
            mFloatVec[0] = x;
            mFloatVec[1] = y;
            mFloatVec[2] = z;
            isFloat = true;
        }
        public Vector(double x, double y, double z){
            mDoubleVec = new double[3];
            mDoubleVec[0] = x;
            mDoubleVec[1] = y;
            mDoubleVec[2] = z;
            isDouble = true;
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
        public double getLength(){
            if(isInt){
                return Math.sqrt((Math.pow(mIntVec[0], 2) + Math.pow(mIntVec[1], 2) + Math.pow(mIntVec[2], 2)));
            }else
            if(isFloat){
                return Math.sqrt((Math.pow(mFloatVec[0], 2) + Math.pow(mFloatVec[1], 2) + Math.pow(mFloatVec[2], 2)));
            }else
            if(isDouble){
                return Math.sqrt((Math.pow(mDoubleVec[0], 2) + Math.pow(mDoubleVec[1], 2) + Math.pow(mDoubleVec[2], 2)));
            }else
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
        public Vector subtractWith(Vector vector){
            if(isInt){
                int[] i = this.getmIntVec();
                int[] i2 = vector.getmIntVec();
                i[0] -= i2[0];
                i[1] -= i2[1];
                i[2] -= i2[2];

                setmIntVec(i);
                return this;
            }else
            if(isFloat){
                float[] i = this.getmFloatVec();
                float[] i2 = vector.getmFloatVec();
                i[0] -= i2[0];
                i[1] -= i2[1];
                i[2] -= i2[2];

                setmFloatVec(i);
                return this;
            }else
            if(isDouble){
                double[] i = this.getmDoubleVec();
                double[] i2 = vector.getmDoubleVec();
                i[0] -= i2[0];
                i[1] -= i2[1];
                i[2] -= i2[2];

                setmDoubleVec(i);
                return this;
            }else
                return null;
        }
        public Vector addWith(Vector vector){
            if(isInt){
                int[] i = this.getmIntVec();
                int[] i2 = vector.getmIntVec();
                i[0] += i2[0];
                i[1] += i2[1];
                i[2] += i2[2];

                setmIntVec(i);
                return this;
            }else
            if(isFloat){
                float[] i = this.getmFloatVec();
                float[] i2 = vector.getmFloatVec();
                i[0] += i2[0];
                i[1] += i2[1];
                i[2] += i2[2];

                setmFloatVec(i);
                return this;
            }else
            if(isDouble){
                double[] i = this.getmDoubleVec();
                double[] i2 = vector.getmDoubleVec();
                i[0] += i2[0];
                i[1] += i2[1];
                i[2] += i2[2];

                setmDoubleVec(i);
                return this;
            }else
                return null;
        }
        public void multiplyBy(float mulitplier){
            if(isInt){
                setX((int)(mulitplier*getXi()));
                setY((int) (mulitplier * getYi()));
                setZ((int) (mulitplier * getZi()));
            }else
            if(isFloat){
                setX((int)(mulitplier*getXf()));
                setY((int)(mulitplier*getYf()));
                setZ((int) (mulitplier * getZf()));
            }else
            if(isDouble){
                setX((int)(mulitplier*getXd()));
                setY((int)(mulitplier*getYd()));
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
        public void setX(int x){
            if(mIntVec != null){
                mIntVec[0]=x;
            }else{
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
        public void setX(double x){
            if (mDoubleVec != null) {
                mDoubleVec[0] = x;
            } else {
                System.err.println("[Vector.setX] mDoubleVec is null");
            }
        }
        public void setY(int y){
            if (mIntVec != null) {
                mIntVec[1] = y;
            } else {
                System.err.println("[Vector.setY] mIntVec is null");
            }
        }
        public void setY(float y){
        if (mFloatVec != null) {
            mFloatVec[1] = y;
        } else {
            System.err.println("[Vector.setY] mFloatVec is null");
        }
    }
        public void setY(double y){
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
        public void setZ(float z){
            if (mFloatVec != null) {
                mFloatVec[2] = z;
            } else {
                System.err.println("[Vector.setZ] mFloatVec is null");
            }
        }
        public void setZ(double z){
            if (mDoubleVec != null) {
                mDoubleVec[2] = z;
            } else {
                System.err.println("[Vector.setZ] mDoubleVec is null");
            }
        }

        public void setScreenCenter(Vector screenCenter) {
            this.screenCenter = screenCenter;
        }

        public Vector getScreenCenter() {
            return screenCenter;
        }
        public void setToScreenOrigin(){
            subtractWith(screenCenter);
        }
        public void setToCenter(){
            addWith(screenCenter);
        }

        public int getXi(){
            if(isInt()){
                return mIntVec[0];
            }else{
                String is = "NO VALUE";
                if(isDouble())is = "DOUBLE";
                if(isFloat())is = "FLOAT";

                System.err.println("Vector is no Integer! It's a "+is);
                return -1;
            }

        }
        public float getXf() {
            if(isFloat()){
                return mFloatVec[0];
            }else{
                String is = "NO VALUE";
                if(isDouble())is = "DOUBLE";
                if(isInt())is = "INT";

                System.err.println("Vector is no Integer! It's a "+is);
                return -1;
            }
        }
        public double getXd(){
            if(isDouble()){
                return mDoubleVec[1];
            }else{
                String is = "NO VALUE";
                if(isInt())is = "INT";
                if(isFloat())is = "FLOAT";

                System.err.println("Vector is no Integer! It's a "+is);
                return -1;
            }
        }
        public int getYi(){
            if(isInt()){
                return mIntVec[1];
            }else{
                String is = "NO VALUE";
                if(isDouble())is = "DOUBLE";
                if(isFloat())is = "FLOAT";

                System.err.println("Vector is no Integer! It's a "+is);
                return -1;
            }

        }
        public float getYf() {
            if(isFloat()){
                return mFloatVec[1];
            }else{
                String is = "NO VALUE";
                if(isDouble())is = "DOUBLE";
                if(isInt())is = "INT";

                System.err.println("Vector is no Integer! It's a "+is);
                return -1;
            }
        }
        public double getYd(){
            if(isDouble()){
                return mDoubleVec[1];
            }else{
                String is = "NO VALUE";
                if(isInt())is = "INT";
                if(isFloat())is = "FLOAT";

                System.err.println("Vector is no Integer! It's a "+is);
                return -1;
            }
        }
        public int getZi(){
            if(isInt()){
                return mIntVec[2];
            }else{
                String is = "NO VALUE";
                if(isDouble())is = "DOUBLE";
                if(isFloat())is = "FLOAT";

                System.err.println("Vector is no Integer! It's a "+is);
                return -1;
            }

        }
        public float getZf() {
            if(isFloat()){
                return mFloatVec[2];
            }else{
                String is = "NO VALUE";
                if(isDouble())is = "DOUBLE";
                if(isInt())is = "INT";

                System.err.println("Vector is no Integer! It's a "+is);
                return -1;
            }
        }
        public double getZd(){
            if(isDouble()){
                return mDoubleVec[2];
            }else{
                String is = "NO VALUE";
                if(isInt())is = "INT";
                if(isFloat())is = "FLOAT";

                System.err.println("Vector is no Integer! It's a "+is);
                return -1;
            }
        }


    }
    public static void main(String[] args){
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

        vector = RotationMatrix.multiplyByMatrix(mValues,vector);

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


   
    }


        
        
	
}
