package level2;

import java.util.Arrays;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

public class TestGaussian {
	
	public static double [][] addToArray(double [][] array, double [] element)
	{
		double [][] newarray = new double [array.length+1][array[0].length];
		
		for (int i = 0;i < array.length;i++)
		{	
			newarray[i] = array[i];
			
		}
			newarray[array.length] = element;
			return newarray;
			
	}
	
	public static double[] arrayMeans(double[][] array)
	{	
		double[] means = new double[array[0].length];
		for (int i = 0; i < array.length;i++)
		{
			for(int j = 0; j <array[i].length;j++ )
			{
				means[j] += array[i][j];
			}
		}
		for (int i = 0; i< means.length;i++)
		{
			means[i] = means[i]/array.length;
		}
		return means;
	}

	public static void main(String[] args)
    {
		  double [] means = {1,1,1};
		 
		  double [][]cov = 			{{9,0,0},
				 					{0,4,0},
		 							{0,0,4}};
		  double [][]covv = {{1,2,3}};
		 double [][]covplus = addToArray(covv,means);
		 System.out.println(Arrays.toString(arrayMeans(covplus)));
		 System.out.println(Arrays.deepToString(covplus));
		  double [][] meansmeans = {means,means}; 
		  Covariance cov2 = new Covariance(cov);
		  RealMatrix matrix = new Array2DRowRealMatrix(cov);
			
			//Covariance cov2 = new Covariance(cov);
			System.out.println("aaaaaaaa");
			System.out.println(Arrays.deepToString(cov2.getCovarianceMatrix().getData()));
			
			System.out.println(Arrays.deepToString(matrix.getData()));
		  MultivariateNormalDistribution MND = new MultivariateNormalDistribution(means,cov);//2.getCovarianceMatrix().getData());//cov2.getCovarianceMatrix().getData());
		  
		  System.out.println(Arrays.toString(MND.getStandardDeviations()));
		  double[][] kaas = MND.sample(10);
		  //double[][] haas =;
		  
		  System.out.println(Arrays.deepToString(kaas));//MND.getCovariances().getData()));
		  System.out.println(cov2.getCovarianceMatrix().toString());
		  

    }
}
