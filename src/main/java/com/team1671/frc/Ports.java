package com.team1671.frc;

public class Ports {
    //TODO: Update Ports with IRL Number
    
    // CAN Devices 
    // Falcons
    public static final int FRONT_RIGHT_ROTATION= 3; 
    public static final int FRONT_RIGHT_DRIVE   = 2; 
    public static final int FRONT_LEFT_ROTATION = 1;
    public static final int FRONT_LEFT_DRIVE    = 7; 
    public static final int REAR_LEFT_ROTATION  = 4;
    public static final int REAR_LEFT_DRIVE     = 5;
    public static final int REAR_RIGHT_ROTATION = 6;
    public static final int REAR_RIGHT_DRIVE    = 8;
    
        
    // MISC CAN
    public static final int PIGEON = 9;
    public static final int SECONDARY_PIGEON = 23;
    
    //PWM
    public static final int HOOD_SERVO = 0;

    //Digital Inputs
    public static final int FRONT_RIGHT_ENCODER = 40;
    public static final int FRONT_LEFT_ENCODER = 10;
    public static final int REAR_LEFT_ENCODER = 20;
    public static final int REAR_RIGHT_ENCODER = 30;
    public static final int[] kModuleEncoders = new int[]{FRONT_RIGHT_ENCODER, FRONT_LEFT_ENCODER,
        REAR_LEFT_ENCODER, REAR_RIGHT_ENCODER};

    // Pneumatics
    public static final int PCM = 21;
    public static final int HOOD_FOLDER = 4;
    public static final int HOOD_EXTENDER = 5;
    public static final int INTAKE_EXTENDER = 1;
    public static final int WOF_EXTENDER = 3;
    public static final int HANGER_EXTENDER = 0;


    //Canifier
    public static final int CANIFIER = 30;
        
    }
    