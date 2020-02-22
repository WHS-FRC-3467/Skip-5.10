/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team3467.robot2020;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import org.team3467.robot2020.Autonomous.SimpleDrive;
import org.team3467.robot2020.Constants.DriveConstants;
import org.team3467.robot2020.Constants.OIConstants;
import org.team3467.robot2020.Constants.ShooterConstants;
import org.team3467.robot2020.subsystems.DriveSubsystem.SplitArcadeDrive;
import org.team3467.robot2020.subsystems.DriveSubsystem.TankDrive;
import org.team3467.robot2020.subsystems.CD7Subsystem.CD7Default;
import org.team3467.robot2020.subsystems.CD7Subsystem.CD7Subsystem;
import org.team3467.robot2020.subsystems.DriveSubsystem.AutoLineup;
import org.team3467.robot2020.subsystems.DriveSubsystem.DriveSubsystem;
import org.team3467.robot2020.subsystems.IntakeSubsystem.IntakeSubsystem;
import org.team3467.robot2020.subsystems.IntakeSubsystem.Pneumatics;
import org.team3467.robot2020.subsystems.IntakeSubsystem.RunIntake;
import org.team3467.robot2020.subsystems.IntakeSubsystem.ToggleIntake;
import org.team3467.robot2020.subsystems.IntakeSubsystem.ToggleIntakeDrive;
import org.team3467.robot2020.subsystems.SPathSubsystem.SPathDefault;
import org.team3467.robot2020.subsystems.SPathSubsystem.SPathSubsystem;
import org.team3467.robot2020.subsystems.ShooterFlyWheelSubsystem.FlyWheelSubsystem;
import org.team3467.robot2020.subsystems.ShooterGateSubsystem.GateDefault;
import org.team3467.robot2020.subsystems.ShooterGateSubsystem.GateSubsystem;
import org.team3467.robot2020.subsystems.ShooterGroups.PrepareShot;
import org.team3467.robot2020.subsystems.ShooterGroups.runGateAuto;
import org.team3467.robot2020.subsystems.ShooterHoodSubsystem.HoodSubsystem;
import org.team3467.robot2020.subsystems.DriveSubsystem.RocketSpinDrive;
import org.team3467.robot2020.control.XBoxControllerDPad;
import org.team3467.robot2020.control.XboxController;
import org.team3467.robot2020.control.XboxControllerButton;
import org.team3467.robot2020.sensors.Limelight.Limelight;
import org.team3467.robot2020.sensors.Limelight.Limelight.LightMode;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a "declarative" paradigm, very little robot logic should actually be
 * handled in the {@link Robot} periodic methods (other than the scheduler calls). Instead, the structure of the robot (including subsystems, commands, and
 * button mappings) should be declared here.
 */
public class RobotContainer
{

    // The robot's subsystems
    private final DriveSubsystem m_robotDrive = new DriveSubsystem();
    private final IntakeSubsystem m_intakeSub = new IntakeSubsystem();
    private final FlyWheelSubsystem m_flyWheelsub = new FlyWheelSubsystem();
    private final GateSubsystem m_gateSub = new GateSubsystem();
    private final HoodSubsystem m_hoodSub = new HoodSubsystem();
    private final SPathSubsystem m_sPath = new SPathSubsystem();
    private final CD7Subsystem m_CD7 = new CD7Subsystem();

    // The autonomous routines
    // A simple auto routine that drives forward a specified distance, and then stops.
    // private final Command m_simpleAuto =
    // new DriveDistance(AutoConstants.kAutoDriveDistanceInches, AutoConstants.kAutoDriveSpeed, m_robotDrive);

    // A complex auto routine that drives forward, drops a hatch, and then drives backward.
    // private final Command m_complexAuto = new ComplexAuto(m_robotDrive, m_hatchSubsystem);

    // A chooser for autonomous commands
    SendableChooser<Command> m_chooser = new SendableChooser<>();

    // The Driver and Operator controllers
    public static XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);
    public static XboxController m_operatorController = new XboxController(OIConstants.kOperatorControllerPort);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer()
    {  
        //fieldCamera = new FieldCamera();
        Pneumatics.getInstance();
        /*
        limelightFeed = new HttpCamera("limelight", "http://limelight.local:5800/stream.mjpg");
        ShuffleboardTab dashboardTab = Shuffleboard.getTab("Dash");
        dashboardTab.add("LL", limelightFeed).withPosition(0,0).withSize(15, 8).withProperties(Map.of("Show Crosshair", true, "Show Controls", false));
        */
        // Configure the button bindings
        configureButtonBindings();
        // m_pneumatics.compressorStart();
        // Configure default commands
        // Set the default drive command to split-stick arcade drive
        switch (DriveConstants.m_driveMode)
        {
        case DriveConstants.driveMode_Tank:
            m_robotDrive.setDefaultCommand(
                new TankDrive(m_robotDrive,
                    () -> m_driverController.getLeftY(),
                    () -> m_driverController.getRightY()));
            break;

        default:
        case DriveConstants.driveMode_SplitArcade:
            m_robotDrive.setDefaultCommand(
                // A split-stick arcade command, with forward/backward controlled by the left hand,
                // and turning controlled by the right.
                new SplitArcadeDrive(m_robotDrive,
                    () -> m_driverController.getLeftY(),
                    () -> m_driverController.getRightX()));
            break;

        case DriveConstants.driveMode_RocketSpin:
            m_robotDrive.setDefaultCommand(
                new RocketSpinDrive(m_robotDrive,
                    () -> m_driverController.getLeftX(),
                    () -> m_driverController.getLeftTrigger(),
                    () -> m_driverController.getRightTrigger()));
            break;
        }

        // Set the Default command for the IntakeSubsystem
        // Provide controls to 1) Run CD7 & Shooter Belts, 2) Run Intake, 3) Reverse Intake
    
        // Set the Default command for the ShooterSubsystem
        // Control Shooter Gate using Right Stick Y-Axis
        
        //S path is based on left Y axis
        m_sPath.setDefaultCommand(
            new SPathDefault(m_sPath,
                () -> m_operatorController.getLeftY()));

        //run CD7 in using left trigger, run CD7 out using right trigger
        m_CD7.setDefaultCommand(
            new CD7Default(m_CD7, 
                () -> m_operatorController.getLeftTrigger(),
                () -> m_operatorController.getRightTrigger()));

        //Run gate once using right trigger, run gate multiple times using left Trigger
        m_gateSub.setDefaultCommand(
            new GateDefault(m_gateSub,
            () -> m_driverController.getRightTrigger(),
            () -> m_driverController.getLeftTrigger()));

        // Add commands to the autonomous command chooser
        // m_chooser.addOption("Simple Auto", m_simpleAuto);
        // m_chooser.addOption("Complex Auto", m_complexAuto);

        // Put the chooser on the dashboard
        Shuffleboard.getTab("Autonomous").add(m_chooser);
        Limelight.setLedMode(LightMode.eOff);
    }

    /**
     * Use this method to define your button->command mappings.
     */
    private void configureButtonBindings()
    {
        /*
         * Operator controller
         */
        //Run intake in while held. bumper Left
        new XboxControllerButton(m_operatorController, XboxController.Button.kBumperLeft)
            .whileHeld(new RunIntake(m_intakeSub, 1.0));

        //Run Intake out whileheld bumper right
        new XboxControllerButton(m_operatorController, XboxController.Button.kBumperRight)
            .whileHeld(new RunIntake(m_intakeSub, -1.0));

        //Rev shooter for trench shot, button X
        new XboxControllerButton(m_operatorController, XboxController.Button.kX)
            .whileHeld(new PrepareShot(m_flyWheelsub, m_hoodSub, ShooterConstants.kTrenchShotVelocity));
        
        //Rev Shooter for auto line shot, button A
        new XboxControllerButton(m_operatorController, XboxController.Button.kA)
            .whileHeld(new PrepareShot(m_flyWheelsub, m_hoodSub, ShooterConstants.kInitLineShotVelocity));
        
        //Don't use these until PIDF is tuned
        new XBoxControllerDPad(m_operatorController, XboxController.DPad.kDPadUp)
            .whenActive(new InstantCommand(m_hoodSub::positionManualHood));
            
        new XBoxControllerDPad(m_operatorController, XboxController.DPad.kDPadDown)
            .whenActive(new InstantCommand(m_hoodSub::dropShooterHood));
        
        // Deploys/Retracts intake
        new XboxControllerButton(m_operatorController, XboxController.Button.kBack)
            .whenPressed(new ToggleIntake(m_intakeSub));

        
        // Driver Controller
        new XboxControllerButton(m_driverController, XboxController.Button.kBumperLeft)
            .whenPressed(new AutoLineup(m_robotDrive));
            
        new XboxControllerButton(m_driverController, XboxController.Button.kBack)
            .whenPressed(new ToggleIntakeDrive(m_intakeSub));

    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand()
    {
        return new SimpleDrive(m_robotDrive);
    }
}
