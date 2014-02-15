/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;



import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogModule;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.DigitalInput;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
        RobotDrive chassis = new RobotDrive(1, 2);
        AnalogModule exampleAnalog;
        Joystick driveStick = new Joystick(1);
        Joystick camStick = new Joystick(2);
        Victor armLow = new Victor(5);
        Relay armVertical = new Relay(1);
        DigitalInput extendedLimit = new DigitalInput(1);
        DigitalInput retractedLimit = new DigitalInput(2);
        boolean vertArmLower = false;
        boolean vertArmRaise = false;
        double counter;
        Servo camPan = new Servo(3);
        Servo camTilt = new Servo(4);
        double tiltIn;
        double panIn;
        
        int m_autoPeriodicLoops;
        int relayCounter;
    public RobotTemplate() {
        this.counter = 0.0;
        
    }

        
        
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        chassis.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        chassis.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        exampleAnalog = AnalogModule.getInstance(1);
        vertArmLower = false;
        vertArmRaise = false;

    }
    public void autonomousInit() {
        m_autoPeriodicLoops = 0;
    }

    /**
     * This function is called periodically during autonomous
     */
    
    	static int printSec;
	static int startSec;
    
	public void autonomousPeriodic() {
		
		m_autoPeriodicLoops++;


		if (m_autoPeriodicLoops < (5 * /*GetLoopsPerSec()*/50)) {
			// When on the first periodic loop in autonomous mode, start driving forwards at half speed
			chassis.drive(-0.5, 0.0);			// drive forwards at half speed
		}
                else {
			// After 2 seconds, stop the robot
			chassis.drive(0.0, 0.0);			// stop robot
		}
		
	}
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        SmartDashboard.putNumber("Counter", counter++);
        SmartDashboard.putNumber("Ultrasonic Value", exampleAnalog.getValue(1));
        SmartDashboard.putNumber("Voltage", exampleAnalog.getVoltage(1));
        
        chassis.setSafetyEnabled(true);
        chassis.arcadeDrive(driveStick);
        
        tiltIn = (camStick.getY() +0.5);
        camTilt.set(tiltIn);
        panIn = (camStick.getX() +0.5 );
        camPan.set(panIn);
        
        /* 
           Vertical arm
        */
        vertArmLower = false;
        vertArmRaise = false;
        
        /* Button 2 lowers arm */
        if (camStick.getRawButton(2) && retractedLimit.get()) {
            vertArmLower = true;
        }
        /* Button 3 Raises arm */
        else if (camStick.getRawButton(3) && extendedLimit.get()) {
            vertArmRaise = true;
        }
        
        /* Raise or lower arm */
        if (vertArmLower) {
            /* If limit not reached, set motor to reverse */
            armVertical.set(Relay.Value.kReverse);
        }
        else if (vertArmRaise) {
            armVertical.set(Relay.Value.kForward);
        }
        else {
            /* Stop motor if limit reached or neither button pressed */
            armVertical.set(Relay.Value.kOff);
        }

         /*if(camStick.getRawButton(4)){
            armLow.set(1);    
        } 
        else if(camStick.getRawButton(5)){
            armLow.set(-1);
        }
        else{
            armLow.set(0);
        }*/
            
  
    }
    
        
    
    /**                       
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }

}

