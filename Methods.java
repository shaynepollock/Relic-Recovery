package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;


public class Methods {
/*
    public static void mecanumDrive()
    {  
        robot.motorPower = 1 - robot.gamepad1.right_trigger;
            
        //strafe doesn't work when motor power <.40
        if (robot.motorPower < .40)
        {   
            robot.motorPower= (float) .40;
        }
            
        double r = Math.hypot(robot.gamepad1.left_stick_x, robot.gamepad1.left_stick_y);  
        double robotAngle = Math.atan2(robot.gamepad1.left_stick_y, -robot.gamepad1.left_stick_x) - Math.PI / 4;  
        double rightX = -robot.gamepad1.right_stick_x;  
        final double FL = r * Math.cos(robotAngle) + rightX;  
        final double FR = r * Math.sin(robotAngle) - rightX;  
        final double BL = r * Math.sin(robotAngle) + rightX;  
        final double BR = r * Math.cos(robotAngle) - rightX;  
            
        robot.frontLeft.setPower(FL* robot.motorPower);  
        robot.frontRight.setPower(FR* robot.motorPower);  
        robot.backLeft.setPower(BL* robot.motorPower);  
        robot.backRight.setPower(BR* robot.motorPower);
    }//end of mecanumDrive
    public static void drive(CrownJoules2018.Robot robot)
    {
        robot.leftMotor.setPower(robot.gamepad1.left_stick_y);
        robot.rightMotor.setPower(robot.gamepad1.right_stick_y);
    }
    public static void pivot(CrownJoules2018.Robot robot)  
    {  
        //left pivot  
        while (robot.gamepad1.dpad_left)   
        {  
            /*robot.frontLeft.setPower(-.5);  
            robot.frontRight.setPower(.5);  
            robot.backLeft.setPower(-.5);  
            robot.backRight.setPower(.5);
            robot.leftMotor.setPower(-.5);
            robot.rightMotor.setPower(.5);
        }  
              
        //right pivot  
        while (robot.gamepad1.dpad_right)   
        {  
            /*robot.frontLeft.setPower(.5);  
            robot.frontRight.setPower(-.5);  
            robot.backLeft.setPower(.5);  
            robot.backRight.setPower(-.5);
            robot.leftMotor.setPower(.5);
            robot.rightMotor.setPower(-.5);
        }  
    }  
    
    public static void runIntake(CrownJoules2018.Robot robot)
    {
        
    }
    
    //test wheels to check if secure 
    public static void test(CrownJoules2018.Robot robot)   
    {  
        /*robot.frontLeft.setPower(robot.gamepad1.left_stick_y);  
        robot.frontRight.setPower(robot.gamepad1.right_stick_y);  
        robot.backLeft.setPower(robot.gamepad1.left_stick_x);  
        robot.backRight.setPower(robot.gamepad1.right_stick_x);
        robot.leftMotor.setPower(robot.gamepad1.left_stick_y);
        robot.rightMotor.setPower(robot.gamepad1.right_stick_y);
    } */
} //end of class
