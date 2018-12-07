package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class SAuto extends LinearOpMode {

    public DcMotor leftMotor;
    public DcMotor rightMotor;

    public Servo intakePivotServo;
    public float intakePivotServoPos = 0.5f; //What the servo above's current pos is; Sets to this at start
    public float intakePivotSensitivity = 500;

    public CRServo intakeServo1;
    public CRServo intakeServo2;

    @Override
    public void runOpMode() {

        final double COUNTS_PER_MOTOR_REV =1120; //NeveRest 40 motor gives 1120         counts per revolution of output shaft
        final double DRIVE_GEAR_REDUCTION = .66; //<1.0 if geared Up (big to small) output/input. input gear turns 1/2 for output to turn once.
        final double WHEEL_DIAMETER_INCHES = 6.0;
        final double COUNTS_PER_INCH = ( COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
        //encoder counts x gear reduction / circumference of wheel
        final double DRIVE_SPEED = .6;

        //connects motors to hub & phone- use name in quotes for config
        leftMotor = hardwareMap.get(DcMotor.class, "left_Motor");
        rightMotor = hardwareMap.get(DcMotor.class, "right_Motor");
        intakeServo1 = hardwareMap.get(CRServo.class, "leftVexMotor");
        intakeServo2 = hardwareMap.get(CRServo.class, "rightVexMotor");
        intakePivotServo = hardwareMap.get(Servo.class, "intakePivotServo");

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);

        int positionLM;
        int positionRM;
        int targetLM;
        int targetRM;

        waitForStart(); //press play button, actives opMode

        intakePivotServo.setPosition(intakePivotServoPos);

        while (opModeIsActive()) {
            positionLM= leftMotor.getCurrentPosition();
            positionRM= rightMotor.getCurrentPosition();

            targetLM= (positionLM + (int)(COUNTS_PER_INCH*4));
            targetRM= (positionRM + (int)(COUNTS_PER_INCH*4));

            leftMotor.setTargetPosition(targetLM);
            rightMotor.setTargetPosition(targetRM);

            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            leftMotor.setPower(.2);
            rightMotor.setPower(.2);

            while(leftMotor.isBusy()){ //once @ position, stop robot
                idle();
            }

            leftMotor.setPower(0);
            rightMotor.setPower(0);

        }//end opModeIsActive

    }//end runOpMode
}//end class
