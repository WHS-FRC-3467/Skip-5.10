/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team3467.robot2020.subsystems.ShooterGateSubsystem;

import org.team3467.robot2020.subsystems.ShooterFlyWheelSubsystem.FlyWheelSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class runShooterGate extends CommandBase {
  GateSubsystem m_gate;
  double m_speed;
  FlyWheelSubsystem m_flyWheel;
  public runShooterGate(GateSubsystem gateSubsys, FlyWheelSubsystem FlyWheelSubsys, double speed)
  {
    m_gate = gateSubsys;
    m_speed = speed;
    m_flyWheel = FlyWheelSubsys;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize()
  {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {
    if(m_flyWheel.isWheelAtSpeed()){
      m_gate.runShooterGate(m_speed);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean isFinished)
  {
    m_gate.runShooterGate(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished()
  {
      return false;
  }
}

