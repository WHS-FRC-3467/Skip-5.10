/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team3467.robot2020.subsystems.ShooterFlyWheelSubsystem;

/**
 * Add your docs here.
 */
public interface ISpeedControl
{
    public void updateGains(double kP, double kI, double kD, double kF);
    public int runVelocityPIDF(double targetVelocity);
    public int getError();
    public double getOutputPercent();
    public void stop();
}
