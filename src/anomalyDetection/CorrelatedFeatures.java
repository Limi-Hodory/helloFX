package anomalyDetection;

public class CorrelatedFeatures
{
	public final String feature1;
	public final String feature2;
	public final float correlation;
	public final Line lin_reg;
	public final float threshold;
	
	public CorrelatedFeatures(String feature1, String feature2, float correlation, Line lin_reg, float threshold)
	{
		this.feature1 = feature1;
		this.feature2 = feature2;
		this.correlation = correlation;
		this.lin_reg = lin_reg;
		this.threshold = threshold;
	}

	public CorrelatedFeatures(String feature1, String feature2, float correlation)
	{
		this.feature1 = feature1;
		this.feature2 = feature2;
		this.correlation = correlation;
		lin_reg = null;
		threshold = 0f;
	}

}