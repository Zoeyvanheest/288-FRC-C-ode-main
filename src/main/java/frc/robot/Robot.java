// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.Timer;
import javax.swing.text.html.parser.Element;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.AddressableLED;




/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
  public class Robot extends TimedRobot {
  
  //drivetrain
  private final CANSparkMax m_leftMotorLeader = new CANSparkMax(2, MotorType.kBrushless);
  private final CANSparkMax m_leftMotorFollower = new CANSparkMax(3, MotorType.kBrushless);
  private final CANSparkMax m_rightMotorLeader = new CANSparkMax(4, MotorType.kBrushless);
  private final CANSparkMax m_rightMotorFollower = new CANSparkMax(5, MotorType.kBrushless);

  //LED
  AddressableLED m_led = new AddressableLED(2);
  AddressableLEDBuffer m_ledBuffer = new AddressableLEDBuffer(1000);

  //flywheel
  private final TalonFX m_fwTop = new TalonFX(6);
  private final TalonFX m_fwBottom = new TalonFX(7);

  //pneumatics
  private final PneumaticHub pneumaticHub =  new PneumaticHub(8); 
  Solenoid m_LHsolenoid = new Solenoid(PneumaticsModuleType.REVPH, 1);
  Solenoid m_RHsolenoid = new Solenoid(PneumaticsModuleType.REVPH, 2);
  Solenoid m_ampSolenoid = new Solenoid(PneumaticsModuleType.REVPH, 3);

  private final Compressor m_Compressor = new Compressor(PneumaticsModuleType.REVPH);
  Boolean pneumaticToggle = false;

  //auto timer
  private Timer time1 = new Timer();

  //Intake
  private final TalonFX m_itTop = new TalonFX(10);
  private final TalonFX m_itBottom = new TalonFX(9);

  //Controllers
  private final PS4Controller m_stick = new PS4Controller(2);
  private final XboxController m_operator = new XboxController(1);

  String[] auton = {"blue right", "middle"};

  //drivetrain
  private final DifferentialDrive m_robotDrive =  new DifferentialDrive(m_leftMotorLeader::set, m_rightMotorLeader::set);

  public Robot() {
    m_leftMotorFollower.follow(m_leftMotorLeader);
    m_rightMotorFollower.follow(m_rightMotorLeader); 
    SendableRegistry.addChild(m_robotDrive, m_leftMotorLeader);
    SendableRegistry.addChild(m_robotDrive, m_leftMotorFollower);
    SendableRegistry.addChild(m_robotDrive, m_rightMotorLeader);
    SendableRegistry.addChild(m_robotDrive, m_rightMotorFollower);
  }

  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_rightMotorFollower.setInverted(true);
    m_rightMotorLeader.setInverted(true);
    SmartDashboard.putStringArray("Auto List", auton );
    SmartDashboard.putString("test","hello");
    m_Compressor.enableDigital();
    CameraServer.startAutomaticCapture("cam0",0);

    //LED
    m_led.setLength(m_ledBuffer.getLength());
    m_led.setData(m_ledBuffer);
    m_led.start();
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 0, 0, 0);
   }
   m_led.setData(m_ledBuffer);
  }

  // private void offLed() {
  //   m_led.setLength(m_ledBuffer.getLength());
  //   m_led.setData(m_ledBuffer);
  //   m_led.start();
  //   for (var i = 0; i < m_ledBuffer.getLength(); i++) {
  //     m_ledBuffer.setRGB(i, 0, 0, 0);
  //  }
  //  m_led.setData(m_ledBuffer);
  // }
  
  @Override
  public void autonomousInit() {
    time1.start();
    m_leftMotorFollower.setIdleMode(CANSparkBase.IdleMode.kBrake);
    m_leftMotorLeader.setIdleMode(CANSparkBase.IdleMode.kBrake);
    m_rightMotorFollower.setIdleMode(CANSparkBase.IdleMode.kBrake);
    m_rightMotorLeader.setIdleMode(CANSparkBase.IdleMode.kBrake);
  }

  @Override 
  public void autonomousPeriodic() {
  //  String auton = SmartDashboard.getString("Auto Selector", "middle");
    //switch(auton) {
      // //case "blue right":
        // if(time1.get() <.5){
        //   m_fwBottom.set(.6);
        //   m_fwTop.set(.6);}
        // if(time1.get() >.5 && time1.get() <1){
        //   m_itBottom.set(-.5);
        //   m_itTop.set(-.5); }
        // if(time1.get() >1 && time1.get() <1.5){
        //   m_fwBottom.set(0);
        //   m_fwTop.set(0);
        //   m_itBottom.set(0);
        //   m_itTop.set(0);
        // }
        // if(time1.get() >1.5 && time1.get() <2.7){
        //   m_leftMotorLeader.set(.5);
        //   m_rightMotorLeader.set(.65);
        // }
        // if(time1.get() >2.7 && time1.get() <3.2){
        //   m_leftMotorLeader.set(5);
        //   m_rightMotorLeader.set(5);
        // }
        // if(time1.get()>3.2){
        //   m_leftMotorLeader.set(0);
        //   m_rightMotorLeader.set(0);
        // }
        //break;

      // case "middle":
        if(time1.get() <.5){
          m_fwBottom.set(.6);
          m_fwTop.set(.6);
        } 
        if(time1.get() >.5 && time1.get() <1){
          m_itBottom.set(.5);
          m_itTop.set(.5); 
        }
        if(time1.get() >1 && time1.get() <1.5){
          m_fwBottom.set(.2);
          m_fwTop.set(.2);
          m_itBottom.set(0);
          m_itTop.set(0);
        }
        if(time1.get() >1.5 && time1.get() <2){
          m_fwBottom.set(0);
          m_fwTop.set(0);
        }
        if(time1.get() >2 && time1.get() <3.3){
          m_leftMotorLeader.set(.5);
          m_rightMotorLeader.set(.5);
          m_itTop.set(.5);
          m_itBottom.set(.5);
        }
        if(time1.get() >3.3 && time1.get() <3.5){
          m_leftMotorLeader.set(0);
          m_rightMotorLeader.set(0);
          m_itTop.set(0);
          m_itBottom.set(0);
        }
        if(time1.get()>4 && time1.get() <5.3){
          m_leftMotorLeader.set(-.5);
          m_rightMotorLeader.set(-.5);
        }
        if(time1.get()>5.3 && time1.get() <5.5){
          m_leftMotorLeader.set(0);
          m_rightMotorLeader.set(0);
        }
        if(time1.get()>6 && time1.get() <7){
          m_fwBottom.set(.65);
          m_fwTop.set(.65);
        }
        if(time1.get()>7 && time1.get() <7.5){
          m_itBottom.set(.5);
          m_itTop.set(.5);
        }
        if(time1.get()>7.5 && time1.get()<8){
          m_fwBottom.set(0);
          m_fwTop.set(0);
          m_itBottom.set(0);
          m_itTop.set(0);
        }
        if(time1.get()>7.5){
          m_leftMotorLeader.set(0);
          m_rightMotorLeader.set(0);
        }
       

         // break;

      //    //case "blue left":
      //       if(time1.get() <.5){
      //     m_fwBottom.set(.75);
      //     m_fwTop.set(.75);
      //   }
      //   if(time1.get() >.5 && time1.get() <1){
      //     m_itBottom.set(-.5);
      //     m_itTop.set(-.5); }
      //   if(time1.get() >1 && time1.get() <1.5){
      //     m_fwBottom.set(0);
      //     m_fwTop.set(0);
      //     m_itBottom.set(0);
      //     m_itTop.set(0);
      //   }
      //   if(time1.get() >1.5 && time1.get() <2.1){
      //     m_leftMotorLeader.set(.1);
      //     m_rightMotorLeader.set(.5);
      //   }
      //   if(time1.get()>2.1 && time1.get() <2.3){
      //     m_leftMotorLeader.set(0);
      //     m_rightMotorLeader.set(0);
      //   }
      //   if(time1.get()>2.3 && time1.get() <3.3){
      //     m_leftMotorLeader.set(.5);
      //     m_rightMotorLeader.set(.5);
      //     m_itBottom.set(-.55);
      //     m_itTop.set(-.55);
      //   }
      //   if(time1.get()>3.3 && time1.get() <3.5){
      //     m_leftMotorLeader.set(0);
      //     m_rightMotorLeader.set(0);
      //     m_itBottom.set(0);
      //     m_itTop.set(0);
      //   }
      //   if(time1.get()>3.5 && time1.get() <4.5){
      //     m_leftMotorLeader.set(-.5);
      //     m_rightMotorLeader.set(-.5);
      //   }
      //   if(time1.get()>4.5 && time1.get() <4.7){
      //     m_leftMotorLeader.set(0);
      //     m_rightMotorLeader.set(0);
      //   }
      //   if(time1.get() >4.7 && time1.get() <5.2){
      //     m_leftMotorLeader.set(-.5);
      //     m_rightMotorLeader.set(-.1);
      //   }        
      //   if(time1.get() >5.2 && time1.get() <5.8){
      //     m_fwBottom.set(.75);
      //     m_fwTop.set(.75);
      //   }
      //   if(time1.get() >5.8 && time1.get() <6.2){
      //     m_itBottom.set(-.5);
      //     m_itTop.set(-.5);
      //   }
      //   if(time1.get() >6.2){
      //     m_leftMotorLeader.set(0);
      //     m_rightMotorLeader.set(0);
      //   }

        // //red amp
        // if(time1.get() <.5){
        //   m_fwBottom.set(.75);
        //   m_fwTop.set(.75);
        // }
        // if(time1.get() >.5 && time1.get() <1){
        //   m_itBottom.set(-.5);
        //   m_itTop.set(-.5); }
        // if(time1.get() >1 && time1.get() <1.5){
        //   m_fwBottom.set(0);
        //   m_fwTop.set(0);
        //   m_itBottom.set(0);
        //   m_itTop.set(0);
        // }
        // if(time1.get() >1.5 && time1.get() <2.2){
        //   m_leftMotorLeader.set(.1);
        //   m_rightMotorLeader.set(.5);
        // }
        // if(time1.get()>2.2 && time1.get() <2.3){
        //   m_leftMotorLeader.set(0);
        //   m_rightMotorLeader.set(0);
        // }
        // if(time1.get()>2.3 && time1.get() <3.3){
        //   m_leftMotorLeader.set(.5);
        //   m_rightMotorLeader.set(.5);
        //   m_itBottom.set(-.55);
        //   m_itTop.set(-.55);
        // }
        // if(time1.get()>3.3 && time1.get() <3.5){
        //   m_leftMotorLeader.set(0);
        //   m_rightMotorLeader.set(0);
        //   m_itBottom.set(0);
        //   m_itTop.set(0);
        // }
        // if(time1.get()>3.5 && time1.get() <4.5){
        //   m_leftMotorLeader.set(-.5);
        //   m_rightMotorLeader.set(-.5);
        // }
        // if(time1.get()>4.5 && time1.get() <4.7){
        //   m_leftMotorLeader.set(0);
        //   m_rightMotorLeader.set(0);
        // }
        // if(time1.get() >4.7 && time1.get() <5.2){
        //   m_leftMotorLeader.set(-.1);
        //   m_rightMotorLeader.set(-.5);
        // }        
        // if(time1.get() >5.2 && time1.get() <5.8){
        //   m_fwBottom.set(.75);
        //   m_fwTop.set(.75);
        // }
        // if(time1.get() >5.8 && time1.get() <6.2){
        //   m_itBottom.set(-.5);
        //   m_itTop.set(-.5);
        // }
        // if(time1.get()>3 && time1.get() <3.5){
        //   m_rightMotorLeader.set(.5);
        // }
        // if(time1.get() >2.7 && time1.get() <3){
        //   m_leftMotorLeader.set(0);
        //   m_rightMotorLeader.set(0);
        // }

        //red leave
        // if(time1.get() <.5){
        //   m_fwBottom.set(.6);
        //   m_fwTop.set(.6);}
        // if(time1.get() >.5 && time1.get() <1){
        //   m_itBottom.set(-.5);
        //   m_itTop.set(-.5); }
        // if(time1.get() >1 && time1.get() <1.5){
        //   m_fwBottom.set(0);
        //   m_fwTop.set(0);
        //   m_itBottom.set(0);
        //   m_itTop.set(0);
        // }
        // if(time1.get() >1.5 && time1.get() <2.7){
        //   m_rightMotorLeader.set(.5);
        //   m_leftMotorLeader.set(.65);
        // }
        // if(time1.get() >2.7 && time1.get() <3){
        //   m_leftMotorLeader.set(0);
        //   m_rightMotorLeader.set(0);
        // }
      }

  
  @Override
  public void teleopInit() {
    time1.start();
    m_leftMotorLeader.setIdleMode(CANSparkBase.IdleMode.kCoast);
    m_rightMotorLeader.setIdleMode(CANSparkBase.IdleMode.kCoast);
  }

  @Override
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.
    //color changes with condiiotnals should be first in the perodic but setting the data should be at the end so you dont overrun your other commands

     m_robotDrive.arcadeDrive(-m_stick.getLeftY(), -m_stick.getRightX());

    //flywheel
    if (m_operator.getRawAxis(XboxController.Axis.kRightTrigger.value) > .05) {
      m_fwTop.set(.67);
      m_fwBottom.set(.67);
    } else if (m_operator.getRawButton(XboxController.Button.kRightBumper.value)) {
      m_fwTop.set(-.50);
      m_fwBottom.set(-.50);
    } else if (m_operator.getRawButton(XboxController.Button.kX.value)) {
      m_fwTop.set(.12);
      m_fwBottom.set(.20);
    }else if (m_stick.getRawButton(PS4Controller.Button.kR2.value) && (m_stick.getRawButton(PS4Controller.Button.kL2.value))){
      m_fwTop.set(.8);
      m_fwBottom.set(.8);
    } else {
      m_fwTop.stopMotor();
      m_fwBottom.stopMotor();
    } 
    //Intake
    if (m_operator.getRawAxis(XboxController.Axis.kLeftTrigger.value) > .05) {
      for (var i = 0; i < m_ledBuffer.getLength(); i++) 
      m_ledBuffer.setRGB(i, 150, 0, 150);
      m_itTop.set(.35);
      m_itBottom.set(.35);
    }else if (m_operator.getRawButton(XboxController.Button.kLeftBumper.value)) {
      m_itTop.set(-.25);
      m_itBottom.set(-.25);
    } else if (m_operator.getRawButton(XboxController.Button.kX.value)) {
      m_itTop.set(.25);
      m_itBottom.set(.25);
    } else {
      for (var i = 0; i < m_ledBuffer.getLength(); i++) 
      m_ledBuffer.setRGB(i, 0, 0, 0);
      m_itTop.stopMotor();
      m_itBottom.stopMotor();
     }

  //hang
  if (m_stick.getRawButton(PS4Controller.Button.kL1.value)){
    m_LHsolenoid.set(true);
    m_RHsolenoid.set(true);
    pneumaticHub.checkSolenoidChannel(1);
    pneumaticHub.checkSolenoidChannel(2);
  } if (m_stick.getRawButton(PS4Controller.Button.kR1.value)){
    m_LHsolenoid.set(false);
    m_RHsolenoid.set(false);
    pneumaticHub.checkSolenoidChannel(1);
    pneumaticHub.checkSolenoidChannel(2);
    }

  //amp 
  if (m_operator.getRawButton(XboxController.Button.kA.value)) {
    m_ampSolenoid.set(true);
    pneumaticHub.checkSolenoidChannel(3);
  }else if (m_operator.getRawButton(XboxController.Button.kY.value)) {
    m_ampSolenoid.set(false);
    pneumaticHub.checkSolenoidChannel(3);
  }

  //move to top of Telopperodic()
  m_led.setData(m_ledBuffer);
  }
}

