/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team1671.frc;

import java.util.Arrays;

import com.team1671.frc.loops.LimelightProcessor;
import com.team1671.frc.loops.Loop;

import com.team1671.frc.subsystems.SubsystemManager;
import com.team1671.frc.subsystems.Superstructure;
import com.team1671.frc.subsystems.Swerve;

import com.team1671.io.Xbox;
import com.team1671.lib.util.Util;
import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Rotation2d;
import com.team254.lib.geometry.Translation2d;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * A class to assign controller inputs to robot actions
 */
public class DriverControls implements Loop {

    private static DriverControls instance = null;

    public static DriverControls getInstance() {
        if (instance == null)
            instance = new DriverControls();
        return instance;
    }

	Xbox driver, coDriver;

    private Swerve swerve;

    private Superstructure s;

    private SubsystemManager subsystems;
    public SubsystemManager getSubsystems(){ return subsystems; }

    private RobotState robotState;

    private LimelightProcessor limelight;

    private final boolean oneControllerMode = false;
	private boolean flickRotation = false;
    private boolean robotCentric = false;
    private boolean isTurret180Rotation = false;
        
    private boolean inAuto = true;
    public void setAutoMode(boolean auto) {
        inAuto = auto;
    }

    public boolean getInAuto() {
        return inAuto;
    }

    public DriverControls() {
        driver = new Xbox(0);
		coDriver = new Xbox(1);
        driver.setDeadband(0.0);
		coDriver.setDeadband(0.25);
		coDriver.rightBumper.setLongPressDuration(1.0);

        swerve = Swerve.getInstance();

        s = Superstructure.getInstance();

        subsystems = new SubsystemManager(
				Arrays.asList(swerve, s));

        robotState = RobotState.getInstance();

        limelight = LimelightProcessor.getInstance();
    }

    @Override
    public void onStart(double timestamp) {
        if(inAuto) {
            swerve.zeroSensors();
            swerve.setNominalDriveOutput(0.0);
            swerve.requireModuleConfiguration();
            s.enableCompressor(false);
        } else {
            s.enableCompressor(true);
            swerve.setNominalDriveOutput(0.0);
            swerve.set10VoltRotationMode(false);
        }
    }

    @Override
    public void onLoop(double timestamp) {
        if(inAuto) {
            if(s.swerve.getState() == Swerve.ControlState.VISION_PID){
            }else{
            }
        } else {
            driver.update();
			coDriver.update();
            if(oneControllerMode) oneControllerMode();
            else twoControllerMode();
            String message = DriverStation.getInstance().getGameSpecificMessage();
        }
    }

    @Override
    public void onStop(double timestamp) {
        subsystems.stop();
    }

    private void twoControllerMode() {
        double swerveYInput = driver.getX(Hand.kLeft);
        double swerveXInput = -driver.getY(Hand.kLeft);
        double swerveRotationInput = (flickRotation ? 0.0 : driver.getX(Hand.kRight));
        
        swerve.sendInput(swerveXInput, swerveYInput, swerveRotationInput, robotCentric, driver.leftTrigger.isBeingPressed());
            
        if (flickRotation) {
            swerve.updateControllerDirection(new Translation2d(-driver.getY(Hand.kRight), driver.getX(Hand.kRight)));
            if (!Util.epsilonEquals(
            Util.placeInAppropriate0To360Scope(swerve.getTargetHeading(),
            swerve.averagedDirection.getDegrees()),
            swerve.getTargetHeading(), swerve.rotationDivision / 2.0)) {
                swerve.rotate(swerve.averagedDirection.getDegrees());
            }
        }
        
        if (driver.bButton.isBeingPressed())
        swerve.rotate(90);
        else if (driver.aButton.isBeingPressed()) 
        swerve.rotate(180);
        else if (driver.xButton.isBeingPressed())
        swerve.rotate(270);
        else if (driver.yButton.isBeingPressed())
        swerve.rotate(0.0);

        if(driver.rightBumper.isBeingPressed()){
            swerve.setVelocity(Rotation2d.fromDegrees(0), 24.0);
        }else if(driver.leftBumper.isBeingPressed()){
            swerve.stop();
        }

        

        if (driver.startButton.isBeingPressed()) 
            swerve.setState(Swerve.ControlState.NEUTRAL);

        if (driver.backButton.shortReleased() || driver.backButton.longPressed()) {
            swerve.temporarilyDisableHeadingController();
            swerve.zeroSensors(new Pose2d());
            swerve.resetAveragedDirection();
        }
    }

    private void oneControllerMode() {

    }
}
