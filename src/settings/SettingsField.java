package settings;

public class SettingsField 
{
	
	private String featureName;
	private float maxVal;
	private float minVal;
	
	public SettingsField() 
	{
	}
	
	public SettingsField(String featureName, float maxVal, float minVal) 
	{
		this.featureName = featureName;
		this.maxVal = maxVal;
		this.minVal = minVal;
	}
	
	public String getFeatureName() 
	{
		return featureName;
	}
	
	public void setFeatureName(String featureName) 
	{
		this.featureName = featureName;
	}
	
	public float getMaxVal() 
	{
		return maxVal;
	}
	
	public void setMaxVal(float maxVal) 
	{
		this.maxVal = maxVal;
	}
	
	public float getMinVal() 
	{
		return minVal;
	}
	
	public void setMinVal(float minVal) 
	{
		this.minVal = minVal;
	}	
}