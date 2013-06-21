package level2;

import java.util.Arrays;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.stat.correlation.Covariance;

public class TestGaussian {
	
	

	public static void main(String[] args)
    {
		  double [] means = {1,1,1};
		 
		  double [][]cov = 			{{1,0,0},
				 					{0,100,0},
		 							{0,0,10}};
		  Covariance cov2 = new Covariance(cov);

		  MultivariateNormalDistribution MND = new MultivariateNormalDistribution(means,cov);//cov2.getCovarianceMatrix().getData());
		  
		  System.out.println(Arrays.toString(MND.getStandardDeviations()));
		  double[][] kaas = MND.sample(10);
		  //double[][] haas =;
		  
		  System.out.println(Arrays.deepToString(kaas));//MND.getCovariances().getData()));
		  System.out.println(cov2.getCovarianceMatrix().toString());
		  

    }
}
