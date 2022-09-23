package com.team1671.frc.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.sensors.CANCoder;
import com.team1671.frc.Constants;
import com.team1671.frc.Ports;
import com.team1671.frc.Settings;
import com.team1671.frc.loops.ILooper;
import com.team1671.lib.util.Util;
import com.team254.drivers.LazyTalonFX;
import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Rotation2d;
import com.team254.lib.geometry.Translation2d;
import com.wpilib.SwerveModuleState;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SwerveDriveModule extends Subsystem{
	LazyTalonFX rotationMotor, driveMotor;
	CANCoder rotationEncoder;
	int moduleID;
	String name = "Module ";
	int rotationSetpoint = 0;
	double driveSetpoint = 0;
	double encoderOffset;
	int encoderReverseFactor = 1;
	boolean isRotationEncoderFlipped = false;
	boolean rotationMotorZeroed = false;
	boolean useDriveEncoder = true;
	boolean tenVoltRotationMode = false;
	boolean moduleZeroedWitoutMagEnc = false;
	private double previousEncDistance = 0;
	private Translation2d position;
	private Translation2d startingPosition;
	private Pose2d estimatedRobotPose = new Pose2d();
	boolean standardCarpetDirection = true;
	public void setCarpetDirection(boolean standardDirection){
		standardCarpetDirection = standardDirection;
	}
	
	PeriodicIO periodicIO = new PeriodicIO();
	
	public SwerveDriveModule(int rotationSlot, int driveSlot, int moduleID, 
	double encoderOffset, Translation2d startingPose, boolean flipMagEncoder){
		name += (moduleID + " ");
		rotationMotor = new LazyTalonFX(rotationSlot);
		driveMotor = new LazyTalonFX(driveSlot);
		if (RobotBase.isReal())
			rotationEncoder = new CANCoder(Ports.kModuleEncoders[moduleID]);
			//new DutyCycle(new DigitalInput(Ports.kModuleEncoders[moduleID]));
		this.encoderOffset = encoderOffset;
		this.isRotationEncoderFlipped = flipMagEncoder;
		configureMotors();
		this.moduleID = moduleID;
		previousEncDistance = 0;
		position = startingPose;
		this.startingPosition = startingPose;
	}
	
	public synchronized void invertDriveMotor(TalonFXInvertType invertType){
		driveMotor.setInverted(invertType);
	}

	public synchronized void invertDriveMotor(boolean invert) {
		driveMotor.setInverted(invert);
	}
	
	public synchronized void invertRotationMotor(TalonFXInvertType invertType){
		rotationMotor.setInverted(invertType);
	}

	public synchronized void reverseRotationMagEncoder(boolean invert) {
		this.isRotationEncoderFlipped = invert;
	}
	
	public synchronized void reverseDriveSensor(boolean reverse){
		driveMotor.setSensorPhase(reverse);
	}
	
	public synchronized void reverseRotationSensor(boolean reverse){
		encoderReverseFactor = reverse ? -1 : 1;
		rotationMotor.setSensorPhase(reverse);
	}
	
	public synchronized void setNominalDriveOutput(double voltage){
		driveMotor.configNominalOutputForward(voltage / 12.0, 10);
		driveMotor.configNominalOutputReverse(-voltage / 12.0, 10);
	}
	
	public synchronized void setMaxRotationSpeed(double maxSpeed){
		rotationMotor.configMotionCruiseVelocity((int)maxSpeed, 0);
	}
	
	public synchronized void disableDriveEncoder(){
		useDriveEncoder = false;
	}
	
	private void configureMotors(){

		rotationMotor.configFactoryDefault();
		driveMotor.configFactoryDefault();

		rotationMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
		/*
		rotationMotor.setSensorPhase(true);
		rotationMotor.setInverted(false);
		*/
		rotationMotor.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20, 10);
		rotationMotor.setNeutralMode(NeutralMode.Brake);
		rotationMotor.configVoltageCompSaturation(7.0, 10);
		rotationMotor.enableVoltageCompensation(true);
		rotationMotor.configAllowableClosedloopError(0, 0, 10);
		rotationMotor.configMotionAcceleration((int)(Constants.kSwerveRotationMaxSpeed*12.5), 10);
		rotationMotor.configMotionCruiseVelocity((int)(Constants.kSwerveRotationMaxSpeed), 10);
		rotationMotor.selectProfileSlot(0, 0);
		rotationMotor.configAllowableClosedloopError(0, 50, Constants.kCANTimeoutMs);
		//Slot 1 is for normal use
		rotationMotor.config_kP(0, Constants.kSwerveRotationP0, 10); // 1.55
		rotationMotor.config_kI(0, Constants.kSwerveRotationI0, 10);
		rotationMotor.config_kD(0, Constants.kSwerveRotationD0, 10); // 5.0
		rotationMotor.config_kF(0, 1023.0/Constants.kSwerveRotationMaxSpeed, 10);
		//Slot 2 is reserved for the beginning of auto
		rotationMotor.config_kP(1, Constants.kSwerveRotationP1, 10);
		rotationMotor.config_kI(1, Constants.kSwerveRotationI1, 10);
		rotationMotor.config_kD(1, Constants.kSwerveRotationD1, 10);
		rotationMotor.config_kF(1, 1023.0/Constants.kSwerveRotation10VoltMaxSpeed, 10);
		rotationMotor.configAllowableClosedloopError(0, 50, Constants.kCANTimeoutMs);
		rotationMotor.set(ControlMode.MotionMagic, rotationMotor.getSelectedSensorPosition(0));
		if(!isRotationSensorConnected()){
			DriverStation.reportError(name + "rotation encoder not detected!", false);
			hasEmergency = true;
		}
		resetRotationToAbsolute();
		

		//driveMotor.enableLogging(true);
		driveMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
		driveMotor.setSelectedSensorPosition(0, 0, 10);
		driveMotor.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20, 10);
		driveMotor.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_10Ms, 10);
		driveMotor.configVelocityMeasurementWindow(32, 10);
		driveMotor.configNominalOutputForward(0/12.0, 10);
		driveMotor.configNominalOutputReverse(0/12.0, 10);
		driveMotor.configVoltageCompSaturation(12.0, 10);
		driveMotor.enableVoltageCompensation(true);
		driveMotor.configOpenloopRamp(0.5, 10);
		driveMotor.configClosedloopRamp(0.0);
		driveMotor.configAllowableClosedloopError(0, 0, 10);
		/*
		driveMotor.setInverted(true);
		driveMotor.setSensorPhase(true);
		*/
		driveMotor.setNeutralMode(NeutralMode.Brake);
		// Slot 0 is reserved for MotionMagic
		driveMotor.selectProfileSlot(0, 0);
		driveMotor.config_kP(0, Constants.kSwerveDriveP0, 10);
		driveMotor.config_kI(0, Constants.kSwerveDriveI0, 10);
		driveMotor.config_kD(0, Constants.kSwerveDriveD0, 10);
		driveMotor.config_kF(0, 1023.0/Constants.kSwerveDriveMaxSpeed, 10);
		driveMotor.configMotionCruiseVelocity((int)(Constants.kSwerveDriveMaxSpeed*0.9), 10);
		driveMotor.configMotionAcceleration((int)(Constants.kSwerveDriveMaxSpeed), 10);
		// Slot 1 corresponds to velocity mode
		driveMotor.config_kP(1, Constants.kSwerveDriveP1, 10);
		driveMotor.config_kI(1, Constants.kSwerveDriveI1, 10);
		driveMotor.config_kD(1, Constants.kSwerveDriveD1, 10);
		driveMotor.config_kF(1, 1023.0/Constants.kSwerveDriveMaxSpeed, 10);
		if(!isDriveSensorConnected()){
			DriverStation.reportError(name + "drive encoder not detected!", false);
			hasEmergency = true;
		}
	}
	
	private boolean isRotationSensorConnected(){
		// if(RobotBase.isReal()){
		// 	return rotationEncoder.getFrequency() != 0;
		// }
		return true;
	}
	
	private boolean isDriveSensorConnected(){
		/*if(RobotBase.isReal()){
			int pulseWidthPeriod = driveMotor.getSensorCollection().getPulseWidthRiseToRiseUs();
			return pulseWidthPeriod != 0;
		}*/
		return true;
	}
	
	private double getRawAngle(){
		return encUnitsToDegrees(periodicIO.rotationPosition);
	}
	
	public Rotation2d getModuleAngle(){
		return Rotation2d.fromDegrees(getRawAngle());
	}
	
	public Rotation2d getFieldCentricAngle(Rotation2d robotHeading){
		Rotation2d normalizedAngle = getModuleAngle();
		return normalizedAngle.rotateBy(robotHeading);
	}
	
	public void setModuleAngle(double goalAngle){
		double newAngle = Util.placeInAppropriate0To360Scope(getRawAngle(), goalAngle);
		int setpoint = degreesToEncUnits(newAngle);
		periodicIO.rotationControlMode = ControlMode.MotionMagic;
		periodicIO.rotationDemand = setpoint;
	}
	
	public boolean angleOnTarget(){
		double error = encUnitsToDegrees(Math.abs(rotationMotor.getClosedLoopError(0)));
		return error < 4.5;
	}
	
	public void set10VoltRotationMode(boolean tenVolts){
		if(tenVolts && !tenVoltRotationMode){
			rotationMotor.selectProfileSlot(1, 0);
			rotationMotor.configVoltageCompSaturation(10.0, 10);
			tenVoltRotationMode = true;
		}else if(!tenVolts && tenVoltRotationMode){
			rotationMotor.selectProfileSlot(0, 0);
			rotationMotor.configVoltageCompSaturation(7.0, 10);
			tenVoltRotationMode = false;
		}
	}
	
	public void setRotationOpenLoop(double power){
		periodicIO.rotationControlMode = ControlMode.PercentOutput;
		periodicIO.rotationDemand = power;
	}
	
	/**
	* @param velocity Normalized value
	*/
	public void setDriveOpenLoop(double velocity){
		periodicIO.driveControlMode = ControlMode.PercentOutput;
		periodicIO.driveDemand = velocity;
	}
	
	public void setDrivePositionTarget(double deltaDistanceInches){
		driveMotor.selectProfileSlot(0, 0);
		periodicIO.driveControlMode = ControlMode.MotionMagic;
		periodicIO.driveDemand = periodicIO.drivePosition + inchesToEncUnits(deltaDistanceInches);
	}
	
	public boolean drivePositionOnTarget(){
		if(driveMotor.getControlMode() == ControlMode.MotionMagic)
		return encUnitsToInches((int)Math.abs(periodicIO.driveDemand - periodicIO.drivePosition)) < 2.0;
		return false;
	}
	
	public void setVelocitySetpoint(double inchesPerSecond){
		driveMotor.selectProfileSlot(1, 0);
		periodicIO.driveControlMode = ControlMode.Velocity;
		periodicIO.driveDemand = inchesPerSecondToEncVelocity(inchesPerSecond);
	}

	private double getAbsoluteEncoderDegrees() {
		if (!RobotBase.isReal())
			return 0.0;
		return (isRotationEncoderFlipped ? -1.0 : 1.0) * rotationEncoder.getAbsolutePosition();
	}
	
	private double getDriveDistanceInches(){
		return encUnitsToInches(periodicIO.drivePosition);
	}
	
	public double encUnitsToInches(double encUnits){
		return encUnits/Constants.kSwerveEncUnitsPerInch;
	}
	
	public int inchesToEncUnits(double inches){
		return (int) (inches*Constants.kSwerveEncUnitsPerInch);
	}
	
	public double encVelocityToInchesPerSecond(double encUnitsPer100ms){
		return encUnitsToInches(encUnitsPer100ms) * 10;
	}
	
	public int inchesPerSecondToEncVelocity(double inchesPerSecond){
		return (int) (inchesToEncUnits(inchesPerSecond / 10.0));
	}
	
	
	public int degreesToEncUnits(double degrees){
		return (int) ((degrees / 360.0) * Constants.kSwerveRotationReduction * Constants.kSwerveRotationEncoderResolution);
	}
	
	public double encUnitsToDegrees(double encUnits){
		return (encUnits / Constants.kSwerveRotationEncoderResolution) / Constants.kSwerveRotationReduction * 360.0;
	}
	
	public Translation2d getPosition(){
		return position;
	}
	
	public Pose2d getEstimatedRobotPose(){
		return estimatedRobotPose;
	}
	
	public SwerveModuleState getState() {
		return new SwerveModuleState(encVelocityToInchesPerSecond(periodicIO.velocity), getModuleAngle());
	}
	
	public synchronized void updatePose(Rotation2d robotHeading){
		double currentEncDistance = getDriveDistanceInches();
		double deltaEncDistance = (currentEncDistance - previousEncDistance) * Constants.kWheelScrubFactors[moduleID];
		Rotation2d currentWheelAngle = getFieldCentricAngle(robotHeading);
		Translation2d deltaPosition = new Translation2d(currentWheelAngle.cos()*deltaEncDistance, 
		currentWheelAngle.sin()*deltaEncDistance);
		
		double xScrubFactor = Constants.kXScrubFactor;
		double yScrubFactor = Constants.kYScrubFactor;
		if(Constants.kSimulateReversedCarpet){
			if(Util.epsilonEquals(Math.signum(deltaPosition.x()), 1.0)){
				if(standardCarpetDirection){
					xScrubFactor = 1.0 / Constants.kXScrubFactor;
				}else{
					xScrubFactor = 1.0;
				}
			}else{
				if(standardCarpetDirection){
					xScrubFactor = Constants.kXScrubFactor * Constants.kXScrubFactor;
				}else{
					
				}
			}
			if(Util.epsilonEquals(Math.signum(deltaPosition.y()), 1.0)){
				if(standardCarpetDirection){
					yScrubFactor = 1.0 / Constants.kYScrubFactor;
				}else{
					yScrubFactor = 1.0;
				}
			}else{
				if(standardCarpetDirection){
					yScrubFactor = Constants.kYScrubFactor * Constants.kYScrubFactor;
				}else{
					
				}
			}
		}else{
			if(Util.epsilonEquals(Math.signum(deltaPosition.x()), 1.0)){
				if(standardCarpetDirection){
					xScrubFactor = 1.0;
				}else{
					
				}
			}else{
				if(standardCarpetDirection){
					
				}else{
					xScrubFactor = 1.0;
				}
			}
			if(Util.epsilonEquals(Math.signum(deltaPosition.y()), 1.0)){
				if(standardCarpetDirection){
					yScrubFactor = 1.0;
				}else{
					
				}
			}else{
				if(standardCarpetDirection){
					
				}else{
					yScrubFactor = 1.0;
				}
			}
		}
		
		deltaPosition = new Translation2d(deltaPosition.x() * xScrubFactor,
		deltaPosition.y() * yScrubFactor);
		Translation2d updatedPosition = position.translateBy(deltaPosition);
		Pose2d staticWheelPose = new Pose2d(updatedPosition, robotHeading);
		Pose2d robotPose = staticWheelPose.transformBy(Pose2d.fromTranslation(startingPosition).inverse());
		position = updatedPosition;
		estimatedRobotPose =  robotPose;
		previousEncDistance = currentEncDistance;
	}
	
	public synchronized void resetPose(Pose2d robotPose){
		Translation2d modulePosition = robotPose.transformBy(Pose2d.fromTranslation(startingPosition)).getTranslation();
		position = modulePosition;
	}
	
	public synchronized void resetPose(){
		position = startingPosition;
	}
	
	public synchronized void resetLastEncoderReading(){
		previousEncDistance = getDriveDistanceInches();
	}
	
	@Override
	public synchronized void readPeriodicInputs() {
		periodicIO.velocity = driveMotor.getSelectedSensorVelocity(0);
		periodicIO.rotationPosition = rotationMotor.getSelectedSensorPosition(0);
		if(useDriveEncoder) periodicIO.drivePosition = driveMotor.getSelectedSensorPosition(0);

		if (Settings.debugSwerve()) {
			periodicIO.driveVoltage = driveMotor.getMotorOutputVoltage();
		}
	}
	
	@Override
	public synchronized void writePeriodicOutputs() {
		rotationMotor.set(periodicIO.rotationControlMode, periodicIO.rotationDemand);
		driveMotor.set(periodicIO.driveControlMode, periodicIO.driveDemand);
	}
	
	@Override
	public synchronized void stop(){
		setDriveOpenLoop(0.0);
		setModuleAngle(getModuleAngle().getDegrees());
	}
	
	public synchronized void disable(){
		setDriveOpenLoop(0.0);
		setRotationOpenLoop(0.0);
	}
	
	// int zeroCount = 0;
	// public synchronized void resetRotationToAbsolute(){
	// 	if (!rotationMotorZeroed) {
	// 		ErrorCode rotationFalconCheck = rotationMotor.setSelectedSensorPosition(0);
	// 		if (zeroCount < 50) {
	// 			if (rotationFalconCheck == ErrorCode.OK) {
	// 				if (isRotationSensorConnected() && RobotBase.isReal()) {
	// 					rotationMotor.setSelectedSensorPosition(degreesToEncUnits(getAbsoluteEncoderDegrees() - encoderOffset), 0, 10);
	// 					moduleZeroedWitoutMagEnc = false;
	// 					//System.out.println(name + "Absolute angle: " + getAbsoluteEncoderDegrees() + ", encoder offset: " + encoderOffset + ", difference: " + (getAbsoluteEncoderDegrees() - encoderOffset) + ", degreesToEncUnits: " + degreesToEncUnits(getAbsoluteEncoderDegrees() - encoderOffset));
	// 				} else {
	// 					rotationMotor.setSelectedSensorPosition(degreesToEncUnits(0), 0, 10);
	// 					DriverStation.reportError("MAG ENCODER FOR " + name + " WAS NOT CONNECTED UPON BOOT", false);
	// 					moduleZeroedWitoutMagEnc = true;
	// 				}
	// 			} else {
	// 				DriverStation.reportError("ROTATION FALCON NOT FOUND ON " + name, false);
	// 				moduleZeroedWitoutMagEnc = true;
	// 			}
	// 			zeroCount++;
	// 		} else {
	// 			System.out.println("MODULE " + name + " ZEROED");
	// 			System.out.println(name + "Absolute angle: " + getAbsoluteEncoderDegrees() + ", encoder offset: " + encoderOffset + ", difference: " + (getAbsoluteEncoderDegrees() - encoderOffset) + ", degreesToEncUnits: " + degreesToEncUnits(getAbsoluteEncoderDegrees() - encoderOffset));
	// 			rotationMotorZeroed = true;
	// 		}
	// 	}
	// }

	int zeroCount = 0;
	public synchronized void resetRotationToAbsolute(){
		if (!rotationMotorZeroed) {
			ErrorCode rotationFalconCheck = rotationMotor.setSelectedSensorPosition(0);
			if (rotationFalconCheck == ErrorCode.OK) {
				if (isRotationSensorConnected() && RobotBase.isReal()) {
					rotationMotor.setSelectedSensorPosition(degreesToEncUnits(getAbsoluteEncoderDegrees() - encoderOffset), 0, 10);
					moduleZeroedWitoutMagEnc = false;
					//System.out.println(name + "Absolute angle: " + getAbsoluteEncoderDegrees() + ", encoder offset: " + encoderOffset + ", difference: " + (getAbsoluteEncoderDegrees() - encoderOffset) + ", degreesToEncUnits: " + degreesToEncUnits(getAbsoluteEncoderDegrees() - encoderOffset));
				} else {
					rotationMotor.setSelectedSensorPosition(degreesToEncUnits(0), 0, 10);
					DriverStation.reportError("MAG ENCODER FOR " + name + " WAS NOT CONNECTED UPON BOOT", false);
					moduleZeroedWitoutMagEnc = true;
				}
			} else {
				DriverStation.reportError("ROTATION FALCON NOT FOUND ON " + name, false);
				moduleZeroedWitoutMagEnc = true;
			}
			if (zeroCount < 50) {
				zeroCount++;
			} else {
				System.out.println("MODULE " + name + " ZEROED");
				System.out.println(name + "Absolute angle: " + getAbsoluteEncoderDegrees() + ", encoder offset: " + encoderOffset + ", difference: " + (getAbsoluteEncoderDegrees() - encoderOffset) + ", degreesToEncUnits: " + degreesToEncUnits(getAbsoluteEncoderDegrees() - encoderOffset));
				rotationMotorZeroed = true;
			}
		}
	}

	public synchronized void setRotationMotorZeroed(boolean isZeroed) {
		rotationMotorZeroed = isZeroed;
	}
	
	@Override
	public synchronized void zeroSensors() {
		zeroSensors(new Pose2d());
	}
	
	public synchronized void zeroSensors(Pose2d robotPose) {
		//driveMotor.setSelectedSensorPosition(0, 0, 100); TODO check if this is necessary
		resetPose(robotPose);
		estimatedRobotPose = robotPose;
		previousEncDistance = getDriveDistanceInches();
	}
	
	@Override
	public void registerEnabledLoops(ILooper enabledLooper) {
		
	}

	public synchronized double getModuleVelocity() {
		return encVelocityToInchesPerSecond(periodicIO.velocity);
	}
	
	@Override
	public void outputTelemetry() {
		SmartDashboard.putNumber(name + "Angle", getModuleAngle().getDegrees());
		if (RobotBase.isReal()) {
			SmartDashboard.putNumber(name + "Absolute Angle", getAbsoluteEncoderDegrees());
		}
		SmartDashboard.putNumber(name + "Inches Driven", getDriveDistanceInches());
		//SmartDashboard.putBoolean(name + "Zeroed With Encoder", moduleZeroedWitoutMagEnc);
		if(Settings.debugSwerve()){
			if (RobotBase.isReal()) {
				SmartDashboard.putNumber(name + "CANCoder Absolute Angle", getAbsoluteEncoderDegrees());
			}
			SmartDashboard.putNumber(name + "Rotation Encoder", periodicIO.rotationPosition);
			SmartDashboard.putNumber(name + "Drive Voltage", periodicIO.driveVoltage);
			SmartDashboard.putNumber(name + "Rotation Voltage", rotationMotor.getMotorOutputVoltage());
			SmartDashboard.putNumber(name + "Velocity - in", encVelocityToInchesPerSecond(periodicIO.velocity));

			SmartDashboard.putNumber(name + "Raw Velocity", periodicIO.velocity);
			if(rotationMotor.getControlMode() == ControlMode.MotionMagic)
				SmartDashboard.putNumber(name + "Error", encUnitsToDegrees(rotationMotor.getClosedLoopError(0)));
			SmartDashboard.putNumber(name + "Drive Current", driveMotor.getOutputCurrent());
			SmartDashboard.putNumber(name + "Drive Percent Out", driveMotor.getMotorOutputPercent());
			SmartDashboard.putNumber(name + "Rotation Speed", rotationMotor.getSelectedSensorVelocity(0));
		}
	}
	
	public static class PeriodicIO{
		//Inputs
		public int rotationPosition = 0;
		public int drivePosition = 0;
		public int velocity = 0;
		public double driveVoltage = 0.0;
		
		
		//Outputs
		public ControlMode rotationControlMode = ControlMode.PercentOutput;
		public ControlMode driveControlMode = ControlMode.PercentOutput;
		public double rotationDemand;
		public double driveDemand;
	}

}
