package settings;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserSettings 
{	
	private static String lastConfiguration = "lastConf.xml";
	
	//private SettingsField latitudeField = new SettingsField();
	//private SettingsField longitudeField = new SettingsField();
	private SettingsField altitudeField = new SettingsField();
	private SettingsField speedField = new SettingsField();
	private SettingsField directionField = new SettingsField();
	private SettingsField yawField = new SettingsField();
	private SettingsField pitchField = new SettingsField();
	private SettingsField rollField = new SettingsField();
	private SettingsField throttleField = new SettingsField();
	private SettingsField rudderField = new SettingsField();
	private SettingsField joystickXField = new SettingsField();
	private SettingsField joystickYField = new SettingsField();
	//private SettingsField aileronField = new SettingsField();
	private int samplesPerSecond;
	private String validFlightFilePath;
	private String anomalyDetectionAlgoFilePath;
	
	/*
	public SettingsField getLatitudeField() {
		return latitudeField;
	}

	public void setLatitudeField(SettingsField latitudeField) {
		this.latitudeField = latitudeField;
	}

	public SettingsField getLongitudeField() {
		return longitudeField;
	}

	public void setLongitudeField(SettingsField longitudeField) {
		this.longitudeField = longitudeField;
	}
	*/
	
	public SettingsField getAltitudeField() {
		return altitudeField;
	}

	public void setAltitudeField(SettingsField altitudeField) {
		this.altitudeField = altitudeField;
	}

	public SettingsField getSpeedField() {
		return speedField;
	}

	public void setSpeedField(SettingsField speedField) {
		this.speedField = speedField;
	}

	public SettingsField getDirectionField() {
		return directionField;
	}

	public void setDirectionField(SettingsField directionField) {
		this.directionField = directionField;
	}

	public SettingsField getYawField() {
		return yawField;
	}

	public void setYawField(SettingsField yawField) {
		this.yawField = yawField;
	}

	public SettingsField getPitchField() {
		return pitchField;
	}

	public void setPitchField(SettingsField pitchField) {
		this.pitchField = pitchField;
	}

	public SettingsField getRollField() {
		return rollField;
	}

	public void setRollField(SettingsField rollField) {
		this.rollField = rollField;
	}

	public SettingsField getThrottleField() {
		return throttleField;
	}

	public void setThrottleField(SettingsField throttleField) {
		this.throttleField = throttleField;
	}
	
	public SettingsField getRudderField() {
		return rudderField;
	}
	
	public void setRudderField(SettingsField rudderField) {
		this.rudderField = rudderField;
	}
	
	public SettingsField getJoystickXField() {
		return joystickXField;
	}

	public void setJoystickXField(SettingsField joystickXField) {
		this.joystickXField = joystickXField;
	}

	public SettingsField getJoystickYField() {
		return joystickYField;
	}

	public void setJoystickYField(SettingsField joystickYField) {
		this.joystickYField = joystickYField;
	}
	
	/*
	public SettingsField getAileronField() {
		return aileronField;
	}

	public void setAileronField(SettingsField aileronField) {
		this.aileronField = aileronField;
	}
	*/
	
	public int getSamplesPerSecond() {
		return samplesPerSecond;
	}

	public void setSamplesPerSecond(int samplesPerSecond) {
		this.samplesPerSecond = samplesPerSecond;
	}

	public String getValidFlightFilePath() {
		return validFlightFilePath;
	}

	public void setValidFlightFilePath(String validFlightFilePath) {
		this.validFlightFilePath = validFlightFilePath;
	}

	public String getAnomalyDetectionAlgoFilePath() {
		return anomalyDetectionAlgoFilePath;
	}

	public void setAnomalyDetectionAlgoFilePath(String anomalyDetectionAlgoFilePath) {
		this.anomalyDetectionAlgoFilePath = anomalyDetectionAlgoFilePath;
	}
		
	
	static public void encodeXML(UserSettings settings, String fileName) {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			XMLEncoder encoder = new XMLEncoder(fos);
			encoder.writeObject(settings);
			encoder.close();
			fos.close();
			
			fos = new FileOutputStream(lastConfiguration);
			encoder = new XMLEncoder(fos);
			encoder.writeObject(settings);
			encoder.close();
			fos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static public UserSettings decodeXML(String fileName) {
		UserSettings settings = new UserSettings();
		try {
			FileInputStream fis = new FileInputStream(fileName);
			XMLDecoder decoder = new XMLDecoder(fis);
			settings = (UserSettings)decoder.readObject();
			decoder.close();
			fis.close();
		}
		//TODO: Handle exceptions in GUI.
		catch(FileNotFoundException e) {
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return settings;
	}
	
	static public UserSettings decodeXML() {
		return decodeXML(lastConfiguration);
	}
}