package org.usfirst.frc.team1923.robot.commands;

import org.usfirst.frc.team1923.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

//import org.usfirst.frc.team1923.robot.subsystems.DriveTrainSubsystem;

/**
 *
 */
public class DriveWithJoyStickCommand extends Command {

    public DriveWithJoyStickCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveTrainSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	boolean rightTrigger = Robot.oi.rightStick.getTrigger();
    	boolean leftTrigger = Robot.oi.leftStick.getTrigger();
    	if(rightTrigger && leftTrigger){
        	Robot.driveTrainSubsystem.manualDrive(-Robot.oi.leftStick.getY(), -Robot.oi.rightStick.getY());
    	} else {
    		Robot.driveTrainSubsystem.manualDrive(-Robot.oi.leftStick.getY()/2, -Robot.oi.rightStick.getY()/2);    		
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}

--------------------

package com.nicoletfear.Robot2015.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class LightSensor extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	DigitalInput lightSensor;
	DigitalOutput LEDOut;

	public LightSensor(){
		lightSensor = new DigitalInput(0);
		LEDOut = new DigitalOutput(1);
		LEDOut.set(false);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}


--------------------

package org.usfirst.frc.team558.robot.autocommands;

import org.usfirst.frc.team558.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class StopElevatorCommand extends Command {

    public StopElevatorCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.elevator);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.elevator.SetElevatorMotors(0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}

--------------------

