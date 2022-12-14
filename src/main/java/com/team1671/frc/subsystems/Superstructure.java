package com.team1671.frc.subsystems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.team1671.frc.Ports;
import com.team1671.frc.RobotState;
import com.team1671.frc.loops.ILooper;
import com.team1671.frc.loops.Loop;
import com.team1671.frc.subsystems.requests.Request;
import com.team1671.frc.vision.ShooterAimingParameters;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Timer;

public class Superstructure extends Subsystem {

	private Compressor compressor;
	
	public Swerve swerve;

	private RobotState robotState;

	private boolean isClimbing = false;
	public boolean isClimbing(){ return isClimbing; }
	public void enableInterpolator(boolean enable){ 
		isClimbing = enable;
		swerve.setLowPowerScalar(0.25);
	}
	public void stopClimbing(){ 
		isClimbing = false; 
		swerve.setLowPowerScalar(0.6);
	}
	
	public Superstructure(){
		compressor = new Compressor(Ports.PCM);
		compressor.clearAllPCMStickyFaults();
		
		swerve = Swerve.getInstance();
		

		robotState = RobotState.getInstance();
		
		queuedRequests = new ArrayList<>(0);
	}
	private static Superstructure instance = null;
	public static Superstructure getInstance(){
		if(instance == null)
			instance = new Superstructure();
		return instance;
	}

	private Request activeRequest = null;
	private List<Request> queuedRequests = new ArrayList<>();
	
	private boolean newRequest = false;
	private boolean allRequestsCompleted = false;
	public boolean requestsCompleted(){ return allRequestsCompleted; }
	
	private void setActiveRequest(Request request){
		activeRequest = request;
		newRequest = true;
		allRequestsCompleted = false;
	}
	
	private void setQueue(List<Request> requests){
		clearQueue();
		for(Request request : requests) {
			queuedRequests.add(request);
		}
	}

	private void setQueue(Request request) {
		setQueue(Arrays.asList(request));
	}

	private void clearQueue() {
		queuedRequests.clear();
	}
	
	public void request(Request r){
		setActiveRequest(r);
		clearQueue();
	}
	
	public void request(Request active, Request queue){
		setActiveRequest(active);
		setQueue(queue);
	}
	
	public void queue(Request request){
		queuedRequests.add(request);
	}
	
	public void replaceQueue(Request request){
		setQueue(request);
	}
	
	boolean isPrefire = false;

	private final Loop loop = new Loop(){

		@Override
		public void onStart(double timestamp) {
			stop();
		}

		@Override
		public void onLoop(double timestamp) {
			synchronized(Superstructure.this){
				Optional<ShooterAimingParameters> aim = robotState.getAimingParameters(false);

				// if (swerve.modules.get(0).getModuleVelocity() >= 60.0 && intake.getState() == Intake.State.INTAKE) {
				// 	intake.setOpenLoop(Constants.Intake.kFastIntakeSpeed);
				// } else if (swerve.modules.get(0).getModuleVelocity() < 60.0 && intake.getState() == Intake.State.INTAKE) {
				// 	intake.setOpenLoop(Constants.Intake.kIntakeSpeed);
				// }

				// if (turret.isTracking()) {
				// 	if (aim.isPresent()) {
				// 		if ((!turret.inTurretRange(aim.get().getTurretAngle().getDegrees()) || 
				// 			!turret.inVisionRange(turret.boundToTurretRange(aim.get().getTurretAngle().getDegrees())))
				// 			&& !swerve.isGoingToPole()) {
				// 			//swerve.rotate(swerve.closestPole());
				// 		}
				// 	}
				// }

				// if (aim.isPresent() && isPrefire) {
				// 	motorizedHood.visionExtension();
				// }

				if(newRequest && activeRequest != null) {
					activeRequest.act();
					newRequest = false;
				} 

				if(activeRequest == null) {
					if(queuedRequests.isEmpty()) {
						allRequestsCompleted = true;
					} else {
						setActiveRequest(queuedRequests.remove(0));
					}
				} else if(activeRequest.isFinished()) {
					activeRequest = null;
				}
			
			}
		}

		@Override
		public void onStop(double timestamp) {
			
		}
		
	};
	
	public void enableCompressor(boolean enable){
		compressor.setClosedLoopControl(enable);
	}

	@Override
	public void stop() {
	}

	@Override
	public void zeroSensors() {
		
	}

	@Override
	public void registerEnabledLoops(ILooper enabledLooper) {
		enabledLooper.register(loop);
	}

	@Override
	public void outputTelemetry() {
	}

	public Request waitRequest(double seconds){
		return new Request(){
			double startTime = 0.0;
			double waitTime = 1.0;
		
			@Override
			public void act() {
				startTime = Timer.getFPGATimestamp();
				waitTime = seconds;
			}

			@Override
			public boolean isFinished(){
				return (Timer.getFPGATimestamp() - startTime) > waitTime;
			}
		};
	}

	public Request waitForVisionRequest(){
		return new Request(){

			@Override
			public void act() {

			}

			@Override
			public boolean isFinished(){
				return robotState.seesTarget();
			}

		};
	}


	private boolean needsToNotifyDrivers = false;
    public boolean needsToNotifyDrivers() {
        if (needsToNotifyDrivers) {
            needsToNotifyDrivers = false;
            return true;
        }
        return false;
	}

}
