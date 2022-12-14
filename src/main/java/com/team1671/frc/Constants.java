package com.team1671.frc;

import java.util.Arrays;
import java.util.List;

import com.team1671.lib.util.InterpolatingTreeMap;
import com.team1671.lib.util.InterpolatingDouble;
import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Rotation2d;
import com.team254.lib.geometry.Translation2d;

public class Constants {
    /*All distance measurements are in inches, unless otherwise noted.*/
    
    public static final double kLooperDt = 0.02;
    
    public static final double kEpsilon = 0.0001;
    
    //Physical Robot Dimensions (including bumpers)
    public static final double kRobotWidth = 30.0; //35.5
    public static final double kRobotLength = 30.0; //35.5
    public static final double kRobotHalfWidth = kRobotWidth / 2.0;
    public static final double kRobotHalfLength = kRobotLength / 2.0;
    
    public static final double kFieldLength = 629.25;
    
    //Field Landmarks
    public static final Pose2d kFirstTrenchBall = new Pose2d(new Translation2d(kFieldLength - (120.0 + 122.63), 133.875), Rotation2d.fromDegrees(180.0));
    public static final Pose2d kLastTrenchBall = new Pose2d(new Translation2d(kFieldLength - (120.0 + 194.63), 133.875), Rotation2d.fromDegrees(180.0));
    public static final Pose2d kTrenchBallSet = new Pose2d(new Translation2d(kFieldLength - (120.0 + 258.90), 133.875), Rotation2d.fromDegrees(180.0));
    public static final Pose2d kOppositeTrenchBalls = new Pose2d(new Translation2d(kFieldLength - (120.0 + 130.36), -133.875), Rotation2d.fromDegrees(180.0));
    // Ball 1 starts on 2 ball side and goes in order till last ball on 3 ball side
    public static final Pose2d kGeneratorBall1 = new Pose2d(new Translation2d(kFieldLength - (120.0 + 130.25), (161.625 - 94.66 - 19.79)), Rotation2d.fromDegrees(67.5 + 180.0));
    public static final Pose2d kGeneratorBall2 = new Pose2d(new Translation2d(kFieldLength - (120.0 + 114.94), (161.625 - 94.66 - 26.16)), Rotation2d.fromDegrees(67.5 + 180.0));
    public static final Pose2d kGeneratorBall3 = new Pose2d(new Translation2d(kFieldLength - (120.0 + 107.83), (161.625 - 94.66 - 50.54)), Rotation2d.fromDegrees(-22.5 + 180.0));
    public static final Pose2d kGeneratorBall4 = new Pose2d(new Translation2d(kFieldLength - (120.0 + 114.17), (161.625 - 94.66 - 65.84)), Rotation2d.fromDegrees(-22.5 + 180.0));
    public static final Pose2d kGeneratorBall5 = new Pose2d(new Translation2d(kFieldLength - (120.0 + 120.51), (161.625 - 94.66 - 81.14)), Rotation2d.fromDegrees(-22.5 + 180.0));
    public static final Pose2d kGeneratorBall1and2 = new Pose2d(new Translation2d((kGeneratorBall1.getTranslation().x() + kGeneratorBall2.getTranslation().x()) / 2.0, (kGeneratorBall1.getTranslation().y() + kGeneratorBall2.getTranslation().y()) / 2.0), Rotation2d.fromDegrees(67.5 + 180.0));
    public static final Pose2d kGeneratorBall3and4 = new Pose2d(new Translation2d((kGeneratorBall3.getTranslation().x() + kGeneratorBall4.getTranslation().x()) / 2.0, (kGeneratorBall3.getTranslation().y() + kGeneratorBall4.getTranslation().y()) / 2.0), Rotation2d.fromDegrees(-22.5 + 180.0));

    public static final Pose2d kEnemyHumanLoader = new Pose2d(new Translation2d(kFieldLength, -56.0), Rotation2d.fromDegrees(0.0));
    
    public static final Pose2d kRobotStartingPose = new Pose2d(new Translation2d(0,0), Rotation2d.fromDegrees(0.0));//this is where robot was going backward in auto
    
    //public static final Pose2d kRobotStartingPose = new Pose2d(new Translation2d(kFieldLength - (120.0 + 2.0 + kRobotHalfLength), (161.625 - 94.66)), Rotation2d.fromDegrees(0.0));//this is where robot was going backward in auto
    public static final Pose2d kPartnerRobotStartingPose = new Pose2d(new Translation2d(kFieldLength - (120.0 + 2.0 + kRobotHalfLength), (161.625 - 27.75)), Rotation2d.fromDegrees(180.0));
    public static final Pose2d kAltRobotStartingPose = new Pose2d(new Translation2d(kFieldLength - (120.0 + 2.0 + kRobotHalfLength), -(161.625 - 27.75)), Rotation2d.fromDegrees(180.0));
    
    /**
    * Target Specifications
    */
    public static final double kVisionTargetHeight = 98.25; //81.0 to bottom
    public static final Rotation2d kPortTargetOrientation = Rotation2d.fromDegrees(0.0);
    public static final Translation2d kOuterPortToInnerPort = new Translation2d(29.25, 0.0);
    
    //Swerve Calculations Constants (measurements are in inches)
    public static final double kWheelbaseLength = 30.0;
    public static final double kWheelbaseWidth = 30.0;
    public static final double kSwerveDiagonal = Math.hypot(kWheelbaseLength, kWheelbaseWidth);
    
    //Camera Constants (X and Y are with respect to the turret's center)
    public static final double kCameraYOffset = 0.0;//0.25
    public static final double kCameraXOffset = 8.216; //8.5
    public static final double kCameraZOffset = 25.0; //26.776 24.524
    public static final double kCameraYawAngleDegrees = -1.0;//-12.7
    public static final double kCameraPitchAngleDegrees = Settings.kIsUsingCompBot ? 0.0 : 35.5; //21.75 for bottom 34.3 37.0604

    //Limelight
    public static final double kHorizontalFOV = 59.6; // degrees
    public static final double kVerticalFOV = 49.7; // degrees
    public static final double kVPW = 2.0 * Math.tan(Math.toRadians(kHorizontalFOV / 2.0));
    public static final double kVPH = 2.0 * Math.tan(Math.toRadians(kVerticalFOV / 2.0));
    public static final double kImageCaptureLatency = 11.0 / 1000.0; // seconds
    
    //Goal tracker constants
    public static final double kMaxGoalTrackAge = 0.5;
    public static final double kMaxTrackerDistance = 18.0;
    public static final double kCameraFrameRate = 90.0;
    public static final double kTrackStabilityWeight = 1.0;
    public static final double kTrackAgeWeight = 1.0;
    public static final double kTrackSwitchingWeight = 0.0;
    public static final double kClosestVisionDistance = 26.0;//36.0
    
    public static final double kVisionPIDOutputPercent = 0.5;

    public static final double kPosePredictionTime = 0.125; // seconds 0.25
    
    public static final double kDistanceToTargetTolerance = 1.0;

    public static final double kGyroDriftPerRotation = -0.25; // degrees
    
    //Path following constants
    public static final double kPathLookaheadTime = 0.25;  // seconds to look ahead along the path for steering 0.4
    public static final double kPathMinLookaheadDistance = 6.0;  // inches 24.0 (we've been using 3.0)
    
    //Swerve Speed Constants
    public static final double kSwerveDriveMaxSpeed = 23000.0; //ticks/s (measure in phoenix tuner)
    public static final double kSwerveMaxSpeedInchesPerSecond = 12.5 * 12.0;
    public static final double kSwerveRotationMaxSpeed = 22430.0 * 0.8; //The 0.8 is to request a speed that is always achievable | ticks/s (measure in phoenix tuner)
    public static final double kSwerveRotation10VoltMaxSpeed = 1350.0;
    public static final double kSwerveRotationSpeedScalar = ((1.0 / 0.125) - 1.0) / kSwerveMaxSpeedInchesPerSecond;
    
    //Swerve Module Wheel Offsets (Rotation encoder values when the wheels are facing 0 degrees)
    /**
    * To Zero: Rotate module so that bevel gear is face out. Rotate module 90?? CW from the top
    * Enter angle read by the absolute encoder. Insert as degrees and subtract or add 90?? to the value
    * based on where the bevel ended up.
    * these are your module absolute angle
    */
    public static final double kFrontRightEncoderStartingPos = Settings.kIsUsingCompBot ? -334.68750 : -48.808594; //Module 0 - Front Right -3.52 | -7.91 | 5.71
    public static final double kFrontLeftEncoderStartingPos = Settings.kIsUsingCompBot ? -160.048828 : 127.324219; //Module 1 - Front Left -109.97 | -109.95
    public static final double kRearLeftEncoderStartingPos = Settings.kIsUsingCompBot ? -36.914063 : 21.738281; //Module 2 - Rear Left -219.4 | -219.28 | 242.94
    public static final double kRearRightEncoderStartingPos = Settings.kIsUsingCompBot ? -303.662109 : 16.171875; //Module 3 - Rear Right -353.5 | 351.77
    
    //Swerve Module Positions (relative to the center of the drive base)
    public static final Translation2d kVehicleToModuleZero = new Translation2d(kWheelbaseLength / 2, kWheelbaseWidth / 2);
    public static final Translation2d kVehicleToModuleOne = new Translation2d(kWheelbaseLength / 2, -kWheelbaseWidth / 2);
    public static final Translation2d kVehicleToModuleTwo = new Translation2d(-kWheelbaseLength / 2, -kWheelbaseWidth / 2);
    public static final Translation2d kVehicleToModuleThree = new Translation2d(-kWheelbaseLength / 2, kWheelbaseWidth / 2);
    
    public static final List<Translation2d> kModulePositions = Arrays.asList(kVehicleToModuleZero,
    kVehicleToModuleOne, kVehicleToModuleTwo, kVehicleToModuleThree);
    
    //Scrub Factors
    public static final boolean kSimulateReversedCarpet = false;
    public static final double[] kWheelScrubFactors = new double[]{1.0, 1.0, 1.0, 1.0};
    public static final double kXScrubFactor = 1.0;//1.0 / (1.0 - (9549.0 / 293093.0));
    public static final double kYScrubFactor = 1.0;//1.0 / (1.0 - (4.4736 / 119.9336));
    
    //Voltage-Velocity equation constants {m, b, x-intercept}
    //First set is the positive direction, second set is negative
    public static final double[][][] kVoltageVelocityEquations = new double[][][]{
        {{1.70, -4.39, 2.58}, {1.83, 5.23, -2.85}},
        {{1.59, -3.86, 2.42}, {1.43, 3.09, -2.16}},
        {{1.53, -3.66, 2.39}, {1.66, 4.15, -2.50}},
        {{1.84, -4.70, 2.56}, {1.85, 5.34, -2.89}}
    };
    
    //Swerve Odometry Constants
    public static final double kSwerveWheelDiameter = 4.0587; //inches (actual diamter is closer to 3.87, but secondary algorithm prefers 4.0901) 3.76
    public static final double kSwerveDriveEncoderResolution = 2048.0; //2048.0 for falcon 500
    public static final double kSwerveRotationEncoderResolution = 2048.0;
    /** The number of rotations the swerve rotation motor undergoes for every rotation of the module. */
    public static final double kSwerveRotationReduction = 12.0; // 8/24 18/72
    /** The number of rotations the swerve drive encoder undergoes for every rotation of the wheel. */
    public static final double kSwerveEncoderToWheelRatio = 8.5714; // 10/40 28/20 15/45 IN REVERSE //16.8
    public static final double kSwerveEncUnitsPerWheelRev = kSwerveDriveEncoderResolution * kSwerveEncoderToWheelRatio;
    public static final double kSwerveEncUnitsPerInch = kSwerveEncUnitsPerWheelRev / (Math.PI * kSwerveWheelDiameter);

    //swerve rotation PID
    //normal
    public static final double kSwerveRotationP0 = 1.55;
    public static final double kSwerveRotationI0 = 0.0;
    public static final double kSwerveRotationD0 = 5.0;

    //beginning of auto
    public static final double kSwerveRotationP1 = 8.0;
    public static final double kSwerveRotationI1 = 0.0;
    public static final double kSwerveRotationD1 = 200.0;
    
    //swerve Drive PID
    //position/motion magic
    public static final double kSwerveDriveP0 = 0.18; //0.18
    public static final double kSwerveDriveI0 = 0.0; //0.0
    public static final double kSwerveDriveD0 = 3.6; //3.6

    //velocity
    public static final double kSwerveDriveP1 = 0.11; //0.11
    public static final double kSwerveDriveI1 = 0.0; //0.0
    public static final double kSwerveDriveD1 = 0.0; //0.0
    
    public static final int kCANTimeoutMs = 10; // use for important on the fly updates
    public static final int kLongCANTimeoutMs = 100; // use for constructors
    
    public static class Turret {
        public static final double kMaxControlAngle = 200.0; //In both positive and negative directions | 220.0
        public static final double kMinControlAngle = -35.0; // -25.0
        public static final double kMaxInitialAngle = 220.0; // 220.0
        public static final double kMinInitialAngle = -35.0; //-25.0
        
        public static final double kEncoderStartingAngle = Settings.kIsUsingCompBot ? -40.2 : -110.5; // Absolute position of the magnet 309.25
        public static final double kAngleTolerance = 1.0;
        
        // Ratios
        public static final double kInternalEncToOutputRatio = 100.0;
        public static final double kEncoderToOutputRatio = 1.0;
        
        //PID
        public static final double kP = 0.3;
        public static final double kI = 0.0;
        public static final double kD = 0.0;
        public static final double kF = 0.0465; // Theoretical: 0.0465
        
        public static final double kMaxSpeed = 22000.0;
        
        // Turret pose with respect to the robot's center
        public static final double kXOffset = -4.25;
        public static final double kYOffset = 0.0; 

        public static final List<double[]> kVisionRanges = Arrays.asList(
            new double[] {kMinControlAngle, 87.0},
            new double[] {93.0, kMaxControlAngle}
        );
    }
    
    public static class WheelOfFortune {
        
        public static final double kFieldWheelDiameter = 32.0;
        public static final double kWheelDiameter = 3.0;
        public static final double kTileDegrees = 45.0;
        public static final int kColorOffset = 2;
        
        public static final double kP = 0.0;
        public static final double kI = 0.0;
        public static final double kD = 0.0;
        public static final double kF = 0.0;
        public static final double kMaxRawVelocity = 30000.0; // TODO: Tune

        public static final double kMaxRPM = 55.0;
        public static final double kWheelToFieldWheelRotations = 96*2; // Need To Calculate

        public static final double kInternalEncToOutputRatio = 3.0;
        public static final double kRamp = 0.5;
        
    }
    
    public static class LEDs {
        
        //LED Colors
        public static final List<Double> pink = Arrays.asList(255.0, 20.0, 30.0);
        public static final List<Double> blue = Arrays.asList(0.0, 0.0, 255.0);
        public static final List<Double> red = Arrays.asList(255.0, 0.0, 0.0);
        public static final List<Double> orange = Arrays.asList(255.0, 20.0, 0.0);
        public static final List<Double> yellow = Arrays.asList(255.0, 60.0, 0.0);
        public static final List<Double> green = Arrays.asList(0.0, 255.0, 0.0);
        public static final List<Double> purple = Arrays.asList(255.0, 0.0, 255.0);
        
        //LED Arrays
        public static final List<List<Double>> rainbow = Arrays.asList(red, orange, yellow, green, blue, pink, purple);
        
    }
    
    // Top Wheel Treemap
    public static InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> kTopWheelTreeMap = new InterpolatingTreeMap<>();
    static{
        kTopWheelTreeMap.put(new InterpolatingDouble(-500.0), new InterpolatingDouble(0.051)); //0.041 0.079
        kTopWheelTreeMap.put(new InterpolatingDouble(500.0), new InterpolatingDouble(0.051)); //0.041 0.079
        kTopWheelTreeMap.put(new InterpolatingDouble(1500.0), new InterpolatingDouble(0.048)); //0.042 0.08
        kTopWheelTreeMap.put(new InterpolatingDouble(3000.0), new InterpolatingDouble(0.048)); //0.044 0.082
        kTopWheelTreeMap.put(new InterpolatingDouble(5000.0), new InterpolatingDouble(0.048)); //0.0445 0.0825
    }
    
    // Bottom Wheel Treemap
    public static InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> kBottomWheelTreeMap = new InterpolatingTreeMap<>();
    static{
        kBottomWheelTreeMap.put(new InterpolatingDouble(-500.0), new InterpolatingDouble(0.051)); //0.051
        kBottomWheelTreeMap.put(new InterpolatingDouble(500.0), new InterpolatingDouble(0.051)); //0.051
        kBottomWheelTreeMap.put(new InterpolatingDouble(1500.0), new InterpolatingDouble(0.048)); //0.048
        kBottomWheelTreeMap.put(new InterpolatingDouble(3000.0), new InterpolatingDouble(0.048)); //0.075
        kBottomWheelTreeMap.put(new InterpolatingDouble(5000.0), new InterpolatingDouble(0.048)); //0.048
    }
    
    /**
    * First double is distance and second double is the rpm for the bottom wheel
    */
    public static InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> kBottomWheelRPMToDistanceTreeMap = new InterpolatingTreeMap<>();
    static {
        kBottomWheelRPMToDistanceTreeMap.put(new InterpolatingDouble(100.0), new InterpolatingDouble(3400.0));
        kBottomWheelRPMToDistanceTreeMap.put(new InterpolatingDouble(120.0), new InterpolatingDouble(3400.0));
        kBottomWheelRPMToDistanceTreeMap.put(new InterpolatingDouble(130.0), new InterpolatingDouble(3400.0));
        kBottomWheelRPMToDistanceTreeMap.put(new InterpolatingDouble(150.0), new InterpolatingDouble(3400.0));
        kBottomWheelRPMToDistanceTreeMap.put(new InterpolatingDouble(170.0), new InterpolatingDouble(4200.0));
        kBottomWheelRPMToDistanceTreeMap.put(new InterpolatingDouble(190.0), new InterpolatingDouble(4200.0));
        kBottomWheelRPMToDistanceTreeMap.put(new InterpolatingDouble(200.0), new InterpolatingDouble(4200.0));
    }
    
    public static InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> kTopWheelRPMToDistanceTreeMap = new InterpolatingTreeMap<>();
    static {
        kTopWheelRPMToDistanceTreeMap.put(new InterpolatingDouble(100.0), new InterpolatingDouble(1200.0));
        kTopWheelRPMToDistanceTreeMap.put(new InterpolatingDouble(120.0), new InterpolatingDouble(1200.0));
        kTopWheelRPMToDistanceTreeMap.put(new InterpolatingDouble(130.0), new InterpolatingDouble(1200.0));
        kTopWheelRPMToDistanceTreeMap.put(new InterpolatingDouble(150.0), new InterpolatingDouble(1200.0));
        kTopWheelRPMToDistanceTreeMap.put(new InterpolatingDouble(170.0), new InterpolatingDouble(1400.0));
        kTopWheelRPMToDistanceTreeMap.put(new InterpolatingDouble(190.0), new InterpolatingDouble(1400.0));
        kTopWheelRPMToDistanceTreeMap.put(new InterpolatingDouble(200.0), new InterpolatingDouble(1400.0));
    }
    
    public static class Shooter{
        public static final double kTopEncToOutputRatio = 36.0 / 16.0;
        public static final double kBottomEncToOutputRatio = 1.0;
        public static final double kShooterRamp = 1.0; //2.0
        
        //Shooter RPM 
        public static final double kCloseTopRPM = 875.0;
        public static final double kCloseBottomRPM = 2200.0;

        public static final double kMidTopRPM = 1150.0; //1100, 3300, 55 deg
        public static final double kMidBottomRPM = 3100.0; //1500, 3300, 53.5 deg

        public static final double kFarTopRPM = 1135.0; //1400, 3600, 63 deg
        public static final double kFarBottomRPM = 4000.0; //1700, 3400, 61 deg

        public static final double kCornerTopRPM = 1135.0;
        public static final double kCornerBottomRPM = 4000.0;
        
        /**
        * Spin Up
        */
        public static final double kTopP = 0.019; // WCP Single 4in: 0.02
        public static final double kTopI = 0.0001; // WCP Single 4in: 0.0001
        public static final double kTopD = 0.0;
        public static final double kTopF = 0.048; // Theoretical is 0.568 | WCP Single 4in: 0.08
        
        public static final double kBottomP = 0.008; //0.01
        public static final double kBottomI = 0.0001; // 0.0001
        public static final double kBottomD = 0.0;
        public static final double kBottomF = 0.048; // Theoretical is 0.568 | WCP 3 x 4in: 0.048
        /**
        * Hold
        */
        public static final double kTopHoldP = 0.2; // WCP Single 4in: 0.4
        public static final double kTopHoldI = 0.0;
        public static final double kTopHoldD = 1.0; // WCP Single 4in: 6.38
        public static final double kTopHoldF = 0.048; // Value gets changed as soon as it enters hold state so value never gets used3
        
        public static final double kBottomHoldP = 1.0; // WCP 3 x 4in: 1.9 | 1.8
        public static final double kBottomHoldI = 0.0;
        public static final double kBottomHoldD = 5.0; // WCP 3 x 4in: 10.0 | 1.8 | 3.6
        public static final double kBottomHoldF = 0.048; // Value gets changed as soon as it enters hold state so value never gets used
        
        public static final double kShooterRPMTolerance = 150.0;
    }
    
    public static class Intake{
        
        public static final double kIntakeP = 1.0;
        public static final double kIntakeI = 0.0;
        public static final double kIntakeD = 0.0;
        public static final double kIntakeF = 0.0;
        
        public static final double kIntakeSpeed = 0.6;
        public static final double kOuttakeSpeed = -0.5;
        public static final double kFeedingSpeed = 0.5;
        public static final double kHumanLoadSpeed = 0.5;
        public static final double kFastIntakeSpeed = 0.75;
    }
    
    public static class Feeder {
        public static final double kFeedingSpeed = 0.9;
        public static final double kReverseSpeed = -0.5;
        public static final double kReceivingSpeed = 0.25;
        
        public static final double kRamp = 0.125;
    }
    
    public static class Hood {
        public static final double kHoodExtensionDelay = 0.25;
    }

    public static class ActuatingHood {
        public static final double kMinHoodAngle = 30.0;
        public static final double kMaxHoodAngle = 70.0;
        public static final double kDeltaAngle = kMaxHoodAngle - kMinHoodAngle;

        public static final double kCloseProtectedAngle = 32.0;
        public static final double kMidHoodAngle = 62.0;

        public static final double kMaxLength = 1.9685;
        public static final double kMinLength = 0.0;
        public static final double kLengthToAngle = 180.0 / 1.9685;
    }

    public static class MotorizedHood {
        public static final double kReduction = 80.89;

        public static final double kMaxSpeed = 6380.0 * 2048.0 / 600.0;

        public static final double kP = 0.1; //Bag: 6.0
        public static final double kI = 0.0;
        public static final double kD = 0.0; //Bag: 32.0
        public static final double kF = 1023.0 / kMaxSpeed;

        public static final double kHoodStartingAngle = 65.0;
        public static final double kEncStartingAngle = Settings.kIsUsingCompBot ? -249.1 : -233.2; // The absolute angle (in degrees) of the mag encoder when the hood is at kHoodStartingAngle

        public static final double kTicksPerDegree = 2048.0 / 360.0 * kReduction;

        public static final double kMinInitialAngle = 10.0;
        public static final double kMaxInitialAngle = 70.0;

        public static final double kMinControlAngle = Settings.kIsUsingCompBot ? 16.0 : 18.0;
        public static final double kMaxControlAngle = 63.0;

        public static final double kAngleTolerance = 2.0;

        //Shooter Hood Angles
        public static final double kCloseAngle = 32.25;
        public static final double kMidAngle = 42.5; //55 deg
        public static final double kFarAngle = 59.0;
    }

    public static class Hanger {
        public static final double kMaxSpeed = 6380.0 * 2048.0 / 600.0;

        public static final double kEncoderReduction = 20.25;

        public static final double kP = 0.01;
        public static final double kI = 0.0;
        public static final double kD = 0.0;
        public static final double kF = 1023.0 / kMaxSpeed;

        public static final double kTicksPerInch = 24154.0 / 2.0; //-2.0 -24156 2 inches
        public static final double kHeightTolerance = 2.0;
        
        public static final double kMaxControlHeight = 25.5;
        public static final double kMinControlHeight = 0.0;

        public static final double kMaxInitialHeight = 4.64;//3.64
        public static final double kMinInitialHeight = 2.64;

        public static final double kEncoderStartingAngle = 97.64; //3.433 inches per rotation

        public static final double kManualSpeed = 0.5;
    }

}
